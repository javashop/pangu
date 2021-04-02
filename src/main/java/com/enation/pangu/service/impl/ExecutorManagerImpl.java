package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.domain.*;
import com.enation.pangu.mapper.PluginMapper;
import com.alibaba.fastjson.JSON;
import com.enation.pangu.enums.PluginPathTypeEnum;
import com.enation.pangu.mapper.StepMapper;
import com.enation.pangu.model.Step;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.service.PluginManager;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.utils.HttpUtils;
import com.enation.pangu.utils.ParseException;
import com.enation.pangu.utils.StringUtil;
import com.enation.pangu.utils.ZipUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 执行器业务层实现类
 *
 * @author zhangsong
 * @date 2020-11-09
 */
@Service
public class ExecutorManagerImpl implements ExecutorManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private StepMapper stepMapper;

    @Autowired
    private PluginManager pluginManager;


    @Autowired
    private PluginMapper pluginMapper;


    @Autowired
    private DeploymentManager deploymentManager;

    @Value("${pangu.synchronizer.url}")
    private String synchronizerUrl;

    @Override
    public PluginConfigVO selectById(String executorId) {
        return pluginManager.parsePlugin(executorId, PluginType.executor, new HashMap()).getConfig();
    }

    @Autowired
    MonitorService monitorService;


    @Override
    public PluginConfigVO selectByIdStepInfo(String executorId, Long stepId) {
        PluginConfigVO config = pluginManager.parsePlugin(executorId, PluginType.executor, new HashMap()).getConfig();
        Step step = stepMapper.selectById(stepId);
        deploymentManager.fillConfigValue(config, step.getExecutorParams());
        return config;
    }
}
