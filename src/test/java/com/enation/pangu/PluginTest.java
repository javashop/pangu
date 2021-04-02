package com.enation.pangu;

import com.enation.pangu.domain.PluginType;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.service.PluginRunner;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientFactory;
import com.enation.pangu.utils.ScriptUtil;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

/**
 * 执行器测试
 *
 * @author zhangsong
 * 2020/11/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class PluginTest {

    /**
     * 插件业务类
     */
    @Autowired
    ExecutorManager executorManager;

    /**
     * ssh客户端
     */
    SshClient sshClient;


    /**
     * 环境变量
     */
    Map env = new HashMap();

    //环境变量
    String workspace = "/opt/workspace/1";

    /**
     * 执行器运行实现类
     */
    @Autowired
    private PluginRunner executorRunner;


    /**
     * 测试前构建ssh client及 workspace
     * @throws IOException
     */
    @Before
    public  void init() throws IOException {
        env.put("workspace",workspace);

        //在这里定义测试机的信息
        sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.105", 22);

        //先创建workspace
//        sshClient.exec("mkdir -p "+ workspace,text->{
//            out.println(text);
//        });

    }

    /**
     * 测试结束后断开ssh连接
     * @throws IOException
     */
    @After
    public void clean() throws IOException {
        sshClient.disconnect();
    }


    /**
     * 克隆源码执行器测试
     * @throws ScriptException
     */
    @Test
    public void gitClone() throws IOException {
        //环境变量

        env.put("username", "2323");
        env.put("password", "2323");
        env.put("git_url", "https://gitee.com/javashop/pangu.git");
        //执行插件，第一个参数时插件id，第二个参数是插件类型，第三个参数是环境变量，第四个参数是ssh客户端
        boolean result = executorRunner.run("git_clone", env, sshClient);
        out.println(result);
    }


    @Autowired()
    PluginRunner checkerRunner;

    /**
     * 检查器测试
     * @throws IOException
     */
    @Test
    public void checkerTest() throws IOException {

        env.put("port", "81");
        boolean result = checkerRunner.run("port_checker",env,sshClient);
        out.println(result);
    }

    /**
     * 安装jdk执行器测试
     * @throws ScriptException
     */
    @Test
    public void installJdk() throws IOException {

        boolean result =  executorRunner.run("install_jdk", env, sshClient);
        out.println(result);

    }

    /**
     * 安装maven测试
     * @throws IOException
     */
    @Test
    public void installMaven() throws IOException {
        boolean result =  executorRunner.run("install_maven", env, sshClient);
        out.println(result);
    }


    @Test
    public void all() throws IOException {
        installJdk();
        installMaven();
        gitClone();
        List<String> command = new ArrayList<>();

        command.add("cd /opt/workspace/1/source/pangu");
        command.add("mvn clean install -DskipTests");
        command.add("mkdir -p /opt/workspace/1/server");
        command.add("cp /opt/workspace/1/source/pangu/target/pangu-1.0.0.jar /opt/workspace/1/server/pangu-1.0.0.jar");
        command.add("nohup java  -jar  /opt/workspace/1/server/pangu-1.0.0.jar > /opt/pang.log &");
        int result  = sshClient.exec(command ,text->{
            out.println(text);
        });

        out.println(result);
    }


}
