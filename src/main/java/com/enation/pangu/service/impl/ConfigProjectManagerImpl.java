package com.enation.pangu.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.ConfigProjectMapper;
import com.enation.pangu.mapper.StepMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.service.ConfigFileManager;
import com.enation.pangu.service.ConfigProjectManager;
import com.enation.pangu.utils.DateUtil;
import com.enation.pangu.utils.PageConvert;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目业务层实现类
 *
 * @author lizhengguo
 * @date 2020-11-18 20:28
 */
@Service
public class ConfigProjectManagerImpl implements ConfigProjectManager {
    @Autowired
    private ConfigProjectMapper configProjectMapper;
    @Autowired
    private StepMapper stepMapper;
    @Autowired
    private ConfigFileManager configFileManager;

    @Override
    public ConfigProject selectById(Long Id) {
        return configProjectMapper.selectById(Id);
    }

    @Override
    public WebPage<ConfigProject> list(int pageNo, int pageSize) {
        QueryWrapper<ConfigProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("edit_time");
        IPage<ConfigProject> iPage = configProjectMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return PageConvert.convert(iPage);
    }

    @Override
    public int add(ConfigProject configProject) throws RuntimeException {
        List<ConfigProject> name = configProjectMapper.selectList(new QueryWrapper<ConfigProject>().eq("name", configProject.getName()).orderByDesc("edit_time"));
        if (!name.isEmpty()) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"项目文件夹已存在，请重命名");
        }
        configProject.setEditTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        int i = configProjectMapper.insert(configProject);
        return i;

    }

    @Override
    public int edit(ConfigProject configProject) {
        configProject.setEditTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        ConfigProject oldproject = configProjectMapper.selectById(configProject.getId());

        if (!oldproject.getName().equals(configProject.getName())) {
            List<ConfigProject> name = configProjectMapper.selectList(new QueryWrapper<ConfigProject>().eq("name", configProject.getName()).orderByDesc("edit_time"));
            if (!name.isEmpty()) {
                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"项目文件夹已存在，请重命名");
            }
        }

        int i = configProjectMapper.updateById(configProject);

        return i;
    }

    @Override
    public int delete(Long id) {
        int i = configProjectMapper.deleteById(id);
        return i;

    }

    @Override
    public List<ConfigProject> listAll() {
        return new QueryChainWrapper<>(configProjectMapper).orderByDesc("edit_time").list();
    }

    @Override
    public Map<String, Object> listAllStepInfo(Long stepId) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectList", this.listAll());
        map.put("stepProjectId", -1);
        Step step = stepMapper.selectById(stepId);
        String executorParams = step.getExecutorParams();
        if (StringUtil.notEmpty(executorParams)) {
            Map<String, String> parse = (Map<String, String>) JSONUtils.parse(executorParams);
            String projectId = parse.get("projectId");
            if (StringUtil.notEmpty(projectId)) {
                map.put("stepProjectId", projectId);
            }
        }
        return map;
    }
}
