<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>密钥组</h1>
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
                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                                data-target="#addProjectPop" onclick="openAdd()">添加密钥对
                        </button>
                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                                data-target="#addProjectPop" onclick="openImportKeys()">导入私钥
                        </button>
                    </div>
                    <div class="modal fade" id="addProjectPop">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="card-header">
                                    <h3 class="card-title" id="projectTitle"></h3>
                                </div>
<#--                                <form id="importForm" enctype="multipart/form-data" style="display: block">-->
<#--                                    <input type="file" name="file" id="file" value="" />-->
<#--                                </form>-->
                                    <form role="form" id="quickForm">
                                        <input name="id" id="uid" type="hidden"/>
                                        <div class="card-body">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">密钥组名称</label>
                                                <input  name="name" class="form-control" id="username" placeholder="请填写密钥名称">
                                            </div>

                                            <div id="importPri">
                                                <div class="form-group">
                                                    <label for="exampleInputFile">文件导入 </label>
                                                    <div class="input-group">
                                                        <div class="custom-file">
                                                            <input type="file" class="custom-file-input" id="file" value=""/>
                                                            <label class="custom-file-label" for="exampleInputFile">请选择私钥文件</label>
                                                        </div>
                                                    </div>
                                                </div>
<#--                                                <textarea id="pri" name="privateKey"class="form-control" type="hidden" rows="4" placeholder="Enter ..." ></textarea>-->
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
<script src="../../plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.min.js"></script>
<script src="../../dist/js/demo.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>
<script src="../../pangu/md5.js"type="text/javascript"></script>

<script>
    var map = new Map()
    $(function () {
        //激活用户菜单
        activeMenu("m-keys");
        //初始化table
        initTable();
    });

    function initTable() {
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 10,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/keys',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '名称',
                    data: "name",

                },
                {
                    title: '操作',
                    render: function (data, type, row) {
                        map.set(row.id, row)
                        var btnHtml = "";

                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteKeys(' + row.id + ')">删除</button> ';
                        if(row.type==0){
                            btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="exportKeys(' + row.id + ')">导出公钥</button> ';
                        }
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="exportPriKeys(' + row.id + ')">导出私钥</button> ';
                        return btnHtml;
                    }
                }
            ]
        });
    }
    function openAdd() {
        $("#adds").html("添加")
        $("#projectTitle").html("添加密钥")
        $("#importPri").css("display", "none")
    }
    function openImportKeys() {
        $("#adds").html("导入私钥")
        $("#projectTitle").html("导入私钥")
        $("#importPri").css("display", "block")
    }

    <#--function importPriKeys(){-->
    <#--    var data = $("#quickForm").serialize();-->
    <#--    $.ajax({-->
    <#--        url: '${context_path}/data/keys/import',-->
    <#--        type: 'POST',-->
    <#--        data: data,-->
    <#--        success: function (res) {-->
    <#--            if (res == "success") {-->
    <#--                location.href = '${context_path}/view/keys/list';-->
    <#--            }else {-->
    <#--                Message.error(res);-->
    <#--            }-->

    <#--        },-->
    <#--        error: function (res) {-->
    <#--            Message.error(res);-->
    <#--        }-->
    <#--    });-->
    <#--}-->
    function add() {
        var data = $("#quickForm").serialize();
        $.ajax({
            url: '${context_path}/data/keys/create',
            type: 'POST',
            data: data,
            success: function (res) {
                if (res == "success") {
                    location.href = '${context_path}/view/keys/list';
                    Message.success("添加成功");
                }else {
                    Message.error(res);
                }

            }
        });

        return false;

    }
    function openImport() {
        // $("#file").click();
        var formData = new FormData();
        var file = $("#file")[0].files[0]
        var name =$("#username").val();
        formData.append("file", file);
        formData.append("name", name);
        $.ajax({
            url: "${context_path}/data/keys/upload",
            type: "POST",
            data: formData,
            processData : false,
            contentType : false,
            success : function (res) {
                if (res == "success") {
                    location.href = '${context_path}/view/keys/list';
                }else {
                    Message.error(res);
                }
            }
        })
        this.value = '';
    }



    function exportKeys(id) {
        location.href = '${context_path}/data/keys/'+id+'/public/export';
    }
    function exportPriKeys(id) {
        location.href = '${context_path}/data/keys/'+id+'/private/export';
    }
    function deleteKeys(id) {
        Message.confirm("确认要删除吗？", function () {
            $.ajax({
                url: '${context_path}/data/keys/' + id,
                type: 'DELETE',
                success: function (res) {
                    location.href = '${context_path}/view/keys/list';
                },
                error: function (xhr, textStatus) {
                    Message.error('删除失败')
                }
            });
        })
    }

    $(document).ready(function () {
        bsCustomFileInput.init();
        $.validator.setDefaults({
            submitHandler: function () {
                // alert("Form successful submitted!");
                if($("#adds").text()=="添加"){
                    add();
                }else if($("#adds").text()=="导入私钥"){
                    openImport()
                }else {
                    edit();
                }
            }
        });
        $('#quickForm').validate({
            rules: {
                name: {
                    required: true,
                    // email: true,
                },
                password: {
                    required: true,
                    // minlength: 5
                },
                name: {
                    required: true
                },
            },
            messages: {
                username: {
                    required: "请填写用户名",
                    email: "Please enter a vaild email address"
                },
                password: {
                    required: "请填写密码",
                    // minlength: "Your password must be at least 5 characters long"
                },
                name: {
                    required: "请填密钥名称",

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
</script>

<#include '../common/footer.ftlh' />

