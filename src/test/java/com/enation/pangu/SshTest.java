package com.enation.pangu;

import com.enation.pangu.domain.Copy;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientFactory;
import com.enation.pangu.ssh.impl.JschImpl;
import com.jcraft.jsch.JSchException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/10/27
 */

public class SshTest {

    /**
     * ssh 命令执行测试
     * @throws JSchException
     * @throws IOException
     */
    @Test
    public void testExec() throws  IOException {
        JschImpl   sshClient = (JschImpl)SshClientFactory.createSsh("root", "752513", "192.168.2.105", 22);

        sshClient.exec("sh /opt/workspace/git_clone.sh",text->{
            out.println(text);
        });
        sshClient.disconnect();
    }

    Map env = new HashMap();

    //环境变量
    String workspace = "/opt/workspace/1";

    @Before
    public  void init() throws IOException {
        env.put("workspace",workspace);
    }

    /**
     * scp 测试
     * @throws IOException
     */
    @Test
    public void testScp() throws  IOException {
        SshClient         sshClient = SshClientFactory.createSsh("root", "Enation752513", "47.105.128.165", 22);

        env.put("username", "4343");
        env.put("password", "3434");
        env.put("git_url", "https://gitee.com/javashop/pangu.git");

        Copy copy = new Copy();
        copy.setSource("executor/git_clone.sh");
        copy.setTarget("/opt/git_clone.sh");
        sshClient.copyFile(copy, env);

        sshClient.disconnect();
    }

    public static void main(String[] args) throws IOException {
        Map env = new HashMap();
        env.put("workspace", "/opt/workspace");


        Map repository = new HashMap();
        repository.put("address","https://gitee.com/javashop/vue-demo.git");
        repository.put("auth_type","password");
        repository.put("username", "333");
        repository.put("password", "3333");

        env.put("repository", repository);
        StringWriter writer = new StringWriter();
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

            cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(),"executor/git_clone");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setNumberFormat("#.##");
            cfg.setClassicCompatible(true);


            Template temp = cfg.getTemplate("git_clone.sh");
            temp.process(env, writer);
             System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

}
