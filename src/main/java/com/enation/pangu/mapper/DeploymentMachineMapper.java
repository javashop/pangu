package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.pangu.model.DeploymentMachine;
import com.enation.pangu.model.Machine;

import java.util.List;


/**
 * 部署、机器关联表mapper
 * @author zhangsong
 * @date 2020-10-31
 */
public interface DeploymentMachineMapper extends BaseMapper<DeploymentMachine> {

    /**
     * 查询某个部署的机器id列表
     * @param deploymentId
     * @return
     */
    List<Long> selectMachineIdList(Long deploymentId);

    /**
     * 查询某个部署的机器列表
     * @param deploymentId
     * @return
     */
    List<Machine> selectMachineList(Long deploymentId);
}
