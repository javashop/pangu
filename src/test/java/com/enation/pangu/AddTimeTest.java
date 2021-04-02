package com.enation.pangu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.pangu.mapper.*;
import com.enation.pangu.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 批量修改数据
 * @author zhanghao
 * @version 1.0
 * 2021/3/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ComponentScan("com.enation.pangu")
public class AddTimeTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BaseGroupMapper baseGroupMapper;
    @Autowired
    private EnvProjectMapper envProjectMapper;

    //UserMapper
    @Test
    public void test() throws InterruptedException {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        for (User user : users) {
            user.setAddTime(System.currentTimeMillis());
            userMapper.update(user, new UpdateWrapper<User>().eq("id", user.getId()));
            Thread.sleep(1000);
        }
    }

    //BaseGroupMapper
    @Test
    public void test1() throws InterruptedException {
        List<BaseGroup> groups = baseGroupMapper.selectList(new QueryWrapper<>());
        for (BaseGroup group : groups) {
            group.setAddTime(System.currentTimeMillis());
            baseGroupMapper.update(group, new UpdateWrapper<BaseGroup>().eq("id", group.getId()));
            Thread.sleep(1000);
        }
    }

    //ConfigProjectMapper
    @Test
    public void test2() throws InterruptedException {
        List<EnvProject> projects = envProjectMapper.selectList(new QueryWrapper<>());
        for (EnvProject project : projects) {
            project.setAddTime(System.currentTimeMillis());
            envProjectMapper.update(project, new UpdateWrapper<EnvProject>().eq("id", project.getId()));
            Thread.sleep(1000);
        }
    }

}
