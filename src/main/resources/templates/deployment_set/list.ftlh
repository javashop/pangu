<#include '../common/header.ftlh' />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>部署集</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="#">Home</a></li>
                        <li class="breadcrumb-item active">DataTables</li>
                    </ol>
                </div>
            </div>
        </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-body">
                                <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#addDeploymentSetPop">新增</button>
<#--                                <button type="button" class="btn btn-success btn-sm" onclick="openImport()">导入</button>-->
<#--                                <form id="importForm" enctype="multipart/form-data" style="display: none">-->
<#--                                    <input type="file" name="file" id="file" value="" />-->
<#--                                </form>-->
                            </div>
                        </div>
                        <!-- /.card-header -->
                        <div class="card-body">
                            <table id="listTable" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th>标题</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list list as item>
                                    <tr>
                                        <td>${item.name}</td>
                                        <td>${(item.status == 0)? string('正常','异常')}</td>
                                        <td>
                                            <button type="button" class="btn btn-primary btn-sm" onclick="deploy(${item.id})">部署作业</button>
                                            <button type="button" class="btn btn-primary btn-sm" onclick="history(${item.id})">部署历史</button>
<#--                                            <button type="button" class="btn btn-primary btn-sm" onclick="checkHealth(${item.id})">健康检查</button>-->
                                            <button type="button" class="btn btn-primary btn-sm" onclick="edit('${item.id}')">编辑</button>
                                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteItem(${item.id})">删除</button>
<#--                                            <button type="button" class="btn btn-primary btn-sm" onclick="exportYaml(${item.id})">导出</button>-->
                                        </td>
                                    </tr>
                                </#list>
                                </tbody>

                            </table>
                        </div>
                        <!-- /.card-body -->
                    </div>
                    <!-- /.card -->


                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
    </section>
    <!-- /.content -->

    <div class="modal fade" id="addDeploymentSetPop">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="card-header">
                    <h3 class="card-title">新增部署集</h3>
                </div>
                <div  class="modal-body">
                    <form id="deploymentSetAdd"  class="form-horizontal" action="${context_path}/view/deploymentSet/add" method="post">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-sm-12">
                                    <!-- select -->
                                    <div class="form-group">
                                        <label for="deploymentSetName">部署集名称</label>
                                        <input class="form-control" name="deploymentSetName"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.card-body -->
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary btn-sm">确认</button>
                            <button class="btn bg-red btn-sm" data-dismiss="modal">取消</button>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
<!-- /.content-wrapper -->

<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${context_path}/plugins/jquery-validation/additional-methods.min.js"></script>

<script>

    $(function () {
        //激活菜单
        activeMenu("m-deploy-set");
    });

    $(function () {
        $.validator.setDefaults({
            submitHandler: function () {
                this.submit();
            }
        });
        $('#deploymentSetAdd').validate({
            rules: {
                deploymentSetName: {
                    required: true
                }
            },
            messages: {
                deploymentSetName: {
                    required: "请输入部署集名称"
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

        $("#file").change(function () {
            var formData = new FormData();
            var file = this.files[0];
            formData.append("file", file);
            $.ajax({
                url: "${context_path}/data/deploymentSet/import",
                type: "POST",
                data: formData,
                processData : false,
                contentType : false,
                success : function (result) {
                    window.location.reload();
                    Message.success("导入成功")
                }
            })
            this.value = '';
        });
    });
    function edit(id) {
        // id = id.replaceAll(",","");
        location.href = '${context_path}/view/deploymentSet/edit/'+id;
    }
    function deleteItem(id){
        Message.confirm("确认要删除这个部署集嘛?",function () {
            $.ajax({
                url: '${context_path}/data/deploymentSet/delete/'+id,
                type: 'POST',
                success: function (res) {
                    Message.success("删除成功");
                    window.location.reload();
                }
            });
        });
    }
    function deploy(id) {
        location.href = '${context_path}/view/deploymentSet/deploy/' + id
    }
    function history(id) {
        location.href = '${context_path}/view/deploymentSet/history/' + id
    }
    function checkHealth() {

    }
    function openImport() {
        $("#file").click();
    }
    function exportYaml(id) {
        location.href = '${context_path}/data/deploymentSet/'+id+'/export';
    }
</script>


<#include '../common/footer.ftlh' />

