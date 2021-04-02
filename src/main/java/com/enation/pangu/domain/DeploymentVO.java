package com.enation.pangu.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 部署插件实体类
 * @author zhangsong
 * @date 2020-10-31
 */
public class DeploymentVO {
    private String name;

    private List<StepVO> stepList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StepVO> getStepList() {
        return stepList;
    }

    public void setStepList(List<StepVO> stepList) {
        this.stepList = stepList;
    }
}
