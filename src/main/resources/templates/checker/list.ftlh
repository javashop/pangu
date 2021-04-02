<#include '../common/header.ftlh' />

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>检测器列表</h1>
                </div>
            </div>
        </div>
    </section>


    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="alert alert-warning alert-dismissible">
                        <div style="float: left">
                            <i class="icon iconfont icon-tip"></i>通过拖拽<i class="iconfont icon-drag" style="color: #9e9e9e"></i>来排序，点击
                        </div>

                        <div class="custom-control custom-switch" style="float: left;margin-left: 10px">
                            <input type="checkbox" class="custom-control-input" id="customSwitch2">
                            <label class="custom-control-label" for="customSwitch2"></label>
                        </div>
                        开启或者关闭检查器
                        <div style="clear: both"></div>
                    </div>
                    <div class="card">
                        <div class="card-body">
                            <button type="button" class="btn btn-success btn-sm" onclick="javascript:location.href = '${context_path}/view/checker/add'">安装新检测器</button>
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

    <#include '../executor/template/pluginDetail.ftlh' />

</div>

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
<script src="${context_path}/plugins/sortable/Sortable.js"></script>

<script>
    let pluginMap = new Map();
    $(function () {
        //激活部署菜单
        activeMenu("m-deploy");

    });

    $(function () {
        activeMenu("m-checker");
        var table = $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            pageLength: 10,
            searching: false,
            ajax: {
                url: '${context_path}/data/checker',
                dataSrc: ''
            },
            columns: [ //定义列
                {
                    title: '名称',
                    data: "name",
                    render: function (data, type, row) {
                        return '<i class="iconfont icon-drag" style="color: #9e9e9e"></i>' + row.name;
                    }
                },{
                    title: '作者',
                    data: "author"
                },{
                    title: '描述',
                    data: "desc",
                    width: 500,
                    className: "column-desc-style"
                },{
                    title: '操作',
                    render: function (data, type, row) {
                        pluginMap.set(row.id, row)
                        var btnHtml = "";
                        // btnHtml += '<div class="btn-group"><button type="button" class="btn btn-danger btn-sm" onclick="deleteDeploy('+row.id+')">移除</button></div>';

                        btnHtml += '<div class="btn-group"><div class="custom-control custom-switch">' +
                            '                                <input type="checkbox" onchange="checkerSwitch(' + row.id + ')" checked class="custom-control-input operation'+row.id+'" id="operation'+row.id+'">' +
                            '                                <label class="custom-control-label" for="operation'+row.id+'"></label>' +
                            '                            </div></div>'
                        btnHtml += '<div class="btn-group" style="margin-left:10px"><button type="button" class="btn btn-sm btn-primary" onclick="pluginDetail('+row.id+')">详情</button></div>';
                        if (row.status == 'CLOSE') {
                            $(".operation" + row.id).removeAttr("checked");
                        }
                        // btnHtml += '<button type="button" class="btn btn-danger btn-sm" onclick="deleteDeploy('+row.id+')">移除</button>  ';
                        return btnHtml;
                    }
                }
            ]
        });
        table.on('draw', function () {
            Sortable.create(document.getElementById("table").getElementsByTagName("tbody")[0], {
                group: "words",
                animation: 150,
                handle: ".icon-drag",
                onEnd: function(evt){
                    const params = {}
                    params.sequence = evt.oldIndex
                    params.new_sequence = evt.newIndex
                    $.ajax({
                        url: '/data/checker/sort',
                        type: 'PUT',
                        data: params,
                        success: function (res) {
                        }
                    });
                }
            })
        });

    });

    function pluginDetail(id) {
        var plugin = pluginMap.get(id);
        $("#pluginName").html(plugin.name)
        $("#pluginAuthor").html(plugin.author)
        $("#pluginDesc").html(plugin.desc)
        $('#pluginDetailModal').modal('show')
    }


    /**
     * 检查器的开启和关闭
     * @param id 执行器id
     */
    function checkerSwitch(id) {
        //是否选中
        const checked = $("#operation" + id).prop("checked");
        if (checked) {
            $.ajax({
                url: '${context_path}/data/plugin/' + id + '/open',
                type: "PUT",
            });
        } else {
            $.ajax({
                url: '${context_path}/data/plugin/' + id + '/close',
                type: "PUT",
            });
        }
    }
    // function deleteDeploy(id) {
    //     var flag = confirm("确认要移除吗？");
    //
    //     if (flag){
    //         $.ajax({
    //             url: '${context_path}/data/repository/'+id,
    //             type: 'DELETE',
    //             success: function (res) {
    //                 location.href = '${context_path}/view/repository/list';
    //             },
    //             error: function (xhr, textStatus) {
    //                 alert('移除失败');
    //             }
    //         });
    //     }
    // }
    //
    // function openEdit(id) {
    //     location.href = '${context_path}/view/repository/edit/'+id;
    // }
    // function openAdd() {
    //     location.href = '${context_path}/view/repository/add';
    // }
</script>

<#include '../common/footer.ftlh' />