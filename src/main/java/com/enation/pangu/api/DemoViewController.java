package com.enation.pangu.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shen
 * @create 2020-10-29-18:38
 */
@Controller
@RequestMapping("/view/demo")
public class DemoViewController {

    @GetMapping("/list")
    public String listDemo(Model model) {
        model.addAttribute("ctx", "");

        return "demo/list";
    }


}
