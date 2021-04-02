package com.enation.pangu.api.data;

import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.model.ConfigProject;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.ConfigProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目控制器
 *
 * @author lizhengguo
 * @create 2020-12-1
 */
@RestController
@RequestMapping("/data/config_project")
public class ConfigProjectDataController {

    @Autowired
    private ConfigProjectManager configProjectManager;

    /**
     * 跳转到项目列表页
     *
     * @return
     */
    @GetMapping("/page")
    public WebPage<ConfigProject> list(int pageNo, int pageSize) {
        return configProjectManager.list(pageNo, pageSize);
    }


    /**
     * 添加项目
     */
    @PostMapping()
    public void addProject(ConfigProject configProject) throws Exception {
        int i = configProjectManager.add(configProject);
        if (i < 1) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"添加项目失败");
        }
    }

    /**
     * 修改项目名称
     */
    @PutMapping()
    public void putProject(ConfigProject configProject) throws Exception {
        int i = configProjectManager.edit(configProject);
        if (i < 1) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"修改项目失败");
        }
    }


    /**
     * 删除项目
     */
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) throws Exception {
        int i = configProjectManager.delete(id);
        if (i < 1) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"删除项目失败");
        }
    }


    @GetMapping()
    public List<ConfigProject> listAll() {
        return configProjectManager.listAll();
    }

    @GetMapping("/step-info")
    public Map<String, Object> listAllStepInfo(Long stepId) {
        return configProjectManager.listAllStepInfo(stepId);
    }


}
