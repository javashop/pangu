package com.enation.pangu.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.enation.pangu.enums.KindEnum;

import java.util.List;
import java.util.Map;

/**
 * 部署导出实体类
 * @author zhangsong
 * @date 2020-01-09
 */
public class DeploymentImport {

//    /**
//     * 资源类型
//     * @see KindEnum
//     */
//    @JSONField(ordinal = 1)
//    private String kind;
//
//    /**
//     * 部署名称
//     */
//    @JSONField(ordinal = 2)
//    private String name;

//    /**
//     * 仓库
//     */
//    @JSONField(ordinal = 3)
//    private Repository repository;

    /**
     * 步骤列表
     */
    @JSONField(ordinal = 4)
    private List<Step> stepList;


//    /**
//     * 环境变量
//     */
//    @JSONField(ordinal = 5)
//    private Map<String, Map<String, String>> environment;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

//    public Repository getRepository() {
//        return repository;
//    }
//
//    public void setRepository(Repository repository) {
//        this.repository = repository;
//    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

//    public Map<String, Map<String, String>> getEnvironment() {
//        return environment;
//    }
//
//    public void setEnvironment(Map<String, Map<String, String>> environment) {
//        this.environment = environment;
//    }

//    public void setKind(String kind) {
//        this.kind = kind;
//    }

//    public String getKind() {
//        return KindEnum.deployment.value();
//    }

    public static class Machine{
        @JSONField(ordinal = 1)
        private String name;
        @JSONField(ordinal = 2)
        private String ip;
        @JSONField(ordinal = 3)
        private String username;
        @JSONField(ordinal = 4)
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Repository{
        /**
         * 是否需要仓库
         */
        @JSONField(ordinal = 1)
        private Boolean need;
        @JSONField(ordinal = 2)
        private String name;
        @JSONField(ordinal = 3)
        private String address;
        @JSONField(ordinal = 4)
        private String branch;
        @JSONField(ordinal = 5)
        private String username;
        @JSONField(ordinal = 6)
        private String password;

        public Boolean getNeed() {
            return need;
        }

        public void setNeed(Boolean need) {
            this.need = need;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }
    }

    public static class Step{
        /**
         * 步骤名称
         */
        @JSONField(ordinal = 1)
        private String name;
        /**
         * 执行器
         */
        @JSONField(ordinal = 2)
        private Plugin executor;
        /**
         * 校验器
         */
        @JSONField(ordinal = 3)
        private Plugin checker;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Plugin getExecutor() {
            return executor;
        }

        public void setExecutor(Plugin executor) {
            this.executor = executor;
        }

        public Plugin getChecker() {
            return checker;
        }

        public void setChecker(Plugin checker) {
            this.checker = checker;
        }
    }

    public static class Plugin{

        @JSONField(ordinal = 1)
        private String id;
        @JSONField(ordinal = 2)
        private Map params;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Map getParams() {
            return params;
        }

        public void setParams(Map params) {
            this.params = params;
        }
    }

}
