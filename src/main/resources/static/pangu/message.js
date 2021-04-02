/**
 * 封装消息提示
 * 依赖toastr和bootstrap
 * @type {{confirm: Message.confirm, success: Message.success, error: Message.error}}
 */
let Message = {

    /**
     * 成功消息
     * @param msg
     */
    success: function (msg){
        toastr.success(msg);
    },

    /**
     * 错误消息
     * @param msg
     */
    error: function (msg) {
        toastr.error(msg);
    },

    /**
     * 确认消息
     * @param text
     * @param fn
     */
    confirm: function (text, fn) {
        //确认对话框的html
        const dialogHtml = '<div class="modal fade" id="confirm-modal">\n' +
            '    <div class="modal-dialog">\n' +
            '        <div class="modal-content">\n' +
            '            <div class="modal-header">\n' +
            '                <h4 class="modal-title">请确认</h4>\n' +
            '            </div>\n' +
            '            <div class="modal-body">\n' +
            '                <p> </p>\n' +
            '            </div>\n' +
            '            <div class="modal-footer justify-content-between">\n' +
            '                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\n' +
            '                <button type="button" class="btn btn-primary" >确认</button>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '        <!-- /.modal-content -->\n' +
            '    </div>\n' +
            '    <!-- /.modal-dialog -->\n' +
            '</div>\n'

        //判断是否存在对话框，如果不存在在body中插入一个
        let confirmDiv =$("#confirm-modal");
        if (confirmDiv.length == 0) {
            confirmDiv =$(dialogHtml);
            confirmDiv.appendTo($("body"))
        }

        //把确认文字插入
        confirmDiv.find(".modal-body>p").html(text);

        //给确认按钮绑定事件
        confirmDiv.find(".modal-footer>button.btn-primary").unbind("click").bind("click",function () {
            confirmDiv.modal('hide');
            fn();
        });

        confirmDiv.modal();
    }
}
