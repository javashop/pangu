//package com.enation.pangu.service;
//
//
//import com.enation.pangu.model.SetTask;
//import com.enation.pangu.model.Task;
//import com.enation.pangu.model.WebPage;
//
//import java.util.List;
//
///**
// * 部署集task接口
// * @author zhangsong
// * @date 2020-01-04
// */
//public interface SetTaskManager {
//
//    WebPage history(Long deploymentSetId, int pageNo, int pageSize);
//
//    List<Task> getTaskLog(Long setTaskId);
//
//    /**
//     * 更新部署集task状态
//     * @param setTaskId
//     */
//    void updateSetTaskState(Long setTaskId);
//
//    /**
//     * 查询正在运行的任务
//     * @return
//     */
//    SetTask selectRunningTask(Long deploymentSetId);
//
//    /**
//     * 根据id查询
//     * @param setTaskId
//     * @return
//     */
//    SetTask selectById(Long setTaskId);
//}
