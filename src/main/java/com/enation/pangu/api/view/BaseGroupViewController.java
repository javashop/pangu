package com.enation.pangu.api.view;

import com.enation.pangu.mapper.BaseGroupMapper;
import com.enation.pangu.model.BaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gy
 * @version v1.0
 * @date 2021/2/25
 * @since v7.2.0
 */
@Controller
@RequestMapping("/view")
public class BaseGroupViewController {

    @Autowired
    private BaseGroupMapper baseGroupMapper;

    /**
     * 跳转部署分组
     *
     * @return
     */
    @GetMapping("/deploymentGroup/list")
    public String list() {
        return "deployment_group/list";
    }

    /**
     * 跳转部署分组
     *
     * @return
     */
    @GetMapping("/deploymentGroup/add")
    public String add() {
        return "deployment_group/add";
    }

    /**
     * 跳转部署分组
     *
     * @return
     */
    @GetMapping("/deploymentGroup/edit/{group_id}")
    public String edit(Model model, @PathVariable("group_id") Long groupId) {
        BaseGroup baseGroup = baseGroupMapper.selectById(groupId);
        model.addAttribute("baseGroup", baseGroup);
        return "deployment_group/edit";
    }

    /**
     * 跳转机器分组
     *
     * @return
     */
    @GetMapping("/machinesGroup/list")
    public String machinesList() {
        return "machines_group/list";
    }

    /**
     * 跳转机器分组
     *
     * @return
     */
    @GetMapping("/machinesGroup/add")
    public String machinesAdd() {
        return "machines_group/add";
    }

    /**
     * 跳转机器分组
     *
     * @return
     */
    @GetMapping("/machinesGroup/edit/{group_id}")
    public String machinesEdit(Model model, @PathVariable("group_id") Long groupId) {
        BaseGroup baseGroup = baseGroupMapper.selectById(groupId);
        model.addAttribute("baseGroup", baseGroup);
        return "machines_group/edit";
    }



}
