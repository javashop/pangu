package com.enation.pangu;

import com.enation.pangu.domain.*;
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

public class DeploymentTestDomain {

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
        DeploymentVO deploymentVO = parseDeploymentVO(deploymentId, new HashMap());

        //打开一个session
        SshClient sshClient = SshClientFactory.createSsh("root", "752513", "192.168.2.55", 22);

        for(StepVO step : deploymentVO.getStepList()){
            //执行executor
            ExecutorVO executorVO = step.getExecutorVO();
            execExecutor(executorVO, sshClient);

            //执行checker
            CheckerVO checkerVO = step.getCheckerVO();
            boolean b = execCheck(checkerVO, sshClient);

            //检测checker执行结果
            if(b){
                System.out.println("执行器："+executorVO.getId()+"检测结果为成功\n");
            }else{
                System.err.println("执行器："+executorVO.getId()+"检测结果为失败\n");
                break;
            }
        }

        //关闭session
        sshClient.disconnect();
    }

    private void execExecutor(ExecutorVO executor, SshClient sshClient) throws Exception{

        sshClient.exec(executor.getExecList().stream().collect(Collectors.joining(";")), out -> {
            System.out.println(out);
        });

    }

    private boolean execCheck(CheckerVO checkerVO, SshClient sshClient) throws Exception{

        String localFilePath ="checker/port_checker/" + checkerVO.getShellFileName();
        Copy copy = new Copy();
        copy.setSource(localFilePath);
        copy.setTarget(checkerVO.getShellTargetDir());
        sshClient.copyFile(copy, new HashMap());

        StringBuilder builder = new StringBuilder();
        sshClient.exec(checkerVO.getExecList().stream().collect(Collectors.joining(";")), out -> {
            builder.append(out);
        });
        String consoleStr = builder.toString().replaceAll("\n","");
        if(checkerVO.getCheckText().equals(consoleStr)){
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
    private DeploymentVO parseDeploymentVO(String deploymentId, Map env) {
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

        List<StepVO> stepList = new ArrayList<>();
        for(Map stepMap : list){
            StepVO stepVO = new StepVO();
            stepMap = (Map)stepMap.get("step");
            Map executorMap = (Map)stepMap.get("executor");
            Map checkerMap = (Map)stepMap.get("checker");
            stepVO.setName((String)stepMap.get("name"));
            String executorId = (String) executorMap.get("id");
            ExecutorVO executorVO;
            if(ExecutorEnum.ssh.executorId().equals(executorId)){
                List<String> execList = (List<String>)executorMap.get("commands");
                executorVO = new ExecutorVO();
                executorVO.setExecList(execList);
            }else{
                executorVO = parseExecutorVO(executorId, new HashMap());
            }
            executorVO.setId(executorId);
            stepVO.setExecutorVO(executorVO);

            stepVO.setCheckerVO(parseCheckVO((String)checkerMap.get("id"), new HashMap(), "port_checker"));
            stepList.add(stepVO);
        }

        DeploymentVO deploymentVO = new DeploymentVO();
        deploymentVO.setStepList(stepList);
        return deploymentVO;
    }

    /**
     * 将executor.yml解析成vo
     * @param executorId
     * @param env
     * @return
     */
    private ExecutorVO parseExecutorVO(String executorId, Map env) {
        String script = ScriptUtil.renderScript(executorId+".yml", env, "executor", PluginPathTypeEnum.resource.value());
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(script);
        List<Map> list = new ArrayList<>();
        result.forEach((v) -> {
            Map map = (Map) v;
            list.add(map);
        });
        Map map = list.get(0);
        ExecutorVO executorVO = new ExecutorVO();
        Map commandMap = (Map) map.get("command");
        executorVO.setType((String)commandMap.get("type"));
        executorVO.setExecList((List<String>) commandMap.get("exec"));
        Map configMap = (Map) map.get("config");
        List<Map> itemMapList = (List<Map>) configMap.get("items");
        List<ExecutorConfigItemVO> itemList = new ArrayList<>();
        for(Map itemMap : itemMapList){
            ExecutorConfigItemVO item = new ExecutorConfigItemVO();
            item.setTitle((String)itemMap.get("title"));
            item.setName((String)itemMap.get("name"));
            item.setType((String)itemMap.get("type"));
            item.setHtmlcss((String)itemMap.get("htmlcss"));
            itemList.add(item);
        }
        PluginConfigVO executorConfigVO = new PluginConfigVO();
        executorConfigVO.setItemList(itemList);
        executorVO.setConfig(executorConfigVO);
        return executorVO;
    }


    /**
     * 将checker.yml解析成vo
     * @param checkerId
     * @param env
     * @return
     */
    private CheckerVO parseCheckVO(String checkerId, Map env, String checkType) {
        String script = ScriptUtil.renderScript(checkerId + ".yml", env, "checker/"+checkType, PluginPathTypeEnum.resource.value());
        Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(script);
        List<Map> list = new ArrayList<>();
        result.forEach((v) -> {
            Map map = (Map) v;
            list.add(map);
        });
        Map map = list.get(0);
        CheckerVO checkerVO = new CheckerVO();
        Map commandMap = (Map) map.get("command");
        Map shellMap = (Map) commandMap.get("shell");
        checkerVO.setType((String)commandMap.get("type"));
        checkerVO.setExecList((List<String>) commandMap.get("exec"));
//        checkerVO.setConfigList((List<String>) map.get("config"));
        checkerVO.setShellFileName((String)shellMap.get("file"));
        checkerVO.setShellTargetDir((String)shellMap.get("target_dir"));
        checkerVO.setCheckText(((Integer)commandMap.get("check_text")).toString());

        return checkerVO;
    }
}
