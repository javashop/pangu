package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * @author 褚帅
 * 2020-10-31
 */
@TableName(value = "deployment_depend")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeploymentDepend implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long deploymentSetId;

    private Long deploymentSetRelId;

    private Long dependId;

    @TableField(exist = false)
    private Long deploymentId;
    @TableField(exist = false)
    private String deploymentName;
    @TableField(exist = false)
    private String dependName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeploymentSetRelId() {
        return deploymentSetRelId;
    }

    public void setDeploymentSetRelId(Long deploymentSetRelId) {
        this.deploymentSetRelId = deploymentSetRelId;
    }

    public Long getDeploymentSetId() {
        return deploymentSetId;
    }

    public void setDeploymentSetId(Long deploymentSetId) {
        this.deploymentSetId = deploymentSetId;
    }

    public Long getDependId() {
        return dependId;
    }

    public void setDependId(Long dependId) {
        this.dependId = dependId;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }

    public String getDependName() {
        return dependName;
    }

    public void setDependName(String dependName) {
        this.dependName = dependName;
    }
}
