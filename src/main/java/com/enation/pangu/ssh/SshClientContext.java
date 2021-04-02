package com.enation.pangu.ssh;

import java.util.List;

/**
 * @author zhangsong
 * @version 1.0
 * @since 1.0.0
 * 2020/03/15
 */
public interface SshClientContext {

    /**
     * 添加一个sshClient
     * @param taskId 任务id
     * @param sshClient
     */
    void addSshClient(Long taskId, SshClient sshClient);

    /**
     * 查询某个任务的sshClient集合
     * @param taskId 任务id
     * @return
     */
    List<SshClient> getSshClient(Long taskId);

    /**
     * 从上下文中移除某个sshClient
     * @param taskId 任务id
     * @param sshClient
     */
    void remove(Long taskId, SshClient sshClient);
}
