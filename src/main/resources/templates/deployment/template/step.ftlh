<#import "config_item.ftlh" as pg>
<div class="form-group ">
    <label>步骤名称：</label>
    <input class="form-control" name="name" placeholder="请输入步骤名称"  value="<#if (step)?? && (step.name)??>${step.name!''}</#if>" >
</div>

<div class="form-group ">
    <label>执行器：</label>
    <select class="form-control " name="executor">
        <option value="">选择执行器</option>
        <#list executorList as executor>
            <option value="${executor.pluginId}" desc="${executor.desc!''}"
                    <#if (step.executor)?? && step.executor == executor.pluginId>selected</#if>>${executor.name}</option>
        </#list>
    </select>
    <span class="invalid-feedback" style="display:inline;color: #6B6B6B"></span>
</div>
<#-- 执行器的配置项-->
<#if (step)?? && (step.executorConfig.itemList)??>
    <div class="form-group executor_config">
        <#list step.executorConfig.itemList as item>
            <@pg.configItems item=item pluginType='executor' />
        </#list>
    </div>
</#if>

<#-- 写入配置文件执行器的配置项-->
<#if (step)?? && step.executor == 'write_config' && (step.writeConfigParams)??>
    <div class="form-group executor_config">
        <label>源:</label>
        <div class="config-project">
            <input type="hidden" name="config_executor_project" value="${step.writeConfigParams.project!''}"/>
            <input type="hidden" name="config_executor_file" value="${step.writeConfigParams.file!''}"/>
            <span>${step.writeConfigParams.project!''}</span>&gt;<span>${step.writeConfigParams.file!''}</span><a href="javascript:;"><i class="iconfont icon-edit"></i></a>
        </div>
    </div>

    <div class="form-group executor_config">
        <label>目标:</label>
        <div class="config-target">
            <input name="config_executor_target" class="form-control " value="${step.writeConfigParams.target!''}"  required data-msg="请填写目标路径" placeholder="请填写目标路径"></input>
        </div>
    </div>
</#if>

<#-- git clone执行器的配置项-->
<#if (step)?? && step.executor == 'git_clone' && (step.writeConfigParams)??>
    <div class="form-group executor_config">
        <label>仓库:</label>
        <div class="config-git-clone">
            <input type="hidden" name="config_executor_repository_id" value="${step.writeConfigParams.repository_id!''}"/>
            <input type="hidden" name="config_executor_branch" value="${step.writeConfigParams.branch!''}"/>
            <span>${step.writeConfigParams.repositoryName!''}</span> / <span>${step.writeConfigParams.branch!''}</span><a href="javascript:;"><i class="iconfont icon-edit"></i></a>
        </div>
    </div>

    <div class="form-group executor_config">
        <label>目标路径:</label>
        <div class="config-target">
            <input name="config_executor_clone_target" class="form-control " value="${step.writeConfigParams.clone_target!''}"  required data-msg="请填写目标路径" placeholder="请填写目标路径"></input>
        </div>
    </div>
</#if>

<div class="form-group ">
    <label>校验器：</label>
    <select class="form-control" name="checkType">
        <option value="">无</option>
        <#list checkerList as checker>
            <option value="${checker.pluginId}"
                    <#if (step.checkType)?? && step.checkType == checker.pluginId>selected</#if>>${checker.name}</option>
        </#list>
    </select>
</div>
<#-- 校验器的配置项-->
<#if (step)?? && (step.checkerConfig.itemList)??>
    <div class="form-group  checker_config">
        <#list step.checkerConfig.itemList as item>
            <@pg.configItems item=item pluginType='checker' />
        </#list>
    </div>
</#if>
