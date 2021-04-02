<#include '../common/header.ftlh' />

<link rel="stylesheet" href="${context_path}/plugins/layui/css/layui.css">
<script src="${context_path}/plugins/layui/layui.js"></script>
<script src="${context_path}/pangu/env-vars.js"></script>

<style>
    .table-wrapper {
        width: 300px;
        padding-left: 50px;
    }

    .alert {
        font-size: 14px;
    }

    .alert .icon {

        margin-right: 0px;
    }

    .content-wrapper .row {
        width: 95%;
    }

    .layui-table-cell i {

    }
</style>

<div class="content-wrapper">


    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>当前页面名称</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="#">父路径</a></li>
                        <li class="breadcrumb-item active">子路径</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>


    <div class="container-fluid">

        <div class="row">
            <div class="col-12">

                <div class="alert alert-warning alert-dismissible">
                    <i class="icon iconfont icon-tip"></i>单击变量单元格可以编辑相应内容
                </div>

            </div>
        </div>

        <div class="row">
            <div class="col-12" style="margin-bottom: 10px">
                <button type="button" class="btn  btn-default btn-sm" onclick="javascript:VarGroup.addGroup()">新增分组
                </button>

            </div>
        </div>
        <!-- end row -->

        <div class="row" id="table_wrapper">
            <div class="col-md-6 group" style="display: none">
                <div class="card card-primary">
                    <div class="card-header">
                        <h3 class="card-title"></h3>

                        <div class="card-tools">

                            <button type="button" class="btn btn-tool edit btn-sm"><i class="iconfont icon-edit"></i>
                            </button>
                            <button type="button" class="btn btn-tool del btn-sm"><i
                                        class="iconfont icon-Iconinfoicon-"></i>
                            </button>
                        </div>
                        <!-- /.card-tools -->
                    </div>
                    <!-- /.card-header -->
                    <div class="card-body">
                        <button type="button" class="btn  btn-default btn-sm addrow">新增</button>
                        <table></table>
                    </div>
                    <!-- /.card-body -->
                </div>
                <!-- /.card -->
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>
</div>


<div class="modal" tabindex="-1" id="add-group-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">请输入分组名</h5>
                <button type="button" class="close btn-sm" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>
                    <input type="text" class="form-control" id="group_name">

                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary btn-sm" id="addGroupBtn">确定</button>

            </div>
        </div>
    </div>
</div>
<script>

    //环境变量rest api 地址
    var varsRestApiUrl = "${context_path}/data/env_project/${project_id}/env-vars";
    var groupRestApiUrl = "${context_path}/data/${project_id}/env-group";

    var tableWrapper, templateHtml;


    $(function () {

        tableWrapper = $("#table_wrapper");
        templateHtml = tableWrapper.html();

        activeMenu("env-vars")

        /**
         * 由api获取数据并渲染环境变量表格
         * */
        $.get(varsRestApiUrl, function (data) {
            EnvVars.initTables(data);
        });

    })


    /**
     * 判断某个值是否为空
     * */
    function notEmpty(value) {
        return !(value == null || $.trim(value) == "")
    }


</script>


<#include '../common/footer.ftlh' />
