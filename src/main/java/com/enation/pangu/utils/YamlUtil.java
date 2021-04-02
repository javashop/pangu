package com.enation.pangu.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.enation.pangu.model.DeploymentImport;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * yaml文件工具类
 *
 * @author zhangsong
 * 2020-01-16
 */
public final class YamlUtil {


    /**
     * 实体类转为yaml字符串
     * @param object
     * @return
     */
    public static String getYaml(Object object){
        Map map = JSON.parseObject(JSON.toJSONString(object), LinkedHashMap.class, Feature.OrderedField);
        return new Yaml().dumpAs(map, null, DumperOptions.FlowStyle.BLOCK);
    }
}