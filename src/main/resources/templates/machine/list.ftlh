<#include '../common/header.ftlh' />
<link rel="stylesheet" href="${context_path}/plugins/select2/css/select2.min.css">
<link rel="stylesheet" href="${context_path}/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css">
<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>机器列表</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body ">
                            <div class="card-box">
                                <div class="btn-boxs">
                                    <button type="button" class="btn btn-success btn-sm" onclick="openAdd()">新增机器
                                    </button>
                                </div>
                                <div class="select-box">
                                    <label>分组：</label>
                                    <select onchange="searchPage()" id="groupSelect" class="form-control" name="groupId">
                                        <option value="">全部</option>
                                    </select>
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
        </div>
    </section>


    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="card-header">
                    <h3 class="card-title">新增标签</h3>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" action="/article/add" method="post">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="hidden" id="mid" name="machineId">
                                </div>
                            </div>
                            <div class="form-group">
                                <label>标签名称</label>

                                <select id="tagSelect" class='select2' multiple='multiple'
                                        data-placeholder='请选择' style='width: 100%;'></select>
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

<script src="${context_path}/plugins/select2/js/select2.full.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    $(function () {
        //为下拉选赋值
        initSelect()
        //激活菜单
        activeMenu("m-machine");
        //初始化table
        initTable('');

    });

    //为下拉选赋值
    function initSelect() {

        $.ajax({
            url: '${context_path}/data/base-group/list?type=MACHINES',
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                var groupSelect = document.getElementById("groupSelect");
                for (var i in res) {
                    var objOption = document.createElement("OPTION");
                    objOption.value = res[i].id;
                    objOption.text = res[i].name;
                    groupSelect.add(objOption);
                }
                ;
            }
        });
    }

    function openEdit(id) {
        location.href = '${context_path}/view/machines/edit/' + id;
    }

    function openAdd() {
        location.href = '${context_path}/view/machines/add';
    }

    function deleteMachine(id) {
        Message.confirm("确认要删除吗？", function (){
            $.ajax({
                url: '${context_path}/data/machines/' + id,
                type: 'DELETE',
                success: function (res) {
                    location.href = '${context_path}/view/machines/list';
                }
            });
        })
    }

    function searchPage() {

        var value = $("#groupSelect option:checked").val()
        this.initTable(value);


    }

    function initTable(groupId) {

        $("#table").DataTable({
            "destroy": true,
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 10,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/machines/listVo?groupId=' + groupId,
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '机器名称',
                    data: "name",
                },
                {
                    title: '机器ip',
                    data: "ip",
                },
                {
                    title: '认证类型',
                    data: "auth_type",
                    render: function (data, type, row) {
                        var type=""
                        type=  data=="password"? "密码认证":"证书认证";
                        return type;
                    }
                },
                {
                    title: '分组',
                    data: "group_name",
                },
                {
                    title: '标签',
                    data: "tags",
                    render: function (data, type, row) {
                        var str = data;

                        var html = "";
                        if (str != null) {
                            str = str.split(",");
                            for (i = 0; i < str.length; i++) {
                                html += '<span class="badge bg-warning">' + str[i] + '</span>' + "&nbsp;"
                            }
                        }
                        html += '<a href="javascript:;" data-toggle="modal" data-target="#myModal" onclick="openAddSetPop(' + row.id + ')"><i  class="iconfont icon-edit"></i></a>'
                        return html;

                    }
                },
                {
                    title: '操作',
                    render: function (data, type, row) {
                        var btnHtml = "";
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="openEdit(' + row.id + ')">编辑</button> ';
                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteMachine(' + row.id + ')">删除</button> ';

                        return btnHtml;
                    }
                },
            ]
        });
    }

    function openAddSetPop(id) {
        $("#mid").val(id);
        $.ajax({
            url: '${context_path}/data/machinetags/tags/' + id,
            type: 'GET',
            success: function (obj) {
                var dependIds = [];
                if (obj != null && obj != "") {
                    dependIds = obj.split(",");
                }
                $.get("${context_path}/data/tags", function (tagList) {
                    var depend_options = "";
                    tagList.forEach(function (index) {
                        if (dependIds.indexOf("" + index.id) != -1) {
                            depend_options += "<option value='" + index.id + "' selected>" + index.name + "</option>";
                        } else {
                            depend_options += "<option value='" + index.id + "'>" + index.name + "</option>";
                        }
                    })
                    $("#tagSelect").html("");
                    $("#tagSelect").append(depend_options);
                    $('.select2').select2()
                    // $('#myModal').modal('show');
                });
            },
            error: function (res) {
                Message.error(res);
            }
        });

        $.get("${context_path}/data/tags", function (tagList) {
            var options = "";
            tagList.forEach(function (index) {
                options += "<option value='" + index.id + "'>" + index.name + "</option>";
            })
            $("#tagSelect").html("");
            $("#tagSelect").append(options);
            $('.select2').select2()
        });
    }


    function add() {
        var machineId = $("#mid").val();
        var tagId = $("#tagSelect").val();

        var data = {"tagId": tagId, "machineId": machineId}
        $.ajax({
            url: '${context_path}/data/machinetags',
            type: 'POST',
            data: data,
            traditional: true,//用传统方式序列化数据
            success: function (res) {
                location.href = '${context_path}/view/machines/list';
            }
        });
    }


</script>
<style>
    .card-box {
        display: flex;
        align-items: center;
    }

    .card-box .btn-boxs {
        flex: 1;
    }

    .select-box {
        display: flex;
        align-items: center;
    }

    .select-box label {
        margin-bottom: 0;
    }

    .select-box .form-control {
        flex: 1;
        margin-right: 10px;
        width: 250px;
    }
</style>

<#include '../common/footer.ftlh' />

