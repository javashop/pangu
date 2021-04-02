package com.enation.pangu.monitor.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.mapper.TaskMapper;
import com.enation.pangu.model.Task;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.task.MachineProcessor;
import com.enation.pangu.task.TaskState;
import com.enation.pangu.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * 基于文件系统的监控实现
 *
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/16
 */

@Service
public class MonitorFileSystemImpl implements MonitorService {

    ThreadLocal<Long> machineIdStorage = new ThreadLocal<Long>();
    ThreadLocal<Long> deploymentIdStorage = new ThreadLocal<Long>();
    ThreadLocal<Long> taskIdStorage = new ThreadLocal<Long>();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    TaskMapper taskMapper;

    @Override
    public void startLog(Long machineId, Long deploymentId, Long taskId) {
        taskIdStorage.set(taskId);
        deploymentIdStorage.set(deploymentId);
        machineIdStorage.set(machineId);
        String path = getRootPath(machineId, deploymentId);
        File file = new File(path);
        file.mkdirs();

    }

    @Override
    public void stopLog() {
        taskIdStorage.remove();
        machineIdStorage.remove();
        deploymentIdStorage.remove();

    }

    private String getRootPath(Long machineId, Long deploymentId) {
        String path = "";
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getSource();
        if (jarFile == null) {
            path =  home.getDir().getAbsolutePath() + "/target";
        }else{
            path = jarFile.getParentFile().toString();
        }
        path += "/logs/" + deploymentId + "/" + machineId;
        return path;
    }

    private String getLogPath(Long machineId, Long deploymentId, Long taskId) {

        String path = getRootPath(machineId, deploymentId);
        path += "/" + taskId + ".log";
        return path;
    }

    @Override
    public void appendMachineLog(String logs) {
        Long deploymentId = deploymentIdStorage.get();
        Long machineId = machineIdStorage.get();
        Long taskId = taskIdStorage.get();
        String path = this.getLogPath(machineId, deploymentId, taskId);
        File file = new File(path);

        try {
            FileUtils.writeLines(file, Arrays.asList(new String[]{logs}), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMachineLogs(Long machineId, Long deploymentId, Long taskId) {
        String path = this.getLogPath(machineId, deploymentId, taskId);
        return innerReadFile(path, 0);
    }

    private String innerReadFile(String path, int times) {
        int MAX_RETRY_TIMES = 3;
        if (times == MAX_RETRY_TIMES) {
            return "";
        }
        try {
            String log = FileUtils.readFileToString(new File(path), "UTF-8");
            return log;
        } catch (FileNotFoundException e) {
            System.out.println("retry " + times);
            times++;
            innerReadFile(path, times);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    @Override
    public List<MachineProcessor> getTaskProcessor(Long parentTaskId) {

        //查询出所有的子任务
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("machine_id", "start_time");
        queryWrapper.eq("parent_id", parentTaskId);
        List<Task> taskList = taskMapper.selectList(queryWrapper);

        //异步执行的任务，有可能为空
        if (taskList.isEmpty()) {
            return new ArrayList<>();
        }

        /*
         * 构建进度列表,列表的结构：
         * machineId
         *   |__taskList
         * machineId
         *   |__taskList
         */
        List<MachineProcessor> processorList = new ArrayList<>();
        List<Task> childTasks = new ArrayList<>();

        long lastMachineId = 0;

        //部署是否完成
        boolean deployComplete = true;

        //部署是否成功
        boolean deploySuccess = true;

        //循环任务并按机器分组
        for (Task task : taskList) {

            //有一个任务没完成则部署还没有完成
            if (TaskState.RUNNING.name().equals(task.getState())) {
                deployComplete = false;
            }

            //有一个任务失败则部署失败了
            if (!TaskState.SUCCESS.name().equals(task.getState())) {
                deploySuccess = false;
            }


            long machineId = task.getMachineId();

            //获取任务日志
            if(task.getIsSkip() == null || task.getIsSkip() != 1){
                String log = getMachineLogs(machineId, task.getDeploymentId(), task.getId());

                task.setLog(log);
            }

            if (machineId != lastMachineId) {
                lastMachineId = machineId;
                childTasks = new ArrayList<>();

                //任务进度vo
                MachineProcessor machineProcessor = new MachineProcessor();
                machineProcessor.setMachineId(machineId);
                machineProcessor.setMachineName(task.getMachineName());
                machineProcessor.setTaskList(childTasks);

                processorList.add(machineProcessor);

            }

            childTasks.add(task);

        }


        //如果部署完成，更新部署状态
        if (deployComplete) {

            Task parentTask = new Task();
            parentTask.setId(parentTaskId);
            parentTask.setEndTime(StringUtil.getDateline());

            if (deploySuccess) {
                parentTask.setState(TaskState.SUCCESS.name());
            } else {
                parentTask.setState(TaskState.ERROR.name());
            }

            taskMapper.updateById(parentTask);
        }

        return processorList;
    }

}
