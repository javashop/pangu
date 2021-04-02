package com.enation.pangu.api.data;

import com.enation.pangu.domain.ExecutorVO;
import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.enums.PluginStatusEnum;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件controller
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/29
 */
@RestController
@RequestMapping("/data/plugin")
public class PluginDataController {

    @Autowired
    private PluginManager pluginManager;

    /**
     * 同步文件中的插件列表到数据库
     * @return
     */
    @GetMapping("/synchronizer")
    public String  listVars() {
        pluginManager.syncFormFileSystem();
        return "ok";
    }


    /**
     * 查询执行器config
     */
    @GetMapping("/{pluginId}/config")
    public PluginConfigVO selectById(@PathVariable String pluginId,String pluginType) {
        ExecutorVO executorVO =pluginManager.parsePlugin(pluginId,   PluginType.valueOf(pluginType),new HashMap());
        return executorVO.getConfig();
    }

    /**
     * 查询插件仓库插件列表
     *
     * @return
     */
    @GetMapping("/pluginList")
    public WebPage pluginList(String key, String metadataKind, int pageNo, int pageSize) {
        return pluginManager.findPluginList(key, metadataKind, pageNo, pageSize);
    }

    /**
     * 下载指定脚本到本地
     */
    @PostMapping("/installPlugin")
    public void installPlugin(@RequestBody Map pluginMap){
        pluginManager.installPlugin(pluginMap);
    }

    public static void main(String[] args) {
        PluginType type = PluginType.valueOf("executor");
        System.out.println(type);
    }


    /**
     * 启用插件
     */
    @PutMapping("/{id}/open")
    public Boolean open(@PathVariable String id) {
        Plugin plugin = pluginManager.getById(id);
        if (plugin != null) {
            plugin.setStatus(PluginStatusEnum.OPEN.name());
        }
        return pluginManager.editPluginById(plugin);
    }

    /**
     * 关闭插件
     */
    @PutMapping("/{id}/close")
    public Boolean close(@PathVariable String id) {
        Plugin plugin = pluginManager.getById(id);
        if (plugin != null) {
            plugin.setStatus(PluginStatusEnum.CLOSE.name());
        }
        return pluginManager.editPluginById(plugin);
    }

}
