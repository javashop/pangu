<#include '../common/header.ftlh' />
<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>文件管理</h1>
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
                            <button type="button" style="float:left;" class="btn btn-primary btn-sm"
                                    data-toggle="modal" data-target="#myModaladd">新增项目
                            </button>
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

    <div class="modal fade" id="myModaladd">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="card-header">
                    <h3 class="card-title">新增项目</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" action="/config_project/add" method="post">
                        <div class="card-body">
                            <div class="row">
                                <label>项目名称</label>
                                <input class="form-control" id="addname" name="name" placeholder="请输入项目名称">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <a href="#" id="confirmDelete" class="btn btn-primary"
                               onclick="add()">确认</a>
                            <a href="#" class="btn bg-red" data-dismiss="modal">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="myModaledit">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="card-header">
                    <h3 class="card-title">修改项目</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" action="/config_project/edit" method="put">
                        <div class="card-body">
                            <div class="row" id="edit">
                                <label>项目名称</label>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <a href="#" id="confirmDelete" class="btn btn-primary"
                               onclick="edit()">确认</a>
                            <a href="#" class="btn bg-red" data-dismiss="modal">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    $(function () {
        //初始化table
        initTable();
        //激活菜单
        activeMenu("m-config-file");
    });

    function initTable() {
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 10,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/config_project/page',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '项目名',
                    data: "name"
                },
                {
                    title: '修改时间',
                    data: "editTime"
                },
                {
                    title: '操作',
                    render: function (data, type, row) {
                        var btnHtml = "";
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="openShow(' + row.id + ')">编辑文件</button> ';
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModaledit" onclick="openEdit(' + row.id + ',\'' +row.name+ '\')">编辑名称</button> ';
                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteDeploy(' + row.id + ')">删除</button> ';
                        return btnHtml;
                    }
                }
            ]
        });
    }

    function openShow(id) {
        location.href = '${context_path}/view/config_project/show/' + id;
    }

    function add() {
        var name = $("#addname").val();
        var data = {"name": name}
        $.ajax({
            url: '${context_path}/data/config_project',
            type: 'POST',
            data: data,
            success: function (res) {
                location.href = '${context_path}/view/config_project';
            }
        });
    }

    function openEdit(id, name) {
        var idh = '<input class="form-control" id="editid" name="id" type="hidden" value="'+id+'">'
        var nameh = '<input class="form-control" id="editname" name="name" placeholder="请输入项目的新名称" >'
        $("#edit").html(idh + nameh);
        $("#editname").val(name)
    }

    function edit() {
        let id = $("#editid").val();
        var name = $("#editname").val();
        var data = {"name": name, "id": id}
        $.ajax({
            url: '${context_path}/data/config_project',
            type: 'PUT',
            data: data,
            success: function (res) {
                location.href = '${context_path}/view/config_project';
            }
        });
    }


    function deleteDeploy(id) {
        Message.confirm("确认要删除吗？", function () {
            $.ajax({
                url: '${context_path}/data/config_project/' + id,
                type: 'DELETE',
                success: function (res) {
                    location.href = '${context_path}/view/config_project';
                }
            });
        })
    }
</script>


<#include '../common/footer.ftlh' />

