package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.enums.MessageStatusEnum;
import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.mapper.MessageMapper;
import com.enation.pangu.model.Message;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.MessageManager;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息业务层实现类
 *
 * @author zhangsong
 * @date 2020-01-25
 */
@Service
public class MessageManagerImpl implements MessageManager {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public WebPage<Message> list(int pageNo, int pageSize) {
        IPage page = new QueryChainWrapper<>(messageMapper).orderByDesc("create_time").page(new Page(pageNo, pageSize));
        return PageConvert.convert(page);
    }

    @Override
    public void insert(Message message) {
        messageMapper.insert(message);
    }

    @Override
    public List<Message> limit(int num) {
        return new QueryChainWrapper<>(messageMapper)
                .eq("status", MessageStatusEnum.unread.value())
                .orderByDesc("create_time")
                .last("limit " + num)
                .list();
    }

    @Override
    public int totalUnread() {
        return new QueryChainWrapper<>(messageMapper).eq("status", MessageStatusEnum.unread.value()).count();
    }

    @Override
    public void read(Long id) {
        new UpdateChainWrapper<>(messageMapper).set("status", MessageStatusEnum.read.value()).eq("id", id).update();
    }
}
