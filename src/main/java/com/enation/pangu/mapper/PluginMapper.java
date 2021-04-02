package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.domain.Plugin;
import org.apache.ibatis.annotations.Param;

/**
 * 插件mapper
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/29
 */

public interface PluginMapper extends BaseMapper<Plugin> {
    /**
     * 递增排序
     * 条件是大于某个等于sequence
     *
     * @param sequence
     */
    void increaseSort(@Param("sequence") Integer sequence, @Param("kind") String kind);

}
