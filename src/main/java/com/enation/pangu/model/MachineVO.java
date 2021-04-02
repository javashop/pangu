package com.enation.pangu.model;

/**
 * @author shenyw
 * @create 2020-11-12-10:59
 */
public class MachineVO extends Machine {
    private String tags;

    private String groupName;



    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
