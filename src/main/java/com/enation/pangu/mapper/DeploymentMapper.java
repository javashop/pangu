package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.enation.pangu.model.Deployment;
import org.apache.ibatis.annotations.Param;


/**
 * 部署mapper
 * @author zhangsong
 * @date 2020-10-31
 */
public interface DeploymentMapper extends BaseMapper<Deployment> {

    /**
     * 分页查询
     * @param page 分页数据
     * @param groupId
     * @return
     */
    IPage<Deployment> selectPageCustom(IPage page, @Param("groupId") Long groupId);

    /**
     * 查询某个部署的 机器数 * 步骤数
     * @param deploymentId
     * @return
     */
    Integer selectTotalStep(Long deploymentId);
}
