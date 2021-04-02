package com.enation.pangu.ssh.impl;

import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.ssh.SshClientContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ssh客户端上下文
 * @author zhangsong
 * @version 1.0
 * @since 1.0.0
 * 2020/03/15
 */

@Component
public class SshClientContextImpl implements SshClientContext {

    /**
     * 存储SshClient Key:任务id value:SshClient集合
     */
    private Map<Long, List<SshClient>> sshClientMap = new HashMap<>();

    @Override
    public void addSshClient(Long taskId, SshClient sshClient) {
        List<SshClient> sshClients = sshClientMap.get(taskId);
        if (sshClients == null) {
            sshClients = new ArrayList<>();
            sshClientMap.put(taskId, sshClients);
        }
        sshClients.add(sshClient);
    }

    @Override
    public List<SshClient> getSshClient(Long taskId) {
        return sshClientMap.get(taskId);
    }

    @Override
    public void remove(Long taskId, SshClient sshClient) {
        List<SshClient> sshClients = sshClientMap.get(taskId);
        if(sshClients != null){
            sshClients.remove(sshClient);
        }
    }


}
