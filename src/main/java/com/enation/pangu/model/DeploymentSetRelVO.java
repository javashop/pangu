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
public class DeploymentSetRelVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    private Long id;
    private Long deploymentSetId;
    private Long deploymentId;
    private String deploymentName;
    private List<DeploymentDependVO> deploymentDependList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeploymentSetId() {
        return deploymentSetId;
    }

    public void setDeploymentSetId(Long deploymentSetId) {
        this.deploymentSetId = deploymentSetId;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public List<DeploymentDependVO> getDeploymentDependList() {
        return deploymentDependList;
    }

    public void setDeploymentDependList(List<DeploymentDependVO> deploymentDependList) {
        this.deploymentDependList = deploymentDependList;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
}
