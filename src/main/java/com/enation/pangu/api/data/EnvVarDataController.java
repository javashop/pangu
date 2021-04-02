package com.enation.pangu.api.data;

import com.enation.pangu.mapper.EnvVarMapper;
import com.enation.pangu.model.EnvVariables;
import com.enation.pangu.service.EnvVarManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 环境变量数据控制器
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/25
 */
@RestController
@RequestMapping("/data/env_project/{projectId}")
public class EnvVarDataController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EnvVarMapper envVarMapper;

    @Autowired
    private EnvVarManager envVarManager;

    /**
     * 按分组group返回变量，最终返回格式示例：
     * const varData = {
     * 1: {
     * "group": "mysql",
     * "data": [{"name": "a", "value": 'v'}, {"name": "b", "value": '2'}]
     * },
     * 2: {
     * "group": "rabbitmq",
     * "data": [{"name": "a1", "value": 'v1'}, {"name": "b1", "value": '21'}]
     * }
     * }
     *
     * @return
     */
    @GetMapping("/env-vars")
    public Map listVars(@PathVariable("projectId") Long projectId) {
        Map result = envVarManager.groupVars(projectId);
        return result;
    }

    @PostMapping("/env-vars")
    public EnvVariables add(@PathVariable("projectId") Long projectId,EnvVariables envVariables) {
        envVariables.setProjectId(projectId);
        envVarMapper.insert(envVariables);
        return envVariables;
    }


    @PutMapping("/env-vars/{envId}")
    public EnvVariables update(@PathVariable("envId") Long envId, EnvVariables envVariables) {
        envVariables.setId(envId);
        envVarMapper.updateById(envVariables);
        return envVariables;
    }


    @DeleteMapping("/env-vars/{envId}")
    public String delete(@PathVariable("envId") Long envId) {
        envVarMapper.deleteById(envId);
        return "ok";
    }
}
