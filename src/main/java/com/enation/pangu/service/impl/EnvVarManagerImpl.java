package com.enation.pangu.service.impl;

import com.enation.pangu.mapper.EnvVarMapper;
import com.enation.pangu.model.EnvVariables;
import com.enation.pangu.service.EnvVarManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/12/14
 */
@Service
public class EnvVarManagerImpl implements EnvVarManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EnvVarMapper envVarMapper;

    @Override
    public Map<Long, Map> groupVars(Long projectId) {

        //定义要返回的map
        Map groups = new HashMap(16);

        //读取的变量数据列表，示例数据如下：
        ///////////////////////////////////////////////////////////////////////////////
        //   [                                                                        //
        //    {GROUP_NAME=mysql, ID=3, VALUE=root, GROUP_ID=15, NAME=username},       //
        //    {GROUP_NAME=mysql, ID=4, VALUE=111111, GROUP_ID=15, NAME=password},     //
        //    {GROUP_NAME=es, ID=5, VALUE=127.0.0.0, GROUP_ID=17, NAME=host},         //
        //    {GROUP_NAME=es, ID=6, VALUE=2205, GROUP_ID=17, NAME=port}               //
        //    ]                                                                       //
        ///////////////////////////////////////////////////////////////////////////////
        List<Map> varsGroup = envVarMapper.groupVars(projectId);

        //某个分组的环境变量列表
        List<EnvVariables> varsList = null;

        //定义循环时最后一个分组id
        //这id初始为-1,每次循环都赋予为变量的groupid
        //通过判断当前循环的groupid和last groupid 是否一致，来决定是否新产生一个group
        //因为如果不一致，说明在循环过程中换组了
        long lastGroupId = -1;

        for (Map map : varsGroup) {

            long newGroupId = (Long) map.get("GID");

            //不一致，说明在循环过程中换组了,需要new一个group
            if (lastGroupId != newGroupId) {

                //形成group中的各个数据
                Map group = new HashMap();
                group.put("group", map.get("GROUP_NAME"));
                varsList = new ArrayList<>();
                group.put("data", varsList);

                //在返回结果的map中以groupid为key,压入group
                groups.put(newGroupId, group);
            }

            //lastGroupId每次循环都赋予为变量的groupid
            lastGroupId = newGroupId;
            if (map.get("ID") != null) {
                //形成group中的 data，是个数组，这里用list
                EnvVariables envVariables = new EnvVariables();
                envVariables.setId((Long) map.get("ID"));
                envVariables.setName((String) map.get("NAME"));
                envVariables.setValue((String) map.get("VALUE"));
                envVariables.setGroupId(newGroupId);

                varsList.add(envVariables);
            }

        }

        return groups;
    }


    @Override
    public Map createVariables(Long projectId) {
        Map result = new LinkedHashMap();
        Map<Long, Map> envMap = this.groupVars(projectId);
        envMap.forEach((key, value) -> {

            //比如:msyql
            String groupName = (String) value.get("group");

            //比如：[{"name": "a1", "value": 'v1'}, {"name": "b1", "value": '21'}]
            List<EnvVariables> envList = (List) value.get("data");
            Map envValueMap = new LinkedHashMap();
            envList.forEach(env -> {
                envValueMap.put(env.getName(), env.getValue());
            });

            /*
             * like this:
             * {mysql:{username:root},{password:111111}}
             * {someGroup:{one:a},{tow:b}}
             */
            result.put(groupName, envValueMap);

        });
        return result;
    }
}
