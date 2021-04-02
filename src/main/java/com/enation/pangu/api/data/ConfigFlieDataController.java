package com.enation.pangu.api.data;

import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.model.ConfigFile;
import com.enation.pangu.model.ResultCode;
import com.enation.pangu.model.WebPage;
import com.enation.pangu.service.ConfigFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 项目文件控制器
 *
 * @author lizhengguo
 * @create 2020-12-1
 */
@RestController
@RequestMapping("/data/config_project")
public class ConfigFlieDataController {

    @Autowired
    private ConfigFileManager configFileManager;


    /**
     * 查询项目文件列表
     */
    @GetMapping("/{id}/page")
    public WebPage<ConfigFile> listProjectFiles(@PathVariable Long id) {
        return configFileManager.list(1, 10, id);
    }


    /**
     * 查询一个项目文件
     */
    @GetMapping("/config_file/{id}")
    public ConfigFile selectById(@PathVariable Long id) {
        return configFileManager.selectById(id);
    }

    /**
     * 添加项目文件
     */
    @PostMapping("/{id}/config_file")
    public void addProjectFiles(ConfigFile projectFiles) throws Exception {
        projectFiles.setConfigProjectId(projectFiles.getId());
        projectFiles.setId(null);
        int i = configFileManager.add(projectFiles);
        if (i < 1) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"添加项目文件失败");
        }
    }

    /**
     * 编辑项目文件
     */
    @PutMapping("/config_file/{id}")
    public int editProjectFiles(ConfigFile projectFile) throws Exception {
        int i = configFileManager.edit(projectFile);
        if (i < 1) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"编辑项目文件失败");
        } else {
            return 1;
        }
    }

    /**
     * 删除项目文件
     */
    @DeleteMapping("/config_file/{id}")
    public void deleteProjectFiles(@PathVariable Long id) throws Exception {
        int i = configFileManager.delete(id);
        if (i < 1) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"删除项目文件失败");
        }
    }

    /**
     * 查询某个项目的所有项目文件
     */
    @GetMapping("/{id}")
    public List<ConfigFile> listAll(@PathVariable Long id) {
        return configFileManager.listAll(id);
    }

    /**
     * 查询某个项目的所有项目文件
     */
    @GetMapping("/{id}/step-info")
    public Map<String, Object> listAllStepInfo(@PathVariable Long id, Long stepId) {
        return configFileManager.listAllStepInfo(id, stepId);
    }


}
