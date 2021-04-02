package com.enation.pangu.domain;

/**
 * 文件copy
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/7
 */

public class Copy {
    public Copy() {
        this.parseEnv = true;
    }

    /**
     * 源文件路径，以resources为根目录的相对路径
     * 如/executor/myexecutor/a.sh
     * 则表示实际目录为：/project_root/src/main/resources/executor/myexecutor/a.sh
     */
    private String source;


    /**
     * 要copy的目标机器上的路径，如/opt/workspace/xxx/a.sh
     */
    private String target;

    /**
     * 是否解析变量
     */
    private boolean parseEnv;

    public boolean isParseEnv() {
        return parseEnv;
    }

    public void setParseEnv(boolean parseEnv) {
        this.parseEnv = parseEnv;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
