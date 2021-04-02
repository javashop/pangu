package com.enation.pangu.service;

import com.enation.pangu.domain.ExecutorVO;
import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.model.WebPage;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/29
 */

public interface PluginManager {

    /**
     * 由文件系统中同步插件到数据库
     */
    void syncFormFileSystem();

    /**
     * 读取某种插件列表
     *
     * @param pluginType 插件类型
     * @param status     插件状态
     * @return
     */
    List<Plugin> list(PluginType pluginType, String status);


    /**
     * 解析插件
     *
     * @param pluginId   插件id
     * @param pluginType 插件类型
     * @param env        环境变量
     * @return 插件vo
     */
    ExecutorVO parsePlugin(String pluginId, PluginType pluginType, Map env)throws PluginNotExistException ;

    /**
     * 修改插件信息
     *
     * @param plugin 插件
     * @return 是否修改成功
     */
    Boolean editPluginById(Plugin plugin);

    /**
     * 插入一个插件到数据库
     * @param plugin 插件
     */
    void insert(Plugin plugin);

    /**
     * 根据插件id查询
     * @param pluginId 插件id
     * @return
     */
    Plugin getModel(String pluginId, PluginType pluginType);

    /**
     * 查找某个文件夹下的yml文件
     *
     * @param dir
     * @return
     */
    File findPluginYml(File dir);
    /**
     * 查询一个插件
     *
     * @param executorId 插件id
     * @return 插件
     */
    Plugin getById(String executorId);

    /**
     * 下载脚本到本地
     * @param fileName 脚本文件名
     */
    void installPlugin(Map<String,String> fileName);

    /**
     * 查询插件列表
     *
     * @param key
     * @param metadataKind
     * @param pageNo
     * @param pageSize
     * @return
     */
    WebPage findPluginList(String key, String metadataKind, int pageNo, int pageSize);


    /**
     * 根据不同的插件类型进行排序
     *
     * @param sourceSequence 排序
     * @param targetSequence 新的排序
     * @param kind           类型
     * @return
     */
    Boolean sort(Integer sourceSequence, Integer targetSequence, String kind);
}
