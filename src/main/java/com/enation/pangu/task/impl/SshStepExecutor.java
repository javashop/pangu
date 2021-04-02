package com.enation.pangu.task.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.enation.pangu.model.Step;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.task.StepExecutor;
import com.enation.pangu.utils.ScriptUtil;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2021/1/18
 */

@Service("ssh_command")
public class SshStepExecutor implements StepExecutor {

    @Autowired
    private MonitorService monitorService;

    @Override
    public boolean execute(Step step, Map env, SshClient sshClient) {
        boolean result = false;

        Map commandsMap = (Map) JSONUtils.parse(step.getExecutorParams());
        String commands = (String) commandsMap.get("command");

        //用变量解析命令
        commands = ScriptUtil.renderScript(commands, env);

        List<String> cmdList = Arrays.asList(commands.split("\\r\\n"));
        try {
            result = sshClient.exec(cmdList, out -> {
                monitorService.appendMachineLog(out);
            }) == 0;
        } catch (IOException e) {
            monitorService.appendMachineLog(StringUtil.getStackTrace(e));
            e.printStackTrace();
        }
        return result;
    }
}
