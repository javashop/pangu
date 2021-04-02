package com.enation.pangu.service;

import com.enation.pangu.domain.PluginType;
import com.enation.pangu.ssh.SshClient;

import java.util.Map;

/**
 * 插件执行器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/3/10
 */
public interface PluginRunner {

    /**
     * 执行一个插件
     * @param executorId
     * @param env
     * @param sshClient
     * @return
     */
     boolean run(String executorId, Map env, SshClient sshClient);

}
