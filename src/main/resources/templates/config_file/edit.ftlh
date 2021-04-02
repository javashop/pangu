<#include '../common/header.ftlh' />
<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>编辑文件</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a
                                    href="${context_path}/view/config_project">项目列表</a></li>
                        <li class="breadcrumb-item active">
                            <a href="${context_path}/view/config_project/show/${config_file.configProjectId}">
                                ${config_file.configProjectName}
                            </a>
                        </li>
                        <li class="breadcrumb-item active">${config_file.name}</li>
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
                            <input type="hidden" name="id" value="${config_file.id}">
                            <div class="form-group">
                                <label>文件名称：</label>
                                <input class="form-control" id="editname" name="name" value="${config_file.name!''}"
                                       placeholder="请输入文件名称">
                            </div>
                            <input type="text" class="input input-small " style="width: 735px;display: none"
                                   name="content" id="content">
                            <div class="form-group">
                                <label>文件内容：</label>
                                <div id="container" style="width: 800px; height: 600px; border: 1px solid grey"></div>
                            </div>
                        </div>
                        <div class="card-footer text-center">
                            <button type="submit" class="btn btn-primary btn-sm" id="saveBtn">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </section>
</div>

<link
        rel="stylesheet"
        data-name="vs/editor/editor.main"
        href="${context_path}/plugins/monaco-editor/min/vs/editor/editor.main.css"
/>
<script>
    var require = {paths: {vs: '${context_path}/plugins/monaco-editor/min/vs'}};
</script>

<script src="${context_path}/plugins/monaco-editor/min/vs/loader.js"></script>
<script src="${context_path}/plugins/monaco-editor/min/vs/editor/editor.main.nls.js"></script>
<script src="${context_path}/plugins/monaco-editor/min/vs/editor/editor.main.js"></script>
<script>
    var editor;
    $(function () {
        activeMenu("m-config-file");
        $.get("${context_path}/data/config_project/config_file/${config_file.id}", function (data) {
            editor = monaco.editor.create(document.getElementById('container'), {
                value: data.content,
                language: data.type
            })
        });
    })

</script>
<script>
    $("#saveBtn").click(function () {
        var name = $("#editname").val();
        var data = {"content": editor.getValue(), "name": name}
        console.log("data:" + data);
        $.ajax({
            type: 'PUT',
            async: true,
            url: '${context_path}/data/config_project/config_file/${config_file.id}',
            data: data,
            success: function (result) {
                if (result == 1) {
                    Message.success('编辑成功')
                }
            }
        });
    })
</script>

<#include '../common/footer.ftlh' />