package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.enums.MessageStatusEnum;
import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.DeploymentMapper;
import com.enation.pangu.mapper.TaskMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentSetManager;
import com.enation.pangu.service.MessageManager;
import com.enation.pangu.service.TaskManager;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientContext;
import com.enation.pangu.task.TaskState;
import com.enation.pangu.utils.PageConvert;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


/**
 * 部署task实现类
 *
 * @author zhangsong
 * @date 2020-01-08
 */
@Service
public class TaskManagerImpl implements TaskManager {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private DeploymentMapper deploymentMapper;

    @Autowired
    private DeploymentSetManager deploymentSetManager;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private SshClientContext sshClientContext;

    @Override
    public Task selectById(Long taskId) {
        return taskMapper.selectById(taskId);
    }

    @Override
    public Task selectRunningDeployTask(Long deploymentId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deployment_id", deploymentId);
        queryWrapper.eq("task_type", TaskTypeEnum.deployment.value());
        queryWrapper.eq("state", TaskState.RUNNING.name());
        return taskMapper.selectOne(queryWrapper);
    }

    @Override
    public Task selectRunningSetTask(Long deploymentSetId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deployment_set_id", deploymentSetId);
        queryWrapper.eq("task_type", TaskTypeEnum.deploymentSet.value());
        queryWrapper.eq("state", TaskState.RUNNING.name());
        return taskMapper.selectOne(queryWrapper);
    }

    @Override
    public TaskState updateParentTaskState(Long parentTaskId, TaskTypeEnum type) {
        Task updateTask = taskMapper.selectById(parentTaskId);

        //子任务理论数量
        Integer totalCount;
        if(TaskTypeEnum.deploymentSet.equals(type)){
            //部署数量
            totalCount = deploymentSetManager.selectDeploymentCount(updateTask.getDeploymentSetId());
        }else if(TaskTypeEnum.deployment.equals(type)){
            //步骤数 * 机器数
            totalCount = deploymentMapper.selectTotalStep(updateTask.getDeploymentId());
        }else {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"任务类型错误");
        }

        //查询出所有的子任务
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentTaskId);
        List<Task> taskList = taskMapper.selectList(queryWrapper);

        //部署是否完成
        boolean deployComplete = true;

        //部署是否成功
        boolean deploySuccess = true;

        //循环任务
        for (Task task : taskList) {

            //有一个任务没完成则部署还没有完成
            if (TaskState.RUNNING.name().equals(task.getState())) {
                deployComplete = false;
            }

            //有一个任务失败则部署失败了
            if (!TaskState.SUCCESS.name().equals(task.getState())) {
                deploySuccess = false;
            }

        }

        //如果部署完成，更新部署状态
        if (deployComplete) {

            updateTask.setEndTime(StringUtil.getDateline());

            if (deploySuccess) {
                if(taskList.size() == totalCount){
                    updateTask.setState(TaskState.SUCCESS.name());
                }else{
                    updateTask.setState(TaskState.RUNNING.name());
                }
            } else {
                updateTask.setState(TaskState.ERROR.name());
            }

            //新增消息
            addMessage(updateTask, type);

        }

        taskMapper.updateById(updateTask);
        return TaskState.valueOf(updateTask.getState());
    }

    /**
     * 插入一条消息
     * @param updateTask 任务
     * @param type 任务类型
     */
    private void addMessage(Task updateTask, TaskTypeEnum type) {
        if(TaskTypeEnum.deployment.value().equals(updateTask.getTaskType()) && updateTask.getParentId() != -1){
            return;
        }
        Message message = new Message();

        //消息内容
        String content = "";
        //跳转地址
        String jumpUrl = "";

        String status = "";
        if(TaskState.SUCCESS.name().equals(updateTask.getState())){
            status = "执行成功";
        }else if(TaskState.ERROR.name().equals(updateTask.getState())){
            status = "执行失败";
        }

        if(TaskTypeEnum.deployment.equals(type)){
            Deployment deployment = deploymentMapper.selectById(updateTask.getDeploymentId());
            content = "部署任务：" + deployment.getName() + " " + status;
            jumpUrl = "/view/task/list?deploymentId="+updateTask.getDeploymentId()+"&parentTaskId=" + updateTask.getId();
        }else if(TaskTypeEnum.deploymentSet.equals(type)){
            DeploymentSet deploymentSet = deploymentSetManager.getById(updateTask.getDeploymentSetId());
            content = "部署集任务：" + deploymentSet.getName() + " " + status;
            jumpUrl = "/view/deploymentSet/deploy/"+updateTask.getDeploymentSetId()+"?setTaskId=" + updateTask.getId();
        }

        message.setStatus(MessageStatusEnum.unread.value());
        message.setCreateTime(StringUtil.getDateline());
        message.setContent(content);
        message.setJumpUrl(jumpUrl);
        messageManager.insert(message);
    }

    @Override
    public Task selectDeployTask(Long setTaskId, Long deploymentId) {
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper();
        taskQueryWrapper.eq("parent_id", setTaskId);
        taskQueryWrapper.eq("deployment_id", deploymentId);
        return taskMapper.selectOne(taskQueryWrapper);
    }

    @Override
    public boolean taskIsSuccess(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if(TaskState.SUCCESS.name().equals(task.getState())){
            return true;
        }
        return false;
    }

    @Override
    public WebPage history(int pageNo, int pageSize, TaskTypeEnum type, Long typeId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("start_time");
        queryWrapper.eq("task_type", type.value());
        if(TaskTypeEnum.deploymentSet.equals(type)){
            queryWrapper.eq("deployment_set_id", typeId);
        }else if(TaskTypeEnum.deployment.equals(type)){
            queryWrapper.eq("deployment_id", typeId);
        }else{
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"任务类型错误");
        }

        Page<Task> page = taskMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        WebPage webPage = PageConvert.convert(page);
        return webPage;
    }

    @Override
    public List<Task> getChildrenTask(Long parentTaskId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentTaskId);
        List<Task> parentTaskList = taskMapper.selectList(queryWrapper);

        return parentTaskList;
    }

    @Override
    public void insert(Task task) {
        taskMapper.insert(task);
    }

    @Override
    public void stopDeployTask(Long taskId) {

        List<SshClient> sshClientList = sshClientContext.getSshClient(taskId);

        if(sshClientList == null || sshClientList.size() == 0){
            new UpdateChainWrapper<>(taskMapper)
                    .set("state", TaskState.ERROR.name())
                    .eq("id", taskId)
                    .or()
                    .eq("parent_id", taskId)
                    .update();
            return;
        }

        for(SshClient sshClient : sshClientList){
            try {
                //TODO 只是断开了连接，未关闭liunx中的进程
                sshClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
