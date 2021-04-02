package com.enation.pangu.service;

import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.ssh.SshClient;

import java.io.IOException;
import java.util.List;

import java.util.Map;

/**
 * 执行器业务层接口
 *
 * @author zhangsong
 * @date 2020-11-07
 */
public interface ExecutorManager {

    /**
     * 查询一个执行器
     *
     * @param executorId 执行器id
     */
    PluginConfigVO selectById(String executorId);


    /**
     * 查询某个步骤的执行器config
     *
     * @param executorId 执行器id
     * @param stepId     步骤id
     * @return
     */
    PluginConfigVO selectByIdStepInfo(String executorId, Long stepId);
}
