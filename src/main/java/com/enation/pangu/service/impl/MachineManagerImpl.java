package com.enation.pangu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.mapper.MachineMapper;
import com.enation.pangu.mapper.MachineTagMapper;
import com.enation.pangu.model.*;
import com.enation.pangu.service.MachineManager;
import com.enation.pangu.service.SecretKeyManager;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenyw
 * @create 2020-10-31-14:35
 */
@Service
public class MachineManagerImpl implements MachineManager {
    @Autowired
    private MachineMapper machineMapper;
    @Autowired
    private MachineTagMapper machineTagMapper;



    @Override
    public void addMachine(Machine machine) {
        machine.setAddTime(System.currentTimeMillis());
         machineMapper.insert(machine);

    }

    @Override
    public void editMachine(Machine machine) {
        machineMapper.updateById(machine);
    }

    @Override
    public void deleteMachine(Long ids) {
        //先删除关联表
        QueryWrapper<MachineTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("machine_id", ids);
        int delete = machineTagMapper.delete(queryWrapper);
        //  删除机器
        machineMapper.deleteById(ids);
    }

    @Override
    public Machine selectOne(Long id) {
        return machineMapper.selectById(id);
    }


    @Override
    public WebPage list(int pageNo, int pageSize) {
        QueryWrapper<Machine> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("add_time");
        Page<Machine> ipage = machineMapper.selectPage(new Page<Machine>(pageNo, pageSize), queryWrapper);
        WebPage page = PageConvert.convert(ipage);
        return page;
    }

    @Override
    public List<Machine> listAll() {
        return machineMapper.selectList(new QueryWrapper<Machine>().orderByDesc("add_time"));
    }

    @Override
    public Machine checkMachine(Machine machine) {
        QueryWrapper<Machine> query = new QueryWrapper<>();
        query.eq("name", machine.getName());
        Machine baseMachine = machineMapper.selectOne(query);
        return baseMachine;
    }

}
