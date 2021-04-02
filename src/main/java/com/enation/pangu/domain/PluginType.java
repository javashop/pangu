package com.enation.pangu.domain;

/**
 * 插件类型
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/24
 */
public enum PluginType {

    /**
     * 执行器
     */
    executor("executor"),

    /**
     * 检查器
     */
    checker("checker");

    String folder;

    PluginType(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
