package com.enation.pangu.api.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.pangu.mapper.TagMapper;
import com.enation.pangu.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签数据控制器
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/9
 */
@RestController
@RequestMapping("/data/tags")
public class TagDataController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    TagMapper tagMapper;

    /**
     * 查询部署列表
     */
    @GetMapping()
    public List<Tag> list() {
        return tagMapper.selectList(new QueryWrapper<Tag>().orderByAsc("id"));
    }


    @PostMapping()
    public Tag add(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.insert(tag);
        return tag;
    }

    @PutMapping("/{id}")
    public String edit(@PathVariable("id") Long id,String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.update(tag, new UpdateWrapper<Tag>().eq("id", id));
        return "ok";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {

        tagMapper.deleteById(id);
        return "ok";
    }


}
