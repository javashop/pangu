package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 环境变量分组
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/25
 */

public class EnvGroup {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    long id;

    /**
     * 项目id
     */
    long projectId;

    /**
     * 分组名字
     */
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
