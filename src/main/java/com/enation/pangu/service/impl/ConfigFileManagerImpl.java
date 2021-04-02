package com.enation.pangu.service.impl;


import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.ConfigFileMapper;
import com.enation.pangu.mapper.ConfigProjectMapper;
import com.enation.pangu.mapper.StepMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.service.ConfigFileManager;
import com.enation.pangu.utils.DateUtil;
import com.enation.pangu.utils.PageConvert;
import com.enation.pangu.utils.PathUtil;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 项目文件业务层实现类
 *
 * @author lizhengguo
 * @date 2020-11-18 20:28
 */
@Service
public class ConfigFileManagerImpl implements ConfigFileManager {

    @Autowired
    private ConfigFileMapper configFileMapper;
    @Autowired
    private ConfigProjectMapper configProjectMapper;
    @Autowired
    private StepMapper stepMapper;

    @Override
    public ConfigFile selectById(Long Id) {
        ConfigFile filesDO = configFileMapper.selectById(Id);
        String name = filesDO.getName();
        filesDO.setType(name.substring(name.lastIndexOf(".") + 1));
        return filesDO;
    }

    @Override
    public WebPage<ConfigFile> list(int pageNo, int pageSize, Long Id) {
        QueryWrapper<ConfigFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_project_id", Id).orderByDesc("edit_time");
        IPage<ConfigFile> iPage = configFileMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return PageConvert.convert(iPage);
    }

    @Override
    public int add(ConfigFile configFile) {
        configFile.setEditTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        List<ConfigProject> name = configProjectMapper.selectList(new QueryWrapper<ConfigProject>().eq("name", configFile.getName()).orderByDesc("edit_time"));
        if (!name.isEmpty()) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"项目文件已存在，请重命名");
        }
        int i = configFileMapper.insert(configFile);
        return i;
    }

    @Override
    public int edit(ConfigFile configFile) {
        configFile.setEditTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        ConfigFile ConfigFile = configFileMapper.selectById(configFile.getId());
        if (configFile == null || ConfigFile == null || configFile.getName() == null) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"参数丢失，请重试");
        }
        if (!configFile.getName().equals(ConfigFile.getName())) {
            List<ConfigProject> name = configProjectMapper.selectList(new QueryWrapper<ConfigProject>().eq("name", configFile.getName()).orderByDesc("edit_time"));
            if (!name.isEmpty()) {
                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"项目文件已存在，请重命名");
            }
        }
        int i = configFileMapper.updateById(configFile);
        return i;
    }

    @Override
    public int delete(Long id) {
        int i = configFileMapper.deleteById(id);
        return i;

    }

    @Override
    public List<ConfigFile> listAll(Long projectId) {
        return new QueryChainWrapper<>(configFileMapper).eq("config_project_id", projectId).orderByDesc("edit_time").list();
    }

    @Override
    public Map<String, Object> listAllStepInfo(Long projectId, Long stepId) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectFileList", this.listAll(projectId));
        map.put("stepFileId", -1);
        Step step = stepMapper.selectById(stepId);
        String executorParams = step.getExecutorParams();
        if (StringUtil.notEmpty(executorParams)) {
            Map<String, String> parse = (Map<String, String>) JSONUtils.parse(executorParams);
            String fileId = parse.get("fileId");
            if (StringUtil.notEmpty(fileId)) {
                map.put("stepFileId", fileId);
            }
        }
        return map;
    }

    @Override
    public String getPath() {
        return PathUtil.getRootPath();
    }

}
