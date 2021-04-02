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
public class RsaKeysViewController {


    @GetMapping("/keys/list")
    public String list() {

        return "/rsa/list";
    }
}
