package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author shenyw
 * @create 2020-11-11-14:24
 */
@TableName(value = "machine_tag")
public class MachineTag implements Serializable {
    /**
     * 关联表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 机器id
     */
    private Long machineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }
}
