package com.enation.pangu.api.data;

import com.alibaba.druid.support.json.JSONUtils;
import com.enation.pangu.domain.ExecutorConfigItemVO;
import com.enation.pangu.domain.ExecutorVO;
import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.model.Deployment;
import com.enation.pangu.model.Step;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.PluginManager;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 步骤控制器
 * @author zhangsong
 * @date 2020-12-29
 */
@RestController
@RequestMapping("/data/step")
public class StepDataController {

    @Autowired
    private DeploymentManager deploymentManager;

    @Autowired
    private PluginManager pluginManager;

    /**
     * 新增步骤
     */
    @PostMapping
    public Step addStep(@RequestBody Step step) {

        //步骤入库
        step =  deploymentManager.addStep(step);

        //执行器的配置
        PluginConfigVO executorConfig = this.setValue(step.getExecutor(), PluginType.executor, step.getExecutorParams());
        step.setExecutorConfig(executorConfig);

        //检查器的配置
        if (StringUtil.notEmpty(step.getCheckType())) {
            PluginConfigVO checkerConfig = this.setValue(step.getCheckType(), PluginType.checker, step.getCheckerParams());
            step.setCheckerConfig(checkerConfig);
        }

        return step;
    }

    /**
     * 解析插件参数，将value放入到config实例中
     * @param pluginId 插件id
     * @param pluginType 插件类型
     * @param params 参数，包含了参数名和参数值
     * @return 放入值后的 PluginConfig
     */
    private PluginConfigVO setValue(String pluginId,PluginType pluginType,String params ) {

        ExecutorVO executorVO =pluginManager.parsePlugin(pluginId, pluginType,new HashMap());
        PluginConfigVO executorConfig = executorVO.getConfig();

        if (executorConfig == null) {
            return null;
        }

        Map<String, String> executorParams = (Map<String, String>) JSONUtils.parse(params);
        List<ExecutorConfigItemVO> itemList  = executorConfig.getItemList();
        for (ExecutorConfigItemVO item : itemList) {
            String name = item.getName();
            String value = executorParams.get(name);
            item.setValue(value);

        }
        return executorConfig;
    }

    /**
     * 编辑步骤
     */
    @PutMapping("/{id}")
    public Step editStep(@PathVariable("id") Long stepId, @RequestBody Step step) {
        step.setId(stepId);
        return deploymentManager.editStepOne(step);
    }

    /**
     * 删除步骤
     */
    @DeleteMapping("/{id}")
    public void delStep(@PathVariable("id") Long stepId) {
        deploymentManager.delStep(stepId);
    }

    /**
     * 设置步骤是否跳过
     */
    @PutMapping("/{id}/skip-status")
    public void setIsSkip(@PathVariable("id") Long stepId, int isSkip) {
        deploymentManager.updateStepSkipStatus(stepId, isSkip);
    }

}
