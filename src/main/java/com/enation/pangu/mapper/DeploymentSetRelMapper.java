package com.enation.pangu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.model.Deployment;
import com.enation.pangu.model.DeploymentSetRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部署集和部署关系表mapper
 * @author 褚帅
 * @create 2020-10-31
 */
public interface DeploymentSetRelMapper extends BaseMapper<DeploymentSetRel> {

    /**
     * 查询该部署集的所有部署
     * @param deploymentSetId 部署集id
     * @return
     */
    List<Deployment> selectDeploymentList(@Param("deploymentSetId") Long deploymentSetId);

    /**
     * 查询某个部署集的所有没有依赖的部署
     * @param deploymentSetId 部署集id
     * @return
     */
    List<Deployment> selectDeploymentListNoDepend(@Param("deploymentSetId") Long deploymentSetId);

}
