<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编辑部署</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="${context_path}/view/deployment/list">部署</a></li>
                        <li class="breadcrumb-item active">编辑部署</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>
    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <form role="form" id="editForm">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label>部署名称：</label>
                                        <input class="form-control" name="name" value="${deployment.name!''}"
                                               placeholder="请输入部署名称" maxlength="20">
                                    </div>


                                    <div class="form-group">
                                        <label>分组：</label>
                                        <select  class="form-control" data-placeholder="请选择部署分组" name="groupId">
                                            <option value="" >请选择部署分组</option>
                                            <#list baseGroupList as group>
                                                <option value="${group.id}"
                                                        <#if (deployment.groupId)?? && deployment.groupId == group.id>selected</#if> >
                                                    ${group.name}</option>

                                            </#list>
                                        </select>
                                    </div>

                                    <div class="form-group">

                                        <label>环境变量：</label>
                                        <select class="form-control" name="environmentId">
                                            <option value="">--请选择环境变量--</option>
                                            <#list envProjectList as envProject>
                                                <option value="${envProject.id}"
                                                        <#if (deployment.environmentId)?? && deployment.environmentId == envProject.id>selected</#if> >${envProject.name}</option>
                                            </#list>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label>部署方式：</label>
                                        <div class="row">
                                            <div class="col-6">
                                                <#include 'template/deploy_type.ftlh' />
                                            </div>

                                        </div>
                                    </div>

                                </div>

                                <div class="card-footer text-center">
                                    <button type="submit" class="btn btn-primary btn-sm">保存</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>


<!-- Select2 -->
<script src="${context_path}/plugins/select2/js/select2.full.min.js"></script>
<!-- Select2 -->
<link rel="stylesheet" href="${context_path}/plugins/select2/css/select2.min.css">
<link rel="stylesheet" href="${context_path}/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
<!-- page script -->
<script>
    $(function () {
        activeMenu("m-deploy");
        //Initialize Select2 Elements
        $('.select2').select2()

        //Initialize Select2 Elements
        $('.select2bs4').select2({
            theme: 'bootstrap4'
        })
        //仓库checkbox改变事件
        $("#dependRepoCheck").click(function () {
            var flag = $(this).is(':checked');
            if (flag) {
                $("#repositoryShowDiv").css("display", "flex");
                $("[name=dependRepo]").val('1')
            } else {
                $("#repositoryShowDiv").css("display", "none");
                $("[name=dependRepo]").val('0')
            }
        });

        $.validator.setDefaults({
            submitHandler: function () {
                edit();
            }
        });

        //表单验证
        $('#editForm').validate({
            rules: {
                name: {
                    required: true,
                    // email: true,
                },
            },
            messages: {
                name: {
                    required: "请填写部署名称",
                },
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

    });

    function findBranch() {
        var repositoryId = $("#repository").val();
        if ("" == repositoryId || null == repositoryId || undefined == repositoryId) {
            return;
        }
        $.ajax({
            url: '${context_path}/data/deployment/findBranch?repositoryId=' + repositoryId,
            type: 'Get',
            success: function (res) {
                var html = "";
                for (var i = 0; i < res.length; i++) {
                    html += "<option value='" + res[i] + "'>" + res[i] + "</option>";
                }
                $("#branch").html(html);
            }
        });
    }

    function edit() {
        //部署方式
        var deployWay = $("#deploy-way-tab .nav-item .active").attr("deploy-way")
        $("#editForm [name=way]").val(deployWay)

        var data = $("#editForm").serialize();
        $.ajax({
            url: '${context_path}/data/deployment/${deployment.id}',
            type: 'PUT',
            data: data,
            success: function (res) {
                location.href = '${context_path}/view/deployment/list';
            }
        });
        return false;
    }

</script>
<style>
    .select2-container .select2-selection--single, .select2-container--default .select2-selection--single .select2-selection__arrow {
        height: 38px;
    }

    .input-group {
        display: flex;
        flex-wrap: nowrap;
        align-items: center;
    }

    .input-group-text {
        padding: .65rem .75rem;
    }
</style>

<#include '../common/footer.ftlh' />
