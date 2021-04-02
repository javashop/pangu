//'源'控件的html模板
let sourceInputTempl = ''+
    '<div class="form-group executor_config">\n' +
    '    <label>源:</label>\n' +
    '    <div class="config-project">\n' +
    '    <select name="config_executor_project" class="form-control " style="width: 49%;display: inline" required  data-msg="请选择项目"></select>\n' +
    '    <select name="config_executor_file" class="form-control " style="width: 50%;display: inline"  ></select>\n' +
    '    </div>\n' +
    '</div>';

//'源'显示为text的html模板
let sourceTextTempl = '<input type="hidden" name="config_executor_project" /><input type="hidden" name="config_executor_file" /><span></span>&gt;<span></span><a href="javascript:;"><i class="iconfont icon-edit"></i></a>';

//'目标'的控件html模板
let targetTempl =
    '<div class="form-group executor_config">\n' +
    '    <label>目标:</label>\n' +
    '    <div class="config-target">\n' +
    '    <input name="config_executor_target" class="form-control " required data-msg="请填写目标路径" placeholder="请填写目标路径"></input>\n' +
    '    </div>\n' +
    '</div>';



var ConfigFile={

    bindProjectEditEvent: function (editBtn) {
        let self = this;
        editBtn.unbind("click");
        editBtn.click(function () {
            //编辑链接的父group的上一个group是执行器所在的group
            let projectFormGroup = $(this).parents(".form-group");
            let executorFormGroup = projectFormGroup.prev();
            let fileSpan = $(this).prev("span");
            let projectSpan = fileSpan.prev("span");
            let project = projectSpan.text();
            let file = fileSpan.text();

            projectFormGroup.remove();
            self.showProject(executorFormGroup,project,file);

        });
    },

    /**
     * 形成项目下拉框dom元素
     * @param projectList 项目列表
     * @param executorFormGroup 要添加到的执行器表单form group div，会添加到这个元素的后面
     */
    addProjectDom: function (projectList,projectSelect,currentProject,currentFile) {

        let self = this;


        //如果没有要选中的项目，则选中第一个
        projectSelect.append("<option   selected>请选择项目</option>");


        $(projectList).each(function (index) {
            let project = projectList[index];
            let selected = "";

            if ( currentProject && project.name == currentProject) {
                selected = "selected";
            }
            projectSelect.append("<option value='" + project.name + "' projectid='"+project.id+"' "+ selected +">" + project.name + "</option>");

        });



        //项目下拉框选择事件
        projectSelect.change(function () {
            let projectId = $(this).find("option:selected").attr("projectid");
            // console.log(projectId);
            self.showConfigFile(projectId, projectSelect);

        });

        if (currentFile) {
            let projectId = projectSelect.find("option:selected").attr("projectid");
            self.showConfigFile(projectId, projectSelect, currentFile);
        }
    },

    /**
     * 填充某个项目的文件列表
     * @param fileList 文件列表
     * @param projectSelect 项目的select
     */
    addConfigFileDom: function (fileList,projectSelect,currentFile) {
        let fileSelect = projectSelect.next("select[name=config_executor_file]");
        fileSelect.empty();
        fileSelect.append("<option value='全部' >全部</option>");

        $(fileList).each(function (index) {

            let file = fileList[index];
            let selected = "";

            if ( currentFile && file.name == currentFile) {
                selected = "selected";
            }

            fileSelect.append("<option value='" + file.name + "' "+ selected +">" + file.name + "</option>")
        });
    },

    addConfigTextDom: function (executorParams,executorFormGroup) {

        let $sourceHtmlTempl = $(sourceInputTempl);
        let $targetHtmlTempl = $(targetTempl);
        let $sourceTextTempl = $(sourceTextTempl);

        $targetHtmlTempl.find(".config-target [name=config_executor_target]").val(executorParams.target);
        $targetHtmlTempl.insertAfter(executorFormGroup);


        $sourceHtmlTempl
            .find(".config-project")
            .empty()
            .append($sourceTextTempl)

        $sourceHtmlTempl.find(".config-project span:first").text(executorParams.project);
        $sourceHtmlTempl.find(".config-project span:last").text(executorParams.file);
        $sourceHtmlTempl.find("[name=config_executor_project]").val(executorParams.project);
        $sourceHtmlTempl.find("[name=config_executor_file]").val(executorParams.file);

        $sourceHtmlTempl.insertAfter(executorFormGroup);
        this.bindProjectEditEvent($sourceHtmlTempl.find(".config-project>a") );
    },
    /**
     * 显示目标元素
     * @param executorFormGroup
     */
    showTarget: function (executorFormGroup) {
        $(targetTempl).insertAfter( executorFormGroup.next())
    },
    /**
     * 显示配置文件项目
     * @param executorFormGroup 要添加到的执行器表单form group div，会添加到这个元素的后面
     */
    showProject: function (executorFormGroup,project,file) {
        let sourceEl =  $(sourceInputTempl);
        //添加到执行器表单group后面
        sourceEl.insertAfter(executorFormGroup);
        let projectSelect = sourceEl.find("select[name=config_executor_project]");
        let self = this;
        $.ajax({
            url: context_path + '/data/config_project' ,
            type: 'get',
            success: function (projectList) {
                self.addProjectDom(projectList,projectSelect,project,file)
            }
        });

    },

    /**
     * 显示某个项目的配置文件
     * @param projectId
     * @param projectSelect
     */
    showConfigFile: function (projectId,projectSelect,currentFile) {
        let self = this;
        if (!projectId) {
            self.addConfigFileDom([],projectSelect,currentFile)
            return;
        }
        $.ajax({
            url: context_path + '/data/config_project/'+projectId ,
            type: 'get',
            success: function (fileList) {
                self.addConfigFileDom(fileList,projectSelect,currentFile)
            }
        });
    }


}
