package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.pangu.domain.PluginConfigVO;

import java.util.List;
import java.util.Map;

/**
 * 步骤实体类
 * @author zhangsong
 * @date 2020-11-01
 */
@TableName(value = "step")
public class Step {
    /**
     * 步骤id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 步骤名称
     */
    private String name;
    /**
     * 执行顺序
     */
    private Integer sequence;
    /**
     * 执行器，可选值：ssh
     */
    private String executor;
    /**
     * 校验器，可选值：port，command，process
     */
    private String checkType;
    /**
     * 部署id
     */
    private Long deploymentId;
    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 执行器参数
     */
    private String executorParams;


    /**
     * 检测器参数
     */
    private String checkerParams;


    /**
     * 是否跳过执行 0不跳过 1跳过
     */
    private Integer isSkip;

    /**
     * 执行器配置
     */
    @TableField(exist = false)
    private PluginConfigVO executorConfig;

    /**
     * 检测器配置
     */
    @TableField(exist = false)
    private PluginConfigVO checkerConfig;

    /**
     * 回显特殊执行器参数用
     *（write_config执行器  git_clone执行器）
     */
    @TableField(exist = false)
    private Map writeConfigParams;

    /**
     * write_config执行器的配置文件列表（回显数据用）
     */
    @TableField(exist = false)
    private List<ConfigFile> configFileList;


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

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getExecutorParams() {
        return executorParams;
    }

    public void setExecutorParams(String executorParams) {
        this.executorParams = executorParams;
    }

    public PluginConfigVO getExecutorConfig() {
        if(this.executorConfig == null){
            return new PluginConfigVO();
        }
        return executorConfig;
    }

    public void setExecutorConfig(PluginConfigVO executorConfig) {
        this.executorConfig = executorConfig;
    }

    public PluginConfigVO getCheckerConfig() {
        if(this.checkerConfig == null){
            return new PluginConfigVO();
        }
        return checkerConfig;
    }

    public void setCheckerConfig(PluginConfigVO checkerConfig) {
        this.checkerConfig = checkerConfig;
    }

    public String getCheckerParams() {
        return checkerParams;
    }

    public void setCheckerParams(String checkerParams) {
        this.checkerParams = checkerParams;
    }

    public Map getWriteConfigParams() {
        return writeConfigParams;
    }

    public void setWriteConfigParams(Map writeConfigParams) {
        this.writeConfigParams = writeConfigParams;
    }

    public List<ConfigFile> getConfigFileList() {
        return configFileList;
    }

    public void setConfigFileList(List<ConfigFile> configFileList) {
        this.configFileList = configFileList;
    }

    public Integer getIsSkip() {
        return isSkip;
    }

    public void setIsSkip(Integer isSkip) {
        this.isSkip = isSkip;
    }
    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequence=" + sequence +
                ", executor='" + executor + '\'' +
                ", checkType='" + checkType + '\'' +
                ", deploymentId=" + deploymentId +
                ", addTime=" + addTime +
                ", executorParams='" + executorParams + '\'' +
                ", checkerParams='" + checkerParams + '\'' +
                ", executorConfig=" + executorConfig +
                ", checkerConfig=" + checkerConfig +
                ", writeConfigParams=" + writeConfigParams +
                ", configFileList=" + configFileList +
                '}';
    }
}
