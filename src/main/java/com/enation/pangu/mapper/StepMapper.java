package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.model.Step;
import org.apache.ibatis.annotations.Param;


/**
 * 步骤mapper
 * @author zhangsong
 * @date 2020-11-01
 */
public interface StepMapper extends BaseMapper<Step> {

    /**
     * 递增排序
     * 条件是大于某个等于sequence
     * @param sequence
     */
    void increaseSort(@Param("sequence") Integer sequence,@Param("deploymentId") Long deploymentId);
}
