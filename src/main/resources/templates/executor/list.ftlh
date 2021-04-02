<#include '../common/header.ftlh' />
<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>执行器列表</h1>
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
                        开启或者关闭执行器
                        <div style="clear: both"></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <button type="button" class="btn btn-success btn-sm"
                                    onclick="javascript:location.href = '${context_path}/view/executor/add'">安装新执行器
                            </button>
<#--                            <button type="button" class="btn btn-success btn-sm" onclick="javascript:sync();">同步-->
<#--                            </button>-->

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

    <#include 'template/pluginDetail.ftlh' />
</div>

<script src="${context_path}/plugins/sortable/Sortable.js"></script>
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
        activeMenu("m-executor");
        var table = $("#table").DataTable({
            "lengthChange": false,
            "ordering": false,
            pageLength: 50,
            searching: false,
            ajax: {
                url: '${context_path}/data/executor',
                dataSrc: ''
            },
            columns: [ //定义列
                {
                    title: '名称',
                    render: function (data, type, row) {
                        return '<i class="iconfont icon-drag" style="color: #9e9e9e"></i>' + row.name;
                    }
                }, {
                    title: '作者',
                    data: "author",
                    render: function (data, type, row) {
                        return "<a href='"+row.authorUrl+"' target='_blank'>"+data+"</a>"
                    }
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
                        btnHtml += '<div class="btn-group"><div class="custom-control custom-switch">' +
                            '                                <input type="checkbox" onchange="executorSwitch(' + row.id + ')" checked class="custom-control-input operation'+row.id+'" id="operation'+row.id+'">' +
                            '                                <label class="custom-control-label" for="operation'+row.id+'"></label>' +
                            '                            </div></div>'
                        if (row.status == 'CLOSE') {
                            $(".operation" + row.id).removeAttr("checked");
                        }

                        btnHtml += '<div class="btn-group" style="margin-left:10px"><button type="button" class="btn btn-sm btn-primary" onclick="pluginDetail('+row.id+')">详情</button></div>';
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
                        url: '/data/executor/sort',
                        type: 'PUT',
                        data: params,
                        success: function (res) {
                        }
                    });
                }
            })
        });
        $("input[data-bootstrap-switch]").each(function(){
            $(this).bootstrapSwitch('state', $(this).prop('checked'));
        });
    });

    /**
     * 执行器的开启和关闭
     * @param id 执行器id
     */
    function executorSwitch(id) {
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

    function sync() {
        $.ajax({
            url: '${context_path}/data/plugin/synchronizer',
            success: function () {
                Message.success("同步成功")
            }
        });
    }

    function pluginDetail(id) {
        var plugin = pluginMap.get(id);
        $("#pluginName").html(plugin.name)
        $("#pluginAuthor").html(plugin.author)
        $("#pluginDesc").html(plugin.desc)
        $('#pluginDetailModal').modal('show')
    }

</script>

<#include '../common/footer.ftlh' />
