package com.enation.pangu.service;

import java.util.Map;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/14
 */
public interface EnvVarManager {

    /**
     * 按分组group返回变量，最终返回格式示例：
     * {
     * 1: {
     * "group": "mysql",
     * "data": [{"name": "a", "value": 'v'}, {"name": "b", "value": '2'}]
     * },
     * 2: {
     * "group": "rabbitmq",
     * "data": [{"name": "a1", "value": 'v1'}, {"name": "b1", "value": '21'}]
     * }
     * }
     * @param projectId
     * @return
     *
     */
    Map<Long,Map> groupVars(Long projectId);

    /**
     * 形成某个工程的环境变量Map，格式如下：
     * {mysql:{username:root},{password:111111}}
     * {someGroup:{one:a},{tow:b}}
     * 主要的目的是为了在插件模板中这样使用：
     * ${mysql.username}
     * @param projectId
     * @return
     */
    Map createVariables(Long projectId);
}
