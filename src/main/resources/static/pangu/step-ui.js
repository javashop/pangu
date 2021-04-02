let configTempl = '<div class="form-group ">\n' +
    '    <label></label>\n' +
    '    <div class="step-executor-config">\n' +
    '    </div>\n' +
    '</div>'

let executor_config_class = "executor_config";
let checker_config_class = "checker_config";

let CONFIG_CLASS = {"executor": executor_config_class, "checker": checker_config_class}

/**
 * 步骤ui管理
 * 主要职责：包含各种事件的绑定，以及ui中dom元素的动态变化
 * @type {{bindAddBtn: StepUI.bindAddBtn, cleanExecutorConfig: StepUI.cleanExecutorConfig, bindDelBtn: StepUI.bindDelBtn, addStepDom: StepUI.addStepDom, addConfigDom: StepUI.addConfigDom, bindEditBtn: StepUI.bindEditBtn, refreshEvent: StepUI.refreshEvent, getStepData: (function(*): {}), validateForm: StepUI.validateForm, bindPluginSelectEventModal: StepUI.bindPluginSelectEventModal, bindShowHideBtn: StepUI.bindShowHideBtn, bindSelectEvent: (function(*, *=): boolean), modalRefresh: StepUI.modalRefresh, stepRefresh: (function(): undefined)}}
 */
