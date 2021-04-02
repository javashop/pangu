package com.enation.pangu.api.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.mapper.EnvGroupMapper;
import com.enation.pangu.mapper.EnvVarMapper;
import com.enation.pangu.model.EnvGroup;
import com.enation.pangu.model.EnvVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 环境变量分组数据控制器
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/25
 */
@RestController
@RequestMapping("/data/{projectId}")
public class EnvGroupDataController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EnvGroupMapper envGroupMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EnvVarMapper envVarMapper;


    @PostMapping("/env-group")
    public EnvGroup add (@PathVariable("projectId") Long projectId,String name)   {

        EnvGroup envGroup = new EnvGroup();
        envGroup.setProjectId(projectId);
        envGroup.setName(name);
        envGroupMapper.insert(envGroup);
        return envGroup;
    }


    @DeleteMapping("/env-group/{groupId}")
    public String add(@PathVariable Long groupId) {
        envGroupMapper.deleteById(groupId);
        envVarMapper.delete(new QueryWrapper<EnvVariables>().eq("group_id", groupId));
        return "ok";
    }


    @PutMapping("/env-group/{groupId}")
    public EnvGroup update(@PathVariable("projectId") Long projectId,@PathVariable("groupId")  Long groupId,String name) {
        EnvGroup group = new EnvGroup();
        group.setId(groupId);
        group.setName(name);
        group.setProjectId(projectId);
        envGroupMapper.updateById(group);
        return group;
    }


}
