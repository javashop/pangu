package com.enation.pangu.api.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标签视图控制器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/9
 */
@Controller
@RequestMapping("/view/tag")
public class TagViewController {

    @GetMapping("/list")
    public String list(Model model) {
        return "tag/list";
    }


}
