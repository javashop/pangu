<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编辑部署分组</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="${context_path}/view/deploymentGroup/list">分组</a></li>
                        <li class="breadcrumb-item active">编辑分组</li>
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
                                        <label>分组名称：</label>
                                        <input class="form-control" name="name" value="${baseGroup.name!''}"
                                               placeholder="请输入部署名称">
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
        //激活部署分组菜单
        activeMenu("m-deploy-group");
        //Initialize Select2 Elements
        $('.select2').select2()

        //Initialize Select2 Elements
        $('.select2bs4').select2({
            theme: 'bootstrap4'
        })


        //表单提交事件
        $("#editForm [type=submit]").click(function () {
            var data = $("#editForm").serialize();
            $.ajax({
                url: '${context_path}/data/base-group/${baseGroup.id}',
                type: 'PUT',
                data: data,
                success: function (res) {
                    location.href = '${context_path}/view/deploymentGroup/list';
                }
            });
            return false;
        });
    });


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