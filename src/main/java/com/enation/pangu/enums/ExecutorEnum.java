package com.enation.pangu.enums;

/**
 * 特殊执行器枚举
 * @author zhangsong
 * @date 2020-11-13
 */
public enum ExecutorEnum {

    /**
     * ssh执行器
     */
    ssh("ssh执行器"),
    /**
     * 配置文件执行器
     */
    write_config("配置文件执行器"),
    /**
     * git clone执行器
     */
    git_clone("git clone执行器");


    private String des;

    ExecutorEnum(String des) {
        this.des = des;
    }

    public String des(){
        return this.des;
    }

    public String executorId(){
        return this.name();
    }
}
