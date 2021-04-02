package com.enation.pangu.domain;


/**
 * 步骤实体类
 * @author zhangsong
 * @date 2020-11-05
 */
public class StepVO {
    /** 步骤名称 */
    private String name;

    /** 执行器 */
    private ExecutorVO executorVO;

    /** 检测器 */
    private CheckerVO checkerVO;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExecutorVO getExecutorVO() {
        return executorVO;
    }

    public void setExecutorVO(ExecutorVO executorVO) {
        this.executorVO = executorVO;
    }

    public CheckerVO getCheckerVO() {
        return checkerVO;
    }

    public void setCheckerVO(CheckerVO checkerVO) {
        this.checkerVO = checkerVO;
    }
}
