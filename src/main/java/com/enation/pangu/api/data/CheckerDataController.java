package com.enation.pangu.api.data;

import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.domain.Plugin;
import com.enation.pangu.domain.PluginType;
import com.enation.pangu.mapper.PluginMapper;
import com.enation.pangu.service.CheckerManager;
import com.enation.pangu.service.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检测器控制器
 * @author zhangsong
 * @date 2020-11-09
 */
@RestController
@RequestMapping("/data/checker")
public class CheckerDataController {

    @Autowired
    private CheckerManager checkerManager;

    @Autowired
    private PluginManager pluginManager;

    /**
     * 查询一个检测器config
     */
    @GetMapping("/{checkerId}/config")
    public PluginConfigVO selectById(@PathVariable String checkerId) {

        return checkerManager.selectById(checkerId);
    }

    /**
     * 查询一个检测器config(查询某个步骤的)
     */
    @GetMapping("/{id}/config/step-info")
    public PluginConfigVO selectByIdStepInfo(@PathVariable String id, Long stepId) {

        return checkerManager.selectByIdStepInfo(id, stepId);
    }

    /**
     * 查询检测器列表
     */
    @GetMapping
    public List<Plugin> list() {
        return checkerManager.list();
    }

    /**
     * 执行器排序
     *
     * @param sequence    排序
     * @param newSequence 新的排序
     */
    @PutMapping("/sort")
    public void executorSort(@RequestParam Integer sequence, @RequestParam("new_sequence") Integer newSequence) {
        pluginManager.sort(sequence, newSequence, PluginType.checker.name());
    }

}
