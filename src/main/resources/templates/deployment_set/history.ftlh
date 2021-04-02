<#include '../common/header.ftlh' />

<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>部署集历史--${deploymentSetName}</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="#">部署集</a></li>
                        <li class="breadcrumb-item active">部署集历史</li>
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
    $(function () {
        initTable();
        activeMenu("m-deploy-set");
    })

    function viewDetail(id) {
        location.href = '${context_path}/view/deploymentSet/deploy/${deploymentSetId}?setTaskId=' + id
    }

    const STATE_TEXT = {'ERROR': '失败', 'SUCCESS': '成功', 'RUNNING': '正在运行'}

    function initTable() {
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            searching: false,
            pageLength: 4,
            serverSide: true,
            ajax: {
                url: '${context_path}/data/setTask/history?deploymentSetId=${deploymentSetId}',
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '部署时间',
                    data: "startTimeText",
                },
                {
                    title: '状态',
                    render: function (data, type, row) {

                        return STATE_TEXT[row.state];
                    }
                },

                {
                    title: '操作',
                    render: function (data, type, row) {
                        var btnHtml = "";
                        btnHtml += '<button type="button" class="btn btn-primary btn-sm" onclick="viewDetail(' + row.id + ')">详情</button> ';
                        return btnHtml;
                    }
                },
            ]
        });
    }
</script>

<#include '../common/footer.ftlh' />
