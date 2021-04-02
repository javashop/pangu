package com.enation.pangu.api.view;

import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentSetManager;
import com.enation.pangu.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *
 * 部署集View控制器
 * @author 褚帅
 * 邮箱：chushuai0429@qq.com
 * 2020/10/31
 *
 */
@Controller
@RequestMapping("/view/deploymentSet")
public class DeploymentSetViewController {

    @Autowired
    private DeploymentSetManager deploymentSetManager;

    @Autowired
    private TaskManager taskManager;

    /**
     * 跳转到部署集列表页
     * @return
     */
    @GetMapping("/list")
    public String list(Model model){
        List<DeploymentSet> list = deploymentSetManager.findList();
        model.addAttribute("list",list);
        return "/deployment_set/list";
    }

    /**
     * 跳转到部署集列表页
     * @return
     */
    @PostMapping("/add")
    public String add(String deploymentSetName,Model model){
        DeploymentSet deploymentSet = new DeploymentSet();
        deploymentSet.setName(deploymentSetName);
        deploymentSet.setAddTime(System.currentTimeMillis());
        deploymentSet.setStatus(0);
        DeploymentSet retBean = deploymentSetManager.add(deploymentSet);
        DeploymentSetVO detail = deploymentSetManager.findDetail(retBean.getId());
        model.addAttribute("detail",detail);
        return "deployment_set/edit";
    }


    /**
     * 转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        DeploymentSet detail = deploymentSetManager.getById(id);
        model.addAttribute("detail",detail);
        return "deployment_set/edit";
    }


    /**
     * 转到部署作业页面
     */
    @GetMapping("/deploy/{id}")
    public String deploy(Model model, @PathVariable Long id, Long setTaskId) {
        DeploymentSet deploymentSet = deploymentSetManager.getById(id);

        List<Deployment> deploymentList = deploymentSetManager.selectDeploymentList(id);

        model.addAttribute("deploymentSet", deploymentSet);
        model.addAttribute("deploymentList", deploymentList);

        if(setTaskId != null){
            Task setTask = taskManager.selectById(setTaskId);
            if(setTask != null){
                model.addAttribute("setTaskId", setTask.getId());
                model.addAttribute("setTaskState", setTask.getState());
            }
        }

        return "deployment_set/deploy";
    }

    /**
     * 转到部署历史页面
     */
    @GetMapping("/history/{id}")
    public String history(Model model, @PathVariable Long id) {
        DeploymentSet deploymentSet = deploymentSetManager.getById(id);
        model.addAttribute("deploymentSetId",id);
        model.addAttribute("deploymentSetName",deploymentSet.getName());
        return "deployment_set/history";
    }
}
