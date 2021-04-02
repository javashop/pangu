<#include '../common/header.ftlh' />
<link href="${context_path}/pangu/prism.css" rel="stylesheet"/>
<script src="${context_path}/pangu/prism.js"></script>

<div class="content-wrapper">

    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>部署作业--${deploymentSet.name}</h1>
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
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <#if !setTaskId??>
                                <div>
                                    <button type="button" class="btn btn-primary btn-sm" id="execBtn">执行部署集</button>
                                </div>
                            </#if>

                            <br>

                            <div class="timeline" >
                                <#list deploymentList as deployment>
                                    <div deployment-id="${deployment.id}">
                                        <i class="fas bg-gray deployment-state" animate="ok"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fas fa-clock"></i> <span>00:00</span></span>
                                            <h3 class="timeline-header task-name show-deployment" deployment-id="${deployment.id}" style="cursor: pointer">${deployment.name}</h3>
                                        </div>

<#--                                        <div>-->
<#--                                            <div class="card">-->
<#--                                                <div class="card-header d-flex p-0 show-deployment" deployment-id="${deployment.id}" >-->
<#--                                                    <h3 class="card-title p-3 step-label">${deployment.name}</h3>-->
<#--                                                    <ul class="nav nav-pills ml-auto p-2">-->
<#--                                                        <li class="nav-item"><span class="time"><i class="fas fa-clock"></i> <span>00:00</span></span></li>-->
<#--                                                    </ul>-->
<#--                                                </div>-->
<#--                                            </div>-->
<#--                                        </div>-->
                                    </div>
                                </#list>

                                <div><i class="fas fa-check bg-gray deployment-set-state"></i>
                                    <div class="timeline-item"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

</div>

<script src="${context_path}/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${context_path}/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${context_path}/dist/js/demo.js"></script>

<script>
    //状态样式对照表
    const STATE_CLASS = {'WAITING':'bg-gray fa-clock','RUNNING': 'bg-green fa-clock', 'SUCCESS': 'bg-green fa-check', 'ERROR': 'bg-danger fa-exclamation'}
    let timerId = "loadDeploySetLog";
    let deploymentCount = ${deploymentList?size}

        $(function () {
            activeMenu("m-deploy-set");

            $("#execBtn").click(function () {
                execDeploymentSet();
            });
            $(".show-deployment").click(function () {
                switchTask($(this));
            });
            <#if setTaskId??>
                <#if setTaskState == 'RUNNING'>
                    addTimer(timerId, loadLog, [${setTaskId}])
                    <#--timer = setInterval(function () {-->
                    <#--    loadLog(${setTaskId})-->
                    <#--}, 1000);-->
                <#else>
                    loadLog(${setTaskId})
                </#if>
            <#else>
                showRunningTask();
            </#if>
        });

    function execDeploymentSet() {
        Message.confirm("确认要执行部署集吗？", function () {
            $.ajax({
                url: '${context_path}/data/deploymentSet/${deploymentSet.id}/exec',
                type: 'GET',
                success: function (setTaskId) {
                    changeState($("i.deployment-state"),'WAITING')
                    addTimer(timerId, loadLog, [setTaskId])
                    // timer = setInterval(function () {
                    //     loadLog(setTaskId)
                    // }, 1000);
                }
            });

        })
    }

    function showRunningTask() {
        $.ajax({
            url: '${context_path}/data/deploymentSet/${deploymentSet.id}/runningTask',
            type: 'GET',
            success: function (runningTask) {
                if(runningTask){
                    addTimer(timerId, loadLog, [runningTask.id])
                    // timer = setInterval(function () {
                    //     loadLog(runningTask.id)
                    // }, 1000);
                }
            }
        });
    }

    function switchTask(el) {
        let parentTaskId = el.attr('parentTaskId')
        if(!parentTaskId){
            return;
        }

        let deploymentId = el.attr('deployment-id')
        location.href = "${context_path}/view/task/list?deploymentId="+deploymentId+"&parentTaskId=" + parentTaskId;
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

    function loadLog(setTaskId) {
        $("#execBtn").attr('disabled', 'true')
        let logUrl = "${context_path}/data/setTask/" + setTaskId + "/logs";

        $.ajax({
            url: logUrl,
            dataType: 'json',
            success: function (parentTaskList) {
                render(parentTaskList);
                Prism.highlightAll()
            }
        });
    }

    function render(parentTaskList) {
        if (parentTaskList.length == 0) {
            return;
        }

        let allComplete = true;
        let allStatus = 'SUCCESS';
        if(parentTaskList.length < deploymentCount){
            allComplete = false;
        }
        for (let index in parentTaskList) {
            let parentTask = parentTaskList[index]
            let deploymentEl = $("[deployment-id=" + parentTask.deploymentId + "]");

            changeState(deploymentEl.find(".deployment-state"), parentTask.state)

            //有一个在运行中就还没有完成
            let endTime;
            if (parentTask.state == 'RUNNING'){
                allComplete =false;

                var now = new Date();
                endTime = now / 1000;
            }else {
                endTime = parentTask.endTime
                if(parentTask.state != 'SUCCESS'){
                    allStatus = 'ERROR';
                    allComplete = true
                }
            }
            let during = getFormatDuringTime(endTime - parentTask.startTime);
            deploymentEl.find(".time>span").text(during);

            deploymentEl.attr("state", parentTask.state)
            deploymentEl.find('.show-deployment').attr("parentTaskId", parentTask.id)
        }

        //闪烁
        flashRunning()

        if (allComplete) {
            deleteTimer(timerId)
            // window.clearInterval(timer)
            $("#execBtn").removeAttr('disabled')
            changeState($(".deployment-set-state"), allStatus)
        }

    }

    /**
     * 闪烁正在运行的deployment
     */
    function flashRunning() {
        let el = $("[state='RUNNING']").find(".deployment-state");

        if (el.attr("animate") != 'running') {
            el.attr("animate", "running")
            el.animate({opacity: '0.4'}, "slow");
            el.animate({opacity: '1'}, "slow", function () {
                $(this).attr("animate", "ok")
            });
        }
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

