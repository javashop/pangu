package com.enation.pangu.task;

import com.enation.pangu.model.Task;

import java.util.List;

/**
 * 任务进度模型
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/18
 */

public class MachineProcessor {

    /**
     * 进度比例
     */
    private int proportion;

    /**
     * 机器id
     */
    private Long machineId;


    /**
     * 机器名称
     */
    private String machineName;


    /**
     * 子任务
     */
    private List<Task> taskList;

    /**
     * 任务状态
     */
    private String taskState;

    /**
     * 时时的日志
     */
    private String taskLog;

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public String getTaskState() {

        //默认是SUCCESS
        taskState=TaskState.SUCCESS.name();
        //部署是否成功
        boolean deploySuccess = true;

        for (Task task : taskList) {

            //有一个任务失败则部署失败了
            if ( TaskState.ERROR.name().equals(task.getState())) {
                taskState=TaskState.ERROR.name();
                break;
            }

            //有一个RUNNING则部署RUNNING
            if ( TaskState.RUNNING.name().equals(task.getState())) {
                taskState=TaskState.RUNNING.name();
                break;
            }

        }
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskLog() {
        return taskLog;
    }

    public void setTaskLog(String taskLog) {
        this.taskLog = taskLog;
    }

    @Override
    public String toString() {
        return "TaskProcessor{" +
                "proportion=" + proportion +
                ", machineId=" + machineId +
                ", machineName='" + machineName + '\'' +
                ", taskList=" + taskList +
                ", taskState='" + taskState + '\'' +
                ", taskLog='" + taskLog + '\'' +
                '}';
    }
}
