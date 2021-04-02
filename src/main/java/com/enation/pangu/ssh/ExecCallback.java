package com.enation.pangu.ssh;

/**
 * ssh 命令执行回调
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/4
 */
public interface ExecCallback {


    /**
     * 回调执行方法
     * @param out 命令执行的输出
     */
    void callback(String out);
}
