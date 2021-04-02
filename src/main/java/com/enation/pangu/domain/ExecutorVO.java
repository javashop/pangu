package com.enation.pangu.domain;

import java.util.List;

/**
 * 执行器vo
 *
 * @author zhangsong
 * @date 2020-11-05
 */
public class ExecutorVO {
    /**
     * 执行器id
     */
    private String id;

    /**
     * 执行器类型 目前支持ssh 命令
     */
    private String type;



    private List<Copy> copyList;


    /**
     * 执行命令集合
     */
    private List<String> execList;

    /**
     * config
     */
    private PluginConfigVO config;

    /**
     * metadata
     */
    private Plugin metadata;

    /**
     * 检查器的检测方法
     */
    private String checkFun;

    public String getCheckFun() {
        return checkFun;
    }

    public void setCheckFun(String checkFun) {
        this.checkFun = checkFun;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getExecList() {
        return execList;
    }

    public void setExecList(List<String> execList) {
        this.execList = execList;
    }

    public PluginConfigVO getConfig() {
        return config;
    }

    public void setConfig(PluginConfigVO config) {
        this.config = config;
    }


    public Plugin getMetadata() {
        return metadata;
    }

    public void setMetadata(Plugin metadata) {
        this.metadata = metadata;
    }

    public List<Copy> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<Copy> copyList) {
        this.copyList = copyList;
    }

    @Override
    public String toString() {
        return "ExecutorVO{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", copyList=" + copyList +
                ", execList=" + execList +
                ", config=" + config +
                ", metadata=" + metadata +
                '}';
    }
}
