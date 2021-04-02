package com.enation.pangu.enums;

/**
 * 插件存储路径类型枚举
 * @author zhangsong
 * @date 2020-03-01
 */
public enum PluginPathTypeEnum {

    /**
     * 工程的resource目录
     */
    resource("工程的resource目录"),
    /**
     * 磁盘的绝对路径
     */
    absolute("磁盘的绝对路径");


    private String des;

    PluginPathTypeEnum(String des) {
        this.des = des;
    }

    public String des(){
        return this.des;
    }

    public String value(){
        return this.name();
    }

}
