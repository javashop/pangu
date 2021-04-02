package com.enation.pangu.api.data;

import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.enums.SecretKeyEnum;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.model.SecretKey;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.SecretKeyManager;
import com.enation.pangu.utils.RsaGen;
import com.enation.pangu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.List;
import java.util.Map;

/**
 * @author shen
 */

@RestController
@RequestMapping("/data/keys")
public class SecretKeyDataController {
    @Autowired
    private SecretKeyManager secretKeyManager;

    /**
     * 查询密钥对列表
     */
    @GetMapping
    public WebPage listKeys(int pageNo, int pageSize) {
        return secretKeyManager.list(pageNo, pageSize);
    }

    /**
     * 查询密钥对L
     */
    @GetMapping("/list")
    public List<SecretKey> getList() {
        return secretKeyManager.keysList();
    }


    /**
     * 查询一个密钥对
     */
    @GetMapping("/{id}")
    public SecretKey selectById(@PathVariable Long id) {
        return secretKeyManager.selectById(id);
    }


    /**
     * 添加密钥对
     */
    @PostMapping
    public void addKeys(SecretKey keys) {
        secretKeyManager.add(keys);
    }

    /**
     * 删除密钥对
     */
    @DeleteMapping("/{id}")
    public void deleteKeys(@PathVariable Long id) {
        secretKeyManager.delete(id);
    }


    /**
     * 生成秘钥
     */
    @PostMapping("/create")
    public String createKey(String name) throws IOException {

        Map<String, String> keyMap = RsaGen.getKeyMap(null);
        SecretKey secretKey = new SecretKey();
        secretKey.setName(name);
        secretKey.setType(0);
        secretKey.setPrivateKey(keyMap.get(SecretKeyEnum.privatekey.name()));
        secretKey.setPublicKey(keyMap.get(SecretKeyEnum.publickey.name()));
        SecretKey baseKey = secretKeyManager.selectByName(name);
        if (baseKey != null) {
            throw new ServiceException(ResultCode.PARAMETER_CHECK_ERROR.getCode(), "密钥名称重复");
        }
        secretKeyManager.add(secretKey);
        if (StringUtil.isEmpty(keyMap.get(SecretKeyEnum.publickey.name())) || StringUtil.isEmpty(keyMap.get(SecretKeyEnum.privatekey.name()))) {
            return "error";
        }
        return "success";
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, String name) {
        String alartTxt = "";
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            while ((lineTxt = bf.readLine()) != null) {
                lineTxt += '\n';
                alartTxt += lineTxt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SecretKey secretKey = new SecretKey();
        secretKey.setName(name);
        secretKey.setType(1);
        secretKey.setPrivateKey(alartTxt);

        SecretKey baseKey = secretKeyManager.selectByName(name);
        if (baseKey != null) {
            throw new ServiceException(ResultCode.PARAMETER_CHECK_ERROR.getCode(), "密钥名称重复");
        }
        secretKeyManager.add(secretKey);

        return "success";

    }

    /**
     * 下载公钥文件
     */
    @GetMapping("/{id}/public/export")
    public void exportPublic(HttpServletResponse response, @PathVariable("id") Long id) {
        secretKeyManager.export(response, id);
    }

    /**
     * 下载私钥文件
     */
    @GetMapping("/{id}/private/export")
    public void exportPrivate(HttpServletResponse response, @PathVariable("id") Long id) {
        secretKeyManager.exportPrivate(response, id);
    }
}
