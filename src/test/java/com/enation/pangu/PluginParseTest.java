package com.enation.pangu;

import com.enation.pangu.enums.PluginPathTypeEnum;
import com.enation.pangu.mapper.PluginMapper;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.utils.ScriptUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.Yaml;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 * 执行器插件测试
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class PluginParseTest {


    /**
     * 插件解析测试
     * @throws ScriptException
     */
    @Test
    public void pluginParse() throws ScriptException {
        Map env = new HashMap();
        env.put("username", "kingapex");
        env.put("password", "abc");
        env.put("url", "gitxx.com");
        env.put("dir", "/opt");
//        String script = ScriptUtil.renderScript("port_checker.yml", env, "plugin/port_checker");

        String script = ScriptUtil.renderScript("git_clone.yml", env, "executor", PluginPathTypeEnum.resource.value());
        Yaml yaml = new Yaml();
        Iterable<Object> result =    yaml.loadAll(script);
        result.forEach(( v)->{
            Map map = (Map)v;
            out.println(map);
        });
    }


    /**
     * 部署yml文件解析测试
     */
    @Test
    public void deploymentParse() {
        Map env = new HashMap();
        String script = ScriptUtil.renderScript("b2b2c_set.yml", env, "set", PluginPathTypeEnum.resource.value());
        Yaml yaml = new Yaml();
        Iterable<Object> result =  yaml.loadAll(script);
        result.forEach(( v)->{
            Map map = (Map)v;
            out.println(map);
        });
    }

    @Autowired
    ExecutorManager executorManager;

    @Autowired
    PluginMapper pluginMapper;

//    @Test
//    public void listTest() {
//        List<PluginMetaDataVO> list = executorManager.list();
//
//        for (PluginMetaDataVO plugin : list) {
//            out.println(plugin.getName());
//
//            Plugin pluginDo = new Plugin();
//            pluginDo.setPluginId(plugin.getId());
//            pluginDo.setName(plugin.getName());
//            pluginDo.setAuthor(plugin.getAuthor());
//            pluginDo.setAuthorUrl(plugin.getAuthorUrl());
//            pluginDo.setDesc(plugin.getDesc());
//            pluginDo.setKind(PluginType.executor.name());
//            pluginDo.setPath(plugin.getPath());
//
//            pluginMapper.insert(pluginDo);
//        }
//    }

}
