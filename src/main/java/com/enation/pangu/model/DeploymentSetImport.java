package com.enation.pangu.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.enation.pangu.enums.KindEnum;

import java.util.List;
import java.util.Map;

/**
 * 部署导出实体类
 * @author zhangsong
 * @date 2020-01-16
 */
public class DeploymentSetImport {

    /**
     * 资源类型
     * @see KindEnum
     */
    @JSONField(ordinal = 1)
    private String kind;

    /**
     * 部署集名称
     */
    @JSONField(ordinal = 2)
    private String name;

    /**
     * 部署集依赖
     */
    @JSONField(ordinal = 3)
    private Map<String,List<String>> depend;

    public String getKind() {
        return KindEnum.deploymentSet.value();
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<String>> getDepend() {
        return depend;
    }

    public void setDepend(Map<String, List<String>> depend) {
        this.depend = depend;
    }
}
