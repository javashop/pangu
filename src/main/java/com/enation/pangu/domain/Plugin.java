package com.enation.pangu.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.pangu.enums.PluginPathTypeEnum;

/**
 * 执行器vo
 * @author zhanghao
 * @date 2020-11-11
 */
@TableName(value = "plugin")
public class Plugin {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**插件名称*/
    private String name;

    /**插件id*/
    private String pluginId;

    /**插件描述*/
    private String desc;

    /**作者名字*/
    private String author;

    /**作者的链接*/
    private String authorUrl;

    /**插件类型**/
    private String kind;

    /**
     * 插件路径，基于resources目录
     */
    private String path;

    /**
     * 插件路径类型
     * @see PluginPathTypeEnum
     */
    private String pathType;

    /**
     * 状态 值详见 PluginStatusEnum
     * @see com.enation.pangu.enums.PluginStatusEnum
     **/
    private String status;

    /**
     * 执行器顺序
     */
    private Integer sequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPathType() {
        return pathType;
    }

    public void setPathType(String pathType) {
        this.pathType = pathType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", desc='" + desc + '\'' +
                ", author='" + author + '\'' +
                ", authorUrl='" + authorUrl + '\'' +
                ", kind='" + kind + '\'' +
                ", path='" + path + '\'' +
                ", pathType='" + pathType + '\'' +
                ", kind='" + kind + '\'' +
                ", path='" + path + '\'' +
                ", status='" + status + '\'' +
                ", sequence=" + sequence +
                '}';
    }
}
