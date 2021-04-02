package com.enation.pangu.service.impl;

import com.enation.pangu.mapper.TagMapper;
import com.enation.pangu.model.Tag;
import com.enation.pangu.service.TagManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenyw
 * @create 2020-11-11-14:49
 */
@Service
public class TagManagerImpl implements TagManager {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public void add(Tag tag) {
        int insert = tagMapper.insert(tag);
    }

    @Override
    public void edit(Tag tag) {
        int i = tagMapper.updateById(tag);
    }

    @Override
    public void delelte(Long id) {
        int i = tagMapper.deleteById(id);

    }

    @Override
    public List<Tag> list() {
        List<Tag> tags = tagMapper.selectList(null);
        return tags;
    }
}
