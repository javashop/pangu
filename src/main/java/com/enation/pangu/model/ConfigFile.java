package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.InputStream;
import java.util.List;

/**
 * @Program: pangu
 * @Author: lizhengguo
 * @Date: 2020-11-12 20:28
 */
@TableName(value = "config_file")
public class ConfigFile {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 项目主键
     */
    private Long configProjectId;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件内容
     */
    private String content;

    /**
     * 修改时间
     */
    private String editTime;

    /**
     * 文件类型
     */
    @TableField(exist = false)
    private String type;

    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String configProjectName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigProjectId() {
        return configProjectId;
    }

    public void setConfigProjectId(Long configProjectId) {
        this.configProjectId = configProjectId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConfigProjectName() {
        return configProjectName;
    }

    public void setConfigProjectName(String configProjectName) {
        this.configProjectName = configProjectName;
    }

    @Override
    public String toString() {
        return "ConfigFile{" +
                "id=" + id +
                ", configProjectId=" + configProjectId +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", editTime='" + editTime + '\'' +
                ", type='" + type + '\'' +
                ", configProjectName='" + configProjectName + '\'' +
                '}';
    }
}
