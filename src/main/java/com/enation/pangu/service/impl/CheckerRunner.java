package com.enation.pangu.service.impl;

import com.enation.pangu.domain.Copy;
import com.enation.pangu.domain.ExecutorVO;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.enums.PluginPathTypeEnum;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.utils.ScriptUtil;
import com.enation.pangu.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 检查器运行器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/3/11
 */
@Service
public class CheckerRunner extends BasePluginRunner{

    /**
     * 检测器检测成功需要输出的字样
     */
    private static final String SUCCESS_TEXT = "1";


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
     * 执行检测器
     * @param executorId
     * @param env
     * @param sshClient
     * @return
     */
    boolean innerRun(String executorId, Map env, SshClient sshClient)throws Exception {
        //解析插件的yml
        ExecutorVO executorVO = pluginManager.parsePlugin(executorId, PluginType.checker, env);

        String checkFun = executorVO.getCheckFun();

        if (StringUtil.isEmpty(checkFun)) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"检查器未实现checkfun");
        }

        //解析后端检测方法
        checkFun = ScriptUtil.renderScript(checkFun, env, "", PluginPathTypeEnum.resource.value());


        String target = "${workspace}/checker_runner.sh";
        target = ScriptUtil.renderScript(target, env);

        //copy checker_runner.sh 最终要执行整个脚本来检测
        Copy checkRunnerFile = new Copy();
        checkRunnerFile.setSource("/checker/checker_runner.sh");
        checkRunnerFile.setTarget(target);
        env.put("checkFun", checkFun);
        checkRunnerFile.setParseEnv(true);

        //先将需要copy的文件传输至服务器
        List<Copy> copyList = executorVO.getCopyList();
        if (copyList == null) {
            copyList = new ArrayList<>();
        }

        copyList.add(checkRunnerFile);
        copy(copyList,env,sshClient);


        String checkCommand = "sh " + target;

        AtomicBoolean checkResult = new AtomicBoolean(false);

        //检测最终输出
       sshClient.exec1(checkCommand, text -> {
//           monitorService.appendMachineLog(text);

           if (text.startsWith(SUCCESS_TEXT)) {
               checkResult.set(true);
            }
        }) ;

        return  checkResult.get();
    }

}
