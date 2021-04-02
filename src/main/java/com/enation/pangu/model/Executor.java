package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 执行器实体类
 * @author zhangsong
 * @date 2020-10-31
 */
@TableName(value = "executor")
public class Executor {
    /**
     * 执行器id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 执行器名称
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
     * 分支
     */
    private String branch;
    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 仓库名称（非数据库字段）
     */
    @TableField(exist = false)
    private String repositoryName;

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
}
