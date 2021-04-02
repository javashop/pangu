package com.enation.pangu.ssh.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.enation.pangu.constant.PluginConstant;
import com.enation.pangu.domain.Copy;
import com.enation.pangu.enums.SecretKeyEnum;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.config.exception.SshException;
import com.enation.pangu.model.Machine;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.ssh.ExecCallback;
import com.enation.pangu.ssh.SshClient;
import com.enation.pangu.utils.ParseException;
import com.enation.pangu.utils.PathUtil;
import com.jcraft.jsch.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ssh 命令操作类
 *
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/11/4
 */

public class JschImpl implements SshClient {

    /**
     * jsch session
     */
    Session session;





    /**
     * 基于用户名密码认证的构造器
     *
     * @param username 用户名
     * @param password 密码
     * @param host     主机ip
     * @param port     ssh端口号
     * @throws JSchException
     */

    public JschImpl(String username, String password, String host, int port) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, port);
        session.setConfig("PreferredAuthentications", "password");
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();

    }

    /**
     * 基于用户名密码认证的构造器
     * Machine实体类
     *
     * @throws JSchException
     */
    public JschImpl(Machine machine) throws JSchException {

        JSch jsch = new JSch();
        //添加密钥(信任登录方式)
        if (SecretKeyEnum.publickey.name().equals(machine.getAuthType())) {
            jsch.addIdentity(null, machine.getPrivateKey().getBytes(), null, null);
        }
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getPort());
        //设置密码
        if (SecretKeyEnum.password.name().equals(machine.getAuthType()) && StringUtils.isNotBlank(machine.getPassword())) {
            session.setPassword(machine.getPassword());
        }
        //第一次登陆时候，是否需要提示信息
        session.setConfig("StrictHostKeyChecking", "no");
        //设置ssh的DH秘钥交换
        session.setConfig("kex", "diffie-hellman-group1-sha1");
        //跳过Kerberos username 身份验证提示
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        // 通过Session建立链接
        session.connect();
    }

    @Override
    public void disconnect() {

        session.disconnect();
    }

    @Override
    public void exec1(String command, ExecCallback execCallback) {

        ChannelExec channel = null;

        //命令执行结果
        int result = 0;

        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            channel.setInputStream(null);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){

                    int i=in.read(tmp, 0, 1024);

                    String  text =  new String(tmp, 0, i);
                    execCallback.callback(text);

                    if(i<0) {
                        break;
                    }

                }


                if(channel.isClosed()){
                    if(in.available()>0) {
                        continue;
                    }
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }



        } catch (JSchException | IOException e) {
            throw new SshException("execute command error", e);
        } finally {
            channel.disconnect();
        }

    }

    @Override
    public int exec(String command, ExecCallback execCallback) throws IOException {

        ChannelShell channel = null;
        try {
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        InputStream inputStream = channel.getInputStream();
        OutputStream outputStream = channel.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

        printWriter.println(command);

        printWriter.println("exit");
        printWriter.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String msg = null;
        while ((msg = in.readLine()) != null) {
            execCallback.callback(msg);
        }
        int result = channel.getExitStatus();

        in.close();

        return result;
    }


    @Override
    public int exec(List<String> commands, ExecCallback execCallback) throws IOException {

        ChannelShell channel = null;
        try {
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        InputStream inputStream = channel.getInputStream();
        OutputStream outputStream = channel.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

        for (String command : commands) {
            printWriter.println(command);
        }


        printWriter.println("exit");
        printWriter.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String msg = null;
        while ((msg = in.readLine()) != null) {
            execCallback.callback(msg);
        }
        while (true) {
            if (channel.isClosed()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int result = channel.getExitStatus();

        in.close();
        channel.disconnect();

        return result;
    }

    /**
     * ssh 传输文件
     * 支持两种文件路径：
     * 1、绝对路径：需要使用file:开头
     * 2、相对classpath的路径：需要使用classpath:开头
     *
     * @param copy 要执行的copy对象
     * @throws IOException
     */
    @Override
    public void copyFile(Copy copy, Map env) throws IOException {
        String localFilePath = copy.getSource();
        String remoteFilePath = copy.getTarget();
        boolean ptimestamp = true;
        // exec 'scp -t remoteFilePath' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remoteFilePath;
        ChannelExec channel = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            // get I/O streams for remote scp
            out = channel.getOutputStream();
            in = channel.getInputStream();

            channel.connect();

            innerCopy(in, out, localFilePath, copy.isParseEnv(), env);

            //替换/r为空
            this.exec("sed -i 's/\\r//' "+ remoteFilePath,str->{});

        } catch (JSchException e) {
            throw new SshException("execute scp error", e);
        } catch (IOException e) {
            throw new SshException("execute scp error", e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }

            channel.disconnect();
        }


    }

    /**
     * 文件传输
     *
     * @param in
     * @param out
     * @param localFilePath
     * @throws IOException
     */
    private void innerCopy(InputStream in, OutputStream out, String localFilePath, boolean parseEnv, Map env) throws IOException {
        boolean ptimestamp = true;

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        String content = getFileContent(env, localFilePath, parseEnv);
        File _localFile = new File(localFilePath);
        String command;
        if (ptimestamp) {
            command = "T" + (_localFile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_localFile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"scp error,detail see the log");
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = content.getBytes().length;
        command = "C0644 " + filesize + " ";
        if (localFilePath.lastIndexOf('/') > 0) {
            command += localFilePath.substring(localFilePath.lastIndexOf('/') + 1);
        } else {
            command += localFilePath;
        }
        command += "\n";

        out.write(command.getBytes());
        out.flush();
        if (checkAck(in) != 0) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"scp error,detail see the log");
        }

        // send a content of lfile
//        FileInputStream fis = new FileInputStream(localFilePath);
        InputStream stream = new ByteArrayInputStream(content.getBytes());

        byte[] buf = new byte[1024];
        while (true) {
            int len = stream.read(buf, 0, buf.length);
            if (len <= 0) {
                break;
            }
            //out.flush();
            out.write(buf, 0, len);
        }
        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
        stream.close();

        if (checkAck(in) != 0) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"scp error,detail see the log");
        }

    }

    private static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) {
            return b;
        }
        if (b == -1) {
            return b;
        }

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            // error
            if (b == 1) {
                System.out.print(sb.toString());
            }
            // fatal error
            if (b == 2) {
                System.out.print(sb.toString());
            }
        }
        return b;
    }


    String getFileContent(String filePath) {
        try {
            if (filePath.startsWith("file:")) {
                filePath = filePath.replaceAll("file:", "");

                return FileUtils.readFileToString(new File(PathUtil.getRootPath() + File.separator + PluginConstant.PLUGINS_PATH_PROFIX + File.separator + filePath), "UTF-8");

            } else {
                ClassLoader classLoader = JschImpl.class.getClassLoader();
                filePath = filePath.replaceAll("classpath:", "");
                //去掉开头的'/'（如果有的话）
                filePath = filePath.startsWith("/") ? filePath.substring(1)
                        : filePath;
                URL url = classLoader.getResource(filePath);
                return FileUtils.readFileToString(new File(url.getFile()), "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "";
    }


    /**
     * 读取文件内容
     * 支持两种文件路径：
     * 1、绝对路径：需要使用file:开头
     * 2、相对classpath的路径：需要使用classpath:开头
     *
     * @param env      要解析的环境变量
     * @param filePath 模板文件路径
     * @param parseEnv 是否解析变量
     * @return 通过freemarker解析后端文件内容
     */
    String getFileContent(Map env, String filePath, boolean parseEnv) {
        if (!parseEnv) {
            return getFileContent(filePath);
        }
        StringWriter writer = new StringWriter();
        try {

            Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

            //file:表示要使用绝对路径，否则使用classpath下的相对模板路径
            if (filePath.startsWith("file:")) {
                filePath = filePath.replaceAll("file:", "");
                File file = new File(filePath);
                cfg.setDirectoryForTemplateLoading(file.getParentFile());
                filePath = file.getName();

            } else {
                cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "");
                filePath = filePath.replaceAll("classpath:", "");

            }


            cfg.setDefaultEncoding("UTF-8");
            cfg.setNumberFormat("#.##");
            Template temp = cfg.getTemplate(filePath);
            temp.process(env, writer);

            return writer.toString();
        } catch (Exception e) {
            throw new ParseException("模板解析出错", e);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        String path = PathUtil.getRootPath() + "/privtekey";
        System.out.println(path);
    }

    public static void main1(String[] args) {

        StringWriter writer = new StringWriter();
        try {
            String localFilePath = "b2b2c测试/application.yml";
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
            cfg.setDirectoryForTemplateLoading(new File("/Users/kingapex/ideaworkspace/pangu/target/config_project"));
//            cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setNumberFormat("#.##");
            Template temp = cfg.getTemplate(localFilePath);
            Writer out = new OutputStreamWriter(System.out);

            temp.process(new HashMap<>(), out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

}
