package com.enation.pangu.utils;

import com.enation.pangu.enums.PluginPathTypeEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本生成工具类
 *
 * @author duanmingyu
 * @version v1.0
 * @date 2020-01-06
 * @since v7.2.0
 */
public class ScriptUtil {


    /**
     * 渲染并读取脚本内容
     *
     * @param name  脚本模板名称（例：test.js，test.html，test.ftl等）  或文件绝对路径
     * @param model 渲染脚本需要的数据内容
     * @return
     */
    public static String renderScript(String name, Map<String, Object> model, String path, String pathType) {
        StringWriter stringWriter = new StringWriter();

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

            if(PluginPathTypeEnum.resource.value().equals(pathType)){
                cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), path);
            }else {
                File file = new File(PathUtil.getRootPath() + name);
                cfg.setDirectoryForTemplateLoading(file.getParentFile());
                name = file.getName();
            }

            cfg.setDefaultEncoding("UTF-8");
            cfg.setNumberFormat("#.##");
            cfg.setClassicCompatible(true);

            Template temp = cfg.getTemplate(name);
            temp.process(model, stringWriter);

            stringWriter.flush();

            return stringWriter.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stringWriter.close();
            } catch (IOException ex) {
            }
        }

        return null;
    }


    /**
     * 解析字串
     *
     * @param str
     * @param model
     * @return
     */
    public static String renderScript(String str, Map<String, Object> model) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setTemplateLoader(new StringTemplateLoader(str));
        cfg.setDefaultEncoding("UTF-8");

        Template template = null;
        try {
            template = cfg.getTemplate("");


            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return (writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return "";

    }


    public static void main(String[] args) throws IOException {

//        ClassLoader classLoader = Thread.currentThread()
//                .getContextClassLoader();
//        InputStream stream = classLoader.getResourceAsStream("executor/docker1/install_docker.yml");
//        String content = IOUtils.toString(stream, "utf-8");
//        System.out.println(content);
    }
}
