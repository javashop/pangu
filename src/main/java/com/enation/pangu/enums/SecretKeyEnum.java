package com.enation.pangu.enums;

/**
 * @Author shen
 * @Date 2021/3/15 16:33
 */
public enum SecretKeyEnum {
    /**
     * 公钥
     */
    publickey("公钥"),
    /**
     * 私钥
     */
    privatekey("私钥"),

    /**
     * 密码
     */
    password("密码");

    private String des;

    SecretKeyEnum(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String value(){
        return this.name();
    }
}
