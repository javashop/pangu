package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * 仓库表实体
 * @author zhanghao
 * @create 2020-10-31
 */
@TableName(value = "repository")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Repository implements Serializable {
    /**
     * 仓库id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 仓库名称
     */
    private String name;
    /**
     * 仓库地址
     */
    private String address;
    /**
     * 认证类型，可选值：password,publicKey
     */
    private String authType;
    /**
     * 认证用户名
     */
    private String username;
    /**
     * 认证密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 分支
     */
    @TableField(exist=false)
    private String branch;

    /**
     * 目标克隆目录
     */
    @TableField(exist=false)
    private String cloneTarget;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getCloneTarget() {
        return cloneTarget;
    }

    public void setCloneTarget(String cloneTarget) {
        this.cloneTarget = cloneTarget;
    }
}
