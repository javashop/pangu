<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>盘古</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="${context_path}/plugins/fontawesome-free/css/all.min.css">
    <link rel="stylesheet" href="${context_path}/plugins/iconfont/iconfont.css">

    <!-- DataTables -->
    <link rel="stylesheet" href="${context_path}/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${context_path}/dist/css/adminlte.min.css">
    <link rel="stylesheet" href="${context_path}/plugins/toastr/toastr.min.css">

    <link rel="shortcut icon" href="${context_path}/favicon.ico" type="image/x-icon"/>
    <script src="${context_path}/plugins/jquery/jquery.min.js"></script>
    <script src="${context_path}/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="${context_path}/plugins/datatables/jquery.dataTables.min.js"></script>
    <script src="${context_path}/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
    <script src="${context_path}/plugins/jquery-validation/jquery.validate.min.js"></script>
    <script src="${context_path}/plugins/toastr/toastr.min.js"></script>
    <script src="${context_path}/pangu/message.js"></script>
    <script src="${context_path}/dist/js/adminlte.min.js"></script>
    <script src="${context_path}/plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>

    <script>
        var context_path = '${context_path}';

        function activeMenu(menuid) {
            var menu = $("#" + menuid);
            $(".sidebar .active").removeClass("active")
            menu.addClass("active");
            menu.parents("li.has-treeview").children("a.nav-link").addClass("active");
        }

        //定时器列表
        var timerList = [];

        /**
         * 添加定时器
         * @param fnId 唯一标识
         * @param fn 执行的方法
         * @param fnArgs 方法参数
         */
        function addTimer(fnId, fn, fnArgs) {
            console.log("添加定时任务：" + fnId)
            timerList.push({id: fnId, fn: fn, args: fnArgs})
        }

        /**
         * 删除定时器
         */
        function deleteTimer(fnId) {
            timerList.forEach(function (fn, index) {
                if (fnId == fn.id) {
                    console.log("删除定时任务：" + fnId)
                    timerList.splice(index, 1);
                    return
                }
            })
        }

        /**
         * 定时器执行
         */
        function callTimer() {
            timerList.forEach(function (fn) {
                fn.fn.apply(this, fn.args)
            })
        }

        /**
         * 异步全局设置
         * */
        $.put = function (url, data, callback) {
            $.ajax({
                url: url,
                type: "put",
                dataType: "json",
                data: data,
                success: callback
            });
        };

        $.delete = function (url, callback) {
            $.ajax({
                url: url,
                type: "delete",
                success: callback
            });
        };

        $.ajaxSetup({
            beforeSend: function () {
                if ($.clickBtn) {
                    $.clickBtn.addClass("disabled").attr("origin_text", $.clickBtn.text()).html("请稍后");
                }
            },
            complete: function () {
                if ($.clickBtn) {
                    $.clickBtn.removeClass("disabled").text($.clickBtn.attr("origin_text"));
                }
            },
            error: function (result) {
                console.log(result)
            }
        });

        //添加读取消息定时器
        this.addTimer("getUnreadMessage", getUnreadMessage, [])

        $(function () {
            setInterval(callTimer, 1000)
        })

        /**
         * 查询未读消息列表
         */
        function getUnreadMessage() {
            $.ajax({
                url: '${context_path}/data/message/limit?num=5',
                dataType: 'json',
                success: function (res) {
                    let unreadCount = res.total_unread
                    if (unreadCount > 0) {
                        $("#unreadCount").css("display", "block");
                        $("#unreadCount").html(unreadCount);
                        $("#noMessage").css("display", "none");
                    } else {
                        $("#unreadCount").css("display", "none");
                        $("#noMessage").css("display", "block");
                    }

                    let messageList = res.limit
                    let html = "";
                    messageList.forEach(function (item, index) {
                        let timeStr = formatTime(new Date().getTime() - item.createTime * 1000)
                        html += '<a onclick="readMessage(' + item.id + ',\'${context_path}' + item.jumpUrl + '\')" href="javascript:void(0)" class="dropdown-item">\n' +
                            '    <div class="media">\n' +
                            '        <div class="media-body">\n' +
                            '            <h3 class="dropdown-item-title">\n' +
                            '                ' + item.content + '\n' +
                            '                <span class="float-right text-sm text-danger"><i class="fas fa-star"></i></span>\n' +
                            '            </h3>\n' +
                            '            <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> ' + timeStr + '</p>\n' +
                            '        </div>\n' +
                            '    </div>\n' +
                            '</a>\n' +
                            '<div class="dropdown-divider"></div>'
                    })

                    $("#itemList").html(html)
                }
            });
        }

        /**
         * 格式化时间
         * */
        function formatTime(duringTime) {
            let days = parseInt(duringTime / (1000 * 60 * 60 * 24));
            if (days > 0) {
                return days + " 天前";
            }
            ;
            let hours = parseInt((duringTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            if (hours > 0) {
                return hours + " 小时前 ";
            }
            ;
            let minutes = parseInt((duringTime % (1000 * 60 * 60)) / (1000 * 60));
            if (minutes > 0) {
                return minutes + " 分钟前 ";
            }
            ;
            let seconds = parseInt((duringTime % (1000 * 60)) / 1000);
            return seconds + " 秒前 ";
        }


        function readMessage(messageId, jumpUrl) {
            $.ajax({
                url: '${context_path}/data/message/' + messageId + '/read',
                success: function (resJson) {
                    location.href = jumpUrl;

                }
            });

            return false
        }


        /**
         * 退出异步操作
         */
        function doLogout() {
            $.ajax({
                url: '${context_path}/data/user/logout',
                type: "post",
                success: function (res) {
                    if (res == "success") {
                        location.href = '${context_path}/view/login';
                    }
                }
            });
        }

        /**
         * 退出登录
         */
        function logout() {
            Message.confirm("确认退出登录吗？", function () {
                doLogout()
            });

        }

        //全局的ajax访问，处理ajax清求时异常
        $.ajaxSetup({
            dataType: 'json',
            dataFilter: function (res) {
                let jsonData = JSON.parse(res);
                if (jsonData.success) {
                    return JSON.stringify(jsonData.data);
                } else {
                    toastr.error(jsonData.message);
                }
            }

        });


    </script>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <!-- Navbar -->
    <nav class="main-header navbar navbar-expand navbar-white navbar-light">
        <!-- Left navbar links -->
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
            </li>
            <li class="nav-item d-none d-sm-inline-block">
                <a href="https://pangu.javamall.com.cn" class="nav-link" target="_blank">盘古首页</a>
            </li>
            <li class="nav-item d-none d-sm-inline-block">
                <a href="https://pangu.javamall.com.cn/docs" class="nav-link" target="_blank">盘古文档</a>
            </li>
            <li class="nav-item d-none d-sm-inline-block">
                <a href="https://www.javamall.com.cn" class="nav-link" target="_blank">Javashop首页</a>
            </li>
        </ul>

        <!-- Right navbar links -->
        <ul class="navbar-nav ml-auto">
            <!-- Messages Dropdown Menu -->
            <li class="nav-item dropdown">
                <a class="nav-link" data-toggle="dropdown" href="#dropdownMenu">
                    <i class="far fa-bell"></i>
                    <span class="badge badge-danger navbar-badge" id="unreadCount" style="display: none">0</span>
                </a>
                <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right" id="dropdownMenu">
                    <div id="itemList">

                    </div>
                    <div style="text-align: center;margin:15px 0 15px 0;font-size: small;color: rgb(153, 153, 153)"
                         id="noMessage">暂无未读消息
                    </div>
                    <div class="dropdown-divider"></div>
                    <a href="/view/message/list" class="dropdown-item dropdown-footer">查看所有消息</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link" href="javascript:logout();">
                    <i class="fas fa-sign-out-alt"></i>
                </a>
            </li>
        </ul>
    </nav>
    <!-- /.navbar -->
    <!-- Main Sidebar Container -->
    <aside class="main-sidebar sidebar-dark-primary elevation-4">
        <!-- Brand Logo -->
        <a href="https://pangu.javamall.com.cn/" target="_blank" class="brand-link">
            <img src="${context_path}/dist/logo.png"
                 alt="AdminLTE Logo"
                 class="brand-image img-circle elevation-3"
                 style="opacity: .8">
            <span class="brand-text font-weight-light">盘古</span>
        </a>

        <!-- Sidebar -->
        <div class="sidebar">

            <!-- Sidebar Menu -->
            <nav class="mt-2">
                <ul class="nav nav-pills nav-sidebar nav-child-indent flex-column" data-widget="treeview" role="menu"
                    data-accordion="false">
                    <!-- Add icons to the links using the .nav-icon class
                         with font-awesome or any other icon font library -->
                    <#include "../common/menu.ftlh" />
                </ul>
            </nav>
            <!-- /.sidebar-menu -->
        </div>
        <!-- /.sidebar -->
    </aside>