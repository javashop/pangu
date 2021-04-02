package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * @author 褚帅
 * 2020-10-31
 */
@TableName(value = "deployment_set")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeploymentSet implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private int status;

    private Long addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}
