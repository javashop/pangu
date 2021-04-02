//package com.enation.pangu.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.enation.pangu.mapper.SetTaskMapper;
//import com.enation.pangu.mapper.TaskMapper;
//import com.enation.pangu.model.SetTask;
//import com.enation.pangu.model.Task;
//import com.enation.pangu.model.WebPage;
//import com.enation.pangu.service.DeploymentSetManager;
//import com.enation.pangu.service.SetTaskManager;
//import com.enation.pangu.task.TaskState;
//import com.enation.pangu.utils.PageConvert;
//import com.enation.pangu.utils.StringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * 部署集task实现类
// *
// * @author zhangsong
// * @date 2020-01-04
// */
//@Service
//public class SetTaskManagerImpl implements SetTaskManager {
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private SetTaskMapper setTaskMapper;
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private TaskMapper taskMapper;
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private DeploymentSetManager deploymentSetManager;
//
//    @Override
//    public WebPage history(Long deploymentSetId, int pageNo, int pageSize) {
//        QueryWrapper<SetTask> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("start_time");
//        queryWrapper.eq("deployment_set_id", deploymentSetId);
//        Page<SetTask> page = setTaskMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
//        WebPage webPage = PageConvert.convert(page);
//        return webPage;
//    }
//
//    @Override
//    public List<Task> getTaskLog(Long setTaskId) {
//        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("set_task_id", setTaskId);
//        queryWrapper.eq("parent_id", 0L);
//        List<Task> parentTaskList = taskMapper.selectList(queryWrapper);
//
//        return parentTaskList;
//    }
//
//    @Override
//    public void updateSetTaskState(Long setTaskId) {
//        SetTask updateSetTask = new SetTask();
//        updateSetTask.setId(setTaskId);
//        updateSetTask.setState(TaskState.RUNNING.name());
//
//        SetTask setTask = setTaskMapper.selectById(setTaskId);
//        //部署数量
//        Integer deploymentCount = deploymentSetManager.selectDeploymentCount(setTask.getDeploymentSetId());
//
//        //查询出部署集的子任务
//        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("set_task_id", setTaskId);
//        queryWrapper.eq("parent_id", 0L);
//        List<Task> parentTaskList = taskMapper.selectList(queryWrapper);
//
//        if(parentTaskList.size() == deploymentCount){
//            //部署是否完成
//            boolean deployComplete = true;
//
//            //部署是否成功
//            boolean deploySuccess = true;
//
//            //循环任务
//            for (Task task : parentTaskList) {
//
//                //有一个任务没完成则部署还没有完成
//                if (TaskState.RUNNING.name().equals(task.getState())) {
//                    deployComplete = false;
//                }
//
//                //有一个任务失败则部署失败了
//                if (!TaskState.SUCCESS.name().equals(task.getState())) {
//                    deploySuccess = false;
//                }
//
//            }
//
//            //如果部署完成，更新部署状态
//            if (deployComplete) {
//
//                updateSetTask.setEndTime(StringUtil.getDateline());
//
//                if (deploySuccess) {
//                    updateSetTask.setState(TaskState.SUCCESS.name());
//                } else {
//                    updateSetTask.setState(TaskState.ERROR.name());
//                }
//
//            }
//        }
//
//        setTaskMapper.updateById(updateSetTask);
//    }
//
//    @Override
//    public SetTask selectRunningTask(Long deploymentSetId) {
//        QueryWrapper<SetTask> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("deployment_set_id", deploymentSetId);
//        queryWrapper.eq("state", TaskState.RUNNING.name());
//        return setTaskMapper.selectOne(queryWrapper);
//    }
//
//    @Override
//    public SetTask selectById(Long setTaskId) {
//        return setTaskMapper.selectById(setTaskId);
//    }
//}
