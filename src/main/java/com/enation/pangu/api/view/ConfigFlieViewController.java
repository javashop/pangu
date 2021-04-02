package com.enation.pangu.api.view;

import com.enation.pangu.model.ConfigFile;
import com.enation.pangu.model.ConfigProject;
import com.enation.pangu.service.ConfigFileManager;
import com.enation.pangu.service.ConfigProjectManager;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 项目文件控制器
 *
 * @author zhangsong
 * @date 2020-10-31
 */
@Controller
@RequestMapping("/view/config_project")
public class ConfigFlieViewController {

    @Autowired
    private ConfigFileManager configFileManager;

    @Autowired
    private ConfigProjectManager configProjectManager;

    /**
     * 转到编辑页面
     */
    @GetMapping("/config_file/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        ConfigFile configFile = configFileManager.selectById(id);
        if (configFile != null) {
            ConfigProject projectDO = configProjectManager.selectById(configFile.getConfigProjectId());
            configFile.setConfigProjectName(projectDO.getName());
        }
        if (configFile.getConfigProjectName().isEmpty()) {
            configFile.setConfigProjectName("当前项目");
        }

        model.addAttribute("config_file", configFile);
        return "/config_file/edit";
    }


    /**
     * 跳转配置文件列表
     */
    @GetMapping("/{id}")
    public String page(Model model, @PathVariable Long id) {
        model.addAttribute("config_project_id", id);
        ConfigProject project = configProjectManager.selectById(id);
        model.addAttribute("config_project_name", project.getName());
        return "/config_file/list";
    }

}
