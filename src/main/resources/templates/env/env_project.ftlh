<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>项目列表</h1>
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
                            <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#addProjectPop" onclick="openAdd()">
                                新增
                            </button>
                        </div>
                        <div class="modal fade" id="addProjectPop">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="card-header">
                                        <h3 class="card-title" id="projectTitle"></h3>
                                    </div>
                                    <div class="modal-body">
                                        <form id="quickForm" role="form">
                                            <input name="id" id="uid" type="hidden"/>
                                            <div class="card-body">
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1">项目名称</label>
                                                    <input  name="name" class="form-control" id="name" placeholder="请填写项目名称">
                                                </div>

                                            </div>

                                            <div class="modal-footer">
                                                <button id="adds" type="submit" class="btn btn-primary btn-sm">确认</button>
                                                <button class="btn bg-red btn-sm" data-dismiss="modal">取消</button>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <table id="table" class="table table-bordered table-hover">
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>



</div>

<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="../../plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    var map = new Map()
    $(function () {

        //激活部署菜单
        activeMenu("env-vars");
        //初始化table
        initTable();
        openAdd();


    })
    $(document).ready(function () {
        $.validator.setDefaults({
            submitHandler: function () {
                 // alert("Form successful submitted!");
                if($("#adds").text()=="添加"){
                    add();
                }else {
                    edit();
                }
            }
        });
        $('#quickForm').validate({
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "请输入项目名称"
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
    });



    function showEnv(projectid) {
        location.href = "${context_path}/view/env-variables/"+projectid+"/list";
    }

    function initTable() {
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 10,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/envProject',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '项目名称',
                    data: "name"
                },
                {
                    title: '操作',
                    render: function (data, type, row) {
                        map.set(row.id, row)
                        var btnHtml = "";
                        btnHtml += '<button type="button" data-toggle="modal" data-target="#addProjectPop" class="btn btn-primary btn-sm" onclick="openEdit(' + row.id + ')">修改</button> ';
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="showEnv(' + row.id + ')">变量</button> ';
                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteDeploy(' + row.id + ')">删除</button> ';
                        return btnHtml;
                    }
                }
            ]
        });
    }

    function openAdd() {

        $("#adds").html("添加")
        $("#projectTitle").html("新增项目")


    }
    function add() {
            var data = $("#quickForm").serialize();
            $.ajax({
                url: '${context_path}/data/envProject',
                type: 'POST',
                data: data,
                success: function (res) {
                    if (res == "success") {
                        location.href = '${context_path}/view/env-project/list';
                        Message.success('成功');
                    }else {
                        Message.error(res);
                    }
                }
            });


    }
    function openEdit(id) {
        $("#uid").val(id);
        $("#adds").html("更新");
        $("#projectTitle").html("修改项目")
        $('#name').val( map.get(id).name);
        // $('#name').attr('placeholder',map.get(id).name);


    }
    function edit() {
        var id=$("#uid").val();
        var data = $("#quickForm").serialize();
        $.ajax({
            url: '${context_path}/data/envProject/'+ id,
            type: 'PUT',
            data: data,
            success: function (res) {
                if(res=="success"){
                    location.href = '${context_path}/view/env-project/list';
                }else {
                    Message.error(res);
                }

            }
        });
    }
    function deleteDeploy(id) {
        Message.confirm("确认要删除吗？", function () {
            $.ajax({
                url: '${context_path}/data/envProject/' + id,
                type: 'DELETE',
                success: function (res) {
                    location.href = '${context_path}/view/env-project/list';
                }
            });
        })
    }

</script>


<#include '../common/footer.ftlh' />