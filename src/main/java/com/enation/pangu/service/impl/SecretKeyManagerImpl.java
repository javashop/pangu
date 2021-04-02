package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.mapper.SecretKeyMapper;
import com.enation.pangu.model.SecretKey;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.SecretKeyManager;
import com.enation.pangu.utils.ExportUtil;
import com.enation.pangu.utils.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
/**
 * @author shenyanwu
 */
@Service
public class SecretKeyManagerImpl implements SecretKeyManager {
    @Autowired
    private SecretKeyMapper secretKeyMapper;
    @Override
    public WebPage list(int pageNo, int pageSize) {
        QueryWrapper<SecretKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name","type").orderByDesc("id");
        Page<SecretKey> ipage = secretKeyMapper.selectPage(new Page<SecretKey>(pageNo, pageSize), queryWrapper);
        WebPage page = PageConvert.convert(ipage);
        return page;
    }

    @Override
    public List<SecretKey> keysList() {
        QueryWrapper<SecretKey> wrapper = new QueryWrapper<>();
        wrapper.select("id","name","type");

        return secretKeyMapper.selectList(wrapper);
    }

    @Override
    public SecretKey selectById(Long id) {
        return secretKeyMapper.selectById(id);
    }

    @Override
    public SecretKey selectByName(String name) {
        QueryWrapper<SecretKey> query = new QueryWrapper<>();
        query.eq("name",name);
        return secretKeyMapper.selectOne(query);
    }

    @Override
    public void add(SecretKey SecretKey) {
        secretKeyMapper.insert(SecretKey);
    }

    @Override
    public void delete(Long id) {
        secretKeyMapper.deleteById(id);
    }
    @Override
    public void export(HttpServletResponse response, Long id){

        SecretKey SecretKey = selectById(id);
        String publicKey = SecretKey.getPublicKey();

        String filename = "id_rsa.pub";


        ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKey.getBytes());
        ExportUtil.export(filename, response, inputStream);

    }

    @Override
    public void exportPrivate(HttpServletResponse response, Long id) {
        SecretKey SecretKey = selectById(id);
        String publicKey = SecretKey.getPrivateKey();

        String filename = "id_rsa";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKey.getBytes());
        ExportUtil.export(filename, response, inputStream);
    }
}
