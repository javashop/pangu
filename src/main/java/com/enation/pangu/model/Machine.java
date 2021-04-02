package com.enation.pangu.model;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author shenyw
 * @create 2020-10-31-13:46
 */

@TableName(value = "machine")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Machine {
    /**
     * 机器id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 机器ip
     */
    private String ip;
    /**
     * 机器名称
     */
    private String name;
    /**
     * 认证类型，可选值：
     * password,certificate
     */
    private String authType;
    /**
     * 认证用户名
     */
    private String username;
    /**
     * 认证密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Long addTime;
    /**
     * 密钥id
     */
    private String secretkeyId;
    /**
     * 临时存储密钥
     */
    private String privateKey;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 分组id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long groupId;

    public String getSecretkeyId() {
        return secretkeyId;
    }

    public void setSecretkeyId(String secretkeyId) {
        this.secretkeyId = secretkeyId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        if(null==port){
            this.port = 22;
        }
        this.port = port;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", authType='" + authType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", addTime=" + addTime +
                ", secretkeyId='" + secretkeyId + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", port=" + port +
                ", groupId=" + groupId +
                '}';
    }
}
