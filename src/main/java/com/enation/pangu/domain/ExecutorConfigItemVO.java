package com.enation.pangu.domain;

import java.util.List;

/**
 * 执行器配置vo
 * @author zhangsong
 * @date 2020-11-07
 */
public class ExecutorConfigItemVO {
    /** 配置标题 */
    private String title;

    /** 前端form表单name值 */
    private String name;

    /** 表单类型 input :单行文本 textarea:多行文本 */
    private String type;

    /** 表单样式 */
    private String htmlcss;

    /** 表单值 */
    private String value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHtmlcss() {
        return htmlcss;
    }

    public void setHtmlcss(String htmlcss) {
        this.htmlcss = htmlcss;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExecutorConfigItemVO{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", htmlcss='" + htmlcss + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
