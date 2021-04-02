package com.enation.pangu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.model.DeploymentSet;
import com.enation.pangu.model.DeploymentSetRelVO;
import com.enation.pangu.model.DeploymentSetVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部署集mapper
 * @author 褚帅
 * @create 2020-10-31
 */
public interface DeploymentSetMapper extends BaseMapper<DeploymentSet> {


    List<DeploymentSetRelVO> findByDeploymentSetId(@Param("deploymentSetId") Long deploymentSetId);

}
