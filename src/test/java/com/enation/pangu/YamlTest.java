package com.enation.pangu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.enation.pangu.model.DeploymentImport;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

/**
 * @author zhangsong
 * 2020-01-11
 */

public class YamlTest {

    @Test
    public void testExport() throws IOException {

        Map map1 = new HashMap();
        map1.put("kind", "deployment");
        map1.put("name", "123");
        Map map2 = new HashMap();
        map2.put("kind", "deploymentSet");
        map2.put("name", "456");
        List list = new ArrayList();
        list.add(map1);
        list.add(map2);

        String dump1 = new Yaml().dumpAs(map1, null, DumperOptions.FlowStyle.BLOCK);
        String dump2 = new Yaml().dumpAs(map2, null, DumperOptions.FlowStyle.BLOCK);

        System.out.println(dump1 + "---\n" + dump2);

    }

    @Test
    public void testImport() throws IOException {

        File file = new File("D:/测试部署集.yml");
        InputStream inputStream = new FileInputStream(file);
        Iterable objects = new Yaml().loadAll(inputStream);

        List list = new ArrayList();
        objects.forEach(o -> {
            Map map = (Map) o;
            DeploymentImport deploymentImport = JSON.parseObject(JSON.toJSONString(map), DeploymentImport.class, Feature.OrderedField);
            list.add(deploymentImport);
        });

        System.out.println(list);

    }
}
