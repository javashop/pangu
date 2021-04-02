package com.enation.pangu.enums;

/**
 * 任务类型枚举
 * @author zhangsong
 * @date 2020-01-20
 */
public enum TaskTypeEnum {

    /**
     * 步骤任务
     */
    step("步骤任务"),
    /**
     * 部署任务
     */
    deployment("部署任务"),
    /**
     * 部署集任务
     */
    deploymentSet("部署集任务");


    private String des;

    TaskTypeEnum(String des) {
        this.des = des;
    }

    public String des(){
        return this.des;
    }

    public String value(){
        return this.name();
    }

}
