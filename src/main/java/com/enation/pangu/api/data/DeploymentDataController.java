package com.enation.pangu.api.data;

import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentManager;
import com.enation.pangu.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部署控制器
 * @author zhangsong
 * @date 2020-10-31
 */
@RestController
@RequestMapping("/data/deployment")
public class DeploymentDataController {

    @Autowired
    private DeploymentManager deploymentManager;

    @Autowired
    private TaskManager taskManager;

    /**
     * 查询部署列表
     */
    @GetMapping
    public WebPage<Deployment> list(int pageNo, int pageSize,Long groupId ) {

        return deploymentManager.list(pageNo, pageSize,groupId);
    }

    /**
     * 查询一个部署
     */
    @GetMapping("/{id}")
    public Deployment selectById(@PathVariable Long id) {

        return deploymentManager.selectById(id);
    }

    /**
     * 添加部署
     */
    @PostMapping
    public void add(Deployment deployment) {

        deploymentManager.add(deployment);
    }

    /**
     * 编辑部署
     */
    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, Deployment deployment) {
        deployment.setId(id);
        deploymentManager.edit(deployment);
    }

    /**
     * 删除部署
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        deploymentManager.delete(id);
    }


    /**
     * 执行部署
     */
    @GetMapping("/{id}/exec")
    public Long exec(@PathVariable Long id) {
       return deploymentManager.exec(id);
    }

    /**
     * 查询部署列表(不分页)
     */
    @GetMapping("/findAll")
    public List<Deployment> findAll() {
        return deploymentManager.findAll();
    }

    /**
     * 查询分支
     */
    @GetMapping("/findBranch")
    public List<String> findBranch(Long repositoryId) {

        return deploymentManager.findBranch(repositoryId);
    }

    /**
     * 修改部署的环境变量
     */
    @PutMapping("{id}/environment")
    public void editEnvironment(@PathVariable("id") Long deploymentId, Long environmentId) {

        deploymentManager.editEnvironment(deploymentId, environmentId);
    }

    /**
     * 查询正在运行的任务
     */
    @GetMapping("{id}/runningTask")
    public Task runningTask(@PathVariable("id") Long deploymentId) {

        return taskManager.selectRunningDeployTask(deploymentId);
    }


    /**
     * 导出为yml
     */
    @GetMapping("{id}/export")
    public void export(@PathVariable("id") Long id, HttpServletResponse response){

        deploymentManager.export(id, response);
    }

    /**
     * 导入步骤
     */
    @PostMapping("/{id}/import")
    public void importYml(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws Exception{
        DeploymentImport deploymentImport = new Yaml().loadAs(file.getInputStream(), DeploymentImport.class);

        deploymentManager.importYml(deploymentImport, id);
    }

    /**
     * 步骤排序
     *
     * @param id          部署集id
     * @param sequence    排序
     * @param newSequence 新的排序
     */
    @PutMapping("/{id}/sort")
    public void stepSort(@PathVariable Long id, @RequestParam Integer sequence, @RequestParam("new_sequence") Integer newSequence) {
        deploymentManager.stepSort(sequence, newSequence, id);
    }


    /**
     * 复制部署
     *
     * @param id  部署id
     */
    @PostMapping("/{id}/copy")
    public void copy(@PathVariable Long id) {
        deploymentManager.copy(id);
    }

}
