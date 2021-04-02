package com.enation.pangu.api.view;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.mapper.MachineTagMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/17
 */
@Controller
@RequestMapping("/view/task")
public class TaskViewController {

    @Autowired
    private DeploymentManager deploymentManager;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private MachineTagMapper machineTagMapper;

    /**
     * 某个部署的任务列表
     *
     * @param deploymentId
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Long deploymentId, Long parentTaskId, Model model) {
        Deployment deployment = deploymentManager.selectById(deploymentId);

        List<Machine> machineList = deploymentManager.getMachineList(deployment);

        List<Step> stepList = deploymentManager.selectStep(deploymentId);
        model.addAttribute("stepList", stepList);
        model.addAttribute("machineList", machineList);
        model.addAttribute("deployment", deployment);
        model.addAttribute("deploymentId", deploymentId);

        if (parentTaskId != null) {
            Task task = taskManager.selectById(parentTaskId);
            if (task != null) {
                model.addAttribute("parentTaskId", parentTaskId);
                model.addAttribute("taskState", task.getState());
            }
        }


        return "task/task";
    }


    /**
     * 某个部署的历史记录
     *
     * @param deploymentId
     * @param model
     * @return
     */
    @GetMapping("/history")
    public String history(Long deploymentId, Model model) {
        model.addAttribute("deploymentId", deploymentId);
        return "task/history";
    }

}
