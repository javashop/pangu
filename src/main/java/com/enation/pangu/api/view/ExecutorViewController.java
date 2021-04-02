package com.enation.pangu.api.view;


import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.service.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 执行器视图控制器
 * @author zhanghao
 * @date 2020-11-11
 */
@Controller
@RequestMapping("/view/executor")
public class ExecutorViewController {

    @Autowired
    private ExecutorManager executorManager;

    @Autowired
    private PluginManager pluginManager;

    /**
     * 跳转到执行器列表页
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Plugin> list = pluginManager.list(PluginType.executor, null);
        model.addAttribute("list", list);
        return "/executor/list";
    }

    /**
     * 跳转到执行器列表页
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "/executor/add";
    }
}
