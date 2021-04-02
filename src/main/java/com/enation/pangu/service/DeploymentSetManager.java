package com.enation.pangu.service;

import com.enation.pangu.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DeploymentSetManager {

    /**
     * 查询部署集列表
     * @return
     */
    List<DeploymentSet> findList();

    /**
     * 部署集详情
     * @param id
     * @return
     */
    DeploymentSetVO findDetail(Long id);

    /**
     * 根据部署集id查询部署元素详情列表
     * @param deploymentSetId
     * @return
     */
    List<DeploymentSetRelVO> getDeploymentSetRel(Long deploymentSetId);

    void saveItem(DeploymentSetRelDTO item);

    void deleteItem(Long id);

    DeploymentSet add(DeploymentSet deploymentSet);

    /**
     * 根据id查询部署集
     * @param id
     * @return
     */
    DeploymentSet getById(Long id);

    /**
     * 更新部署集名称
     * @param id 部署集id
     * @param name 部署集名称
     */
    void updateName(Long id, String name);

    /**
     * 删除部署集
     * @param id
     */
    void delete(Long id);


    /**
     * 部署集成员详情
     * @param id
     * @return
     */
    DeploymentSetRelDTO getDeploymentSetRelBySetId(Long id);

    /**
     * 查询某个部署集的所有部署
     * @param id
     * @return
     */
    List<Deployment> selectDeploymentList(Long id);

    /**
     * 查询某个部署集的所有没有依赖的部署
     * @param id
     * @return
     */
    List<Deployment> selectDeploymentListNoDepend(Long id);

    /**
     * 执行部署集
     * @param id
     */
    Long exec(Long id);

    /**
     * 执行依赖该部署的所有部署
     * @param successDeployTask 刚刚执行成功的部署任务
     */
    void execDependDeployment(Task successDeployTask);

    /**
     * 查询部署数量
     * @param deploymentSetId
     * @return
     */
    Integer selectDeploymentCount(Long deploymentSetId);

    void export(Long id, HttpServletResponse response);

    void importYml(DeploymentSetImport setImport, List<DeploymentImport> list);

    /**
     * 将依赖该部署的其他部署任务标识为失败
     * @param errorDeployTask 刚刚执行失败的部署任务
     */
    void identifyDependDeploymentFail(Task errorDeployTask);
}
