package com.enation.pangu.service;

import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.model.Task;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.task.TaskState;

import java.util.List;

/**
 * 部署task接口
 * @author zhangsong
 * @date 2020-01-08
 */
public interface TaskManager {

    /**
     * 查询一个task
     * @param taskId
     * @return
     */
    Task selectById(Long taskId);

    /**
     * 查询正在执行的部署任务
     * @param deploymentId 部署id
     * @return
     */
    Task selectRunningDeployTask(Long deploymentId);


    /**
     * 查询正在执行的部署集任务
     * @param deploymentSetId 部署集id
     * @return
     */
    Task selectRunningSetTask(Long deploymentSetId);

    /**
     * 更新父任务的状态
     * @param parentTaskId 父任务id
     * @param type 父任务类型
     * @return 父任务状态
     */
    TaskState updateParentTaskState(Long parentTaskId, TaskTypeEnum type);

    /**
     * 查询某次部署集任务下的某个部署任务
     * @param setTaskId 部署集任务id
     * @param deploymentId 部署id
     * @return 部署任务
     */
    Task selectDeployTask(Long setTaskId, Long deploymentId);

    /**
     * 判断某个任务是否执行成功
     * @param taskId 任务id
     * @return
     */
    boolean taskIsSuccess(Long taskId);

    /**
     * 分页查询任务列表
     * @param pageNo
     * @param pageSize
     * @param type 任务类型
     * @param typeId 部署/部署集id
     * @return
     */
    WebPage history(int pageNo, int pageSize, TaskTypeEnum type, Long typeId);

    /**
     * 查询父任务下的子任务
     * @param parentTaskId
     * @return
     */
    List<Task> getChildrenTask(Long parentTaskId);

    /**
     * 插入一个任务
     * @param task
     */
    void insert(Task task);

    /**
     * 停止运行部署任务
     * @param taskId 任务id
     */
    void stopDeployTask(Long taskId);
}
