package com.enation.pangu.api.data;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.enation.pangu.enums.KindEnum;
import com.enation.pangu.model.*;
import com.enation.pangu.service.DeploymentSetManager;
import com.enation.pangu.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 *
 * 部署集Data控制器
 * @author 褚帅
 * 邮箱：chushuai0429@qq.com
 * 2020/10/31
 *
 */
@RestController
@RequestMapping("/data/deploymentSet")
public class DeploymentSetDataController {

    @Autowired
    private DeploymentSetManager deploymentSetManager;

    @Autowired
    private TaskManager taskManager;



    /**
     * 更新部署集名称
     * @return
     */
    @PostMapping("/updateName/{id}")
    public void updateName(@PathVariable Long id,String name){
        deploymentSetManager.updateName(id,name);
    }

    /**
     * 删除部署集
     * @return
     */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        deploymentSetManager.delete(id);
    }

    /**
     * 部署集中新增部署
     * @return
     */
    @PostMapping("/saveItem")
    public void saveItem(DeploymentSetRelDTO item){
        deploymentSetManager.saveItem(item);
    }

    /**
     * 删除部署集中的单个部署
     * @return
     */
    @DeleteMapping("/deleteItem/{id}")
    public void deleteItem(@PathVariable Long id){
        deploymentSetManager.deleteItem(id);
    }

    /**
     * 查询部署集下的部署元素详情列表
     */
    @GetMapping("/findSets/{id}")
    @ResponseBody
    public List<DeploymentSetRelVO> findSets(@PathVariable Long id) {
        return deploymentSetManager.getDeploymentSetRel(id);
    }

    /**
     * 查询部署集中的指定部署详情
     */
    @GetMapping("/findDeploymentSetRelBySetId/{id}")
    public DeploymentSetRelDTO findDeploymentSetRelBySetId(@PathVariable Long id) {
        return deploymentSetManager.getDeploymentSetRelBySetId(id);
    }


    /**
     * 执行部署集
     */
    @GetMapping("/{id}/exec")
    public Long execDeploymentSet(@PathVariable Long id) {
        return deploymentSetManager.exec(id);
    }

    /**
     * 查询某个部署集正在运行的任务
     */
    @GetMapping("/{id}/runningTask")
    public Task runningTask(@PathVariable(value = "id") Long deploymentSetId) {
        return taskManager.selectRunningSetTask(deploymentSetId);
    }

    /**
     * 导出为yml
     */
    @GetMapping("{id}/export")
    public void export(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {

        deploymentSetManager.export(id, response);
    }

    /**
     * 导出为yml
     */
    @PostMapping("/import")
    public void importYml(@RequestParam("file") MultipartFile file) throws Exception{
        Iterable kindList = new Yaml().loadAll(file.getInputStream());

        List<DeploymentImport> list = new ArrayList();
        final AtomicReference<DeploymentSetImport> setImport = new AtomicReference();
        kindList.forEach(object -> {
            Map<String,Object> itemMap = (Map) object;
            //资源类型
            String kind = (String) itemMap.get("kind");

            if(KindEnum.deploymentSet.value().equals(kind)){
                DeploymentSetImport deploymentSetImport = JSON.parseObject(JSON.toJSONString(itemMap), DeploymentSetImport.class, Feature.OrderedField);
                setImport.set(deploymentSetImport);
            }else if(KindEnum.deployment.value().equals(kind)){
                DeploymentImport deploymentImport = JSON.parseObject(JSON.toJSONString(itemMap), DeploymentImport.class, Feature.OrderedField);
                list.add(deploymentImport);
            }
        });

        deploymentSetManager.importYml(setImport.get(), list);
    }
}
