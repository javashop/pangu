package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.*;
import com.enation.pangu.enums.DeployWay;

import java.util.List;

/**
 * 部署实体类
 *
 * @author zhangsong
 * @date 2020-10-31
 */
@TableName(value = "deployment")
public class Deployment {
    /**
     * 部署id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 部署名称
     */
    private String name;
    /**
     * 是否需要仓库 1需要 0不需要
     */
    private String dependRepo;
    /**
     * 仓库id
     */
    private Long repositoryId;
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 分支
     */
    private String branch;
    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 环境变量id
     */
    private Long environmentId;

    /**
     * 部署方式
     * @see DeployWay
     */
    private String way;

    /**
     * 分组id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long groupId;

    /**
     * 分组名称(非数据库字段)
     */
    @TableField(exist = false)
    private String groupName;


    /**
     * 仓库名称（非数据库字段）
     */
    @TableField(exist = false)
    private String repositoryName;


    /**
     * 环境变量名称（非数据库字段）
     */
    @TableField(exist = false)
    private String environmentName;

    /**
     * 要部署机器列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Long> machineIdList;

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

    public String getDependRepo() {
        return dependRepo;
    }

    public void setDependRepo(String dependRepo) {
        this.dependRepo = dependRepo;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public List<Long> getMachineIdList() {
        return machineIdList;
    }

    public void setMachineIdList(List<Long> machineIdList) {
        this.machineIdList = machineIdList;
    }

    public Long getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Long environmentId) {
        this.environmentId = environmentId;
    }


    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
