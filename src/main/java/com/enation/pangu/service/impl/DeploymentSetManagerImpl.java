package com.enation.pangu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.pangu.enums.TaskTypeEnum;
import com.enation.pangu.config.exception.ServiceException;
import com.enation.pangu.mapper.*;
import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.DeploymentSetManager;
import com.enation.pangu.service.TaskManager;
import com.enation.pangu.task.TaskState;
import com.enation.pangu.utils.ExportUtil;
import com.enation.pangu.utils.StringUtil;
import com.enation.pangu.utils.YamlUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.*;

import static com.enation.pangu.utils.StringUtil.getDateline;

/**
 * 部署集服务
 * @author 褚帅
 * 2020/10/31
 */
@Service
public class DeploymentSetManagerImpl implements DeploymentSetManager {

    @Autowired
    private DeploymentSetMapper deploymentSetMapper;

    @Autowired
    private DeploymentSetRelMapper deploymentSetRelMapper;

    @Autowired
    private DeploymentDependMapper deploymentDependMapper;

    @Autowired
    private DeploymentMapper deploymentMapper;

    @Autowired
    private DeploymentManager deploymentManager;

    @Autowired
    private TaskManager taskManager;

    @Override
    public List<DeploymentSet> findList() {

        return deploymentSetMapper.selectList(new QueryWrapper<DeploymentSet>().orderByDesc("add_time"));
    }

    @Override
    public DeploymentSetVO findDetail(Long id) {

        DeploymentSet deploymentSet = deploymentSetMapper.selectById(id);
        if (null == deploymentSet){
            return null;
        }
        List<DeploymentSetRelVO> list = getDeploymentSetRel(id);
        DeploymentSetVO vo = new DeploymentSetVO();
        BeanUtils.copyProperties(deploymentSet,vo);
        vo.setList(list);
        return vo;
    }

    @Override
    public List<DeploymentSetRelVO> getDeploymentSetRel(Long deploymentSetId) {
        List<DeploymentSetRelVO> list = deploymentSetMapper.findByDeploymentSetId(deploymentSetId);
        for (DeploymentSetRelVO item : list) {
            for (DeploymentDependVO dependVO: item.getDeploymentDependList()) {
                Deployment deployment = deploymentMapper.selectById(dependVO.getDependId());
                dependVO.setDependName(deployment.getName());
            }
        }
        return list;
    }

    @Override
    public void saveItem(DeploymentSetRelDTO item) {
        DeploymentSetRel deploymentSetRel = new DeploymentSetRel();
        BeanUtils.copyProperties(item,deploymentSetRel);
        if (item.getDeploymentSetRelId() == null){
            deploymentSetRelMapper.insert(deploymentSetRel);
        }else{
            deploymentSetRel.setId(item.getDeploymentSetRelId());
            deploymentSetRelMapper.updateById(deploymentSetRel);
            deleteDeploymentDependByDeploymentSetRelId(deploymentSetRel.getId());
        }
        addDeploymentDependList(deploymentSetRel.getDeploymentSetId(),deploymentSetRel.getId(),item.getDeploymentDependIds());
    }

