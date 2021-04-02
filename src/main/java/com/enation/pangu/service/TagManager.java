package com.enation.pangu.service;

import com.enation.pangu.model.Tag;

import java.util.List;

/**
 * @author shenyw
 * @create 2020-11-11-14:44
 */
public interface TagManager {
    /**
     * 添加标签
     * @param tag  标签实体
     *
     * */
    void add(Tag tag);

    /**
     * 更新标签
     * @param tag  标签实体
     *
     * */
    void edit(Tag tag);

    /**
     * 更新标签
     * @param id  标签id
     *
     * */
    void delelte(Long id);

    /**
     * 标签列表
     * @param
     *
     * */
    List<Tag> list();

}
