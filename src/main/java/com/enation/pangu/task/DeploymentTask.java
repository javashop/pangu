package com.enation.pangu.task;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.enums.SecretKeyEnum;
import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.mapper.StepMapper;
import com.enation.pangu.mapper.TaskMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.service.*;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientContext;
import com.enation.pangu.ssh.SshClientFactory;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.enation.pangu.utils.StringUtil.getDateline;

/**
 * 部署任务
 * 通过异步的方式执行每个步骤
 *
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/16
 */
@Service
public class DeploymentTask {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private StepMapper stepMapper;

    @Autowired
    private ExecutorManager executorManager;

    @Autowired
    private MonitorService monitorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private DeploymentSetManager deploymentSetManager;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private SshClientContext sshClientContext;

    @Autowired
    private Map<String, StepExecutor> stepContext = new ConcurrentHashMap<>();


    @Autowired()
    PluginRunner checkerRunner;


    @Autowired
    SecretKeyManager secretKeyManager;


    /**
     * 异步执行某台机器的部署
     *
     * @param deployTask 总的部署任务
     * @param deployment 部署实例
     * @param machine    机器
     * @param env        内置变量
     */
    @Async
    public void execute(Task deployTask, Deployment deployment, Machine machine, Map env) {

        Long deploymentId = deployment.getId();

        SshClient sshClient =null;


        //步骤列表
        List<Step> stepList = stepMapper.selectList(new QueryWrapper<Step>().eq("deployment_id", deploymentId).orderByAsc("sequence"));
        if (stepList.isEmpty()) {
            return;
        }
        //建立workspace
        try {
            //开启session
            //证书认证，需要先读取证书放在privatekey属性中
            if (SecretKeyEnum.publickey.name().equals(machine.getAuthType())) {
                //查出私钥
                SecretKey secretKey = secretKeyManager.selectById(Long.parseLong(machine.getSecretkeyId()));
                String privateKey = secretKey.getPrivateKey();
                machine.setPrivateKey(privateKey);
            }
            sshClient = SshClientFactory.createSsh(machine);
            sshClientContext.addSshClient(deployTask.getId(), sshClient);
            sshClient.exec("mkdir -p " + env.get("workspace"), log -> {

            });
        } catch (IOException e) {
            monitorService.startLog(machine.getId(), deploymentId, stepList.get(0).getId());
            monitorService.stopLog();
            this.updateTaskState(deployTask, false);
            e.printStackTrace();
            return;
        }


        //执行所有步骤
        for (Step step : stepList) {

            //任务入库
            Task stepTask = new Task();
            //判断该步骤是否要跳过执行，如果跳过，生成一条执行成功的任务
            if(step.getIsSkip() == 1){
                stepTask.setIsSkip(1);
                this.insertTaskToDataBase(stepTask, deployTask, deployment, machine, step, TaskState.SUCCESS);
                continue;
            }
            stepTask.setIsSkip(0);
            this.insertTaskToDataBase(stepTask, deployTask, deployment, machine, step, TaskState.RUNNING);
            //开始记录日志
            monitorService.startLog(machine.getId(), deploymentId, stepTask.getId());

            //调起步骤
            boolean result = execStep(step, env, sshClient);


            //更新任务的执行状态
            updateTaskState(stepTask, result);

            //停止记录日志
            monitorService.stopLog();

            //有一个步骤失败，则终端执行
            if (!result) {
                break;
            }
        }

        //关闭session
        try {
            sshClient.disconnect();
            sshClientContext.remove(deployTask.getId(), sshClient);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //步骤全部执行完成后，更新所属部署任务的状态
        TaskState deployTaskState = taskManager.updateParentTaskState(deployTask.getId(), TaskTypeEnum.deployment);

        Long setTaskId = deployTask.getParentId();
        //如果不属于某个部署集任务
        if (setTaskId == -1) {
            return;
        }

        //如果部署任务执行成功，执行依赖它的其他部署任务
        if (TaskState.SUCCESS.equals(deployTaskState)) {
            deploymentSetManager.execDependDeployment(deployTask);
        }

        //如果部署任务执行完成，更新所属部署集任务的状态
        if (!TaskState.RUNNING.equals(deployTaskState)) {
            taskManager.updateParentTaskState(setTaskId, TaskTypeEnum.deploymentSet);
        }

    }


    /**
     * 插入一个任务到数据库中
     *
     * @param stepTask   步骤任务
     * @param deployTask 部署任务
     * @param deployment 部署
     * @param machine    机器
     * @param step       步骤
     * @param taskState  任务状态
     * @return 插入成功后的任务id
     */
    Long insertTaskToDataBase(Task stepTask, Task deployTask, Deployment deployment, Machine machine, Step step, TaskState taskState) {


        stepTask.setParentId(deployTask.getId());
        stepTask.setDeploymentName(deployment.getName());
        stepTask.setDeploymentId(deployment.getId());

        stepTask.setMachineId(machine.getId());
        stepTask.setMachineName(machine.getIp());

        stepTask.setStepId(step.getId());
        stepTask.setStepName(step.getName());

        stepTask.setStartTime(getDateline());
        if(taskState != TaskState.RUNNING){
            stepTask.setEndTime(getDateline());
        }
        stepTask.setState(taskState.name());

        stepTask.setTaskType(TaskTypeEnum.step.value());

        taskMapper.insert(stepTask);
        return stepTask.getId();
    }


    /**
     * 更新任务状态
     *
     * @param task   要更新的任务
     * @param result 任务执行结果
     */
    private void updateTaskState(Task task, boolean result) {
        //更新任务结束时间
        task.setEndTime(getDateline());

        //任务执行状态
        if (result) {
            task.setState(TaskState.SUCCESS.name());
        } else {
            task.setState(TaskState.ERROR.name());
        }

        taskMapper.updateById(task);
    }



    /**
     * 执行一个步骤
     *
     * @param step      步骤
     * @param env       环境变量
     * @param sshClient ssh 客户端
     * @throws Exception
     */
    boolean execStep(Step step, Map env, SshClient sshClient) {
        boolean result = false;
        try {

            String executorId = step.getExecutor();
            StepExecutor stepExecutor = stepContext.get(executorId);

            if (stepExecutor == null) {
                //如果执行器没有对应的bean，则使用默认步骤执行器
                stepExecutor = stepContext.get("defaultStepExecutor");
            }

            //定义执行器执行时的环境变量
            //此变量的来源为通用变量集env，再加入executorParams
            //为了不污染变量集env，所以new 了一个map
            Map executorEnv = new HashMap(env);
            String executorParams = step.getExecutorParams();
            if (StringUtil.notEmpty(executorParams)) {
                Map<String, String> params = (Map<String, String>) JSONUtils.parse(executorParams);
                executorEnv.putAll(params);

            }
            //调用步骤执行器，执行具体的步骤
            result = stepExecutor.execute(step, executorEnv, sshClient);

            //执行检测器(如果存在）
            String checkerId = step.getCheckType();
            if (StringUtil.notEmpty(checkerId)) {
                monitorService.appendMachineLog("执行检查：" + checkerId+"...");

                //定义检查器执行时的环境变量
                //此变量的来源为通用变量集env，再加入chekerrParams
                //为了不污染变量集env，所以new 了一个map
                Map checkerEnv = new HashMap(env);
                String checkerParams = step.getCheckerParams();
                if (StringUtil.notEmpty(executorParams)) {
                    Map<String, String> params = (Map<String, String>) JSONUtils.parse(checkerParams);
                    checkerEnv.putAll(params);

                }

                //执行检查器
                result = checkerRunner.run(checkerId, checkerEnv, sshClient);
                if (result) {
                    monitorService.appendMachineLog("检测成功");
                } else {
                    monitorService.appendMachineLog("检测失败");
                }

            }

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

}
