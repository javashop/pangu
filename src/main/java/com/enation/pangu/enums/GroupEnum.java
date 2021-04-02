package com.enation.pangu.enums;

/**
 * @author gy
 * @version v1.0
 * @date 2021/3/1
 * @since v7.2.0
 *
 * 分组
 */
public enum GroupEnum {

    /**
     * 机器
     */
    MACHINES("机器"),

    /**
     * 部署
     */
    DEPLOY("部署");

    private String des;

    GroupEnum(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String value(){
        return this.name();
    }
}
