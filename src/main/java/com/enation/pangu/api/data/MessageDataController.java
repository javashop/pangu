package com.enation.pangu.api.data;

import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.MessageManager;
import com.enation.pangu.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息控制器
 * @author zhangsong
 * @date 2020-01-05
 */
@RestController
@RequestMapping("/data/message")
public class MessageDataController {

    @Autowired
    private MessageManager messageManager;

    /**
     * 查询消息列表
     */
    @GetMapping
    public WebPage<Message> list(int pageNo, int pageSize) {

        return messageManager.list(pageNo, pageSize);
    }


    /**
     * 查询前几条消息
     */
    @GetMapping("/limit")
    public Map limit(int num) {
        Map map = new HashMap(2);
        map.put("limit", messageManager.limit(num));
        map.put("total_unread", messageManager.totalUnread());

        return map;
    }

    /**
     * 消息状态改为已读
     */
    @GetMapping("/{id}/read")
    public void limit(@PathVariable Long id) {
        messageManager.read(id);
    }

}
