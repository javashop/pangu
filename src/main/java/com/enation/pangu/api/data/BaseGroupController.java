package com.enation.pangu.api.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.annotation.Create;
import com.enation.pangu.mapper.BaseGroupMapper;
import com.enation.pangu.model.BaseGroup;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.utils.PageConvert;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gy
 * @version v1.0
 * @date 2021/2/24
 * @since v7.2.0
 * 基本分组控制器
 */
@RestController
@RequestMapping("/data/base-group")
public class BaseGroupController {

    @Autowired
    private BaseGroupMapper baseGroupMapper;


    @PostMapping("")
    public BaseGroup add(@Validated({Create.class}) BaseGroup baseGroup) {
        baseGroup.setAddTime(System.currentTimeMillis());
        baseGroupMapper.insert(baseGroup);
        return baseGroup;
    }


    @DeleteMapping("/{groupId}")
    public String delete(@PathVariable Long groupId) {
        baseGroupMapper.deleteById(groupId);
        return "ok";
    }


    @PutMapping("/{groupId}")
    public BaseGroup update(@PathVariable("groupId") Long groupId, @Validated BaseGroup baseGroup) {
        baseGroup.setId(groupId);
        baseGroupMapper.updateById(baseGroup);
        return baseGroup;
    }

    @GetMapping("")
    public WebPage<BaseGroup> list(String type, String name, int pageNo, int pageSize) {

        QueryWrapper<BaseGroup> queryWrapper = new QueryWrapper<BaseGroup>();
        queryWrapper.eq(!StringUtil.isEmpty(type), "type", type);
        queryWrapper.like(!StringUtil.isEmpty(name), "name", name);
        queryWrapper.orderByDesc("add_time");

        Page<BaseGroup> page = baseGroupMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return PageConvert.convert(page);

    }

    @GetMapping("/list")
    public List<BaseGroup> list(String type) {
        //分组列表
       return baseGroupMapper.selectList(new QueryWrapper<BaseGroup>().eq("type",type).orderByDesc("add_time"));

    }


}
