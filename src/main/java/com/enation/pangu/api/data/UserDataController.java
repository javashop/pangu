package com.enation.pangu.api.data;

import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.model.User;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author shenyw
 * @create 2020-10-31-14:38
 */
@RestController
@RequestMapping("/data/user")
public class UserDataController {
    @Autowired
    private UserManager userManager;
    /**
     * 添加用户
     */
    @PostMapping()
    public String addUser(User user) {

        return userManager.addUser(user);
    }

    /**
     * 编辑用户
     *
     */
    @PutMapping()
    public void editUser(User user) {

        userManager.updateUser(user);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        WebPage<User> list = userManager.list(1, 10);
        if(list.getRecordsTotal()==1){
            return "至少保留一个管理员账号";
        }
        userManager.deleteUser(id);
        return "success";
    }

    /**
     * 查询列表
     */
    @GetMapping()
    public WebPage list(int pageNo, int pageSize) {
        WebPage list = userManager.list(pageNo, pageSize);
        return list;
    }

    /**
     * 登录
     */
    @PostMapping("/login")
        public String login(User user,  HttpServletRequest request) {
        User base = userManager.selectOneUser(user);
        if (null != base) {
            if (base.getPassword().equals(user.getPassword())) {
                request.getSession().setAttribute("login", "login");
                return "success";
            }
        }
        throw new ServiceException(ResultCode.PARAMETER_CHECK_ERROR.getCode(),"用户名密码不正确");
    }
    /**
     * 退出
     */
    @PostMapping("/logout")
        public String logout(HttpServletRequest request) {

        request.getSession().removeAttribute("login");

        return "success";
    }

}
