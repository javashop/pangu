package com.enation.pangu.task;

/**
 * 任务状态
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/12/18
 */

public enum TaskState {
    /**
     * 正在运行
     */
    RUNNING,

    /**
     * 运行成功
     */
    SUCCESS,

    /**
     * 运行错误
     */
    ERROR;
}
