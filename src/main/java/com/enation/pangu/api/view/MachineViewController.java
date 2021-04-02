package com.enation.pangu.api.view;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.enums.GroupEnum;
import com.enation.pangu.mapper.BaseGroupMapper;
import com.enation.pangu.mapper.TagMapper;
import com.enation.pangu.model.BaseGroup;
import com.enation.pangu.model.Machine;
import com.enation.pangu.model.SecretKey;
import com.enation.pangu.model.Tag;
import com.enation.pangu.service.MachineManager;
import com.enation.pangu.service.SecretKeyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author shen
 * @create 2020-10-29-18:38
 */
@Controller
@RequestMapping("/view/machines")
public class MachineViewController {
    @Autowired
    private MachineManager machineManager;
    @Autowired
    private SecretKeyManager secretKeyManager;
    @Autowired
    TagMapper tagMapper;

    @Autowired
    BaseGroupMapper baseGroupMapper;

    @GetMapping("/list")
    public String list(Model model) {
        List<Tag> tagList = tagMapper.selectList(new QueryWrapper<Tag>().orderByAsc("id"));
        model.addAttribute("tagList", tagList);
        return "machine/list";
    }


    /**
     * 转到添加页面
     */
    @GetMapping("/add")
    public String add(Model model) {
        
        //keysList
        List<SecretKey> keysList = secretKeyManager.keysList();
        model.addAttribute("keysList", keysList);
        //分组列表
        List<BaseGroup> baseGroupList = baseGroupMapper.selectList(new QueryWrapper<BaseGroup>().eq("type", GroupEnum.MACHINES.value()).orderByDesc("add_time"));
        model.addAttribute("baseGroupList", baseGroupList);

        return "machine/add";
    }

    /**
     * 转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        Machine machine = machineManager.selectOne(id);
        model.addAttribute("machine", machine);


        //keysList
        List<SecretKey> keysList = secretKeyManager.keysList();
        model.addAttribute("keysList", keysList);

        //分组列表
        List<BaseGroup> baseGroupList = baseGroupMapper.selectList(new QueryWrapper<BaseGroup>().eq("type",GroupEnum.MACHINES.value()).orderByDesc("add_time"));
        model.addAttribute("baseGroupList", baseGroupList);
        return "machine/edit";
    }

}
