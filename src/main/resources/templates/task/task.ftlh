<#include '../common/header.ftlh' />
<link href="${context_path}/pangu/prism.css" rel="stylesheet"/>
<script src="${context_path}/pangu/prism.js"></script>
<style>
    .timeline-body {
        height: 300px;
        overflow-y: auto;
    }
</style>
<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>${deployment.name}的详情</h1>
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

    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">

                        <div class="alert alert-warning alert-dismissible">
                            <i class="icon iconfont icon-tip"></i> 点击步骤名称可以显示详细日志
                        </div>
                </div>
            </div>

            <div class="row mb-2">
                <div class="col-md-12">

                    <div class="card">

                        <div class="card-header border-0">
                            <div class="d-flex justify-content-between">
                                <#if !parentTaskId??>
                                    <div class="card-tools">
                                        <button type="button" class="btn   btn-primary btn-sm" id="exeBtn">执行部署</button>
                                        <button type="button" class="btn   btn-primary btn-sm" id="editBtn">编辑步骤</button>
                                        <button type="button" class="btn   btn-danger btn-sm" id="stopBtn" disabled>停止执行</button>
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="card-body">
                            <#include 'machine.ftlh' />
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>

</div>

<script>

    let deployTaskId = '${parentTaskId!''}'

    let timerId = "loadDeployLog";

    $(function () {
        activeMenu("m-deploy");
        <#if parentTaskId??>
            <#if taskState?? && taskState == 'RUNNING'>
                addTimer(timerId, loadLog, [${parentTaskId}])
                <#--timer = setInterval(function () {-->
                <#--    loadLog(${parentTaskId})-->
                <#--}, 1000);-->
            <#else>
                loadLog(${parentTaskId})
            </#if>
        <#else>
            showRunningTask();
        </#if>

        $("#exeBtn").click(function () {
            Message.confirm("确认要执行部署吗？", function () {
                $.ajax({
                    url: '${context_path}/data/deployment/${deploymentId}/exec',
                    type: 'GET',
                    success: function (parentTaskId) {
                        deployTaskId = parentTaskId;
                        changeState($("i.step-state"),'WAITING')
                        changeState($(".machine-state"),'WAITING')
                        addTimer(timerId, loadLog, [parentTaskId])
                        // timer = setInterval(function () {
                        //     loadLog(parentTaskId)
                        // }, 1000);
                        Message.success('执行成功');
                    }
                });

            })
        });


        $("#editBtn").click(function () {
            location.href="${context_path}/view/deployment/${deploymentId}/editStep"
        });

        $("#stopBtn").click(function () {
            Message.confirm("确认停止执行吗？", function () {
                $.ajax({
                    url: '${context_path}/data/task/'+deployTaskId+'/stop',
                    type: 'GET',
                    success: function () {
                        Message.success('停止成功');
                    }
                });

            })
        });
    });

    //状态样式对照表
    const STATE_CLASS = {'WAITING':'bg-gray fa-clock','RUNNING': 'bg-green fa-clock', 'SUCCESS': 'bg-green fa-check', 'ERROR': 'bg-danger fa-exclamation'}

    function show(id) {
        $(id).toggle();
    }

    function showRunningTask() {
        $.ajax({
            url: '${context_path}/data/deployment/${deploymentId}/runningTask',
            type: 'GET',
            success: function (runningTask) {
                if(runningTask){
                    deployTaskId = runningTask.id;
                    addTimer(timerId, loadLog, [runningTask.id])
                    // timer = setInterval(function () {
                    //     loadLog(runningTask.id)
                    // }, 1000);
                }
            }
        });
    }


    /**
     * 闪烁正在运行的step
     */
    function flashRunning() {
        let el = $("[state='RUNNING']>i");

        if (el.attr("animate") != 'running') {
            el.attr("animate", "running")
            el.animate({opacity: '0.4'}, "slow");
            el.animate({opacity: '1'}, "slow", function () {
                $(this).attr("animate", "ok")
            });
        }
    }

    function render(machineList) {
        //任务的执行是异步的，有可能还没有获取到任务列表
        if (machineList.length == 0) {
            return;
        }

        let allComplete = true;
        for (let machineIndex in machineList) {
            let machine = machineList[machineIndex]
            let machineEl = $("[machine-id=" + machine.machineId + "]");

            machineEl.find("span.machine-name").text(machine.machineName);
            let taskList = machine.taskList;
            for (let taskIndex in taskList) {
                let task = taskList[taskIndex];

                let stepEl = machineEl.find("[step-id=" + task.stepId + "]");
                stepEl.attr("state", task.state)

                <#--if (task.state == 'RUNNING' || <#if parentTaskId??>true<#else >false</#if>) {-->

                    stepEl.find(".task-name").text(task.stepName);
                    stepEl.find(".task-log").text(task.log);

                    if(task.state == 'RUNNING'){
                        allComplete =false;

                        //日志滚动条滚动到最底部
                        let logEl = stepEl.find(".timeline-body");
                        logEl.scrollTop(logEl[0].scrollHeight);
                        var now = new Date();
                        let endTime = now / 1000;
                        let during = getFormatDuringTime(endTime - task.startTime);
                        stepEl.find(".time>span").text(during);
                    }


                // }

                changeState(stepEl.find("i.step-state"),task.state)

            }

            //更改机器状态
            if (machine.taskState != 'RUNNING') {
                changeState( machineEl.find(".machine-state"),machine.taskState);
            }
        }

        //闪烁
        flashRunning()

        if (allComplete) {
            deleteTimer(timerId)
            // window.clearInterval(timer)
            $("#exeBtn").removeAttr('disabled')
            $("#stopBtn").attr('disabled', 'true')
        }

    }

    /**
     * 更改某个元素的状态样式
     * @param el
     * @param state
     */
    function changeState(el,state) {
        for(let index in STATE_CLASS){
            el.removeClass(STATE_CLASS[index]);
        }
        el.addClass(STATE_CLASS[state])
    }

    function loadLog(parentTaskId) {
        $("#exeBtn").attr('disabled', 'true')
        $("#stopBtn").removeAttr('disabled')
        let logUrl = "${context_path}/data/task/" + parentTaskId + "/logs";

        $.ajax({
            url: logUrl,
            dataType: 'json',
            success: function (taskList) {
                render(taskList);
                Prism.highlightAll()
            }
        });

    }


    function getFormatDuringTime(during) {
        var s = Math.floor(during / 1) % 60;
        during = Math.floor(during / 60);
        var i = during % 60;
        during = Math.floor(during / 60);
        var h = during % 24;
        during = Math.floor(during / 24);
        var d = during;
        let result = "";
        if (d == 0) {
            d = "00"
        }

        if (i < 10) {
            i = "0" + i;
        }

        if (s < 10) {
            s = "0" + s;
        }
        if (d > 0) {
            result += d + ":";
        }
        if (h > 0) {
            result += h + ":"
        }
        result += i + ":" + s;
        return result;
    }

</script>


<#include '../common/footer.ftlh' />
