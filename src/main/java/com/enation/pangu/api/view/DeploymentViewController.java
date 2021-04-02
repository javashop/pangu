package com.enation.pangu.api.view;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.enums.GroupEnum;
import com.enation.pangu.mapper.BaseGroupMapper;
import com.enation.pangu.enums.PluginStatusEnum;
import com.enation.pangu.mapper.MachineTagMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 部署控制器
 *
 * @author zhangsong
 * @date 2020-10-31
 */
@Controller
@RequestMapping("/view/deployment")
public class DeploymentViewController {

    @Autowired
    private DeploymentManager deploymentManager;
    @Autowired
    private RepositoryManager repositoryManager;
    @Autowired
    private MachineManager machineManager;
    @Autowired
    private TagManager tagManager;

    @Autowired
    private BaseGroupMapper baseGroupMapper;


    /**
     * 转到列表页面
     */
    @GetMapping("/list")
    public String list() {

        return "deployment/list";
    }

    /**
     * 转到添加页面
     */
    @GetMapping("/add")
    public String add(Model model) {
        //仓库列表
        List<Repository> repositoryList = repositoryManager.list();
        model.addAttribute("repositoryList", repositoryList);

        //机器列表
        List<Machine> machineList = machineManager.listAll();
        model.addAttribute("machineList", machineList);


        //标签列表
        List<Tag> tagList = tagManager.list();
        model.addAttribute("tagList", tagList);

        //环境变量列表
        List envProjectList = envProjectManager.getAll();
        model.addAttribute("envProjectList", envProjectList);

        //分组列表
        List<BaseGroup> baseGroupList = baseGroupMapper.selectList(new QueryWrapper<BaseGroup>().eq("type", GroupEnum.DEPLOY.value()).orderByDesc("add_time"));
        model.addAttribute("baseGroupList", baseGroupList);


        return "deployment/add";
    }

    /**
     * 转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        //部署信息
        Deployment deployment = deploymentManager.selectById(id);
        List<Long> machineIdList = deploymentManager.selectMachineIdList(id);
        deployment.setMachineIdList(machineIdList);
        model.addAttribute("deployment", deployment);

        //仓库列表
        List<Repository> repositoryList = repositoryManager.list();
        model.addAttribute("repositoryList", repositoryList);


        //标签列表
        List<Tag> tagList = tagManager.list();
        model.addAttribute("tagList", tagList);

        //机器列表
        List<Machine> machineList = machineManager.listAll();
        model.addAttribute("machineList", machineList);

        //环境变量列表
        List envProjectList = envProjectManager.getAll();
        model.addAttribute("envProjectList", envProjectList);

        //分组列表
        List<BaseGroup> baseGroupList = baseGroupMapper.selectList(new QueryWrapper<BaseGroup>().eq("type",GroupEnum.DEPLOY.value()).orderByDesc("add_time"));
        model.addAttribute("baseGroupList", baseGroupList);
        return "deployment/edit";
    }

    @Autowired
    private EnvProjectManager envProjectManager;

    /**
     * 转到步骤编辑页面
     */
    @GetMapping("/{id}/editStep")
    public String editStep(Model model, @PathVariable Long id) {

        //部署信息
        Deployment deployment = deploymentManager.selectById(id);
        model.addAttribute("deployment", deployment);


        //步骤列表
        List<Step> stepList = deploymentManager.selectStep(id);
        model.addAttribute("stepList", stepList);

        List envProjectList = envProjectManager.getAll();
        model.addAttribute("envProjectList", envProjectList);

        //执行器列表
        List<Plugin> executorList = pluginManager.list(PluginType.executor, PluginStatusEnum.OPEN.name());
        model.addAttribute("executorList", executorList);

        //检测器列表
        List<Plugin> checkerList = pluginManager.list(PluginType.checker, PluginStatusEnum.OPEN.name());
        model.addAttribute("checkerList", checkerList);

        return "deployment/editStep";
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PluginManager pluginManager;

}
