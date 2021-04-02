package com.enation.pangu.ssh;

import com.enation.pangu.config.exception.SshException;
import com.enation.pangu.model.Machine;
import com.enation.pangu.ssh.impl.JschImpl;
import com.jcraft.jsch.JSchException;

/**
 * ssh客户端创建工厂
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/4
 */

public class SshClientFactory {



    /**
     * 基于用户名密码认证创建客户端
     * @param username 用户名
     * @param password 密码
     * @param host 主机ip
     * @param port ssh端口号
     * @return
     */
    public static SshClient createSsh(String username, String password, String host, int port)  {
        try {
            return new JschImpl(username, password, host, port);
        } catch (JSchException e) {
            throw new SshException("创建ssh客户端出错", e);
        }
    }
    /**
     *
     * 用户名密码或私钥连接客户端
     * @param properties 配置参数
     * 主要包含 用户名 密码 私钥 主机ip 端口 认证类型
     *
     * */
    public static SshClient createSsh(Machine properties)  {
        try {
            return new JschImpl(properties);
        } catch (JSchException e) {
            throw new SshException("创建ssh客户端出错", e);
        }
    }


}
