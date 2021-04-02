package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.model.EnvGroup;
import com.enation.pangu.model.EnvVariables;

import java.util.List;
import java.util.Map;

/**
 * 变量数据Mapper
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/25
 */

public interface EnvVarMapper extends BaseMapper<EnvVariables> {

    /**
     * 分组读取环境变量
     * @param projectId 项目id
     * @return
     */
    List<Map> groupVars(Long projectId);
}
