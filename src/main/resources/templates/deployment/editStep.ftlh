<#include '../common/header.ftlh' />

<style>
    .step-content-offset {
        margin-left: 40px
    }

    .item-title-color {
        color: #9e9e9e
    }

    #stepTemplate {
        display: none;
    }

    #executorConfigItemTemplate {
        display: none;
    }
</style>

<script src="${context_path}/pangu/step-ui.js"></script>
<script src="${context_path}/pangu/step.js"></script>
<script src="${context_path}/pangu/config-file.js"></script>
<script src="${context_path}/pangu/git-clone.js"></script>
<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编排部署步骤</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="${context_path}/view/deployment/list">部署列表</a></li>
                        <li class="breadcrumb-item active">${deployment.name}</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">

            <div class="row">
                <div class="col-12">

                    <div class="alert alert-warning alert-dismissible">
                        <i class="icon iconfont icon-tip"></i>通过拖拽<i class="iconfont icon-drag" style="color: #9e9e9e"></i>来排序，
                        点击 <i class="far fa-pause-circle " style="color: #6B6B6B" ></i>跳过此步骤 ，
                        点击 <i class="far fa-play-circle " style="color: #6B6B6B" ></i>继续此步骤，
                        点击步骤名称显示编辑步骤界面
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <div>
                                <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                                        data-target="#addStepModal" onclick="StepUI.modalRefresh()"><b>+</b> 新增
                                </button>
                                <button type="button" class="btn btn-success btn-sm"  onclick="goExec()">执行</button>
                                <button type="button" class="btn btn-success btn-sm"  onclick="exportYaml(${deployment.id})">导出</button>
                                <button type="button" class="btn btn-success btn-sm" onclick="openImport()">导入</button>
                                <form id="importForm" enctype="multipart/form-data" style="display: none">
                                    <input type="file" name="file" id="file" value=""/>
                                </form>
                            </div>
                            <br>
                            <div id="stepList">
                                <#list stepList as step>
                                    <#include 'template/stepTemplate.ftlh' />
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <#include 'template/add_step_modal.ftlh' />

</div>

<#-- 步骤内容模板 -->
<div id="stepTemplate">
    <#include 'template/stepTemplate.ftlh' />
</div>
<script src="${context_path}/plugins/sortable/Sortable.js"></script>
<script src="${context_path}/plugins/sortable/app.js"></script>
<script>
    var  deploymentId = "${deployment.id}"

    $(function () {
        activeMenu("m-deploy");
        $('[data-toggle="tooltip"]').tooltip()

        let stepForms = $(".step-form");
        StepUI.validateForm(stepForms)
        StepUI.refreshEvent();

        $("#file").change(function () {
            var formData = new FormData();
            var file = this.files[0];
            formData.append("file", file);
            $.ajax({
                url: "${context_path}/data/deployment/${deployment.id}/import",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    window.location.reload();
                }
            })
            this.value = '';
        });
    });

    function goExec() {
        location.href='${context_path}/view/task/list?deploymentId=${deployment.id}';
    }

    function exportYaml(id) {
        location.href = '${context_path}/data/deployment/' + id + '/export';
    }

    function openImport() {
        $("#file").click();
    }

</script>

<#include '../common/footer.ftlh' />
