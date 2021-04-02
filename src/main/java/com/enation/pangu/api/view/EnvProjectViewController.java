package com.enation.pangu.api.view;

import com.enation.pangu.service.EnvProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shen
 * @create 2020-12-01-18:32
 */
@Controller
@RequestMapping("/view/env-project")
public class EnvProjectViewController {
    @Autowired
    private EnvProjectManager envProjectManager;
    @GetMapping("/list")
    public String list(Model model) {
        return "env/env_project";
    }

}
