<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>消息列表</h1>
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
    //激活部署菜单
    activeMenu("m-message");

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
            url: '${context_path}/data/message',
            dataSrc: 'data'
        },
        columns: [ //定义列
            {
                title: '消息内容',
                data: "content"
            },
            {
                title: '创建时间',
                data: "createTime",
                render: function (data, type, row) {
                    return formatDate(data);
                }
            },
            {
                title: '状态',
                data: "status",
                render: function (data, type, row) {
                    if(data == 'read'){
                        return "<span class='bg-green'>已读</span>";
                    }else {
                        return "<span class='bg-red'>未读</span>";
                    }
                }
            },
            {
                title: '查看消息',
                render: function (data, type, row) {
                    return "<button class='btn-primary' onclick='readMessage("+row.id+",\""+row.jumpUrl+"\")'>查看消息</button>"
                }
            }
        ]
    });
}

function formatDate(value) {
    var date = new Date(value * 1000);
    var y = date.getFullYear(),
        m = date.getMonth() + 1,
        d = date.getDate(),
        h = date.getHours(),
        i = date.getMinutes(),
        s = date.getSeconds();
    if (m < 10) { m = '0' + m; }
    if (d < 10) { d = '0' + d; }
    if (h < 10) { h = '0' + h; }
    if (i < 10) { i = '0' + i; }
    if (s < 10) { s = '0' + s; }
    var t = y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;
    return t;
}


function reloadTable() {
  table.ajax.reload();
}

function viewHistory(id) {
    location.href = '${context_path}/view/task/history?deploymentId='+id;
}


function openAdd() {
    location.href = '${context_path}/view/deployment/add';
}
function openEdit(id) {
    location.href = '${context_path}/view/deployment/edit/'+id;
}
function openEditStep(id) {
    location.href = '${context_path}/view/deployment/'+id+'/editStep';
}
function deleteDeploy(id) {
    Message.confirm("确认要删除吗？",function () {
        $.ajax({
            url: '${context_path}/data/deployment/'+id,
            type: 'DELETE',
            success: function (res) {
              reloadTable();
              Message.success('删除成功')
            }
        });
    })
}
function execDeployment(id) {
    location.href = "${context_path}/view/task/list?deploymentId="+id;
}

function exportYaml(id) {
    location.href = '${context_path}/data/deployment/'+id+'/export';
}
</script>

<#include '../common/footer.ftlh' />
