package com.enation.pangu;

import com.enation.pangu.domain.Copy;
import com.enation.pangu.enums.PluginPathTypeEnum;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientFactory;
import com.enation.pangu.utils.ScriptUtil;
import com.jcraft.jsch.*;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测器测试
 * @author zhangsong
 * 2020/11/3
 */

public class CheckerTest {


    /**
     * 检测测试
     * @throws ScriptException
     */
    @Test
    public void check() throws ScriptException {

        execCheck("port_checker");
    }

    private boolean execCheck(String checkId) {

        //环境变量
        Map env = new HashMap();
        String script = ScriptUtil.renderScript(checkId + ".yml", env, "checker/port_checker", PluginPathTypeEnum.resource.value());
        Map scriptMap = parseExecutorMap(script);

        Map command = (Map) scriptMap.get("command");
        Map shell = (Map) command.get("shell");
        String fileName = (String) shell.get("file");
        String targetDir = (String) shell.get("target_dir");
        Integer checkText = (Integer) command.get("check_text");
        List<String> execList = (List<String>) command.get("exec");
        StringBuilder sb = new StringBuilder();

        for (String exec : execList) {
            sb.append(exec + ";");
        }

        SshClient sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.55", 22);

        try {
            String path = ResourceUtils.getFile("classpath:checker/port_checker/" + fileName).getPath();

            Copy copy = new Copy();
            copy.setSource(path);
            copy.setTarget(targetDir);
            sshClient.copyFile(copy, env);

            boolean checkPortResult = false;
            int result = sshClient.exec(sb.toString(), out -> {
                System.out.println("检测端口返回：" + out);
                if (checkText.toString().equals(out)) {
                    System.out.println("成功");
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map parseExecutorMap(String script) {
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(script);
        List<Map> list = new ArrayList<>();
        result.forEach((v) -> {
            Map map = (Map) v;
            list.add(map);
        });
        return list.get(0);
    }
}
