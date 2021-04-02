package com.enation.pangu.api.view;

import com.enation.pangu.model.ConfigProject;
import com.enation.pangu.service.ConfigProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * 项目视图控制器
 *
 * @author lizhengguo
 * @date 2020-11-12
 */
@Controller
@RequestMapping("/view/config_project")
public class ConfigProjectViewController {

    @Autowired
    private ConfigProjectManager configProjectManager;

    /**
     * 跳转到项目列表页
     *
     * @return
     */
    @GetMapping()
    public String list() {
        return "/config_file/project_list";
    }

    /**
     * 编辑项目
     */
    @GetMapping("/show/{id}")
    public String editProject(Model model, @PathVariable Long id) {
        model.addAttribute("config_project_id", id);
        ConfigProject projectDO = configProjectManager.selectById(id);
        model.addAttribute("config_project_name", projectDO.getName());
        return "/config_file/list";
    }

}
