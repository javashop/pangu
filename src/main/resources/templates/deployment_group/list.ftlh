<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>部署分组列表</h1>
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
                            <button type="button" class="btn btn-success btn-sm" onclick="openAdd()">新增分组</button>
                            <form id="importForm" enctype="multipart/form-data" style="display: none">
                                <input type="file" name="file" id="file" value="" />
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

    //激活部署分组菜单
    activeMenu("m-deploy-group");
    //初始化table
    initTable();


});



function initTable() {
    table = $("#table").DataTable({
        "lengthChange": false,
        "ordering": false,
        searching: false,
        pageLength: 10,
        serverSide: true,
        ajax: {
            url: '${context_path}/data/base-group?type=DEPLOY',
            dataSrc: 'data'
        },
        columns: [ //定义列
            {
                title: '分组名称',
                data: "name"
            },
            {
                title: '操作',
                render: function (data, type, row) {
                    var btnHtml = "";

                    btnHtml += '<div class="btn-group">\n' +
                        '<button type="button" class="btn btn-sm btn-default" onclick="openEdit('+row.id+')">编辑</button>\n' +
                        '<button type="button" class="btn btn-sm btn-default" onclick="deleteDeploy('+row.id+')">删除</button>\n' +
                        '</div>';

                    return btnHtml;
                }
            }
        ]
    });
}



function reloadTable() {
    table.ajax.reload();
}
function openAdd() {
    location.href = '${context_path}/view/deploymentGroup/add';
}
function openEdit(id) {
    location.href = '${context_path}/view/deploymentGroup/edit/'+id;
}

function deleteDeploy(id) {
    Message.confirm("确认要删除吗？",function () {
        $.ajax({
            url: '${context_path}/data/base-group/'+id,
            type: 'DELETE',
            success: function (res) {
              reloadTable();
              Message.success('删除成功')
            }
        });
    })
}
</script>

<#include '../common/footer.ftlh' />
