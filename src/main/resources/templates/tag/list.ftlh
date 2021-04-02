<#include '../common/header.ftlh' />

<link rel="stylesheet" href="${context_path}/plugins/layui/css/layui.css">
<script src="${context_path}/plugins/layui/layui.js"></script>
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
                    <h1>标签</h1>
                </div>
            </div>
        </div>
    </section>



    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">

                    <div class="card-body">
                        <div class="alert alert-warning alert-dismissible">
                            <i class="icon iconfont icon-tip"></i> 单击标签名称可以编辑标签
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">

                            <div class="table-wrapper">
                                <button type="button" class="btn  btn-default btn-sm" onclick="javascript:addRow()">新增标签</button>
                                <table id="test" lay-filter="test"></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

</div>


<script>
    $(function () {
        activeMenu("m-tags")
    })

    //标题栏
    var cols = [[
        {field: 'id', hide: true, title: "id"}
        , {field: 'name', title: '标签', edit: 'text'}
        , {
            field: 'del', title: '删除', templet: function (tag) {
                return '<a href="javascript:;" onclick="deleteTag(' + tag.id + ')"><i class="nav-icon iconfont icon-Iconinfoicon-"></i></a>'
            }
        }
    ]]

    let table;
    const tableEle = "test";

    /**
     * 获取表格数据
     ***/
    function getTableData() {
        let tableData = table.cache[tableEle]; //获取现有数据
        return tableData;
    }


    /**
     * 使用table data 渲染表格数据
     **/
    function tableRender(tableData){
        table.render({
            elem: "#"+tableEle
            , cols: cols
            , data: tableData
            ,page: false
            ,limit:100
        });
    }

    //初始化表格
    layui.use('table', function () {

        table = layui.table;

        table.render({
            elem: "#"+tableEle
            , url: '/data/tags/'
            , cols: cols
            , parseData: function (res) {
                return {
                    "code": 0, //解析接口状态
                    "msg": "", //解析提示文本
                    "count": 1000, //解析数据长度
                    "data": res //解析数据列表
                };
            }
            ,page: false
            ,limit:100
        });
        //监听单元格编辑
        table.on('edit('+tableEle+')', function (obj) {
            var value = obj.value //得到修改后的值
                , data = obj.data //得到所在行所有键值
                , field = obj.field; //得到字段
            save(data.id, value);
        });
    });

    /**
     * 删除标签，删除前的提示
     * @param tagId
     */
    function deleteTag(tagId) {

        //如果直接删除还没入库的，则直接清掉
        if (tagId == -1) {
            deleteRow(tagId);
            return ;
        }
        Message.confirm("确认删除这个标签吗？",function (){
            doDeleteTag(tagId);
        });
    }

    /**
     * 删除标签操作
     * @param tagId
     */
    function doDeleteTag(tagId) {

        $.ajax({
            url: "${context_path}/data/tags/" + tagId,
            type: "delete",
            success: function (result) {
                deleteRow(tagId);
                Message.success('删除成功');
            }
        });
    }

    /**
     * 删除datatable 一行
     **/
    function deleteRow(tagId) {
        let newTableData = [];
        const tableData = getTableData(); //获取现有数据
        let index = 0;
        for (let i = 0; i < tableData.length; i++) {
            const data = tableData[i];
            if (data.id != tagId) {
                newTableData[index] = data;
                index++;
            }
        }

        tableRender(newTableData);
    }

    /**
     * 保存，修改或添加
     * @param id
     * @param name
     */
    function save(id, name) {

        if (name == null || $.trim(name) == "") {
            Message.error("请输入标签名称");
            return false;
        }

        var method = "post";
        var url = "${context_path}/data/tags";

        //id=-1说明是新增数据
        // >0 说明在修改数据
        if (id > 0) {
            method = "put";
            url = url + "/" + id;
        }


        $.ajax({
            url: url,
            type: method,
            data: "name=" + name,
            success: function (tag) {

                //添加数据则更新表格数据的id，以便删除时获取正确的id
                if (id  == -1) {
                    let tableData = getTableData(); //获取现有数据
                    for (let i = 0; i < tableData.length; i++) {
                        const data = tableData[i];
                        if (data.id == -1) {
                            data.id = tag.id;
                        }
                    }
                    tableRender(tableData);
                }

                toastr.success('标签[' + name + ']保存成功');
            }
        });
    }

    /**
     * 给表格添加一行
     */
    function addRow() {

        //获取现有数据
        let tableData =getTableData();

        tableData.push({
            "id": "-1"
            , "name": ""
        });

        tableRender(tableData);

    }
</script>


<#include '../common/footer.ftlh' />
