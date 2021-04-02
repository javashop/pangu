<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编辑机器</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">
            <div class="card card-primary">
                <form role="form" id="quickForm">
                    <input type="hidden" name="id" value="${machine.id}">
                    <div class="card-body">
                        <div class="form-group">
                            <label>机器名称：</label>
                            <input class="form-control" value="${machine.name!''}" name="name" placeholder="请输入机器名称">
                        </div>
                        <div class="form-group">
                            <label>机器ip：</label>
                            <input class="form-control" value="${machine.ip!''}" name="ip" placeholder="请输入机器ip">
                        </div>

                        <div class="form-group">
                            <label>分组：</label>
                            <select  class="form-control" data-placeholder="请选择机器分组" name="groupId">
                                <option value="" >请选择机器分组</option>
                                <#list baseGroupList as group>
                                    <option value="${group.id}"
                                            <#if (machine.groupId)?? && machine.groupId == group.id>selected</#if> >
                                        ${group.name}</option>

                                </#list>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>用户名：</label>
                            <input class="form-control" value="${machine.username!''}" name="username"
                                   placeholder="请输入用户名" autocomplete="new-password">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">端口号：</label>
                            <input class="form-control" name="port" value="${machine.port!''}" autocomplete="new-password">
                        </div>
                        <div class="form-group">
                            <label>认证</label>
                        </div>

                        <div class="form-group">
                            <div class="custom-control custom-radio">
                                <div style="width:150px; float:left;">
                                    <input class="custom-control-input" style="" type="radio" id="customRadio1"  name="authType"
                                           <#if (machine.authType)?? && (machine.authType)=="password">checked</#if> value="password">
                                    <label for="customRadio1" class="custom-control-label">密码</label>
                                </div>

                                <div>
                                    <input class="custom-control-input" type="radio" id="customRadio2" name="authType"
                                           <#if (machine.authType)?? && (machine.authType)=="publickey">checked</#if> value="publickey">
                                    <label for="customRadio2" class="custom-control-label">证书</label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group"  style="display: none" id="passwordDiv">


                            <div class="form-group">
                                <label>密码：</label>
                                <input  class="form-control" value="${machine.password!''}"
                                       name="password"
                                       placeholder="请输入密码" autocomplete="new-password">
                            </div>
                        </div>
                        <div class="form-group" style="display: none" id="authDiv">
                            <div class="form-group">
                                <label>密钥对：</label>
                                <select class="form-control" name="secretkeyId" id="keys">
                                    <option value="">--请选择密钥--</option>
                                    <#list keysList as index>
                                        <option value="${index.id}"
                                                <#if (machine.secretkeyId)?? && (machine.secretkeyId)==(index.id)+"">selected</#if>>${index.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="card-footer text-center">
                        <button type="check" class="btn btn-primary btn-sm">验证</button>
                        <button type="submit" class="btn btn-primary btn-sm">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </section>


</div>

<script src="${context_path}/plugins/select2/js/select2.full.min.js"></script>
<link rel="stylesheet" href="${context_path}/plugins/select2/css/select2.min.css">
<link rel="stylesheet" href="${context_path}/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    $(function () {
        activeMenu("m-machine");

        $.validator.setDefaults({
            submitHandler: function () {
                updata();
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

        radioCheck()
        $("input[name='authType']").click(function(){
            radioCheck()
        });


        //表单验证事件
        $("#quickForm [type=check]").click(function () {
            var data = $("#quickForm").serialize();
            $.ajax({
                url: '${context_path}/data/machines/checker',
                type: 'POST',
                data: data,
                success: function (result) {
                    if(result=='success'){
                        Message.success('验证成功');
                    }
                }

            });

            return false;
        });
    });

    function radioCheck(){
        //checkbox改变事件
        var authType = $("input[name='authType']:checked").val();

        if (authType == 'password'){
            $("#passwordDiv").show();
            $("#authDiv").hide();
        } else if(authType == 'publickey'){
            $("#passwordDiv").hide();
            $("#authDiv").show();
        }
    }

    function updata(){
        //表单提交事件
        // $("#quickForm [type=submit]").click(function () {
            var data = $("#quickForm").serialize();
            $.ajax({
                url: '${context_path}/data/machines',
                type: 'PUT',
                data: data,
                success: function (res) {
                    location.href = '${context_path}/view/machines/list';
                }
            });

            return false;
        // });
    }

    function openAddSetPop() {
        var secretkeyId = $("#keys").val();
        if ("" == secretkeyId || null == secretkeyId || undefined == secretkeyId) {
            return;
        }
        $.ajax({
            url: '${context_path}/data/keys/list',
            type: 'Get',
            success: function (res) {
                var html = "";
                for (var i = 0; i < res.length; i++) {
                    html += "<option value='" + res[i].id + "'>" + res[i].name + "</option>";
                }
                $("#keys").html("");
                $("#keys").html(html);
                $('.select2').select2()
            }
        });


    }



    window.onload = function () {
        var btn = document.getElementById("btn");
        var pass = document.getElementById("pass")
        btn.onmousedown = function () {
            pass.type = "text"
        };
        btn.onmouseup = btn.onmouseout = function () {
            pass.type = "password"
        };
    };
</script>


<#include '../common/footer.ftlh' />