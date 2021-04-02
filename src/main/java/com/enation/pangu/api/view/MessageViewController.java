package com.enation.pangu.api.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 消息控制器
 *
 * @author zhangsong
 * @date 2021/2/26
 */
@Controller
@RequestMapping("/view/message")
public class MessageViewController {

    /**
     * 转到列表页面
     */
    @GetMapping("/list")
    public String list() {

        return "message/list";
    }

}
