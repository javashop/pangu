package com.enation.pangu.service.impl;

import com.enation.pangu.constant.PluginConstant;
import com.enation.pangu.domain.Copy;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.service.PluginManager;
import com.enation.pangu.service.PluginRunner;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.utils.ParseException;
import com.enation.pangu.utils.PathUtil;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 插件运行基类
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/3/11
 */

public abstract class BasePluginRunner implements PluginRunner {

    @Autowired
    PluginManager pluginManager;

    @Autowired
    MonitorService monitorService;

    /**
     * 通过ssh copy文件
     * @param copyList 要copy的文件列表
     * @param env 环境变量，文件中可能会用到，文件实际上是一个freemarker 模板
     * @param sshClient 建立好连接的ssh客户端
     */
    void copy(List<Copy> copyList, Map env, SshClient sshClient) {
        if (copyList != null) {
            for (Copy copy : copyList) {
                formatSource(copy);
                try{
                    sshClient.copyFile(copy, env);
                }catch (ParseException | IOException e){
                    monitorService.appendMachineLog(StringUtil.getStackTrace(e));
                }

            }
        }
    }

    private void formatSource(Copy copy) {
        String source = copy.getSource();
        //设置为文件全路径
        if(source.startsWith("file:")){
            source = source.replaceAll("file:", "");
            source = "file:" + PathUtil.getRootPath() + File.separator + PluginConstant.PLUGINS_PATH_PROFIX + File.separator + source;
            copy.setSource(source);
        }
    }
}
