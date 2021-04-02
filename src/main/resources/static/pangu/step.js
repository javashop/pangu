/**
 * 步骤数据管理
 * 主要职责为：请求api完成数据的变动
 * @type {{addStep: Step.addStep, updateStep: Step.updateStep, deleteStep: Step.deleteStep}}
 */
var Step = {

    /**
     * 添加步骤
     * @param stepData 步骤数据
     */
    addStep: function (stepData) {
        $.ajax({
            headers: {
                'Content-Type': 'application/json'
            },
            url: context_path+'/data/step?deploymentId='+stepData.deploymentId ,
            type: 'POST',
            data: JSON.stringify(stepData),
            success: function (step) {
                Message.success('保存成功');
                //关闭窗口
                $('#addStepModal').modal('hide');
                $(".modal-backdrop").remove()

                //dom中新增刚才添加的步骤
                StepUI.addStepDom(step);

            },
            error: function (xhr, textStatus) {
                Message.error('保存失败');
            }
        });
    },
    /**
     * 删除步骤
     * @param stepId 步骤id
     */
    deleteStep: function (stepId) {
        let self = this;
        $.ajax({
            url: context_path + '/data/step/' + stepId,
            type: 'DELETE',
            success: function (res) {
                Message.success('删除成功');

                //移除步骤
                $(".step[step-id="+stepId+"]").remove();

                //刷新步骤列表
                StepUI.stepRefresh();

            },
            error: function (xhr, textStatus) {
                Message.error('删除失败');
            }
        });
    },

    /**
     * 更新步骤
     * @param stepId 步骤id
     * @param stepData 步骤数据
     */
    updateStep: function (stepId,stepData) {
        $.ajax({
            headers: {
                'Content-Type': 'application/json'
            },
            url: context_path + '/data/step/' + stepId,
            type: 'PUT',
            data: JSON.stringify(stepData),
            success: function (step) {
                Message.success('保存成功');
                StepUI.stepRefresh();
            },
            error: function (xhr, textStatus) {
                Message.error('保存失败');
            }
        });
    },
    /**
     * 更新步骤跳过状态
     * @param stepId 步骤id
     * @param isSkip 是否跳过
     */
    updateStepSkipStatus: function (stepId,isSkip) {
        $.ajax({
            url: context_path + '/data/step/' + stepId + '/skip-status',
            type: 'PUT',
            data: {isSkip: isSkip},
            success: function () {

            },
            error: function (xhr, textStatus) {
                Message.error('修改失败');
            }
        });
    }
}

/**
 *             if (executorId == "write_config") {
                $configDiv.html('');
                $configDiv.append($("#executorConfigItemTemplateModal").html())
                var $configItemDiv1 = $configDiv.children(".config-item").last();
                $configItemDiv1.find('.config-item-title').html("项目：")
                $configItemDiv1.find('.config-item-input').html('<select class="form-control" name="config_executor_projectId" placeholder="请选择项目"></select>')

                $configDiv.append($("#executorConfigItemTemplateModal").html())
                var $configItemDiv2 = $configDiv.children(".config-item").last();
                $configItemDiv2.find('.config-item-title').html("配置文件：")
                $configItemDiv2.find('.config-item-input').html('<select class="form-control" name="config_executor_fileId" placeholder="请选择配置文件"></select>')

                $.ajax({
                    url: '${context_path}/data/config_project',
                    type: 'GET',
                    success: function (res) {
                        // $configItemDiv1.find("[name=config_executor_projectId]").append("<option value='-1'>--请选择--</option>")
                        $(res).each(function (index) {
                            $configItemDiv1.find("[name=config_executor_projectId]").append("<option value='" + this.id + "'>" + this.name + "</option>")
                        });

                        bindProjectSelecterChangeModal()
                        $("#addStepModal [name=config_executor_projectId]").trigger('change');
                    },
                    error: function (xhr, textStatus) {
                        Message.error('获取项目失败');
                    }
                });

                return false;
            }

 */
