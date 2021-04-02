package com.enation.pangu;

import com.enation.pangu.domain.ExecutorVO;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.service.PluginManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.parser.ParserException;

import java.util.HashMap;

/**
 * 插件工具测试
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/3
 */

public class PluginUtilTest {
    @Autowired
    private PluginManager pluginManager;

    @Test
    public void testExecutorParse() {
        HashMap env = new HashMap();
        env.put("workspace", "/opt/workspace/1");
        env.put("project_root", "/opt/workspace/1");
        try{
            ExecutorVO executorVO = pluginManager.parsePlugin("install_maven", PluginType.executor, env);
            System.out.println(executorVO);
        }catch (   ParserException e){
            System.out.println("异常");
            e.printStackTrace();
        }

    }
}
