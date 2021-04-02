package com.enation.pangu.monitor;

import com.enation.pangu.task.MachineProcessor;

import java.io.IOException;
import java.util.List;

/**
 * 部署监控服务
 *
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/15
 */

public interface MonitorService {

    /**
     * 开始日志记录
     *
     * @param machineId    机器id
     * @param deploymentId 部署id
     * @param taskId       任务id
     */
    void startLog(Long machineId, Long deploymentId, Long taskId);

    /**
     * 停止记录日志
     */
    void stopLog();


    /**
     * 记录日志
     *
     * @param logs 日志
     */
    void appendMachineLog(String logs);


    /**
     * 获取某次部署日志
     *
     * @param machineId    机器id
     * @param deploymentId 部署id
     * @param taskId       任务id
     * @return 日志内容
     */
    String getMachineLogs(Long machineId, Long deploymentId, Long taskId) throws IOException ;


    /**
     * 获取任务进度
     * @param parentTaskId 父任务id
     * @return
     */
    List<MachineProcessor> getTaskProcessor(Long parentTaskId);


}
