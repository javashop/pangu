package com.enation.pangu.enums;

/**
 * 导出导出 kind类型枚举
 * @author zhangsong
 * @date 2020-01-11
 */
public enum KindEnum {

    /**
     * 部署
     */
    deployment("部署"),
    /**
     * 部署集
     */
    deploymentSet("部署集");


    private String des;

    KindEnum(String des) {
        this.des = des;
    }

    public String des(){
        return this.des;
    }

    public String value(){
        return this.name();
    }

}
