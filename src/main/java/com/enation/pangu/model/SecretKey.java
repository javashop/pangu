package com.enation.pangu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@TableName(value = "secret_key ")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SecretKey  {
    /**
     * 密钥对
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     * 公钥
     */
    private String publicKey;
    /**
     *
     * 私钥
     */
    private String privateKey;
    /**
     *
     * 名称
     */
    private String name;
    /**
     *
     * 密钥类型  0 创建的密钥  1 导入的密钥私钥
     *
     */
    private int  type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
