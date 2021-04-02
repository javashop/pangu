<#include '../common/header.ftlh' />
<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>项目文件管理</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="${context_path}/view/config_project">项目列表</a></li>
                        <li class="breadcrumb-item active"> ${config_project_name}</li>
                    </ol>
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
                                    data-toggle="modal" data-target="#myModaladd">新增文件
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
                    <h3 class="card-title">新增文件</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" action="/config_project/add" method="post">
                        <div class="card-body">
                            <div class="row">
                                <label>文件名称</label>
                                <input class="form-control" id="addname" name="name" placeholder="请输入文件名称">
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
                url: '${context_path}/data/config_project/${config_project_id}/page',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '文件名',
                    data: "name"
                },
                {
                    title: '操作',
                    render: function (data, type, row) {
                        var btnHtml = "";
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="openEdit(' + row.id + ')">编辑</button> ';
                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteDeploy(' + row.id + ')">删除</button> ';
                        return btnHtml;
                    }
                }
            ]
        });
    }

    function add() {
        var name = $("#addname").val();
        var data = {"name": name, "configProjectId": ${config_project_id}}
        $.ajax({
            url: '${context_path}/data/config_project/${config_project_id}/config_file',
            type: 'POST',
            data: data,
            success: function (res) {
                location.href = '${context_path}/view/config_project/${config_project_id}';
            }
        });
    }

    function openEdit(id) {
        location.href = '${context_path}/view/config_project/config_file/edit/' + id;
    }

    function deleteDeploy(id) {
        Message.confirm("确认要删除吗？", function () {
            $.ajax({
                url: '${context_path}/data/config_project/config_file/' + id,
                type: 'DELETE',
                success: function (res) {
                    location.href = '${context_path}/view/config_project/${config_project_id}';
                }
            });
        })
    }
</script>

<#include '../common/footer.ftlh' />

