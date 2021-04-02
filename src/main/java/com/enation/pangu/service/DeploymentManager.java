package com.enation.pangu.service;

import com.enation.pangu.domain.PluginConfigVO;
import com.enation.pangu.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部署业务层接口
 *
 * @author zhangsong
 * @date 2020-10-31
 */
public interface DeploymentManager {

    /**
     * 查询部署列表
     *
     * @return
     */
    WebPage<Deployment> list(int pageNo, int pageSize, Long groupId);

    /**
     * 查询一个部署
     *
     * @param id 部署id
     * @return
     */
    Deployment selectById(Long id);

    /**
     * 新增部署
     *
     * @param deployment
     */
    void add(Deployment deployment);

    /**
     * 修改部署
     *
     * @param deployment
     */
    void edit(Deployment deployment);

    /**
     * 删除一个部署
     *
     * @param id
     */
    void delete(Long id);


    /**
     * 编辑步骤
     *
     * @param deployment 部署
     * @param stepList   步骤集合
     */
    void editStep(Deployment deployment, List<Step> stepList);

    /**
     * 查询某个部署的机器id列表
     *
     * @param id 部署id
     * @return
     */
    List<Long> selectMachineIdList(Long id);

    /**
     * 查询某个部署的机器列表
     *
     * @param id 部署id
     * @return
     */
    List<Machine> selectMachineList(Long id);

    /**
     * 查询某个部署的步骤列表
     *
     * @param id 部署id
     * @return
     */
    List<Step> selectStep(Long id);


    /**
     * 查询部署列表(不分页)
     *
     * @return
     */
    List<Deployment> findAll();


    /**
     * 执行一个部署
     *
     * @param id 部署id
     * @return parent task id
     */
    Long exec(Long id);

    /**
     * 执行一个部署（不进行不校验）
     *
     * @param id        部署id
     * @param setTaskId 部署集任务id
     * @return parent task id
     */
    Long execDeployment(Long id, Long setTaskId);

    /**
     * 查询分支
     *
     * @param repositoryId
     * @return
     */
    List<String> findBranch(Long repositoryId);

    /**
     * 新增一个步骤
     *
     * @param step
     */
    Step addStep(Step step);

    /**
     * 删除一个步骤
     *
     * @param stepId
     */
    void delStep(Long stepId);

    /**
     * 为config填充某个步骤的具体值
     *
     * @param configVO
     * @param executorParams
     */
    void fillConfigValue(PluginConfigVO configVO, String executorParams);

    /**
     * 编辑一个步骤
     *
     * @param step
     * @return
     */
    Step editStepOne(Step step);

    /**
     * 修改部署的环境变量
     *
     * @param deploymentId
     * @param environmentId
     */
    void editEnvironment(Long deploymentId, Long environmentId);

    /**
     * 部署前校验
     *
     * @param id
     */
    void execPreCheck(Long id);

    /**
     * 导出为yaml文件
     *
     * @param id
     * @throws Exception
     */
    void export(Long id, HttpServletResponse response);

    /**
     * 步骤列表
     *
     * @param id
     * @return
     */
    List<Step> selectStepList(Long id);

    /**
     * 部署转为yaml字符串
     *
     * @param deployment
     * @return
     */
    String loadYaml(Deployment deployment);

    /**
     * 导入部署
     *
     * @param deploymentImport 导入实体
     * @param id 部署id
     * @return 部署id
     */
    Long importYml(DeploymentImport deploymentImport, Long id);


    /**
     * 部署步骤排序
     *
     * @param sequence     排序
     * @param newSequence  新的排序
     * @param deploymentId 部署集id
     * @return
     */
    Boolean stepSort(Integer sequence, Integer newSequence, Long deploymentId);

    /**
     * 设置步骤是否跳过
     * @param stepId 步骤id
     * @param isSkip 0不跳过 1跳过
     */
    void updateStepSkipStatus(Long stepId, int isSkip);

    /**
     * 查询某个部署的机器列表
     * @param deployment 部署实体
     * @return
     */
    List<Machine> getMachineList(Deployment deployment);

    /**
     * 复制一个部署
     * @param id 部署id
     */
    void copy(Long id);
}
