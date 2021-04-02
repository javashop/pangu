package com.enation.pangu.api.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.model.Task;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.TaskManager;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部署集任务控制器
 * @author zs
 * @version 1.0
 * @since 1.0.0
 * 2020/1/3
 */
@RestController
@RequestMapping("/data/setTask/")
public class DeploymentSetTaskDataController {

    @Autowired
    private TaskManager taskManager;


    @GetMapping("/history")
    public WebPage history(Long deploymentSetId,int pageNo, int pageSize) {
        return taskManager.history(pageNo, pageSize, TaskTypeEnum.deploymentSet, deploymentSetId);
    }

    @GetMapping("/{setTaskId}/logs")
    public List<Task> getTaskLog(@PathVariable("setTaskId") Long setTaskId) {
        return taskManager.getChildrenTask(setTaskId);
    }

}
