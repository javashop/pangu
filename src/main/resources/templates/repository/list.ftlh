<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>仓库列表</h1>
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
                            <button type="button" class="btn btn-success btn-sm" onclick="openAdd()">新增仓库</button>
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
<script src="${context_path}/dist/js/demo.js"></script>

<script>

    $(function () {
        //激活菜单
        activeMenu("m-repository");
    });

    $(function () {
        activeMenu("m-repository");
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            pageLength: 10,
            searching: false,
            ajax: {
                url: '${context_path}/data/repository',
                dataSrc: ''
            },
            columns: [ //定义列
                {
                    title: '仓库名称',
                    data: "name"
                },{
                    title: '地址',
                    data: "address"
                },{
                    title: '操作',
                    render: function (data, type, row) {
                        var btnHtml = "";
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="openEdit('+row.id+')">编辑</button> ';
                        btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteDeploy('+row.id+')">删除</button>  ';
                        return btnHtml;
                    }
                }
            ]
        });

    });

    function deleteDeploy(id) {
        var flag = confirm("确认要删除吗？");

        if (flag){
            $.ajax({
                url: '${context_path}/data/repository/'+id,
                type: 'DELETE',
                success: function (res) {
                    location.href = '${context_path}/view/repository/list';
                }
            });
        }
    }

    function openEdit(id) {
        location.href = '${context_path}/view/repository/edit/'+id;
    }
    function openAdd() {
        location.href = '${context_path}/view/repository/add';
    }
</script>

<#include '../common/footer.ftlh' />