    private void addDeploymentDependList(Long deploymentSetId,Long deploymentSetRelId,String deploymentDependIds){
        if (!StringUtil.isEmpty(deploymentDependIds)){
            String[] split = deploymentDependIds.split(",");
            DeploymentDepend deploymentDepend = null;
            for (String dependId : split) {
                deploymentDepend = new DeploymentDepend();
                deploymentDepend.setDeploymentSetRelId(deploymentSetRelId);
                deploymentDepend.setDependId(Long.valueOf(dependId));
                deploymentDepend.setDeploymentSetId(deploymentSetId);
                deploymentDependMapper.insert(deploymentDepend);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(Long id) {
        deploymentSetRelMapper.deleteById(id);
        deleteDeploymentDependByDeploymentSetRelId(id);
    }


    private void deleteDeploymentDependByDeploymentSetRelId(Long id){
        QueryWrapper<DeploymentDepend> wapper = new QueryWrapper<>();
        wapper.eq("deployment_set_rel_id",id);
        deploymentDependMapper.delete(wapper);
    }

    @Override
    public DeploymentSet add(DeploymentSet deploymentSet) {
        deploymentSetMapper.insert(deploymentSet);
        return deploymentSet;
    }

    @Override
    public DeploymentSet getById(Long id) {
        return deploymentSetMapper.selectById(id);
    }

    @Override
    public void updateName(Long id, String name) {
        deploymentSetMapper.update(new DeploymentSet(),new UpdateWrapper<DeploymentSet>().set("name",name).eq("id",id));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void delete(Long id) {
        deploymentSetMapper.deleteById(id);
        deploymentSetRelMapper.delete(new QueryWrapper<DeploymentSetRel>().eq("deployment_set_id",id));
        deploymentDependMapper.delete(new QueryWrapper<DeploymentDepend>().eq("deployment_set_id",id));
    }

    @Override
    public DeploymentSetRelDTO getDeploymentSetRelBySetId(Long id) {
        DeploymentSetRel deploymentSetRel = deploymentSetRelMapper.selectById(id);
        DeploymentSetRelDTO deploymentSetRelDTO = new DeploymentSetRelDTO();
        deploymentSetRelDTO.setDeploymentSetRelId(id);
        deploymentSetRelDTO.setDeploymentSetId(deploymentSetRel.getDeploymentSetId());
        deploymentSetRelDTO.setDeploymentId(deploymentSetRel.getDeploymentId());
        List<DeploymentDepend> deploymentDependList = deploymentDependMapper.selectList(new QueryWrapper<DeploymentDepend>().eq("deployment_set_rel_id", id));
        StringBuffer dependIds = new StringBuffer();
        for (int i = 0; i < deploymentDependList.size(); i++) {
            DeploymentDepend depend = deploymentDependList.get(i);
            dependIds.append(depend.getDependId());
            if (deploymentDependList.size()-1 !=i){
                dependIds.append(",");
            }
        }
        deploymentSetRelDTO.setDeploymentDependIds(dependIds.toString());
        return deploymentSetRelDTO;
    }

    @Override
    public List<Deployment> selectDeploymentList(Long id) {
        return deploymentSetRelMapper.selectDeploymentList(id);
    }

    @Override
    public List<Deployment> selectDeploymentListNoDepend(Long id) {
        return deploymentSetRelMapper.selectDeploymentListNoDepend(id);
    }

    @Override
    public Long exec(Long id) {

        List<Deployment> noDependList = this.selectDeploymentListNoDepend(id);
        if(noDependList.size() == 0){
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"所有部署都存在依赖，无法执行");
        }

        Task runningSetTask = taskManager.selectRunningSetTask(id);
        if(runningSetTask != null){
            throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"存在正在运行的任务，无法执行");
        }

        //校验
        List<Deployment> deploymentList = this.selectDeploymentList(id);
        for(Deployment deployment : deploymentList){
            deploymentManager.execPreCheck(deployment.getId());
        }

        //新增部署集任务
        Task setTask = new Task();
        setTask.setStartTime(StringUtil.getDateline());
        setTask.setState(TaskState.RUNNING.name());
        setTask.setDeploymentSetId(id);
        setTask.setTaskType(TaskTypeEnum.deploymentSet.value());

        taskManager.insert(setTask);

        //执行所有不需要依赖的部署
        for(Deployment deployment : noDependList){
            deploymentManager.execDeployment(deployment.getId(), setTask.getId());
        }

        return setTask.getId();
    }


    @Override
    public void execDependDeployment(Task successDeployTask) {

        Long setTaskId = successDeployTask.getParentId();
        Long successDeploymentId = successDeployTask.getDeploymentId();

        Task setTask = taskManager.selectById(setTaskId);
        Long deploymentSetId = setTask.getDeploymentSetId();

        //查询该部署集的所有依赖关系
        List<DeploymentDepend> deploymentDependList = deploymentDependMapper.selectDependList(deploymentSetId);

        Map<Long, List<Long>> dependMap = getDependMap(deploymentDependList);

        for(Long deploymentId : dependMap.keySet()){

            Task task = taskManager.selectDeployTask(setTaskId, deploymentId);
            //任务不为空，说明已经执行过该部署
            if(task != null){
                continue;
            }

            List<Long> dependIdList = dependMap.get(deploymentId);
            if(!dependIdList.contains(successDeploymentId)){
                continue;
            }
            dependIdList.remove(successDeploymentId);

            //该部署的依赖全部执行成功，才能执行该部署
            boolean allDependSuccess = true;
            for(Long dependId : dependIdList){
                Task dependTask = taskManager.selectDeployTask(setTaskId, dependId);
                if(dependTask == null || !taskManager.taskIsSuccess(dependTask.getId())){
                    allDependSuccess = false;
                    break;
                }
            }

            if(allDependSuccess){
                deploymentManager.execDeployment(deploymentId, setTaskId);
            }

        }
    }

    @Override
    public Integer selectDeploymentCount(Long deploymentSetId) {
        QueryWrapper<DeploymentSetRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deployment_set_id", deploymentSetId);
        return deploymentSetRelMapper.selectCount(queryWrapper);
    }

    @Override
    public void export(Long id, HttpServletResponse response) {
        DeploymentSet deploymentSet = this.getById(id);

        //部署集yaml
        DeploymentSetImport deploymentSetImport = this.getDeploymentSetImport(deploymentSet.getId(), deploymentSet.getName());
        String setYaml = YamlUtil.getYaml(deploymentSetImport);
        setYaml = "---\n" + setYaml;

        //部署yaml
        List<Deployment> deploymentList = this.selectDeploymentList(id);
        String separator = "\n---\n";
        for(Deployment deployment : deploymentList){
            String yaml = deploymentManager.loadYaml(deployment);
            setYaml += separator + yaml;
        }

        //导出
        String filename = deploymentSet.getName() + ".yml";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(setYaml.getBytes());
        ExportUtil.export(filename, response, inputStream);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importYml(DeploymentSetImport setImport, List<DeploymentImport> list) {
        //导入部署集
        DeploymentSet deploymentSet = new DeploymentSet();
        deploymentSet.setName(setImport.getName());
        deploymentSet.setAddTime(StringUtil.getDateline());
        deploymentSetMapper.insert(deploymentSet);

        List<String> checkNameList = new ArrayList<>();
        Map<String,Long> nameMap = new HashMap<>();
        Map<String,Long> nameMapRel = new HashMap<>();
        for(DeploymentImport deploymentImport : list){
//            //校验部署名称重复
//            String deploymentName = deploymentImport.getName();
//            if(checkNameList.contains(deploymentName)){
//                throw new ServiceException(ResultCode.BUSINESS_ERROR.getCode(),"部署名称："+deploymentName+" 已存在");
//            }
//            checkNameList.add(deploymentName);
//
//            //导入部署
//            Long deploymentId = deploymentManager.importYml(deploymentImport);
//
//            //导入部署集和部署关联关系
//            DeploymentSetRel deploymentSetRel = new DeploymentSetRel();
//            deploymentSetRel.setDeploymentId(deploymentId);
//            deploymentSetRel.setDeploymentSetId(deploymentSet.getId());
//            deploymentSetRelMapper.insert(deploymentSetRel);
//
//            nameMap.put(deploymentImport.getName(), deploymentId);
//            nameMapRel.put(deploymentImport.getName(), deploymentSetRel.getId());
        }

        //导入部署依赖
        Map<String, List<String>> depend = setImport.getDepend();
        depend.forEach((deploymentName, dependNameList) -> {
            Long deploymentSetRelId = nameMapRel.get(deploymentName);
            if(deploymentSetRelId == null){
                return;
            }

            for(String dependName : dependNameList){
                Long dependId = nameMap.get(dependName);
                if(dependId != null){
                    DeploymentDepend deploymentDepend = new DeploymentDepend();
                    deploymentDepend.setDeploymentSetId(deploymentSet.getId());
                    deploymentDepend.setDeploymentSetRelId(deploymentSetRelId);
                    deploymentDepend.setDependId(dependId);
                    deploymentDependMapper.insert(deploymentDepend);
                }
            }
        });

    }

    @Override
    public void identifyDependDeploymentFail(Task errorDeployTask) {
        Long setTaskId = errorDeployTask.getParentId();
        Long errorDeploymentId = errorDeployTask.getDeploymentId();

        Task setTask = taskManager.selectById(setTaskId);
        Long deploymentSetId = setTask.getDeploymentSetId();

        //查询该部署集的所有依赖关系
        List<DeploymentDepend> deploymentDependList = deploymentDependMapper.selectDependList(deploymentSetId);

        //为依赖该部署的所有部署创建一个失败状态的部署任务
        updateDependDeployFail(errorDeploymentId, deploymentDependList, setTaskId);

        taskManager.updateParentTaskState(setTaskId, TaskTypeEnum.deploymentSet);
    }

    /**
     * 为依赖该失败的部署的所有部署创建一个失败状态的部署任务
     * @param errorDeploymentId 失败的部署
     * @param deploymentDependList 部署依赖关系
     */
    private void updateDependDeployFail(Long errorDeploymentId, List<DeploymentDepend> deploymentDependList, Long setTaskId) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
        //获取所有依赖该失败部署的部署
        for(DeploymentDepend deploymentDepend : deploymentDependList){
            //需要执行的部署id
            Long deploymentId = deploymentDepend.getDeploymentId();
            //所依赖的部署id
            Long dependId = deploymentDepend.getDependId();

            //如果该部署依赖刚刚执行失败的部署，则创建一个失败状态的部署任务
            if(errorDeploymentId.equals(dependId)){
                Task deployTask = new Task();
                deployTask.setDeploymentName(deploymentDepend.getDeploymentName());
                deployTask.setDeploymentId(deploymentId);

                deployTask.setMachineId(0L);
                deployTask.setStepName(deploymentDepend.getDeploymentName() + "部署总步骤");
                deployTask.setStartTime(getDateline());
                deployTask.setEndTime(getDateline());
                deployTask.setState(TaskState.ERROR.name());

                deployTask.setParentId(setTaskId);
                deployTask.setTaskType(TaskTypeEnum.deployment.value());

                taskManager.insert(deployTask);

                updateDependDeployFail(deploymentId, deploymentDependList, setTaskId);
            }
        }
    }

    /**
     * 封装部署集导出数据
     * @param id 部署集id
     * @param name 部署集名称
     * @return
     */
    private DeploymentSetImport getDeploymentSetImport(Long id, String name) {
        DeploymentSetImport deploymentSetImport = new DeploymentSetImport();

        //部署集名称
        deploymentSetImport.setName(name);

        //部署集依赖
        Map<String,List<String>> dependMap = new LinkedHashMap<>();
        List<DeploymentDepend> deploymentDepends = this.deploymentDependMapper.selectDependList(id);
        for(DeploymentDepend deploymentDepend : deploymentDepends){
            //部署名称
            String deploymentName = deploymentDepend.getDeploymentName();
            //依赖的部署名称
            String dependName = deploymentDepend.getDependName();

            List<String> dependList = dependMap.get(deploymentName);
            if(dependList == null){
                dependList = new ArrayList<>();
                dependMap.put(deploymentName, dependList);
            }
            dependList.add(dependName);
        }
        deploymentSetImport.setDepend(dependMap);


        return deploymentSetImport;
    }

    /**
     * 部署依赖
     * @param deploymentDependList
     * @return key：部署id value：所依赖的部署集合
     */
    private Map<Long, List<Long>> getDependMap(List<DeploymentDepend> deploymentDependList) {
        Map<Long, List<Long>> map = new HashMap<>();
        for(DeploymentDepend deploymentDepend : deploymentDependList){
            //需要执行的部署id
            Long deploymentId = deploymentDepend.getDeploymentId();
            //所依赖的部署id
            Long dependId = deploymentDepend.getDependId();

            List<Long> dependIdList = map.get(deploymentId);
            if(dependIdList == null){
                dependIdList = new ArrayList<>();
                map.put(deploymentId, dependIdList);
            }
            dependIdList.add(dependId);
        }
        return map;
    }

}
