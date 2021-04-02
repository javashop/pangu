package com.enation.pangu.task.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.domain.Copy;
import com.enation.pangu.mapper.ConfigFileMapper;
import com.enation.pangu.mapper.ConfigProjectMapper;
import com.enation.pangu.model.ConfigFile;
import com.enation.pangu.model.ConfigFile;
import com.enation.pangu.model.ConfigProject;
import com.enation.pangu.model.Step;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.task.StepExecutor;
import com.enation.pangu.utils.PathUtil;
import com.enation.pangu.utils.ScriptUtil;
import com.enation.pangu.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 写入配置文件的操作
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/1/18
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service("write_config")
public class WriteConfigStepExecutor implements StepExecutor {

    @Autowired
    private ConfigFileMapper configFileMapper;

    @Autowired
    private ConfigProjectMapper projectMapper;

    public static void main(String[] args) {
        String target = "${wrokspace}/server/config-server-starup/";
        Map env = new HashMap();
        env.put("workspace", "/opt");
        target = ScriptUtil.renderScript(target, env);
        System.out.println(target);

    }
    @Autowired
    private MonitorService monitorService;
    private static final String ALL_FILE_TEXT = "全部";

    private String getFilePath(String projectName, String filename) {

        String filePath =  File.separator + projectName + File.separator + filename;
        return filePath;
    }

    private ConfigProject getProjectByName(String projectName) {
        QueryWrapper queryWrapper = new QueryWrapper<ConfigProject>().eq("name", projectName);
        ConfigProject project =   projectMapper.selectOne(queryWrapper);
        return project;
    }

    private boolean writeFile(String projectName, String filename,String target,Map env, SshClient sshClient) {

        ConfigProject project = this.getProjectByName(projectName);

        QueryWrapper queryWrapper = new QueryWrapper<ConfigFile>()
                .eq("config_project_id",project.getId()).eq("name", filename)
                .orderByDesc("edit_time");
        ConfigFile configFile = configFileMapper.selectOne(queryWrapper);
        String content = configFile.getContent();

        String filePath = getFilePath(projectName, filename);
        String absPath = PathUtil.getRootPath() + filePath;

        File file = new File(absPath);
        try {
            sshClient.exec("mkdir -p " + target,text->{});
            FileUtils.writeByteArrayToFile(file, content.getBytes(StandardCharsets.UTF_8));
            String localPath = "file:" + PathUtil.getRootPath() + filePath;
            Copy copy = new Copy();
            copy.setSource(localPath);
            copy.setTarget(target);
            sshClient.copyFile(copy, env);
            file.delete();
            monitorService.appendMachineLog("配置文件【"+filename+"]写入到["+target+"]");
            return true;
        } catch (Exception e) {
            monitorService.appendMachineLog(StringUtil.getStackTrace(e));
        }

        return false;
    }



    @Override
    public boolean execute(Step step, Map env, SshClient sshClient) {
        String executorParams = step.getExecutorParams();
        if (StringUtil.isEmpty(executorParams)) {
            monitorService.appendMachineLog("写入配置文件参数为空，无法执行");
            return false;
        }

        Map<String, String> params = (Map<String, String>) JSONUtils.parse(step.getExecutorParams());
        String project = params.get("project");
        String file = params.get("file");
        String target = params.get("target");

        if (!checkEmpty(project, file, target)) {
            return false;
        }

        //用变量解析target路径
        target = ScriptUtil.renderScript(target, env);

        try {

              //创建目标文件夹
            sshClient.exec("mkdir -p " + target , log -> {});

            //如果是'全部'，则copy整个项目文件夹
            //否则copy指定文件
            if (ALL_FILE_TEXT.equals(file)) {
                return copyDir(project, sshClient, target, env);
            }else {
                return this.writeFile(project ,file, target,env,sshClient);

            }

         } catch (IOException e) {
            monitorService.appendMachineLog(StringUtil.getStackTrace(e));
        }
        return false;
    }

    private boolean copyDir(String projectName, SshClient sshClient,String target,Map env) throws IOException {

        boolean result  =false;
        ConfigProject project = this.getProjectByName(projectName);
        QueryWrapper queryWrapper = new QueryWrapper<ConfigFile>()
                .eq("config_project_id",project.getId())
                .orderByDesc("edit_time");
        List<ConfigFile> fileList = configFileMapper.selectList(queryWrapper);
        for (ConfigFile configFile : fileList) {
            String fileName = configFile.getName();
            result = this.writeFile(projectName, fileName, target, env, sshClient);
            if (!result) {
                break;
            }
        }

        return result;

    }


    private boolean checkEmpty( String project ,String file,String target) {
        if (StringUtil.isEmpty(project)) {
            monitorService.appendMachineLog("写入配置文件参数[project]为空，无法执行");
            return false;
        }

        if (StringUtil.isEmpty(file)) {
            monitorService.appendMachineLog("写入配置文件参数[file]为空，无法执行");
            return false;
        }
        if (StringUtil.isEmpty(target)) {
            monitorService.appendMachineLog("写入配置文件参数[target]为空，无法执行");
            return false;
        }
        return true;
    }

}
