package com.enation.pangu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.model.DeploymentDepend;

import java.util.List;

/**
 * 部署依赖mapper
 * @author 褚帅
 * @create 2020-10-31
 */
public interface DeploymentDependMapper extends BaseMapper<DeploymentDepend> {
    /**
     * 查询某个部署集的部署依赖
     * @param deploymentSetId
     * @return
     */
    List<DeploymentDepend> selectDependList(Long deploymentSetId);
}
