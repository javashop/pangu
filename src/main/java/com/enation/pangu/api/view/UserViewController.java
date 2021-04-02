package com.enation.pangu.api.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shen
 * @create 2020-12-23-19:32
 */
@Controller
@RequestMapping("/view")
public class UserViewController {
    @GetMapping("/login")
    public String login() {

        return "/user/login";
    }

    @GetMapping("/user/list")
    public String list() {

        return "/user/list";
    }
}
