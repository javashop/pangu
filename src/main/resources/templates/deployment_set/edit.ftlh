<#include '../common/header.ftlh' />

<!-- Select2 -->
<link rel="stylesheet" href="${context_path}/plugins/select2/css/select2.min.css">
<link rel="stylesheet" href="${context_path}/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
<!-- jquery-validation -->
<script src="${context_path}/plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="${context_path}/plugins/jquery-validation/additional-methods.min.js"></script>
<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编排部署集</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
    <div class="container-fluid">

        <div class="card ">
            <div class="card-body">
                <div class="input-group input-group-sm" style="width: 10%;float: left">
                    <input type="hidden" id="deploymentSetId" value="${detail.id}">
                    <input type="text" id="setName" value="${detail.name}" class="form-control"  placeholder="请输入部署集名称">
                    <span class="input-group-append"><button type="button" class="btn btn-info btn-flat" onclick="saveName()">保存</button></span>
                </div>
                <button type="button" class="btn btn-success" style="margin-left: 20px;" data-toggle="modal" data-target="#myModal" onclick="openAddSetRelPop()">新增元素</button>
            </div>
            <table id="dc_table" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th width="30%">部署名称</th>
                    <th width="30%">部署依赖</th>
                    <th width="30%">操作</th>
                </tr>
                </thead>
                <tbody id="dc_body">
                </tbody>
            </table>
        </div>
    </div>
    </section>

    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="card-header">
                    <h3 class="card-title">新增部署集元素</h3>
                </div>
                <div  class="modal-body">
                    <input type="hidden" name="deploymentSetId" value="${detail.id}">
                    <input type="hidden" id="deploymentSetRelId">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-sm-12">
                                <!-- select -->
                                <div class="form-group">
                                    <label>部署名称</label>
                                    <select class="form-control" id="deploymentNameSelect" name="deploymentId"></select>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>部署依赖</label>
                            <select id="deploymentDependSelect" name="deploymentDependIds" class='select2' multiple='multiple' data-placeholder='请选择部署依赖' style='width: 100%;'></select>
                        </div>
                    </div>
                    <!-- /.card-body -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary btn-sm" onclick="save()">确认</button>
                        <button class="btn bg-red btn-sm" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>

<script src="${context_path}/plugins/select2/js/select2.full.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    $(function () {
        activeMenu("m-deploy-set");

        $.validator.setDefaults({
            submitHandler: function () {
                this.submit();
            }
        });
        $('#setForm').validate({
            rules: {
                deploymentId: {
                    required: true
                }
            },
            messages: {
                deploymentId: {
                    required: "请输入部署名称"
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
        initTable();
        //Initialize Select2 Elements
        $('.select2').select2()

        //Initialize Select2 Elements
        $('.select2bs4').select2({
            theme: 'bootstrap4'
        })
    });
    function save() {
        var deploymentSetRelId = $("#deploymentSetRelId").val();
        var depends = $("#deploymentDependSelect").val()+"";
        var data = {"deploymentSetId":$("#deploymentSetId").val(),"deploymentSetRelId":deploymentSetRelId,"deploymentId":$("#deploymentNameSelect").val(),"deploymentDependIds":depends}
        $.ajax({
            url: '${context_path}/data/deploymentSet/saveItem',
            type: 'POST',
            data: data,
            success: function () {
                Message.success("保存成功");
                $('#myModal').modal('hide');
                initTable();
            }
        });
    }
    function openAddSetRelPop() {
        $("#deploymentSetRelId").val("")
        $.get("${context_path}/data/deployment/findAll",function (deploymentList) {
            var options = "";
            deploymentList.forEach(function (index) {
                options +="<option value='"+index.id+"'>"+index.name+"</option>";
            })
            $("#deploymentNameSelect").html("");
            $("#deploymentDependSelect").html("");
            $("#deploymentNameSelect").append(options);
            $("#deploymentDependSelect").append(options);
            $('.select2').select2()
        });
    }
    function deleteItem(id) {
        Message.confirm("确认要删除这个部署元素嘛?",function () {
            $.ajax({
                url: '${context_path}/data/deploymentSet/deleteItem/'+id,
                type: 'DELETE',
                success: function () {
                    Message.success("删除成功");
                    initTable();
                }
            });
        });
    }

    function saveName() {
        $.ajax({
            url: '${context_path}/data/deploymentSet/updateName/${detail.id}',
            type: 'POST',
            data:{"name":$("#setName").val()},
            success: function () {
                Message.success("保存成功");
            }
        });
    }
    function openEdit(id) {
        $("#deploymentSetRelId").val(id)
        $.ajax({
            url: '${context_path}/data/deploymentSet/findDeploymentSetRelBySetId/'+id,
            type: 'GET',
            success: function (obj) {
                var dependIds = [];
                if (obj.deployment_depend_ids!=null&&obj.deployment_depend_ids!=""){
                    dependIds = obj.deployment_depend_ids.split(",");
                }
                $.get("${context_path}/data/deployment/findAll",function (deploymentList) {
                    var set_options = "";
                    var depend_options = "";
                    deploymentList.forEach(function (index) {
                        if (obj.deployment_id == index.id){
                            set_options +="<option value='"+index.id+"' selected>"+index.name+"</option>";
                        }else{
                            set_options +="<option value='"+index.id+"'>"+index.name+"</option>";
                        }
                        if (dependIds.indexOf(""+index.id)!=-1){
                            depend_options +="<option value='"+index.id+"' selected>"+index.name+"</option>";
                        }else{
                            depend_options +="<option value='"+index.id+"'>"+index.name+"</option>";
                        }
                    })
                    $("#deploymentNameSelect").html("");
                    $("#deploymentDependSelect").html("");
                    $("#deploymentNameSelect").append(set_options);
                    $("#deploymentDependSelect").append(depend_options);
                    $('.select2').select2()
                    $('#myModal').modal('show');
                });
            }
        });

    }

    function initTable() {

        $.ajax({
            url: '${context_path}/data/deploymentSet/findSets/${detail.id}',
            type: 'get',
            success: function (res) {
                if (null == res){
                    return;
                }
                var setLength = res.length;
                var dependLength = 0;
                var nhtml = "";
                var depends = "";
                for (var i = 0; i < setLength; i++) {
                    depends = "";
                    if (undefined != res[i].deployment_depend_list){
                        dependLength = res[i].deployment_depend_list.length;
                        for (var j = 0; j < dependLength; j++) {
                            depends+=res[i].deployment_depend_list[j].depend_name+"&nbsp;";
                        }
                    }
                    nhtml +="<tr>" +
                        "<td>"+res[i].deployment_name+"</td>" +
                        "<td>"+depends+"</td>" +
                        '<td><button type="button" class="btn btn-primary btn-sm" onclick="openEdit('+res[i].id+')">编辑</button> '+
                        '<button type="button" class="btn btn-danger btn-sm" onclick="deleteItem('+res[i].id+')">删除</button></td>';
                    "</tr>";
                }
                $("#dc_body").html(nhtml);
            }
        });
    }

</script>


<#include '../common/footer.ftlh' />

