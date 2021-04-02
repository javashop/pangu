<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>新增仓库</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">
            <div class="card card-primary">
                <form role="form" id="addForm">
                    <div class="card-body">
                        <div class="form-group">
                            <label>仓库名称：</label>
                            <input class="form-control" name="name" placeholder="请输入仓库名称">
                        </div>
                        <div class="form-group">
                            <label>仓库地址：</label>
                            <input class="form-control" name="address" placeholder="请输入仓库地址">
                        </div>

                        <div class="form-group">
                            <label>认证</label>
                        </div>

                        <div class="form-group">
                            <div class="custom-control custom-radio" style="float:left;">
                                <div style="width:150px; float:left;">
                                    <input class="custom-control-input" type="radio" id="customRadio1" name="authType" checked value="password">
                                    <label for="customRadio1" class="custom-control-label">密码</label>
                                </div>
                            </div>
                            <div class="custom-control custom-radio" style="float:left;">
                                <div>
                                    <input class="custom-control-input" type="radio" id="customRadio2" name="authType" value="publickey">
                                    <label for="customRadio2" class="custom-control-label">公钥</label>
                                </div>
                            </div>
                            <div style="clear: both;"></div>
                        </div>

                        <div id="name-pwd">
                            <div class="form-group">
                                <label>账号：</label>
                                <input class="form-control" name="username" placeholder="请输入账号" autocomplete="new-password">
                            </div>
                            <div class="form-group">
                                <label>密码：</label>
                                <input type="password" class="form-control" name="password" placeholder="请输入密码" autocomplete="new-password">
                            </div>
                        </div>

                        <div id="public-key" style="display: none">
                            <p></p>
                            <i> 请将机器的公钥添加到仓库的部署公钥中,请参考<a href="https://pangu.javamall.com.cn/docs/#/create-ssh-rsa" target="_blank">这里</a>。</i>
                            <p></p>
                        </div>

                    </div>

                    <div class="card-footer" style="float: left; margin-right: 50px;">
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
        $("#addForm [type=button]").click(function () {
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
                add();
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


    });

    function add() {
        //表单提交事件
        //$("#addForm [type=submit]").click(function () {
            var data = $("#addForm").serialize();
            $.ajax({
                url: '/data/repository',
                type: 'POST',
                data: data,
                success: function (res) {
                    location.href = '/view/repository/list';
                },
                error: function (xhr, textStatus) {
                    Message.error('保存失败');
                }
            });
            return false;
        //});
    };

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
    });

</script>


<#include '../common/footer.ftlh' />

