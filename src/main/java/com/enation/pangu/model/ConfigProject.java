package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Program: pangu
 * @Author: lizhengguo
 * @Date: 2020-11-12 20:27
 */
@TableName(value = "config_project")
public class ConfigProject {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 项目名称
     */
    private String name;

    /**
     * 修改时间
     */
    private String editTime;

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

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    @Override
    public String toString() {
        return "ConfigProject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", editTime=" + editTime +
                '}';
    }
}
