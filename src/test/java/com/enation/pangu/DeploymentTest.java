package com.enation.pangu;

import com.enation.pangu.domain.Copy;
import com.enation.pangu.enums.ExecutorEnum;
import com.enation.pangu.enums.PluginPathTypeEnum;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientFactory;
import com.enation.pangu.utils.ScriptUtil;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部署测试
 *
 * @author zhangsong
 * 2020/11/3
 */

public class DeploymentTest {

    /**
     * 部署测试
     * @throws ScriptException
     */
    @Test
    public void deployment() throws Exception{

        String deploymentId = "b2b2c_api";

        execDeployment(deploymentId);
    }

    private void execDeployment(String deploymentId) throws Exception{
        Map env = new HashMap();
        Map scriptMap = parseDeploymentMap(deploymentId, env);

        List<Map> stepList = (List<Map>)scriptMap.get("stepList");

        //打开一个session
        SshClient sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.55", 22);

        for(Map step : stepList){
            step = (Map)step.get("step");

            //执行executor
            Map executor = (Map)step.get("executor");
            execExecutor(executor, sshClient);

            //执行checker
            Map checker = (Map)step.get("checker");
            String checkerId = (String)checker.get("id");
            boolean b = execCheck(checkerId, sshClient);

            //检测checker执行结果
            if(b){
                System.out.println("执行器："+executor.get("id")+"检测结果为成功\n");
            }else{
                System.err.println("执行器："+executor.get("id")+"检测结果为失败\n");
                break;
            }
        }

        //关闭session
        sshClient.disconnect();
    }

    private void execExecutor(Map executor, SshClient sshClient) throws Exception{
        String executorId = (String)executor.get("id");
        List<String> execList;
        if(ExecutorEnum.ssh.executorId().equals(executorId)){
            execList = (List<String>) executor.get("commands");
        }else {
            Map env = new HashMap();
            Map map = parseExecutorMap(executorId, env);

            Map command = (Map) map.get("command");
            execList = (List<String>) command.get("exec");
        }


        String commands = execList.stream().collect(Collectors.joining(";"));

        sshClient.exec(commands, out -> {
            System.out.println(out);
        });


    }

    private boolean execCheck(String checkId, SshClient sshClient) throws Exception{
        Map env = new HashMap();
        Map scriptMap = parseCheckMap(checkId, env, "port_checker");

        Map command = (Map) scriptMap.get("command");
        Map shell = (Map) command.get("shell");
        String fileName = (String) shell.get("file");
        String targetDir = (String) shell.get("target_dir");
        Integer checkText = (Integer) command.get("check_text");
        List<String> execList = (List<String>) command.get("exec");
        String commands = execList.stream().collect(Collectors.joining(";"));

        String path = ResourceUtils.getFile("classpath:checker/port_checker/" + fileName).getPath();
        Copy copy = new Copy();
        copy.setSource(path);
        copy.setTarget(targetDir);
        sshClient.copyFile(copy, env);
        StringBuilder builder = new StringBuilder();
        sshClient.exec(commands, out -> {
            builder.append(out);
        });
        String consoleStr = builder.toString().replaceAll("\n","");
        if(checkText.toString().equals(consoleStr)){
            return true;
        }
        return false;
    }


    /**
     * 将deployment.yml解析成map
     * @param deploymentId
     * @param env
     * @return
     */
    private Map parseDeploymentMap(String deploymentId, Map env) {
        String script = ScriptUtil.renderScript(deploymentId+".yml", env, "deployment", PluginPathTypeEnum.resource.value());
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(script);
        List<Map> list = new ArrayList<>();
        result.forEach((v) -> {
            Map map = (Map) v;
            list.add(map);
        });

        Map map = list.remove(0);
        map.put("stepList", list);
        return map;
    }

    /**
     * 将executor.yml解析成map
     * @param executorId
     * @param env
     * @return
     */
    private Map parseExecutorMap(String executorId, Map env) {
        String script = ScriptUtil.renderScript(executorId+".yml", env, "executor", PluginPathTypeEnum.resource.value());
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(script);
        List<Map> list = new ArrayList<>();
        result.forEach((v) -> {
            Map map = (Map) v;
            list.add(map);
        });
        return list.get(0);
    }


    /**
     * 将checker.yml解析成map
     * @param checkerId
     * @param env
     * @return
     */
    private Map parseCheckMap(String checkerId, Map env, String checkType) {
        String script = ScriptUtil.renderScript(checkerId + ".yml", env, "checker/"+checkType, PluginPathTypeEnum.resource.value());
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
