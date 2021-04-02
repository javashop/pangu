package com.enation.pangu.api.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 检测器视图控制器
 * @author zhanghao
 * @date 2020-11-11
 */
@Controller
@RequestMapping("/view/checker")
public class CheckerViewController {

    /**
     * 跳转到检查器列表页
     * @return
     */
    @GetMapping("/list")
    public String list(){
        return "/checker/list";
    }

    /**
     * 跳转到检查器安装页
     * @return
     */
    @GetMapping("/add")
    public String add(){
        return "/checker/add";
    }
}
