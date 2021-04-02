package com.enation.pangu.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

/**
 * 部署集成员详情
 * @author 褚帅
 * 2020-10-31
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeploymentSetRelDTO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    private Long deploymentSetId;
    private Long deploymentSetRelId;
    private Long deploymentId;
    private String deploymentDependIds;


    public Long getDeploymentSetId() {
        return deploymentSetId;
    }

    public void setDeploymentSetId(Long deploymentSetId) {
        this.deploymentSetId = deploymentSetId;
    }

    public Long getDeploymentSetRelId() {
        return deploymentSetRelId;
    }

    public void setDeploymentSetRelId(Long deploymentSetRelId) {
        this.deploymentSetRelId = deploymentSetRelId;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDeploymentDependIds() {
        return deploymentDependIds;
    }

    public void setDeploymentDependIds(String deploymentDependIds) {
        this.deploymentDependIds = deploymentDependIds;
    }
}
