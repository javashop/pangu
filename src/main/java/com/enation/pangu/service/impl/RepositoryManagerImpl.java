package com.enation.pangu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.RepositoryMapper;
import com.enation.pangu.model.Repository;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.service.RepositoryManager;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * 仓库管理业务层
 * @author zhanghao
 * @create 2020-10-31
 */
@Service
public class RepositoryManagerImpl implements RepositoryManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private RepositoryMapper repositoryMapper;

    @Override
    public List<Repository> list() {
        QueryWrapper<Repository> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("add_time");
        return repositoryMapper.selectList(wrapper);
    }

    @Override
    public Repository selectById(Long id) {
        if(id == null){
            return null;
        }
        return repositoryMapper.selectById(id);
    }

    @Override
    public void add(Repository repository) {
        repository.setAddTime(System.currentTimeMillis());
        repositoryMapper.insert(repository);
    }

    @Override
    public void edit(Repository repository) {
        repositoryMapper.updateById(repository);
    }

    @Override
    public void delete(Long id) {
        repositoryMapper.deleteById(id);
    }

    @Override
    public void validate(Repository repository) {

        try {
            LsRemoteCommand ls = Git.lsRemoteRepository()
            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.getUsername(), repository.getPassword()));
            ls.setRemote(repository.getAddress()).call();

        } catch (Exception e) {
            e.printStackTrace();

            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"仓库信息有误");
        }

    }
}
