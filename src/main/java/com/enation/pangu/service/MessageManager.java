package com.enation.pangu.service;

import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 消息业务层接口
 * @author zhangsong
 * @date 2020-01-25
 */
public interface MessageManager {

    WebPage<Message> list(int pageNo, int pageSize);

    void insert(Message message);

    /**
     * 查询前n条未读消息
     * @param num
     * @return
     */
    List<Message> limit(int num);

    /**
     * 查询未读消息数
     * @return
     */
    int totalUnread();

    /**
     * 消息状态改为已读
     * @param id
     */
    void read(Long id);
}
