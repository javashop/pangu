package com.enation.pangu.api.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Javashop广告
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2021/3/3
 */
@Controller
@RequestMapping("/view/javashop")
public class JavashopViewController {

    @RequestMapping("/")
    public String javashop(Model model) {

        return "/javashop";
    }
}
