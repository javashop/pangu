package com.enation.pangu.api.data;

import com.enation.pangu.model.EnvProject;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.EnvProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shen
 * @create 2020-12-01-18:06
 */
@RestController
@RequestMapping("/data/envProject")
public class EnvProjectDataController {
    @Autowired
    private EnvProjectManager envProjectManager;
    /**
     * 添加项目
     *
     * */
    @PostMapping()
    public String addEnvProject(EnvProject envProject) {
        EnvProject baseEnvProject = envProjectManager.selectProjectByName(envProject.getName());
        if (baseEnvProject==null){
            envProjectManager.addProject(envProject);
            return "success";
        }else {
            return "项目名称重复";
        }
    }

    /**
     * 编辑
     */
    @PutMapping("/{id}")
    public String editEnvProject(@PathVariable("id") Long id,EnvProject envProject) {
        envProject.setId(id);
        EnvProject baseEnvProject = envProjectManager.selectProjectByName(envProject.getName());
        if (baseEnvProject==null){
            envProjectManager.editProject(envProject);
            return "success";
        }else {
            return "项目名称已存在";
        }

    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void deleteEnvProject(@PathVariable Long id) {

        envProjectManager.deleteProject(id);
    }

    /**
     * 查询列表
     */
    @GetMapping()
    public WebPage list(int pageNo, int pageSize) {

        return envProjectManager.list(pageNo, pageSize);

    }
}
