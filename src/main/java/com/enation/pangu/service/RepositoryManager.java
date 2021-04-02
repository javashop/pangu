package com.enation.pangu.service;

import com.enation.pangu.model.Repository;

import java.util.List;

/**
 * 仓库管理业务层
 * @author zhanghao
 * @create 2020-10-31
 */
public interface RepositoryManager {

    /**
     * 查询仓库列表
     * @return
     */
    List<Repository> list();

    /**
     * 查询一个仓库
     * @param id 部署id
     * @return
     */
    Repository selectById(Long id);

    /**
     * 新增仓库
     * @param repository
     */
    void add(Repository repository);

    /**
     * 修改仓库
     * @param repository
     */
    void edit(Repository repository);

    /**
     * 删除一个仓库
     * @param id
     */
    void delete(Long id);

    /**
     * 验证仓库信息是否正确
     * @param repository
     */
    void validate(Repository repository);
}
