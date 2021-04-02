<#macro configItems item pluginType>
    <#if (item.type)??>
        <label>${(item.title)!''}：</label>
        <#if item.type == 'input'>
            <input class="form-control" style="${(item.htmlcss)!''}" name="config_${pluginType}_${(item.name)!''}"
                   value="${(item.value)!''}" placeholder="请输入${(item.title)!''}">
        </#if>
        <#if item.type == 'textarea'>
            <textarea class="form-control" style="${(item.htmlcss)!''}" name="config_${pluginType}_${(item.name)!''}"
                      placeholder="请输入${(item.title)!''}">${(item.value)!''}</textarea>
        </#if>
    </#if>
</#macro>
