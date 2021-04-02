package com.enation.pangu.enums;

/**
 * 插件状态枚举
 *
 * @author zhenghao
 * @date 2020-11-13
 */
public enum PluginStatusEnum {

    /**
     * ssh执行器
     */
    OPEN("开启"),
    /**
     * 配置文件执行器
     */
    CLOSE("关闭");


    private String des;

    PluginStatusEnum(String des) {
        this.des = des;
    }

    public String des() {
        return this.des;
    }

}
