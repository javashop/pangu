//标题栏
var cols = [[
    {field: 'id', hide: true, title: "id"}
    ,{field: 'groupId', hide: true, title: "groupId"}
    , {field: 'name', title: '变量名', edit: 'text'}
    , {field: 'value', title: '变量值', edit: 'text'}
    , {
        field: 'del', title: '删除', templet: function (varData) {
            return '<a href="javascript:;" onclick="EnvVars.deleteVar('+ varData.groupId + ',' + varData.id + ')"><i class=" iconfont icon-Iconinfoicon-"></i></a>'
        }
    }
]]

/**
 * 环境变量分组管理
 */
var VarGroup = {


    /**
     * 添加分组：弹出添加分组对话框
     */
    addGroup: function () {
        var self = this;
        $("#group_name").val("");
        $("#addGroupBtn").unbind("click").click(function () {
            $.clickBtn = $(this);
            self.doAddGroup();
        });
        $("#add-group-modal").modal();
    },

    /**
     * 调用api添加一个分组
     * @returns {boolean}
     */
    doAddGroup: function () {


        let groupName = $("#group_name").val();
        if (!notEmpty(groupName)) {
            Message.error("请输入分组名称");
            return false;
        }

        $.post(groupRestApiUrl, {name: groupName}, function (result) {
            EnvVars.createTableHtml(result.id, groupName);
            EnvVars.renderTable(result.id, []);
            $("#group_name").val("");
            $(".modal").modal('hide');
        });
    },

    /**
     * 删除分组
     * @param group_id
     */
    deleteGroup: function (group_id) {

        $.delete(groupRestApiUrl + "/" + group_id, function () {
            Message.success("删除成功");
            $("div.group[group_id=" + group_id + "]").remove();
        });
    },

    /**
     * 修改分组名称
     * @param group_id
     * @param name
     */
    updateGroup: function (group_id, name) {
        if (!notEmpty(name)) {
            Message.error("请输入分组名称");
        }
        $.put(groupRestApiUrl + "/" + group_id, {name: name}, function () {
            $("div.group[group_id=" + group_id + "] .card-title").text(name);
            $("#group_name").val("");
            $(".modal").modal('hide');
        });
    }

}


/**
 * 环境变量管理
 */
