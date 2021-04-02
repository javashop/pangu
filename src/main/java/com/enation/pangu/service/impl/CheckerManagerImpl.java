package com.enation.pangu.service.impl;

import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.mapper.StepMapper;
import com.enation.pangu.model.Step;
import com.enation.pangu.service.CheckerManager;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

/**
 * 检测器业务层实现类
 *
 * @author zhangsong
 * @date 2020-11-09
 */
@Service
public class CheckerManagerImpl implements CheckerManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private StepMapper stepMapper;

    @Autowired
    private DeploymentManager deploymentManager;

    @Autowired
    private PluginManager pluginManager;

    /**
     * 按照检测器id查询执行器配置
     *
     * @param checkerId
     * @return
     */
    @Override
    public PluginConfigVO selectById(String checkerId) {
        return pluginManager.parsePlugin(checkerId, PluginType.checker, new HashMap()).getConfig();
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<Plugin> list() {
        return pluginManager.list(PluginType.checker, null);
    }

    @Override
    public PluginConfigVO selectByIdStepInfo(String checkerId, Long stepId) {
        PluginConfigVO config = pluginManager.parsePlugin(checkerId, PluginType.checker, new HashMap()).getConfig();
        Step step = stepMapper.selectById(stepId);
        deploymentManager.fillConfigValue(config, step.getCheckerParams());
        return config;
    }


}
