package com.enation.pangu.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.mapper.MachineMapper;
import com.enation.pangu.model.Machine;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shen
 * @create 2020-10-29-18:38
 */
@RestController
@RequestMapping("/data/demo")
public class DemoDataController {
    @Autowired
    private MachineMapper machineMapper;

    @RequestMapping("/pages")
    public WebPage pagesDemo(int pageNo,int pageSize) {
        IPage<Machine> page = machineMapper.selectPage(new Page<>(pageNo, pageSize),new QueryWrapper<Machine>().orderByDesc("add_time"));
        WebPage webPage = PageConvert.convert(page);
        return webPage;
    }


//    @RequestMapping("/list")
//    public List<User> listDemo() {
//
//        List<User> userList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setAge(i);
//            user.setEmail("user" + i + "@mail.com");
//            user.setId(Long.valueOf(i));
//            user.setName("user" + i);
//            userList.add(user);
//        }
//
//        return userList;
//    }


}
