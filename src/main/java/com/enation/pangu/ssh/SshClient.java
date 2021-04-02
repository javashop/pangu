package com.enation.pangu.ssh;

import com.enation.pangu.domain.Copy;
import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/4
 */
public interface SshClient {


    /**
     * 执行ssh 命令
     * @param command 要执行的命令，多个命令通过";"号隔开
     * @param execCallback 命令执行过程中的回调器
     * @return 执行成功返回0，失败返回1
     * @throws IOException
     */
    int exec(String command, ExecCallback execCallback) throws IOException;

    /**
     * 执行多行命令
     * @param command
     * @param execCallback
     * @return
     * @throws IOException
     */
    int exec(List<String> command, ExecCallback execCallback) throws IOException;


    void exec1(String command, ExecCallback execCallback);

    /**
     * ssh 传输文件
     * 支持两种文件路径：
     * 1、绝对路径：需要使用file:开头
     * 2、相对classpath的路径：需要使用classpath:开头
     * @param copy 要执行的copy对象
     * @param env 环境变量
     * @throws IOException
     */
    void copyFile(Copy copy, Map env) throws IOException;

    /**
     * 断开客户端连接
     */
     void disconnect() throws IOException;

}
