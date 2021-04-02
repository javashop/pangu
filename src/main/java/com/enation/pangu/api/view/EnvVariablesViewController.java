package com.enation.pangu.api.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 环境变量视图控制器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/11
 */
@Controller
@RequestMapping("/view/env-variables/{projectId}")
public class EnvVariablesViewController {


    @GetMapping("/list")
    public String list(@PathVariable("projectId") Long projectId, Model model) {
        model.addAttribute("project_id",projectId);
        return "env/env_variables";
    }



}
