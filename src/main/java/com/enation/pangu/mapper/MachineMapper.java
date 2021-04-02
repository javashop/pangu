package com.enation.pangu.mapper;

import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enation.pangu.model.Machine;

import java.util.List;

/**
 * @author shenyw
 * @create 2020-10-31-14:08
 */
public interface MachineMapper extends BaseMapper<Machine> {
    List<Machine> selectLists();
}
