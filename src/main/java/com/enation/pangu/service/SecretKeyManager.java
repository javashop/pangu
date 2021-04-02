package com.enation.pangu.service;


import com.enation.pangu.model.SecretKey;
import com.enation.pangu.model.WebPage;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 密钥对管理
 *
 *
 * @author shenyanwu
 */
public interface SecretKeyManager {
    /**
     * 查询列表
     * @return
     */
    WebPage list(int pageNo, int pageSize);

    /**
     * 查询列表
     * @return
     */
   List<SecretKey> keysList();

    /**
     * 查询一个密钥对
     * @param id 密钥id
     * @return
     */
    SecretKey selectById(Long id);
    /**
     * 查询一个密钥对
     * @param name 密钥名称
     * @return
     */
    SecretKey selectByName(String name);

    /**
     * 新增密钥对
     * @param SecretKey
     */
    void add(SecretKey SecretKey);

    /**
     * 删除一个密钥对
     * @param id
     */
    void delete(Long id);

    /**
     * 导出公钥文件
     *
     * */
    void export(HttpServletResponse response, Long id);

    /**
     * 导出公钥文件
     *
     * */
    void exportPrivate(HttpServletResponse response, Long id);
}
