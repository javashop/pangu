package com.enation.pangu.task;

import com.enation.pangu.model.Step;
import com.enation.pangu.ssh.SshClient;

import java.util.Map;

/**
 * 步骤执行器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/1/18
 */
public interface StepExecutor {


    /**
     * 执行步骤
     * @param step
     * @param env
     * @param sshClient
     */
    boolean execute(Step step, Map env, SshClient sshClient);
}
