package com.enation.pangu.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.pangu.domain.*;
import com.enation.pangu.enums.DeployWay;
import com.enation.pangu.enums.ExecutorEnum;
import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.*;
import com.enation.pangu.model.*;
import com.enation.pangu.service.*;
import com.enation.pangu.task.DeploymentTask;
import com.enation.pangu.task.TaskState;
import com.enation.pangu.utils.PageConvert;
import com.enation.pangu.utils.StringUtil;
import com.enation.pangu.utils.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.enation.pangu.utils.StringUtil.getDateline;

/**
 * 部署业务层实现类
 *
 * @author zhangsong
 * @date 2020-10-31
 */
@Service
public class DeploymentManagerImpl implements DeploymentManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DeploymentMapper deploymentMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DeploymentMachineMapper deploymentMachineMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private StepMapper stepMapper;
    @Autowired
    private RepositoryManager repositoryManager;

    @Autowired
    ExecutorManager executorManager;

    @Autowired
    EnvVarManager envVarManager;

    @Autowired
    DeploymentTask task;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private PluginManager pluginManager;
    @Autowired
    private EnvProjectManager envProjectManager;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private MachineTagMapper machineTagMapper;


    @Override
    public WebPage<Deployment> list(int pageNo, int pageSize, Long groupId) {
        IPage<Deployment> iPage = deploymentMapper.selectPageCustom(new Page<>(pageNo, pageSize),groupId);

        return PageConvert.convert(iPage);
    }

    @Override
    public Deployment selectById(Long id) {
        return deploymentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Deployment deployment) {
        deployment.setAddTime(System.currentTimeMillis());
        Integer count = deploymentMapper.selectCount(new QueryWrapper<Deployment>().eq("name", deployment.getName()));
        if (count!=null && count>0){
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"部署名称已存在");
        }
        deploymentMapper.insert(deployment);

        //如果是按机器部署，插入部署-机器关联表
        if(DeployWay.machine.value().equals(deployment.getWay())){

            List<Long> machineIdList = deployment.getMachineIdList();
            if (machineIdList == null || machineIdList.size() == 0) {
                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"请至少选择一台机器");
            }

            for (Long machineId : machineIdList) {
                DeploymentMachine deploymentMachine = new DeploymentMachine(deployment.getId(), machineId);
                deploymentMachineMapper.insert(deploymentMachine);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Deployment deployment) {
        Long tagId = deployment.getTagId();
        if (tagId == null) {
            deployment.setTagId(0L);
        }
        Integer count = deploymentMapper.selectCount(new QueryWrapper<Deployment>().eq("name", deployment.getName()).ne("id",deployment.getId()));
        if (count!=null && count>0){
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"部署名称已存在");
        }
        deploymentMapper.updateById(deployment);

        //如果是按机器部署，插入部署-机器关联表
        if(DeployWay.machine.value().equals(deployment.getWay())){

            List<Long> machineIdList = deployment.getMachineIdList();
            if (machineIdList == null || machineIdList.size() == 0) {
                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"请至少选择一台机器");
            }

            deploymentMachineMapper.delete(new QueryWrapper<DeploymentMachine>().eq("deployment_id", deployment.getId()));
            if (deployment.getMachineIdList() != null) {
                for (Long machineId : machineIdList) {
                    DeploymentMachine deploymentMachine = new DeploymentMachine(deployment.getId(), machineId);
                    deploymentMachineMapper.insert(deploymentMachine);
                }
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        deploymentMapper.deleteById(id);
        deploymentMachineMapper.delete(new QueryWrapper<DeploymentMachine>().eq("deployment_id", id));
        stepMapper.delete(new QueryWrapper<Step>().eq("deployment_id", id));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStep(Deployment deployment, List<Step> stepList) {
        deploymentMapper.updateById(deployment);

        if (stepList == null) {
            return;
        }
        stepMapper.delete(new QueryWrapper<Step>().eq("deployment_id", deployment.getId()));

        for (Step step : stepList) {
            step.setDeploymentId(deployment.getId());
            step.setAddTime(System.currentTimeMillis());
            stepMapper.insert(step);
        }
    }

    @Override
    public List<Long> selectMachineIdList(Long id) {
        return deploymentMachineMapper.selectMachineIdList(id);
    }

    @Override
    public List<Machine> selectMachineList(Long id) {
        return deploymentMachineMapper.selectMachineList(id);
    }

    @Override
    public List<Step> selectStep(Long deploymentId) {

        List<Step> stepList = this.selectStepList(deploymentId);

        stepList.forEach(step -> {

            try {
                parseStep(step);
            } catch (PluginNotExistException e) {

            }


        });

        return stepList;
    }


    void parseStep(Step step)  throws PluginNotExistException{
        String executor = step.getExecutor();

        PluginConfigVO executorConfigVO = pluginManager.parsePlugin(executor, PluginType.executor, new HashMap()).getConfig();

        if (ExecutorEnum.write_config.executorId().equals(executor)) {
            Map<String, String> params = (Map<String, String>) JSONUtils.parse(step.getExecutorParams());
            step.setWriteConfigParams(params);
        } else if (ExecutorEnum.git_clone.executorId().equals(executor)) {
            Map<String, String> params = (Map<String, String>) JSONUtils.parse(step.getExecutorParams());
            step.setWriteConfigParams(params);

            if (params.get("repository_id") != null) {
                Long repositoryId = Long.valueOf(params.get("repository_id"));
                Repository repository = repositoryManager.selectById(repositoryId);
                if (repository != null) {
                    params.put("repositoryName", repository.getName());
                }
            }

        } else {
            this.fillConfigValue(executorConfigVO, step.getExecutorParams());
            step.setExecutorConfig(executorConfigVO);
        }

        String checker = step.getCheckType();
        if (StringUtil.notEmpty(checker)) {
            PluginConfigVO checkerConfig = pluginManager.parsePlugin(checker, PluginType.checker, new HashMap<>()).getConfig();
            Map<String, String> checkerParams = (Map<String, String>) JSONUtils.parse(step.getCheckerParams());
            checkerConfig.getItemList().forEach(item -> {
                item.setValue(checkerParams.get(item.getName()));
            });
            step.setCheckerConfig(checkerConfig);
        }

    }


    @Override
    public Long exec(Long id) {

        //校验
        this.execPreCheck(id);

        return this.execDeployment(id, -1L);
    }

    @Override
    public Long execDeployment(Long id, Long setTaskId) {

        //定义环境变量
        HashMap env = new HashMap();

        //--------workspace变量------------//
        String workspace = "/opt/workspace";
        env.put("workspace", workspace);

        //查询部署信息
        Deployment deployment = this.selectById(id);

        //--------仓库变量------------//
        //要部署的仓库id
//        Long repositoryId = deployment.getRepositoryId();

//        if (repositoryId != null &&  repositoryId!=-1) {
//            //git仓库环境变量
//            Repository repository = repositoryManager.selectById(repositoryId);
//            if(repository != null){
//                String branch = deployment.getBranch();
//                repository.setBranch(branch);
//                env.put("repository", repository);
//            }
//
//        }


        //--------用户自定义的变量------------//
        Long environmentId = deployment.getEnvironmentId();

        //选择的project 环境变量（如果有的话）
        if (environmentId != null) {
            Map variables = envVarManager.createVariables(environmentId);
            env.putAll(variables);
        }

        //要部署的机器列表
        List<Machine> machineList = this.getMachineList(deployment);

        Task deployTask = new Task();
        deployTask.setDeploymentName(deployment.getName());
        deployTask.setDeploymentId(id);

        deployTask.setMachineId(0L);
        deployTask.setStepName(deployment.getName() + "部署总步骤");
        deployTask.setStartTime(getDateline());
        deployTask.setState(TaskState.RUNNING.name());

        deployTask.setParentId(setTaskId == null ? -1 : setTaskId);
        deployTask.setTaskType(TaskTypeEnum.deployment.value());

        taskMapper.insert(deployTask);

        for (Machine machine : machineList) {
            task.execute(deployTask, deployment, machine, env);
        }
        return deployTask.getId();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    TaskMapper taskMapper;

    @Override
    public List<String> findBranch(Long repositoryId) {
        if (repositoryId == null) {
            return new ArrayList<>();
        }
        Repository repository = repositoryManager.selectById(repositoryId);
        LsRemoteCommand ls = Git.lsRemoteRepository()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.getUsername(), repository.getPassword()))
                .setRemote(repository.getAddress());
        List<String> branchList = null;
        try {
            ls.setHeads(true);
            Collection<Ref> call = ls.call();
            branchList = new ArrayList<>();
            for (Ref ref : call) {
                String[] split = ref.getName().split("/");
                String s = split[split.length - 1];
                branchList.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branchList;
    }


    @Override
    public List<Deployment> findAll() {
        return deploymentMapper.selectList(new QueryWrapper<Deployment>().orderByDesc("add_time"));
    }

    @Override
    public Step addStep(Step step) {

        Step lastStep = new QueryChainWrapper<>(stepMapper)
                .select("sequence")
                .eq("deployment_id", step.getDeploymentId())
                .orderByDesc("sequence")
                .last("limit 1")
                .one();
        if (lastStep == null) {
            step.setSequence(1);
        } else {
            step.setSequence(lastStep.getSequence() + 1);
        }

        step.setDeploymentId(step.getDeploymentId());
        step.setAddTime(System.currentTimeMillis());
        stepMapper.insert(step);
        return step;
    }

    @Override
    public void delStep(Long stepId) {
        stepMapper.deleteById(stepId);
    }

    @Override
    public void fillConfigValue(PluginConfigVO configVO, String executorParams) {
        if (configVO == null) {
            return;
        }

        if (StringUtil.isEmpty(executorParams)) {
            return;
        }

        List<ExecutorConfigItemVO> list = configVO.getItemList();
        Map<String, String> params = (Map<String, String>) JSONUtils.parse(executorParams);
        list.forEach(item -> {
            item.setValue(params.get(item.getName()));
        });
    }

    @Override
    public Step editStepOne(Step step) {
        stepMapper.updateById(step);
        return step;
    }

    @Override
    public void editEnvironment(Long deploymentId, Long environmentId) {
        new UpdateChainWrapper<>(deploymentMapper)
                .set("environment_id", environmentId)
                .eq("id", deploymentId)
                .update();
    }

    @Override
    public void execPreCheck(Long id) {
        QueryWrapper<Step> wrapper = new QueryWrapper<>();
        wrapper.eq("deployment_id", id);
        Integer stepCount = stepMapper.selectCount(wrapper);
        if (stepCount == 0) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"部署中不包含步骤，无法执行");
        }

        Task task = taskManager.selectRunningDeployTask(id);
        if (task != null) {
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"存在正在运行的任务，无法执行");
        }

        List<Machine> machineList = this.getMachineList(selectById(id));
        if(machineList.size() == 0){
            throw new RuntimeException("部署中不包含机器，无法执行");
        }

    }

    @Override
    public List<Step> selectStepList(Long id) {
        return stepMapper.selectList(new QueryWrapper<Step>().eq("deployment_id", id).orderByAsc("sequence"));
    }

    @Override
    public void export(Long id, HttpServletResponse response) {

        Deployment deployment = this.selectById(id);
        //生成yaml
        String yaml = this.loadYaml(deployment);

        //导出
        String filename = deployment.getName() + ".yml";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(yaml.getBytes());
        ExportUtil.export(filename, response, inputStream);
    }

    @Override
    public String loadYaml(Deployment deployment) {
        //封装数据
        DeploymentImport deploymentImport = getDeploymentImport(deployment);

        return YamlUtil.getYaml(deploymentImport);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long importYml(DeploymentImport deploymentImport, Long id) {
//        Integer count = deploymentMapper.selectCount(new QueryWrapper<Deployment>().eq("name", deploymentImport.getName()));
//        if (count!=null && count>0){
//            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"部署名称已存在");
//        }
        //导入仓库
//        Long repositoryId = null;
//        Boolean needRepository = deploymentImport.getRepository().getNeed();
//        if (needRepository != null && needRepository) {
//            Repository repository = new Repository();
//            BeanUtils.copyProperties(deploymentImport.getRepository(), repository);
//            repository.setAuthType("密码认证");
//            repository.setAddTime(StringUtil.getDateline());
//            repositoryMapper.insert(repository);
//            repositoryId = repository.getId();
//        }

        //导入环境变量
//        Long environmentId = envProjectManager.importMap(deploymentImport.getName(), deploymentImport.getEnvironment());

        //导入部署
//        Deployment deployment = new Deployment();
//        deployment.setName(deploymentImport.getName());
//        deployment.setDependRepo("0");
//        deployment.setRepositoryId(-1L);
//        deployment.setBranch("");
//        deployment.setEnvironmentId(-1L);
//        deployment.setAddTime(StringUtil.getDateline());
//        deploymentMapper.insert(deployment);

        //导入步骤
        //查询最后一个步骤，导入时将新步骤追加到最后一个步骤后面
        Step lastStep = new QueryChainWrapper<>(stepMapper).select("sequence").eq("deployment_id", id).orderByDesc("sequence").last("limit 1").one();
        Integer lastSequence = 1;
        if(lastStep != null){
            lastSequence = lastStep.getSequence() + 1;
        }
        List<DeploymentImport.Step> stepList = deploymentImport.getStepList();
        for (int i = 0; i < stepList.size(); i++) {
            DeploymentImport.Step stepImport = stepList.get(i);
            Step step = new Step();
            step.setName(stepImport.getName());
            step.setSequence(i + lastSequence);
            step.setExecutor(stepImport.getExecutor().getId());
            step.setExecutorParams(JSON.toJSONString(stepImport.getExecutor().getParams()));
            step.setCheckType(stepImport.getChecker().getId());
            step.setCheckerParams(JSON.toJSONString(stepImport.getChecker().getParams()));
            step.setDeploymentId(id);
            step.setAddTime(StringUtil.getDateline());
            stepMapper.insert(step);
        }
        return id;
    }


    /**
     * 封装部署导出数据
     *
     * @param deployment
     * @return
     */
    private DeploymentImport getDeploymentImport(Deployment deployment) {
        DeploymentImport deploymentImport = new DeploymentImport();

        //部署名称
//        deploymentImport.setName(deployment.getName());

//        //部署机器
//        List<Machine> machineList = this.selectMachineList(id);
//        List<DeploymentImport.Machine> machineImportList = machineList.stream().map(machine -> {
//            DeploymentImport.Machine machineImport = new DeploymentImport.Machine();
//            BeanUtils.copyProperties(machine, machineImport);
//            return machineImport;
//        }).collect(Collectors.toList());
//        deploymentImport.setMachineList(machineImportList);

        //仓库
//        DeploymentImport.Repository repositoryImport = new DeploymentImport.Repository();
//        if ("1".equals(deployment.getDependRepo())) {
//            if (deployment.getRepositoryId() != null) {
//                Repository repository = repositoryManager.selectById(deployment.getRepositoryId());
//                BeanUtils.copyProperties(repository, repositoryImport);
//                repositoryImport.setBranch(deployment.getBranch());
//            }
//            repositoryImport.setNeed(true);
//        } else {
//            repositoryImport.setNeed(false);
//        }
//        deploymentImport.setRepository(repositoryImport);

        //步骤
        List<Step> stepList = this.selectStepList(deployment.getId());
        List<DeploymentImport.Step> stepImportList = stepList.stream().map(step -> {
            DeploymentImport.Step stepImport = new DeploymentImport.Step();
            BeanUtils.copyProperties(step, stepImport);

            DeploymentImport.Plugin executorPlugin = new DeploymentImport.Plugin();
            executorPlugin.setId(step.getExecutor());
            executorPlugin.setParams((Map) JSONUtils.parse(step.getExecutorParams()));
            stepImport.setExecutor(executorPlugin);

            DeploymentImport.Plugin checkerPlugin = new DeploymentImport.Plugin();
            checkerPlugin.setId(step.getCheckType());
            checkerPlugin.setParams((Map) JSONUtils.parse(step.getCheckerParams()));
            stepImport.setChecker(checkerPlugin);

            return stepImport;
        }).collect(Collectors.toList());
        deploymentImport.setStepList(stepImportList);

//        //环境变量
//        if (deployment.getEnvironmentId() != null) {
//            Map variables = envVarManager.createVariables(deployment.getEnvironmentId());
//            deploymentImport.setEnvironment(variables);
//        }


        return deploymentImport;
    }

    /**
     * 以将排序为3的排序至排序为1,图示如下
     * 排序前                排序后
     * |----0-----|   排序    |----0-----|
     * |----1-----|  ===>    |----3-----|
     * |----2-----|          |----1-----|
     * |----3-----|          |----2-----|
     * 那么更新的逻辑为：
     * 1、先读出排序为3和排序为1的step
     * 2、更新排序>=3的step +1，此时效果为：
     * |----0-----|
     * |----2-----|
     * |----3-----|
     * |----4-----|
     * 3、再把原排序的step（之前排序为3的）更新为目标排序（应该为1），即：
     * |----0-----|
     * <------|
     * |----2-----|     |
     * |----3-----|     |
     * |----4-----|-----|
     *
     * @param sourceSequence 排序
     * @param targetSequence 新的排序
     * @param deploymentId   部署集id
     * @return
     */
    @Override
    public Boolean stepSort(Integer sourceSequence, Integer targetSequence, Long deploymentId) {

        //由sequence排到newSequence
        QueryWrapper wrapper = new QueryWrapper<Step>().eq("deployment_id", deploymentId).last(" limit " + sourceSequence + ",1").orderByAsc("sequence");
        Step sourceStep = stepMapper.selectOne(wrapper);
//        System.out.println(sourceStep);

        wrapper = new QueryWrapper<Step>().eq("deployment_id", deploymentId).last(" limit " + targetSequence + ",1").orderByAsc("sequence");
        Step targetStep = stepMapper.selectOne(wrapper);
//        System.out.println(targetStep);

        stepMapper.increaseSort(targetStep.getSequence(), deploymentId);
        sourceStep.setSequence(targetStep.getSequence());

        stepMapper.updateById(sourceStep);
        return true;
    }

    @Override
    public void updateStepSkipStatus(Long stepId, int isSkip) {
        new UpdateChainWrapper<>(stepMapper).set("is_skip", isSkip).eq("id", stepId).update();
    }

    @Override
    public List<Machine> getMachineList(Deployment deployment) {
        List<Machine> machineList = new ArrayList<>();
        if(DeployWay.machine.value().equals(deployment.getWay())){
            machineList = selectMachineList(deployment.getId());
        }else if(DeployWay.tag.value().equals(deployment.getWay())){
            machineList = machineTagMapper.selelctMachineByTagId(deployment.getTagId());
        }

        return machineList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(Long id) {
        Deployment deployment = this.selectById(id);

        List<Step> steps = this.selectStepList(id);

        //复制部署信息
        Deployment newDeployment = new Deployment();
        newDeployment.setAddTime(System.currentTimeMillis());
        newDeployment.setWay(DeployWay.machine.value());
        newDeployment.setName(deployment.getName() + " 副本");
        deploymentMapper.insert(newDeployment);

        //部署步骤信息
        for(Step step : steps){
            step.setAddTime(StringUtil.getDateline());
            step.setDeploymentId(newDeployment.getId());
            step.setIsSkip(0);
            stepMapper.insert(step);
        }
    }
}