var EnvVars = {

    table:undefined,
    //初始化表格
    initTables: function (data) {

        var self = this;
        layui.use('table', function () {

            self.table = layui.table;

            self.createTable(data, function (key, tableData) {
                self.renderTable(key, tableData.data)
            });

        });
    },

    createTableHtml: function (key, group) {


        let self = this;
        //新建一个表格
        let newTable = $(templateHtml);
        newTable.insertBefore(tableWrapper.children().first()).attr("group_id", key).show();

        //设置表格的group名字
        newTable.find(".card-title").text(group);

        //设置表格的id和lay-filter
        newTable.find(".card-body table").attr("id", "t_" + key).attr("lay-filter", "t_" + key);

        //增加一个行（一个环境变量）
        newTable.find("button.addrow").click(function () {
            self.addRow(key);
        });

        //编辑分组
        newTable.find("button.edit").click(function () {
            $("#group_name").val(group);

            $("#addGroupBtn").unbind("click").click(function () {
                $.clickBtn = $(this);
                VarGroup.updateGroup(key, $("#group_name").val());
            });

            $(".modal").modal();
        });

        //删除分组
        newTable.find("button.del").click(function () {
            Message.confirm("删除分组会同时删除分组下的变量，会导致引用这些变量的部署出错！<br/>确认删除吗？", function () {
                VarGroup.deleteGroup(key);
            });

        });
    },
    createTable: function (tableDatas, callback) {


        for (let key in tableDatas) {

            //表格数据
            let tableData = tableDatas[key];

            //创建表格html
            this.createTableHtml(key, tableData.group)

            //创建数据
            callback(key, tableData);
        }
    },
    /**
     * 对某个变量表格进行重新渲染
     * @param group_id 分组id
     * @param tableData 渲染表格的数据
     */
    reRenderTable: function (group_id, tableData) {
        this.table.render({
            elem: "#t_" + group_id
            , cols: cols
            , page: false
            , limit: 100
            , data: tableData
        });
    },

    /**
     * 渲染一个表格
     * @param group_id 分组id
     * @param tableData 渲染表格的数据
     */
    renderTable: function (group_id, tableData) {

        this.reRenderTable(group_id, tableData);

        var self = this;
        //监听单元格编辑
        self.table.on('edit(t_' + group_id + ')', function (obj) {
            var value = obj.value //得到修改后的值
                , data = obj.data //得到所在行所有键值
                , field = obj.field; //得到字段
            // console.log(data)

            if (notEmpty(data.name) && notEmpty(data.value)) {
                self.save(group_id, data.id, data.name, data.value);
            }

        });
    },

    /**
     * 获取某个分组的环境变量表格数据
     * @param group_id 分组id
     * @returns 表格数据
     */
    getTableData: function (group_id) {
        //获取现有数据
        let tableData = this.table.cache["t_" + group_id];
        return tableData;
    },

    /**
     * 给某个分组的环境变量表格添加一行
     * @param group_id 分组id
     */
    addRow: function (group_id) {

        //获取现有数据
        let tableData = this.getTableData(group_id);
        // console.log(tableData);
        tableData.push({
            "id": "-1"
            , "name": ""
            , "value": ""
            , "groupId": group_id
        });

        this.reRenderTable(group_id, tableData);

    },


    /**
     * 删除一行环境变量，删除前的提示
     * @param groupId 分组id
     * @param varId 变量id
     */
    deleteVar: function (groupId, varId) {

        var self =this;

        //如果直接删除还没入库的，则直接清掉
        if (varId == -1) {
            self.deleteRow(groupId, varId);
            return;
        }
        Message.confirm("确认删除这个变量吗？", function () {
            self.doDeleteVar(groupId, varId);
        });
    },

    /**
     * 删除变量操作
     * @param groupId 分组id
     * @param varId 变量id
     */
    doDeleteVar: function (groupId, varId) {
        var self =this;
        $.ajax({
            url: varsRestApiUrl + "/" + varId,
            type: "delete",
            success: function (result) {
                self.deleteRow(groupId, varId);
                Message.success('删除成功');
            }
        });
    },

    /**
     * 删除某个分组表格的一行
     * @param groupId 分组id
     * @param varId 变量id
     */
    deleteRow: function (groupId, varId) {
        // console.log("groupid" + groupId)
        let newTableData = [];
        const tableData = this.getTableData(groupId); //获取现有数据

        let index = 0;
        for (let i = 0; i < tableData.length; i++) {
            const data = tableData[i];
            if (data.id != varId) {
                newTableData[index] = data;
                index++;
            }
        }

        this.reRenderTable(groupId, newTableData);
    },


    /**
     * 保存一个环境变量
     * @param groupId 分组id
     * @param id 环境变量id
     * @param name 变量名字
     * @param value 变量值
     * @returns {boolean} 如果参数不合法会返回false
     */
    save: function (groupId, id, name, value) {

        if (name == null || $.trim(name) == "") {
            Message.error("请输入变量名称");
            return false;
        }

        if (value == null || $.trim(value) == "") {
            Message.error("请输入变量值");
            return false;
        }

        var self = this;
        var method = "post";
        var url = varsRestApiUrl;

        //id=-1说明是新增数据
        // >0 说明在修改数据
        if (id > 0) {
            method = "put";
            url = url + "/" + id;
        }


        $.ajax({
            url: url,
            type: method,
            data: "name=" + name + "&value=" + value + "&groupId=" + groupId,
            success: function (varData) {

                //添加数据则更新表格数据的id，以便删除时获取正确的id
                if (id == -1) {
                    let tableData = self.getTableData(groupId); //获取现有数据
                    for (let i = 0; i < tableData.length; i++) {
                        const data = tableData[i];
                        if (data.id == -1) {
                            data.id = varData.id;
                        }
                    }
                    self.reRenderTable(groupId, tableData);
                }

                toastr.success('变量[' + name + ']保存成功');
            }
        });
    }

}
