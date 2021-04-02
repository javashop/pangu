package com.enation.pangu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.model.ConfigFile;
import com.enation.pangu.model.WebPage;

import java.util.List;
import java.util.Map;


/**
 * 项目文件业务层接口
 *
 * @author lizhengguo
 * @date 2020-11-18 20:28
 */
public interface ConfigFileManager {

    /**
     * 查询一个项目文件
     *
     * @param Id 项目文件id
     */
    ConfigFile selectById(Long Id);

    /**
     * 查询项目文件列表
     */
    WebPage<ConfigFile> list(int pageNo, int pageSize, Long id);

    /**
     * 新增项目文件
     *
     * @param projectFiles
     */
    int add(ConfigFile projectFiles);

    /**
     * 修改项目文件
     *
     * @param projectFiles
     */
    int edit(ConfigFile projectFiles);

    /**
     * 删除一个项目文件
     *
     * @param id
     */
    int delete(Long id);

    /**
     * 查询某个项目的所有项目文件
     *
     * @param projectId
     * @return
     */
    List<ConfigFile> listAll(Long projectId);

    /**
     * 查询某个项目的所有项目文件（同时查询某个步骤的配置文件id）
     *
     * @param projectId
     * @return
     */
    Map<String, Object> listAllStepInfo(Long projectId, Long stepId);

    /**
     * 获取项目根目录
     *
     * @return
     */
    String getPath();

}
