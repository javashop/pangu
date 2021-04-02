package com.enation.pangu.api.data;

import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.monitor.MonitorService;
import com.enation.pangu.service.TaskManager;
import com.enation.pangu.task.MachineProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/16
 */
@RestController
@RequestMapping("/data/task/")
public class DeploymentLogController {

    @Autowired
    MonitorService monitorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TaskManager taskManager;


    @GetMapping("/history")
    public WebPage history(Long deploymentId,int pageNo, int pageSize) {

        return taskManager.history(pageNo, pageSize, TaskTypeEnum.deployment, deploymentId);
    }

    @GetMapping("/{taskId}/logs")
    public List<MachineProcessor> getTaskLog(@PathVariable("taskId") Long taskId) {
        List<MachineProcessor> list = monitorService.getTaskProcessor(taskId);
        return list;
    }

    @GetMapping("/{taskId}/stop")
    public void stop(@PathVariable("taskId") Long taskId) {
        taskManager.stopDeployTask(taskId);
    }
}
