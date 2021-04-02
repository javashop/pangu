<#include '../common/header.ftlh' />
<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>安装新检查器</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="/view/checker/list">检查器</a></li>
                        <li class="breadcrumb-item active">安装新检查器</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>
    <section class="content">
        <div class="card-body">
            <input class="form-control" style="width: 20%;float: left;margin-right: 3%" type="text" id="executor_name" name="executor_name" placeholder="插件名称">
            <input type="hidden" name="executor_type" id="executor_type" value="checker"/>
<#--            <select class="form-control" style="width: 20%;float: left;margin-right: 1%" id="executor_type" name="executor_type">-->
<#--                <option value="executor">执行器</option>-->
<#--                <option value="checker">检查器</option>-->
<#--                <option value="deployment">部署模板</option>-->
<#--            </select>-->
            <button class="btn btn-info" id="search" onclick="search()">搜索</button>
        </div>
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

<#include '../executor/template/pluginDetail.ftlh' />

<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<style>
    .column-desc-style{
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        max-width:150px
    }
</style>

<script>

    let pluginMap = new Map();
    $(function () {
        initTable();
        activeMenu("m-checker");
    });

    function initTable() {
        $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            searching: false,
            // 件数选择下拉框内容
            lengthMenu: [10, 25, 50, 75, 100],
            // 每页的初期件数 用户可以操作lengthMenu上的值覆盖
            pageLength: 10,
            serverSide: true,
            paging: true,//开启表格分页
            ajax: {
                url: '${context_path}/data/plugin/pluginList',
                data: function (param) {
                    param.key = $('#executor_name').val();
                    param.metadataKind = $('#executor_type').val();
                    return param;
                },
                dataSrc: 'data'
            },
            columns: [ //定义列
                {
                    title: '名称',
                    data: "metadataName"
                }
                ,{
                    title: '作者',
                    data: "metadataAuthor",
                    render: function (data, type, row) {
                        return "<a href='"+row.metadataAuthorUrl+"' target='_blank'>"+data+"</a>"
                    }
                }
                ,{
                    title: '描述',
                    data: "metadataDesc",
                    width: 500,
                    className: "column-desc-style"
                }
                ,{
                    title: '操作',
                    render: function (data, type, row) {
                        pluginMap.set(row.id, row)
                        var btnHtml = "";
                        if(row.isInstall == 1){
                            btnHtml += '<div class="btn-group">\n' +
                                '<span class="bg-green" style="font-size: large">已安装</span>\n'+
                            '</div>';
                        }else {
                            btnHtml += '<div class="btn-group">\n' +
                                '<button type="button" class="btn btn-sm btn-default" onclick="execDeployment('+row.id+')">安装</button>\n'+
                            '</div>';
                        }

                        btnHtml += '<div class="btn-group" style="margin-left:10px"><button type="button" class="btn btn-sm btn-primary" onclick="pluginDetail('+row.id+')">详情</button></div>';
                        return btnHtml;
                    }
                }
            ]
        });
        $("#search").bind("click", function () { //点击按钮 触发table重新请求服务器
            $("#table").dataTable().fnDraw(false);
        });
    }
    function execDeployment (id){
        var plugin = pluginMap.get(id);
        $.ajax({
            headers: {
                'Content-Type': 'application/json'
            },
            url: '${context_path}/data/plugin/installPlugin',
            type: 'POST',
            data: JSON.stringify(plugin),
            success: function (res) {
                $("#table").dataTable().fnDraw(false);
                Message.success("安装成功");
            }
        });
    }

    function pluginDetail(id) {
        var plugin = pluginMap.get(id);
        $("#pluginName").html(plugin.metadataName)
        $("#pluginAuthor").html(plugin.metadataAuthor)
        $("#pluginDesc").html(plugin.metadataDesc)
        $('#pluginDetailModal').modal('show')
    }

</script>

<#include '../common/footer.ftlh' />

