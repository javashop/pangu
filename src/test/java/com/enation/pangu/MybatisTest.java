package com.enation.pangu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.mapper.MachineMapper;
import com.enation.pangu.model.Machine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * Mybatis测试
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/10/31
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class MybatisTest {

    @Autowired
    MachineMapper machineMapper;

    @Test
    public void pageTest() {
        IPage<Machine> page = machineMapper.selectPage(new Page<>(1, 1),null);
        List<Machine> userList = page.getRecords();
        System.out.println(userList);
    }

    @Test
    public void mapPageTest() {
        QueryWrapper<Machine> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id as \"id\"", "ip", "name", "auth_type", "username", "password as \"password\"","add_time");
        queryWrapper.orderByDesc("add_time");
        IPage<Map<String, Object>> page = machineMapper.selectMapsPage(new Page<>(1, 10), queryWrapper);

        System.out.println(page.getRecords());
    }

}
