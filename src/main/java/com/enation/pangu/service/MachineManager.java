package com.enation.pangu.service;

import com.enation.pangu.model.Machine;
import com.enation.pangu.model.WebPage;

import java.util.List;

/**
 * @author shenyw
 * @create 2020-10-31-14:28
 */
public interface MachineManager {
    /**
     * 新增机器
     *
     * */
    void addMachine(Machine machine);
    /**
     * 更新机器信息
     *
     * */
    void editMachine(Machine machine);
    /**
     * 删除
     *
     * */
    void deleteMachine(Long id);

    /**
     * 查询单台机器
     * */

    Machine selectOne(Long id );

    /**
     *
     *列表查询
     * */
    WebPage list(int pageNo, int pageSize);

    Machine checkMachine(Machine machine);
    /**
     * 机器列表
     */
    List<Machine> listAll();
}
