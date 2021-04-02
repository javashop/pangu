<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>部署列表</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body card-box">

                            <div class="btn-boxs">
                                <button type="button" class="btn btn-success btn-sm" onclick="openAdd()">新增部署</button>
<#--                                <button type="button" class="btn btn-success btn-sm" onclick="openImport()">导入</button>-->
                            </div>

                            <div class="select-box">
                                <label>分组：</label>
                                <select id="groupSelect" class="form-control" onchange="searchPage()" name="groupId">
                                    <option value="">全部</option>
                                </select>
                            </div>


                            <form id="importForm" enctype="multipart/form-data" style="display: none">
                                <input type="file" name="file" id="file" value=""/>
                            </form>
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


<script>
    var table
    $(function () {

        //为下拉选赋值
        initSelect();
        //激活部署菜单
        activeMenu("m-deploy");

        //初始化table
        initTable('');

        <#--$("#file").change(function () {-->
        <#--    var formData = new FormData();-->
        <#--    var file = this.files[0];-->
        <#--    formData.append("file", file);-->
        <#--    $.ajax({-->
        <#--        url: "${context_path}/data/deployment/import",-->
        <#--        type: "POST",-->
        <#--        data: formData,-->
        <#--        processData: false,-->
        <#--        contentType: false,-->
        <#--        success: function (result) {-->
        <#--            reloadTable();-->
        <#--            Message.success("导入成功")-->
        <#--        }-->
        <#--    })-->
        <#--    this.value = '';-->
        <#--});-->

        //表单提交事件
        $("#searchFrom [type=submit]").click(function () {
            var groupId = $("#searchFrom").serialize();
            initTable(groupId);
            return false;
        });


    });

    //为下拉选赋值
    function initSelect(){

        $.ajax({
            url: '${context_path}/data/base-group/list?type=DEPLOY',
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                var groupSelect = document.getElementById("groupSelect");
                for (var i in res) {
                    var objOption = document.createElement("OPTION");
                    objOption.value = res[i].id;
                    objOption.text = res[i].name;
                    groupSelect.add(objOption);
                };
            }
        });
    }

    function openImport() {
        $("#file").click();
    }

    function initTable(groupId) {

        table = $("#table").DataTable({
            "destroy": true,
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 10,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/deployment?groupId=' + groupId,
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '标题',
                    data: "name"
                },
                {
                    title: '环境变量',
                    data: "environmentName"
                },
                {
                    title: '分组',
                    data: "groupName"
                },

                {
                    title: '操作',
                    render: function (data, type, row) {
                        var btnHtml = "";

                        btnHtml += '<div class="btn-group">\n' +
                            '<button type="button" class="btn btn-sm btn-default" onclick="openEdit(' + row.id + ')">编辑</button>\n' +
                            '<button type="button" class="btn btn-sm btn-default" onclick="deleteDeploy(' + row.id + ')">删除</button>\n' +
                            // '<button type="button" class="btn btn-sm btn-default" onclick="exportYaml(' + row.id + ')">导出</button>\n' +
                            '<button type="button" class="btn btn-sm btn-default" onclick="copyDeployment(' + row.id + ')">复制</button>\n' +
                            '</div>' +
                            '<div class="btn-group" style="margin-left:10px ">\n' +
                            '<button type="button" class="btn btn-sm btn-default" onclick="openEditStep(' + row.id + ')">步骤</button>\n' +
                            '<button type="button" class="btn btn-sm btn-default" onclick="execDeployment(' + row.id + ')">执行</button>\n' +
                            '<button type="button" class="btn btn-sm btn-default" onclick="viewHistory(' + row.id + ')">历史</button>\n' +
                            '</div>';

                        return btnHtml;
                    }
                }
            ]
        });


    }

    function searchPage() {

        var value = $("#groupSelect option:checked").val()
        this.initTable(value);


    }

    function reloadTable() {
        table.ajax.reload();
    }

    function viewHistory(id) {
        location.href = '${context_path}/view/task/history?deploymentId=' + id;
    }


    function openAdd() {
        location.href = '${context_path}/view/deployment/add';
    }

    function openEdit(id) {
        location.href = '${context_path}/view/deployment/edit/' + id;
    }

    function openEditStep(id) {
        location.href = '${context_path}/view/deployment/' + id + '/editStep';
    }

    function deleteDeploy(id) {
        Message.confirm("确认要删除吗？", function () {
            $.ajax({
                url: '${context_path}/data/deployment/' + id,
                type: 'DELETE',
                success: function (res) {
                    reloadTable();
                    Message.success('删除成功')
                }
            });
        })
    }

    function execDeployment(id) {
        location.href = "${context_path}/view/task/list?deploymentId=" + id;
    }

    function exportYaml(id) {
        location.href = '${context_path}/data/deployment/' + id + '/export';
    }

    function copyDeployment(id) {
        Message.confirm("确认创建副本吗？", function () {
            $.ajax({
                url: '${context_path}/data/deployment/' + id + "/copy",
                type: 'post',
                success: function (res) {
                    reloadTable();
                    Message.success('创建副本成功')
                },
                error: function (xhr, textStatus) {
                    Message.error('创建副本失败')
                }
            });
        })
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
