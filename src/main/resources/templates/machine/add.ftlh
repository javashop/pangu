<#include '../common/header.ftlh' />
<link rel="stylesheet" href="${context_path}/plugins/select2/css/select2.min.css">
<link rel="stylesheet" href="${context_path}/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>新增机器</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">
            <div class="card card-primary">
                <form role="form" id="quickForm">
                    <div class="card-body">
                        <div class="form-group">
                            <label  for="exampleInputEmail1">机器名称：</label>
                            <input class="form-control" name="name" placeholder="请输入机器名称">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">机器ip：</label>
                            <input id="machineIp" class="form-control" name="ip" placeholder="请输入机器ip">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">用户名：</label>
                            <input class="form-control" name="username" placeholder="请输入用户名" autocomplete="new-password">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">端口号：</label>
                            <input class="form-control" name="port" value="22" placeholder="请输入端口号" autocomplete="new-password">
                        </div>
                        <div class="form-group">
                            <label>分组：</label>
                            <select  class="form-control" data-placeholder="请选择机器分组" name="groupId">
                                <option value="" >请选择机器分组</option>
                                <#list baseGroupList as group>
                                    <option value="${group.id}">${group.name}</option>
                                </#list>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>认证</label>
                        </div>
                        <div class="form-group">
                            <div class="custom-control custom-radio">
                                <div style="width:150px; float:left;">
                                    <input class="custom-control-input" type="radio" id="customRadio1" name="authType" checked value="password">
                                    <label for="customRadio1" class="custom-control-label">密码</label>
                                </div>
                                <div>
                                    <input class="custom-control-input" type="radio" id="customRadio2" name="authType" value="publickey">
                                    <label for="customRadio2" class="custom-control-label">证书</label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group"  id="passwordDiv">
                            <div class="form-group">
                                <label>密码：</label>
                                <input  class="form-control" name="password" placeholder="请输入密码" autocomplete="new-password">
                            </div>
                        </div>
                        <div class="form-group" style="display: none" id="authDiv">

                            <div class="form-group">
                                <label>密钥对：</label>
                                <select class="form-control" name="secretkeyId" id="keys" >
                                    <option value="">--请选择密钥--</option>
                                    <#list keysList as index>
                                        <option value="${index.id}">${index.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer text-center">
                        <button type="check" class="btn btn-primary btn-sm" >验证</button>
                        <button type="submit"  class="btn btn-primary btn-sm">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </section>

</div>

<script src="${context_path}/plugins/select2/js/select2.full.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="../../plugins/jquery-validation/jquery.validate.min.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    $(function () {
        activeMenu("m-machine");
        //表单校验事件
        $("#quickForm [type=check]").click(function () {
            var data = $("#quickForm").serialize();
            $.ajax({
                url: '${context_path}/data/machines/checker',
                type: 'POST',
                data: data,
                success: function (result) {
                    Message.success('验证成功');
                }
            });

            return false;
        });
        //Initialize Select2 Elements
        $('.select2').select2()

        //Initialize Select2 Elements
        $('.select2bs4').select2({
            theme: 'bootstrap4'
        })

        //仓库checkbox改变事件
        $("#customRadio1").click(function () {
            $("#passwordDiv").css("display","block");
            $("#authDiv").css("display","none");
        });

        $("#customRadio2").click(function () {
            $("#passwordDiv").css("display","none");
            $("#authDiv").css("display","block");

        });

        $.validator.setDefaults({
            submitHandler: function () {
              add();
            }
        });
        $('#quickForm').validate({
            rules: {
                name: {
                    required: true,
                    // email: true,
                },
                ip: {
                    required: true,
                    // minlength: 5
                },
                username: {
                    required: true
                },
            },
            messages: {
                username: {
                    required: "请填写用户名",
                    email: "Please enter a vaild email address"
                },
                ip: {
                    required: "请填写ip",
                    // minlength: "Your password must be at least 5 characters long"
                },
                name: {
                    required: "请填写机器名称",

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

    function add(){
        //表单提交事件
        // $("#quickForm [type=submit]").click(function () {
            var data = $("#quickForm").serialize();
            $.ajax({
                url: '${context_path}/data/machines',
                type: 'POST',
                data: data,
                success: function (res) {
                    if (res=="success"){
                        location.href = '${context_path}/view/machines/list';
                    }
                },
            });

            return false;
        // });
    }

    function openAddSetPop() {
        var secretkeyId = $("#keys").val();
        if (""==secretkeyId || null == secretkeyId || undefined == secretkeyId){
            return;
        }

        $.ajax({
            url: '${context_path}/data/keys/list',
            type: 'Get',
            success: function (res) {
                var html = "";
                for (var i = 0; i < res.length; i++) {
                    html += "<option value='"+res[i].id+"'>"+res[i].name+"</option>";
                }
                $("#keys").html("");
                $("#keys").html(html);
                // $('.select2').select2()
            }
        });


    }



</script>

<#include '../common/footer.ftlh' />
