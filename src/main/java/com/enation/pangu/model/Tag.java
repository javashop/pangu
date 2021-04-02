package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 标签
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/9
 */
@TableName(value = "Tag")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tag='" + name + '\'' +
                '}';
    }
}
