package com.enation.pangu.api.view;


import com.enation.pangu.enums.SecretKeyEnum;
import com.enation.pangu.model.Repository;
import com.enation.pangu.service.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 仓库信息视图控制器
 * @author zhanghao
 * @create 2020-10-31
 */
@Controller
@RequestMapping("/view/repository")
public class RepositoryViewController {

    @Autowired
    private RepositoryManager repositoryManager;

    /**
     * 跳转到仓库列表页
     * @return
     */
    @GetMapping("/list")
    public String list(){
        return "/repository/list";
    }

    /**
     * 跳转到仓库编辑页面
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id){
        Repository repository = repositoryManager.selectById(id);
        model.addAttribute("repository",repository);
        return "/repository/edit";
    }

    /**
     * 跳转到新增仓库页面
     * @return
     */
    @GetMapping("/add")
    public String add(){
        return "/repository/add";
    }
}
