package com.enation.pangu.api.data;

import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.enums.PluginStatusEnum;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.ExecutorManager;
import com.enation.pangu.service.PluginManager;
import com.enation.pangu.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 执行器控制器
 *
 * @author zhangsong
 * @date 2020-11-07
 */
@RestController
@RequestMapping("/data/executor")
public class ExecutorDataController {

    @Autowired
    private ExecutorManager executorManager;

    @Value("${pangu.synchronizer.data_url:null}")
    private String synchronizerDataUrl;


    /**
     * 查询执行器config(查询某个步骤的)
     */
    @GetMapping("/{executorId}/config/step-info")
    public PluginConfigVO selectByIdStepInfo(@PathVariable String executorId, Long stepId) {

        return executorManager.selectByIdStepInfo(executorId, stepId);
    }

    @Autowired
    private PluginManager pluginManager;


    /**
     * 查询执行器列表
     */
    @GetMapping
    public List<Plugin> list() {
        return pluginManager.list(PluginType.executor, null);
    }

    /**
     * 查询插件仓库插件列表
     *
     * @return
     */
    @GetMapping("/pluginList")
    public WebPage getYmlList(String key, String metadataKind, int pageNo, int pageSize) {
        return pluginManager.findPluginList(key, metadataKind, pageNo, pageSize);
    }

    /**
     * 执行器排序
     *
     * @param sequence    排序
     * @param newSequence 新的排序
     */
    @PutMapping("/sort")
    public void executorSort(@RequestParam Integer sequence, @RequestParam("new_sequence") Integer newSequence) {
        pluginManager.sort(sequence, newSequence, PluginType.executor.name());
    }
}
