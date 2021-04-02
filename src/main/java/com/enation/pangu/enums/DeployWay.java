package com.enation.pangu.enums;

/**
 * 部署方式枚举
 * @author zhangsong
 * @date 2021-03-16
 */
public enum DeployWay {

    /**
     * 按服务器部署
     */
    machine("按服务器部署"),
    /**
     * 部署集
     */
    tag("按标签部署");


    private String des;

    DeployWay(String des) {
        this.des = des;
    }

    public String des(){
        return this.des;
    }

    public String value(){
        return this.name();
    }

}
