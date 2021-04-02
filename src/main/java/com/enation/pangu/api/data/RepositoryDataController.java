package com.enation.pangu.api.data;

import com.enation.pangu.model.Repository;
import com.enation.pangu.service.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库信息控制器
 * @author zhanghao
 * @create 2020-10-31
 */
@RestController
@RequestMapping("/data/repository")
public class RepositoryDataController {

    @Autowired
    private RepositoryManager repositoryManager;

    /**
     * 查询仓库列表
     */
    @GetMapping
    public List<Repository> listRepository() {
        return repositoryManager.list();
    }

    /**
     * 查询一个仓库
     */
    @GetMapping("/{id}")
    public Repository selectById(@PathVariable Long id) {
        return repositoryManager.selectById(id);
    }

    /**
     * 添加仓库
     */
    @PostMapping
    public void addRepository(Repository repository) {
        repositoryManager.add(repository);
    }

    /**
     * 编辑仓库
     */
    @PutMapping("/{id}")
    public void editRepository(Repository repository) {
        repositoryManager.edit(repository);
    }

    /**
     * 删除仓库
     */
    @DeleteMapping("/{id}")
    public void deleteRepository(@PathVariable Long id) {
        repositoryManager.delete(id);
    }

    /**
     * 验证仓库信息是否正确
     */
    @PostMapping("/validate")
    public void validate(Repository repository) {
        repositoryManager.validate(repository);
    }

}
