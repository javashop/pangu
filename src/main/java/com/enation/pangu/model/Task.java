package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.pangu.utils.StringUtil;

/**
 * 部署任务
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/16
 */
@TableName(value = "task")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id ;
    private Long parentId;

    private String deploymentName;
    private Long deploymentId ;

    private String machineName;
    private Long machineId;

    private String stepName;
    private Long stepId;
    private String state;
    private Long startTime;
    private Long endTime ;

    private Long deploymentSetId ;
    private String taskType;

    private Integer isSkip;

    @TableField(exist=false)
    private String startTimeText;

    @TableField(exist=false)
    private String log;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", deploymentName='" + deploymentName + '\'' +
                ", deploymentId=" + deploymentId +
                ", machineName='" + machineName + '\'' +
                ", machineId=" + machineId +
                ", stepName='" + stepName + '\'' +
                ", stepId=" + stepId +
                ", state='" + state + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", log='" + log + '\'' +
                '}';
    }

    public String getStartTimeText() {
        if (this.startTime != null) {
            startTimeText = StringUtil.toString(this.startTime, "yyyy-MM-dd HH:mm:ss");
        }else{
            startTimeText = "";
        }

        return startTimeText;
    }

    public void setStartTimeText(String startTimeText) {
        this.startTimeText = startTimeText;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getDeploymentSetId() {
        return deploymentSetId;
    }

    public void setDeploymentSetId(Long deploymentSetId) {
        this.deploymentSetId = deploymentSetId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Integer getIsSkip() {
        return isSkip;
    }

    public void setIsSkip(Integer isSkip) {
        this.isSkip = isSkip;
    }
}
