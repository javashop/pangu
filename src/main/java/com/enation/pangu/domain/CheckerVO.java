package com.enation.pangu.domain;

import java.util.List;

/**
 * 检测器vo
 * @author zhangsong
 * @date 2020-11-05
 */
public class CheckerVO {
    /** 命令的类型，目前支持ssh 命令 */
    private String type;

    /** shell脚本文件名 */
    private String shellFileName;

    /** shell脚本克隆路径 */
    private String shellTargetDir;

    /** 执行命令集合 */
    private List<String> execList;

    /** 要检测的输出，如果为制定值，则检测成功 */
    private String checkText;

    /** config */
    private PluginConfigVO config;

    /** metadata */
    private Plugin metadata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShellFileName() {
        return shellFileName;
    }

    public void setShellFileName(String shellFileName) {
        this.shellFileName = shellFileName;
    }

    public String getShellTargetDir() {
        return shellTargetDir;
    }

    public void setShellTargetDir(String shellTargetDir) {
        this.shellTargetDir = shellTargetDir;
    }

    public List<String> getExecList() {
        return execList;
    }

    public void setExecList(List<String> execList) {
        this.execList = execList;
    }

    public String getCheckText() {
        return checkText;
    }

    public void setCheckText(String checkText) {
        this.checkText = checkText;
    }

    public PluginConfigVO getConfig() {
        return config;
    }

    public void setConfig(PluginConfigVO config) {
        this.config = config;
    }

    public Plugin getMetadata() {
        return metadata;
    }

    public void setMetadata(Plugin metadata) {
        this.metadata = metadata;
    }
}
