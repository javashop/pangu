package com.enation.pangu.service;

import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.Plugin;

import java.util.List;

/**
 * 检测器业务层接口
 * @author zhangsong
 * @date 2020-11-09
 */
public interface CheckerManager {

    /**
     * 查询一个检测器
     * @param checkerId 执行器id
     */
    PluginConfigVO selectById(String checkerId);

    /**
     * 查询检测器列表
     */
    List<Plugin> list();

    /**
     * 查询某个步骤的检测器
     * @param checkerId 执行器id
     */
    PluginConfigVO selectByIdStepInfo(String checkerId, Long stepId);
}
