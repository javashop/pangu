//'仓库'控件的html模板
let repositoryInputTempl = ''+
    '<div class="form-group executor_config">\n' +
    '    <label>仓库:</label>\n' +
    '    <div class="config-repository">\n' +
    '    <select name="config_executor_repository_id" class="form-control " style="width: 49%;display: inline" required  data-msg="请选择仓库"></select>\n' +
    '    <select name="config_executor_branch" class="form-control " style="width: 50%;display: none"  ></select>\n' +
    '    <input name="config_executor_branch" class="form-control " style="width: 50%;display: none"  placeholder="请输入分支名称"/>\n' +
    '    </div>\n' +
    '</div>';

//'git克隆目标位置'的控件html模板
let cloneTargetTempl =
    '<div class="form-group executor_config">\n' +
    '    <label>目标路径:</label>\n' +
    '    <div class="config-config">\n' +
    '    <input name="config_executor_clone_target" class="form-control " required data-msg="请填写目标路径" placeholder="请填写目标路径"></input>\n' +
    '    </div>\n' +
    '</div>';

let repositoryMap = new Map();

var GitClone={

    /**
     * 形成仓库下拉框dom元素
     * @param repositoryList 仓库列表
     * @param executorFormGroup 要添加到的执行器表单form group div，会添加到这个元素的后面
     */
    addRepositoryDom: function (repositoryList,repositorySelect,currRepositoryId,currBranchName) {

        let self = this;


        //如果没有要选中的，则选中第一个
        repositorySelect.append("<option selected>请选择仓库</option>");


        $(repositoryList).each(function (index) {
            let repository = repositoryList[index];
            repositoryMap.set(repository.id, repository);
            let selected = "";

            if ( currRepositoryId && repository.id == currRepositoryId) {
                selected = "selected";
            }
            repositorySelect.append("<option value='" + repository.id + "' repositoryid='"+repository.id+"' "+ selected +">" + repository.name + "</option>");

        });



        //仓库下拉框选择事件
        repositorySelect.change(function () {
            let repositoryId = $(this).find("option:selected").attr("repositoryid");
            self.showBranch(repositoryId, repositorySelect);
        });

        if (currBranchName) {
            let repositoryId = repositorySelect.find("option:selected").attr("repositoryid");
            self.showBranch(repositoryId, repositorySelect, currBranchName);
        }
    },

    /**
     * 填充某个仓库的分支列表
     * @param branchList 分支列表
     * @param repositorySelect 仓库的select
     */
    addBranchSelectDom: function (branchList,repositorySelect,currentBranchName) {
        let branchSelect = repositorySelect.next("select[name=config_executor_branch]");
        let branchInput = branchSelect.next("input[name=config_executor_branch]");
        branchSelect.css("display", "inline").removeAttr("disabled")
        branchInput.css("display", "none").attr("disabled", "true")
        branchSelect.empty();
        branchSelect.append("<option >请选择分支</option>");

        $(branchList).each(function (index) {

            let branch = branchList[index];
            let selected = "";

            if ( currentBranchName && branch == currentBranchName) {
                selected = "selected";
            }

            branchSelect.append("<option value='" + branch + "' "+ selected +">" + branch + "</option>")
        });
    },

    /**
     * 填充某个仓库的分支列表
     * @param repositorySelect 仓库的select
     */
    addBranchInputDom: function (repositorySelect,currentBranchName) {
        let branchSelect = repositorySelect.next("select[name=config_executor_branch]");
        let branchInput = branchSelect.next("input[name=config_executor_branch]");
        branchSelect.css("display", "none").attr("disabled", "true")
        branchInput.css("display", "inline").removeAttr("disabled")
        branchInput.val(currentBranchName)
    },

    /**
     * 显示目标元素
     * @param executorFormGroup
     */
    showTarget: function (executorFormGroup) {
        $(cloneTargetTempl).insertAfter( executorFormGroup.next())
    },
    /**
     * 显示仓库下拉框
     * @param executorFormGroup 要添加到的执行器表单form group div，会添加到这个元素的后面
     */
    showRepository: function (executorFormGroup,currRepositoryId,currBranchName) {
        let sourceEl =  $(repositoryInputTempl);
        //添加到执行器表单group后面
        sourceEl.insertAfter(executorFormGroup);
        let repositorySelect = sourceEl.find("select[name=config_executor_repository_id]");
        let self = this;
        $.ajax({
            url: context_path + '/data/repository' ,
            type: 'get',
            success: function (repositoryList) {
                self.addRepositoryDom(repositoryList,repositorySelect,currRepositoryId,currBranchName)
            }
        });

    },

    /**
     * 显示某个仓库的分支
     * @param repositoryId
     * @param repositorySelect
     */
    showBranch: function (repositoryId,repositorySelect,currentBranchName) {
        let self = this;
        let authType = repositoryMap.get(parseInt(repositoryId)).auth_type;
        if(authType == 'publickey'){
            self.addBranchInputDom(repositorySelect,currentBranchName)
            return;
        }
        if (!repositoryId) {
            self.addBranchSelectDom([],repositorySelect,currentBranchName)
            return;
        }
        $.ajax({
            url: context_path + '/data/deployment/findBranch?repositoryId=' + repositoryId ,
            type: 'get',
            success: function (branchList) {
                self.addBranchSelectDom(branchList,repositorySelect,currentBranchName)
            }
        });
    },

    bindRepositoryEditEvent: function (editBtn) {
        let self = this;
        editBtn.unbind("click");
        editBtn.click(function () {
            //编辑链接的父group的上一个group是执行器所在的group
            let gitCloneFormGroup = $(this).parents(".form-group");
            let executorFormGroup = gitCloneFormGroup.prev();
            let branchSpan = $(this).prev("span");
            let repositorySpan = branchSpan.prev("span");
            let repositoryName = repositorySpan.text();
            let branch = branchSpan.text();
            let repositoryId = gitCloneFormGroup.find("input[name=config_executor_repository_id]").val()

            gitCloneFormGroup.remove();
            self.showRepository(executorFormGroup,repositoryId,branch);

        });
    }


}
