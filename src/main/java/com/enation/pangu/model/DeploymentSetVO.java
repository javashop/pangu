package com.enation.pangu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

/**
 * 部署集集详情
 * @author 褚帅
 * 2020/10/31
 */
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeploymentSetVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    private Long id;

    private String name;

    private int status;

    private Long addTime;

    private List<DeploymentSetRelVO> list;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public List<DeploymentSetRelVO> getList() {
        return list;
    }

    public void setList(List<DeploymentSetRelVO> list) {
        this.list = list;
    }
}
