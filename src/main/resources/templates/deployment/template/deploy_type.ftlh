<div class="card card-primary card-outline card-outline-tabs">
    <div class="card-header p-0 pt-1">
        <ul class="nav nav-tabs" id="deploy-way-tab" role="tablist">
            <li class="nav-item">
                <a class="nav-link <#if !(deployment.way)?? || deployment.way =='machine'>active</#if>" id="custom-tabs-machine-tab" deploy-way="machine" data-toggle="pill" href="#custom-tabs-machine" role="tab" aria-controls="custom-tabs-machine" aria-selected="true">
                    按服务器部署
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if (deployment.way)?? && deployment.way =='tag'>active</#if>" id="custom-tabs-tag-tab" data-toggle="pill" deploy-way="tag" href="#custom-tabs-tag" role="tab" aria-controls="custom-tabs-one-profile" aria-selected="false">
                    按标签部署
                </a>
            </li>
        </ul>
        <input type="hidden" name="way" value="${(deployment.way)!''}"/>

    </div>
    <div class="card-body">
        <div class="tab-content" id="custom-tabs-one-tabContent">
            <!--  部署服务器 -->
            <div class="tab-pane fade show <#if !(deployment.way)?? || deployment.way =='machine'>active</#if>" id="custom-tabs-machine" role="tabpanel">
                <i class="icon iconfont icon-tip"></i>按住shift可以多选
                <select multiple class="form-control" name="machineIdList" style="height: 200px">
                    <#list machineList as machine>
                        <option value="${machine.id}"
                                <#if deployment?? && deployment.machineIdList?seq_contains(machine.id)>selected</#if> >${machine.name}</option>
                    </#list>
                </select>
            </div>
            <!--  按照标签部署服务器 -->
            <div class="tab-pane fade show <#if (deployment.way)?? && deployment.way =='tag'>active</#if>" id="custom-tabs-tag" role="tabpanel">
                <select class="form-control" name="tagId">
                    <#list tagList as tag>
                        <option
                                value="${tag.id}"
                                <#if deployment?? && deployment.tagId?? && deployment.tagId ==tag.id>selected</#if>>${tag.name}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
</div>
