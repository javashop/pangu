package com.enation.pangu.enums;

/**
 * 消息状态枚举
 * @author zhangsong
 * @date 2020-01-25
 */
public enum MessageStatusEnum {

    /**
     * 未读
     */
    unread("未读"),
    /**
     * 已读
     */
    read("已读");


    private String des;

    MessageStatusEnum(String des) {
        this.des = des;
    }

    public String des(){
        return this.des;
    }

    public String value(){
        return this.name();
    }

}
