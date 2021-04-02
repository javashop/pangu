//package com.enation.pangu.model;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.enation.pangu.utils.StringUtil;
//
///**
// * 部署集任务
// * @author zhangsong
// * @version 1.0
// * @since 1.0.0
// * 2020/12/30
// */
//@TableName(value = "set_task")
//public class SetTask {
//
//    @TableId(type = IdType.AUTO)
//    private Long id ;
//    private Long deploymentSetId ;
//
//    private String state;
//    private Long startTime;
//    private Long endTime ;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getDeploymentSetId() {
//        return deploymentSetId;
//    }
//
//    public void setDeploymentSetId(Long deploymentSetId) {
//        this.deploymentSetId = deploymentSetId;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public Long getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Long startTime) {
//        this.startTime = startTime;
//    }
//
//    public Long getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Long endTime) {
//        this.endTime = endTime;
//    }
//
//    public String getStartTimeText() {
//        if (this.startTime != null) {
//            return StringUtil.toString(this.startTime, "yyyy-MM-dd HH:mm:ss");
//        }else{
//            return "";
//        }
//    }
//}
