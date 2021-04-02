<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编辑仓库</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">
            <div class="card card-primary">
                <form role="form" id="addForm">
                    <input type="hidden" name="id" value="${repository.id}">
                    <div class="card-body">
                        <div class="form-group">
                            <label>仓库名称：</label>
                            <input class="form-control" value="${repository.name!''}" name="name" placeholder="请输入仓库名称">
                        </div>
                        <div class="form-group">
                            <label>仓库地址：</label>
                            <input class="form-control" value="${repository.address!''}" name="address" placeholder="请输入仓库地址">
                        </div>
                        <div class="form-group" style="margin-top: 30px;">
                            <label>认证方式</label>
                        </div>

                        <div class="form-group">
                            <div class="custom-control custom-radio" style="float:left;">
                                <div style="width:150px; float:left;">
                                    <input class="custom-control-input" type="radio" id="customRadio1" name="authType"
                                           <#if (repository.authType)?? && (repository.authType)=="password">checked</#if> value="password">
                                    <label for="customRadio1" class="custom-control-label">密码</label>
                                </div>
                            </div>
                            <div class="custom-control custom-radio" style="float:left;">
                                <div>
                                    <input class="custom-control-input" type="radio" id="customRadio2" name="authType"
                                           <#if (repository.authType)?? && (repository.authType)=="publickey">checked</#if> value="publickey">
                                    <label for="customRadio2" class="custom-control-label">公钥</label>
                                </div>
                            </div>
                            <div style="clear: both;"></div>
                        </div>


                        <div id="name-pwd">
                            <div class="form-group">
                                <label>账号：</label>
                                <input id="u-name" class="form-control" value="${repository.username!''}" name="username" placeholder="请输入账号" autocomplete="new-password">
                            </div>
                            <div class="form-group">
                                <label>密码：</label>
                                <input type="password" id="pass" class="form-control" value="${repository.password!''}" name="password" placeholder="请输入密码" autocomplete="new-password">
                                <input type="button" id="btn" value="按住显示" />
                            </div>
                        </div>

                        <div id="public-key" style="display: none">
                            <p></p>
                            <i> 请将机器的公钥添加到仓库的部署公钥中,请参考<a href="https://pangu.javamall.com.cn/docs/#/create-ssh-rsa" target="_blank">这里</a>。</i>
                            <p></p>
                        </div>

                    </div>

                    <div class="card-footer" id="edityanzheng" style="float: left; margin-right: 50px;">
                        <button type="button" class="btn btn-primary btn-sm">验证</button>
                    </div>
                    <div class="card-footer">
                        <button type="submit" class="btn btn-primary btn-sm">保存</button>
                    </div>

                </form>
            </div>
        </div>
    </section>

</div>

<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>

    //验证git账号密码是否正确
    $(function () {
        activeMenu("m-repository");
        //表单提交事件
        $("#addForm #edityanzheng").click(function () {
            var data = $("#addForm").serialize();
            $.ajax({
                url: '${context_path}/data/repository/validate',
                type: 'POST',
                data: data,
                success: function (res) {
                    Message.success('验证成功');
                }
            });
            return false;
        });


        $.validator.setDefaults({
            submitHandler: function () {
                update();
            }
        });

        $('#addForm').validate({
            rules: {
                name: {
                    required: true,
                },
                address: {
                    required: true,
                },
            },
            messages: {
                name: {
                    required: "请填写仓库名称",
                },
                address: {
                    required: "请填写仓库地址",
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

    });


    function update() {
            var data = $("#addForm").serialize();
            $.ajax({
                url: '/data/repository/' + data.id,
                type: 'PUT',
                data: data,
                success: function (res) {
                    location.href = '/view/repository/list';
                },
                error: function (xhr, textStatus) {
                    Message.error('保存失败');
                }
            });
            return false;
         };


    function radioCheck(){
        //checkbox改变事件
        var authType = $("input[name='authType']:checked").val();

        if (authType == 'password'){
            $("#name-pwd").show();
            $("#public-key").hide();
        } else if(authType == 'publickey'){
            $("#name-pwd").hide();
            $("#public-key").show();
        }
    }


    window.onload=function(){
        var btn=document.getElementById("btn");
        var pass=document.getElementById("pass");
        btn.onmousedown=function(){
            pass.type="text"
        };
        btn.onmouseup=btn.onmouseout=function(){
            pass.type="password"
        };
    };

    // 点击修改认证方式
    $("#fang-shi").click(function(){
        var namekey=document.getElementById("name-key");
        namekey.style.display = "block";
    });

    // 选择密码认证
    $("#customRadio1").click(function(){
        var mima=document.getElementById("name-pwd");
        var key=document.getElementById("public-key");
        mima.style.display = "block";
        key.style.display = "none";
    });

    // 选择公钥认证
    $("#customRadio2").click(function(){
        var mima=document.getElementById("name-pwd");
        var key=document.getElementById("public-key");
        mima.style.display = "none";
        key.style.display = "block";

        var name=document.getElementById("u-name");
        var pass=document.getElementById("pass");
    });

</script>

<#include '../common/footer.ftlh' />
