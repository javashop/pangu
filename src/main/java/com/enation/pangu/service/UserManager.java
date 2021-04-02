package com.enation.pangu.service;

import com.enation.pangu.model.Deployment;
import com.enation.pangu.model.User;
import com.enation.pangu.model.WebPage;

/**
 * @author shen
 * @create 2020-12-23-18:27
 */
public interface UserManager {
    /**
     * 添加用户
     * @return
     */
    String addUser(User user);
    /**
     * 删除用户
     * @return
     */
    void deleteUser(Long id);
    /**
     * 更新用户
     * @return
     */
    void updateUser(User user);
    /**
     * 查询一个用户
     * @return
     */
    User selectOneUser(User use);
    /**
     * 查询y用户列表
     * @return
     */
    WebPage<User> list(int pageNo, int pageSize);
}
