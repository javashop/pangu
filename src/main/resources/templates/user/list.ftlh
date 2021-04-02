<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>用户列表</h1>
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
                                    data-target="#addProjectPop" onclick="openAdd()">添加用户
                            </button>
                        </div>
                        <div class="modal fade" id="addProjectPop">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="card-header">
                                        <h3 class="card-title" id="projectTitle"></h3>
                                    </div>
                                    <form role="form" id="quickForm">
                                        <input name="id" id="uid" type="hidden"/>
                                        <div class="card-body">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">用户名</label>
                                                <input name="username" class="form-control" id="username"
                                                       placeholder="请填写用户名">
                                            </div>
                                            <div class="form-group">
                                                <label for="exampleInputPassword1">密码</label>
                                                <input name="pwd" id="pwd" type="hidden"/>
                                                <input name="password" class="form-control" id="password"
                                                       placeholder="请填写密码">
                                            </div>
                                            <div class="form-group">
                                                <label for="exampleInputPassword1">姓名</label>
                                                <input name="name" class="form-control" id="name" placeholder="请填写姓名">
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <button id="up" type="updata" class="btn btn-primary btn-sm">更新</button>
                                            <button id="adds" type="submit" name="sub" class="btn btn-primary btn-sm">
                                                确认
                                            </button>
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
<script src="../../plugins/jquery-validation/additional-methods.min.js"></script>
<script src="../../dist/js/demo.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>
<script src="../../pangu/md5.js" type="text/javascript"></script>

<script>
    var map = new Map()
    $(function () {
        //激活用户菜单
        activeMenu("m-user");
        //初始化table
        initTable();

        $("#quickForm [type=updata]").click(function () {
            var basepwd = $("#pwd").val();

            var pwd = $("#password").val();

            if (pwd == "") {
                $("#password").val(basepwd);
            } else {
                $("#password").val(hex_md5(pwd.trim()));
            }
            var data = $("#quickForm").serialize();
            $.ajax({
                url: '${context_path}/data/user',
                type: 'PUT',
                data: data,
                success: function (res) {
                    location.href = '${context_path}/view/user/list';
                    Message.success('成功');
                },
                error: function (res) {
                    Message.success('失败');
                }
            });

            return false;
        });
    });

    function initTable() {
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 10,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/user',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '用户名',
                    data: "username",

                },
                {
                    title: '姓名',
                    data: 'name',
                },
                {
                    title: '操作',
                    render: function (data, type, row) {
                        map.set(row.id, row)
                        var btnHtml = "";
                        btnHtml += '<button type="button" data-toggle="modal" data-target="#addProjectPop" class="btn btn-primary btn-sm" onclick="openEdit(' + row.id + ')">修改</button> ';
                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteUser(' + row.id + ')">删除</button> ';
                        return btnHtml;
                    }
                }
            ]
        });
    }

    function openAdd() {
        $('#quickForm')[0].reset()
        $("#adds").html("添加")
        $("#projectTitle").html("添加用户")
        $("#adds").css("display", "block");
        $("#up").css("display", "none");

    }

    function add() {
        var pwd = $("#password").val()
        $("#password").val(hex_md5(pwd))
        var data = $("#quickForm").serialize();
        $.ajax({
            url: '${context_path}/data/user',
            type: 'POST',
            data: data,
            success: function (res) {
                if (res == "success") {
                    location.href = '${context_path}/view/user/list';

                    } else{
                        Message.error(res);
                    }
                }


        });


    }

    function edit() {
        var basepwd = $("#pwd").val();

        var pwd = $("#password").val();

        if (pwd == "如需更换密码，请填写新密码") {
            $("#password").val(basepwd);
        }else{
            $("#password").val(hex_md5(pwd.trim()));
        }
        var data = $("#quickForm").serialize();
        $.ajax({
            url: '${context_path}/data/user',
            type: 'PUT',
            data: data,
            success: function (res) {
                location.href = '${context_path}/view/user/list';
                Message.success('成功');
            },
            error: function (res) {
                Message.success('失败');
            }
        });

        return false;

    }

    function openEdit(id) {
        $("#uid").val(id);
        $("#pwd").val(map.get(id).password);
        $("#projectTitle").html("修改用户")
        $('#username').val(map.get(id).username);
        $('#password').attr('placeholder', '如需更换密码，请填写新密码');
        $('#name').val(map.get(id).name);
        $("#adds").css("display", "none");
        $("#up").css("display", "block");
    }

    function deleteUser(id) {
        Message.confirm("确认要删除吗？", function () {
            $.ajax({
                url: '${context_path}/data/user/' + id,
                type: 'DELETE',
                success: function (res) {
                    if (res == "success") {
                        location.href = '${context_path}/view/user/list';
                    } else {
                        Message.error(res);
                    }

                }
            });
        })
    }

    $(document).ready(function () {
        $.validator.setDefaults({
            submitHandler: function () {
                // alert("Form successful submitted!");
                if ($("#adds").text() == "添加") {
                    add();
                } else {
                    //  edit();
                }
            }
        });
        $('#quickForm').validate({
            rules: {
                username: {
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
                    required: "请填写姓名",

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
