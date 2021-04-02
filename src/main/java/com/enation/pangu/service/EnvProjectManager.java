package com.enation.pangu.service;

import com.enation.pangu.model.EnvProject;
import com.enation.pangu.model.WebPage;

import java.util.List;
import java.util.Map;

/**
 * @author shen
 * @create 2020-11-30-19:04
 */
public interface EnvProjectManager {

    /**
     * 新增项目
     *
     * */
    void addProject(EnvProject project);
    /**
     * 更新项目信息
     *
     * */
    void editProject(EnvProject project);
    /**
     * 删除
     *
     * */
    void deleteProject(Long id);

    /**
     * 查询项目
     * */

    EnvProject selectProject(Long id );


    EnvProject selectProjectByName(String name);


    /**
     *
     *列表查询
     * */
    WebPage list(int pageNo, int pageSize);

    /**
     * 查询所有的项目
     * @return
     */
    List<EnvProject> getAll();


    /**
     * 导入项目
     * @param name 项目名称
     * @param environment 环境变量
     * @return 项目id
     */
    Long importMap(String name, Map<String, Map<String, String>> environment);
}
