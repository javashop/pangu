package com.enation.pangu.task.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.enums.ExecutorEnum;
import com.enation.pangu.model.Repository;
import com.enation.pangu.model.Step;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.service.PluginRunner;
import com.enation.pangu.service.RepositoryManager;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.task.StepExecutor;
import com.enation.pangu.utils.ScriptUtil;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认步骤执行器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/1/18
 */

@Service("defaultStepExecutor")
public class DefaultStepExecutor implements StepExecutor {


    @Autowired
    private ExecutorManager executorManager;

    @Autowired
    private RepositoryManager repositoryManager;

    @Autowired
    private MonitorService monitorService;

    /**
     * 执行器运行实现类
     */
    @Autowired
    private PluginRunner executorRunner;

    @Override
    public boolean execute(Step step, Map env, SshClient sshClient) {
        boolean result = false;
        Map executorParams = (Map) JSONUtils.parse(step.getExecutorParams());
        env.putAll(executorParams);
        String executorId = step.getExecutor();

        if(ExecutorEnum.git_clone.executorId().equals(executorId)){
            putRepositoryEnv(step.getExecutorParams(), env);
        }

         result = executorRunner.run(executorId, env, sshClient);


        return result;
    }

    /**
     * 设置仓库环境变量
     * @param executorParams
     * @param env
     */
    private void putRepositoryEnv(String executorParams, Map env) {
        Map<String,String> params = JSON.parseObject(executorParams, HashMap.class);

        if(params.get("repository_id") == null){
            return;
        }

        Long repositoryId = Long.valueOf(params.get("repository_id"));
        Repository repository = repositoryManager.selectById(repositoryId);
        if(repository != null){
            repository.setBranch(params.get("branch"));
            repository.setCloneTarget(params.get("clone_target"));
            env.put("repository", repository);
        }
    }
}