var StepUI = {

    /**
     * 刷新所有事件
     */
    refreshEvent: function () {
        //绑定添加按钮事件
        this.bindAddBtn();
        //刷新步骤
        this.stepRefresh();

        //绑定步骤中删除按钮的事件
        this.bindDelBtn();

        //绑定步骤中编辑按钮的事件
        this.bindEditBtn();

        //绑定步骤中跳过按钮事件
        this.bindSkipBtn();

        //执行器/检查器下拉框改变事件
        this.bindPluginSelectEventModal();

        ConfigFile.bindProjectEditEvent($(".config-project>a") );

        GitClone.bindRepositoryEditEvent($(".config-git-clone>a") );


    },

    /**
     * 绑定插件选择事件
     * @param select
     * @param pluginType
     * @returns {boolean}
     */
    bindSelectEvent: function (select, pluginType) {
        let self = this;
        let form = select.parents("form");
        let executorFormGroup = select.parent();
        let executorId = select.val();

        self.cleanExecutorConfig(form, pluginType)

        if (!executorId) {
            return false;
        }

        let desc  = select.find("option:selected").attr("desc");
        let span= executorFormGroup.find("span.invalid-feedback")
        span.text(desc)


        if ("write_config" == executorId) {
            ConfigFile.showProject(executorFormGroup);
            ConfigFile.showTarget(executorFormGroup);
            return false;
        }

        if ("git_clone" == executorId) {
            GitClone.showRepository(executorFormGroup);
            GitClone.showTarget(executorFormGroup);
            return false;
        }


        $.ajax({
            url: context_path + '/data/plugin/' + executorId + '/config?pluginType=' + pluginType,
            type: 'GET',
            success: function (res) {
                if (res) {
                    self.addConfigDom(res.itemList,executorFormGroup,pluginType);
                }

            },
            error: function (xhr, textStatus) {
                Message.error('获取配置失败');
            }
        });

    },
    /**
     * modal页面 执行器下拉框改变事件
     */
    bindPluginSelectEventModal: function () {
        let self = this;
        $('form [name=executor]').unbind('change').change(function () {
             self.bindSelectEvent($(this), "executor")
        });

        $('form [name=checkType]').unbind('change').change(function () {
            self.bindSelectEvent($(this), "checker")
        });
    },

    /**
     * 步骤表单验证
     * 目前步骤表单有两种：添加对话框中的，和步骤列表中的表单
     * @param form
     */
    validateForm: function (form) {
        form.validate({
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "请输入步骤名称",
                }
            },
            errorElement: 'span',
            errorPlacement: function (error, element) {
                error.addClass('invalid-feedback');
                element.closest('.form-group').append(error);
            },
            highlight: function (element, errorClass, validClass) {
                $(element).addClass('is-invalid');
            },
            unhighlight: function (element, errorClass, validClass) {
                $(element).removeClass('is-invalid');
            }
        });
    },

    /**
     * dom中新增一个步骤
     */
    addStepDom: function (step) {

        //创建新的步骤元素，并赋值
        var $newStep = $($("#stepTemplate").html()).appendTo($("#stepList"))
        $newStep.attr("step-id", step.id);

        //名称和id控件
        $newStep.find("[name=id]").val(step.id)
        $newStep.find("[name=name]").val(step.name)

        //collapse折叠相关属性
        $newStep.find(".card-title ").attr("href", "#step-content-" + step.id);
        $newStep.find(".step-content").attr("id", "step-content-" + step.id);

        //执行器和检查器
        let executorSelect  =  $newStep.find("[name=executor]");
        let checkerSelect = $newStep.find("[name=checkType]");

        executorSelect.val(step.executor)
        checkerSelect.val(step.checkType)

        // 找到执行器和检查器的包裹div，以便在他的after插入config item list
        let executorFormGroup = executorSelect.parent();
        let checkerFormGroup = checkerSelect.parent();

        //分别为执行器和检查器创建配置项
        this.addConfigDom(step.executorConfig.itemList, executorFormGroup,"executor");
        this.addConfigDom(step.checkerConfig.itemList, checkerFormGroup,"checker");

        //写入配置文件相关输入项
        if ("write_config" == step.executor) {
            ConfigFile.addConfigTextDom( $.parseJSON(step.executorParams), executorFormGroup);
        }

        let form = $newStep.find("form");
        this.validateForm(form);

        //刷新步骤的事件
        this.refreshEvent()
    },



    /**
     * 清除执行器的配置项
     */
    cleanExecutorConfig: function (form, pluginType) {
        form.find("." + CONFIG_CLASS[pluginType]).remove();
    }
    ,

    addConfigDom: function (itemList,executorFormGroup,pluginType) {
        $(itemList).each(function (index) {
            let newConfig = $(configTempl)
            newConfig.addClass(CONFIG_CLASS[pluginType]);
            newConfig.find("label").text(this.title + ":");
            let input = $('<' + this.type + ' class="form-control" style="' + this.htmlcss + '" name="config_' + pluginType + '_' + this.name + '" placeholder="请输入' + this.title + '" />')
            input.appendTo(newConfig.find("div"));
            if (this.value) {
                input.val(this.value);
            }
            newConfig.insertAfter(executorFormGroup);
        });
    },

    /**
     * 添加或删除步骤时，更新dom
     */
    stepRefresh: function () {
        //获取所有步骤（不包含模板）
        var $stepList = $(".step:not(':last')");
        var stepNum = $stepList.length;
        if (stepNum == 0) {
            $("#stepList").html("<font id='tips' color='red'>请点击新增按钮添加步骤！</font>");
            return;
        } else {
            $("#tips").remove();
        }
        //遍历所有步骤
        $stepList.each(function (index) {
            //设置标签
            $(this).find(".step-label>a").html("[步骤" + (index + 1) + "]&nbsp;&nbsp;" + $(this).find("[name=name]").val());
        });
    },

    /**
     * 刷新Modal对话框
     */
    modalRefresh: function () {
        $("#addStepModal input").val('');
        $("#addStepModal .executor_config").remove();
        $("#addStepModal .checker_config").remove();
        $("#addStepModal select[name='executor']>option:first").prop("selected", true);
        $("#addStepModal select[name='checkType']>option:first").prop("selected", true);
        // $("#addStepModal select[name='executor']").trigger("change")
    },

    /**
     * 绑定添加按钮事件
     */
    bindAddBtn: function () {
        let $addForm = $("#addStepModal .step-form");
        let self = this;
        this.validateForm($addForm)
        //表单提交
        $("#saveBtn").unbind("click").bind("click",function () {
            let result = $addForm.valid();
            // console.log(result)
            if (!result) {
                return false;
            }
            let stepData = self.getStepData($addForm);
            Step.addStep(stepData);
        });
    }
    ,

    /**
     * 为"删除按钮"绑定事件
     */
    bindDelBtn: function () {
        //先取消所有事件的绑定
        $(".del-step-btn").unbind("click");

        $(".del-step-btn").click(function () {
            var $this = $(this)
            var stepName = $this.parents(".step").find("[name=name]").val()
            Message.confirm("确认删除 '" + stepName + "' 吗？", function () {
                var stepId = $this.parents(".step").find("[name=id]").val()
                Step.deleteStep(stepId);
            });
            return false;
        });
    },


    /**
     * 为"修改按钮"绑定事件
     */
    bindEditBtn: function () {
        let self = this;
        //先取消所有事件的绑定
        $(".edit-step-btn").unbind("click");

        $(".edit-step-btn").click(function () {
            var $editForm = $(this).parents(".step").find(".step-form");
            if (!$editForm.valid()) {
                return false;
            }
            var stepData = self.getStepData($editForm);
            Step.updateStep(stepData.id, stepData);
        });
    },


    /**
     * 为"跳过按钮"绑定事件
     */
    bindSkipBtn: function () {
        let self = this;
        //先取消所有事件的绑定
        $(".skip-step-btn").unbind("click");

        $(".skip-step-btn").click(function () {
            var stepId = $(this).parents(".step").attr("step-id")
            var isSkip = $(this).attr("isSkip")
            if(isSkip == 0){
                isSkip = 1;
                $(this).removeClass("fa-pause-circle").addClass("fa-play-circle").attr("data-original-title","继续执行").tooltip('update').tooltip('show');

                $(this).prev("a").css("color","#9e9e9e")
            }else {
                isSkip = 0;
                $(this).removeClass("fa-play-circle").addClass("fa-pause-circle").attr("data-original-title","暂停执行").tooltip('update').tooltip('show');
                $(this).prev("a").css("color","#007bff")
            }
            $(this).attr("isSkip", isSkip)

            Step.updateStepSkipStatus(stepId, isSkip);
            return false;
        });
    },


    /**
     * 将form表单数据组装成实体
     */
    getStepData: function ($form) {
        var fields = $form.serializeArray();
        var stepData = {};
        var executorParams = {};
        var checkerParams = {};
        $.each(fields, function (index, field) {
            if (field.name.indexOf("config_executor_") == 0) {
                executorParams[field.name.replace("config_executor_", "")] = field.value
            } else if (field.name.indexOf("config_checker_") == 0) {
                checkerParams[field.name.replace("config_checker_", "")] = field.value
            } else {
                stepData[field.name] = field.value;
            }
        })
        stepData.executorParams = JSON.stringify(executorParams);
        stepData.checkerParams = JSON.stringify(checkerParams);
        stepData.deploymentId = deploymentId
        return stepData;
    }


}
