package com.enation.pangu.service;


import com.enation.pangu.model.ConfigProject;
import com.enation.pangu.model.WebPage;

import java.util.List;
import java.util.Map;

/**
 * 项目业务层接口
 * @author lizhengguo
 * @date 2020-11-18 20:28
 */
public interface ConfigProjectManager {

    /**
     * 查询一个项目
     * @param Id 执行器id
     */
    ConfigProject selectById(Long Id);

    /**
     * 查询项目列表
     */
    WebPage<ConfigProject> list(int pageNo, int pageSize);
    /**
     * 新增项目
     * @param configProject
     */
    int add(ConfigProject configProject) throws IllegalAccessException;

    /**
     * 修改项目
     * @param configProject
     */
    int edit(ConfigProject configProject);

    /**
     * 删除一个项目
     * @param id
     */
    int delete(Long id);

    /**
     * 查询所有项目
     * @return
     */
    List<ConfigProject> listAll();

    /**
     * 查询所有项目(同时查询某个步骤的项目id)
     * @param stepId
     * @return
     */
    Map<String, Object> listAllStepInfo(Long stepId);
}
