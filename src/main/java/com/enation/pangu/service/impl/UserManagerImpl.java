package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.mapper.UserMapper;
import com.enation.pangu.model.User;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.UserManager;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shen
 * @create 2020-12-23-18:31
 */
@Service
public class UserManagerImpl implements UserManager {
    @Autowired
    private UserMapper userMapper;
    @Override
    public String addUser(User user) {
        User base = selectOneUser(user);
        if(base==null){
           user.setAddTime(System.currentTimeMillis());
           userMapper.insert(user);
            return "success";
        }
            return "用户已存在";

    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User selectOneUser(User user) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", user.getUsername());
        User base = userMapper.selectOne(query);
        return base;
    }

    @Override
    public WebPage<User> list(int pageNo, int pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("add_time");
        Page<User> ipage = userMapper.selectPage(new Page<User>(pageNo, pageSize), queryWrapper);
        WebPage page = PageConvert.convert(ipage);
        return page;
    }
}
