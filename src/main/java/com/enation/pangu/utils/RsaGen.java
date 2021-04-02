package com.enation.pangu.utils;

import com.enation.pangu.enums.SecretKeyEnum;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成密钥对
 * @author shenyanwu
 */
public class RsaGen {

    public static Map<String, String> getKeyMap(String comment) {
        Map<String, String> keys = new HashMap<>();
        int type = KeyPair.RSA;
        JSch jsch = new JSch();

        try {
            KeyPair kpair = KeyPair.genKeyPair(jsch, type, 2048);
            //私钥
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //向OutPutStream中写入
            kpair.writePrivateKey(baos);
            String privateKeyString = baos.toString();

            //公钥
            baos = new ByteArrayOutputStream();
            kpair.writePublicKey(baos, comment);
            String publicKeyString = baos.toString();
            kpair.dispose();

            // 得到公钥字符串
            keys.put(SecretKeyEnum.publickey.name(), publicKeyString);

            // 得到私钥字符串
            keys.put(SecretKeyEnum.privatekey.name(), privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }


}
