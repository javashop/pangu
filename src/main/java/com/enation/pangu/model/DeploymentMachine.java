package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * 部署、机器关联表实体类
 * @author zhangsong
 * @date 2020-10-31
 */
@TableName(value = "deployment_machine")
public class DeploymentMachine {
    /**
     * 关联表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 部署id
     */
    private Long deploymentId;
    /**
     * 机器id
     */
    private Long machineId;

    public DeploymentMachine(){}

    public DeploymentMachine(Long deploymentId, Long machineId){
        this.deploymentId= deploymentId;
        this.machineId= machineId;
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
}
