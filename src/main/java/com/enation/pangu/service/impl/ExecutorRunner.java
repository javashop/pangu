package com.enation.pangu.service.impl;

import com.enation.pangu.domain.Copy;
import com.enation.pangu.domain.ExecutorVO;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.service.PluginManager;
import com.enation.pangu.service.PluginRunner;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.utils.ParseException;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 执行器运行逻辑实现
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/3/10
 */
@Service
public class ExecutorRunner extends BasePluginRunner{


    @Override
    public boolean run(String executorId, Map env, SshClient sshClient) {
        try {
            return innerRun(executorId, env, sshClient);
        } catch (Exception e) {
            monitorService.appendMachineLog(StringUtil.getStackTrace(e));
        }
        return false;
    }

    /**
     *
     * @param executorId
     * @param env
     * @param sshClient
     * @return
     */
    boolean innerRun(String executorId, Map env, SshClient sshClient)throws Exception {
        //解析插件的yml
        ExecutorVO executorVO = pluginManager.parsePlugin(executorId, PluginType.executor, env);

        //先将需要copy的文件传输至服务器
        List<Copy> copyList = executorVO.getCopyList();

        copy(copyList,env,sshClient);

        List<String> commands = executorVO.getExecList();

        //记录日志
        boolean result = sshClient.exec(commands, text -> {
            monitorService.appendMachineLog(text);

        }) == 0;

        return result;
    }


}
