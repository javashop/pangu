package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.mapper.EnvGroupMapper;
import com.enation.pangu.mapper.EnvVarMapper;
import com.enation.pangu.mapper.EnvProjectMapper;
import com.enation.pangu.model.EnvGroup;
import com.enation.pangu.model.EnvProject;
import com.enation.pangu.model.EnvVariables;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.EnvProjectManager;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shen
 * @create 2020-12-01-8:54
 */
@Service
public class EnvProjectManagerImpl implements EnvProjectManager {
    @Autowired
    private EnvProjectMapper projectMapper;
    @Autowired
    private EnvVarMapper envVarMapper;
    @Autowired
    private EnvGroupMapper envGroupMapper;

     @Override
    public void addProject(EnvProject project) {
        project.setAddTime(System.currentTimeMillis());
        projectMapper.insert(project);
    }

    @Override
    public void editProject(EnvProject project) {

        projectMapper.updateById(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectMapper.deleteById(id);
//        envVarMapper.deleteByProjectId(id);
    }

    @Override
    public EnvProject selectProject(Long id) {

             return projectMapper.selectById(id);
    }
    @Override
    public EnvProject  selectProjectByName(String name)
    {
        QueryWrapper<EnvProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        return projectMapper.selectOne(queryWrapper);
    }

    @Override
    public WebPage list(int pageNo, int pageSize) {
        QueryWrapper<EnvProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("add_time");
        Page<EnvProject> page = projectMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        WebPage convert = PageConvert.convert(page);
        return convert;
    }


    @Override
    public List<EnvProject> getAll() {
        return  projectMapper.selectList( new QueryWrapper<EnvProject>().orderByDesc("add_time"));
    }

    @Override
    public Long importMap(String name, Map<String, Map<String, String>> environment) {
        if(environment == null){
            return null;
        }
        //导入环境变量项目
        EnvProject envProject = new EnvProject();
        envProject.setName(name);
        envProject.setAddTime(System.currentTimeMillis());
        projectMapper.insert(envProject);


        environment.forEach((groupName, variablesMap) -> {
            if(variablesMap == null){
                return;
            }
            //导入环境变量组
            EnvGroup envGroup = new EnvGroup();
            envGroup.setName(groupName);
            envGroup.setProjectId(envProject.getId());
            envGroupMapper.insert(envGroup);

            //导入环境变量
            variablesMap.forEach((varName, varValue) -> {
                EnvVariables envVariables = new EnvVariables();
                envVariables.setName(varName);
                envVariables.setValue(varValue);
                envVariables.setGroupId(envGroup.getId());
                envVariables.setProjectId(envProject.getId());
                envVarMapper.insert(envVariables);
            });

        });

        return envProject.getId();
    }
}
