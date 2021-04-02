<div class="step card1" step-id="${(step.id)!''}">
    <div class="card">

        <div class="card-header d-flex p-0 show-hide-btn ">

            <h3 class="card-title p-3 step-label"  data-toggle="collapse" href="#step-content-${(step.id)!''}">
                <i class="iconfont icon-drag" style="color: #9e9e9e"></i>
                <#if (step.isSkip)?? && step.isSkip == 0>
                    <a href="javascript:;" >${(step.name)!''}</a>
                    <i class="far fa-pause-circle skip-step-btn" style="color: #6B6B6B" data-toggle="tooltip" data-placement="top" title="暂停执行"   isSkip="${step.isSkip}"></i>
                </#if>

                <#if (step.isSkip)?? && step.isSkip == 1>
                    <a href="javascript:;" style="color: #9e9e9e">${(step.name)!''}</a>
                    <i class="far fa-play-circle skip-step-btn" style="color: #6B6B6B" data-toggle="tooltip" data-placement="top" title="继续执行"   isSkip="${step.isSkip}"></i>
                </#if>

                <#if !(step.isSkip)??>
                    <a href="javascript:;" ></a>
                    <i class="far fa-pause-circle skip-step-btn" style="color: #6B6B6B" data-toggle="tooltip" data-placement="top" title="暂停执行"   isSkip="0"></i>
                </#if>



            </h3>
            <ul class="nav nav-pills ml-auto p-2">
                <li class="nav-item"><button type="button" style="float: right" class="btn btn-primary btn-sm edit-step-btn">保存</button></li>
                <li class="nav-item">&nbsp;</li>
                <li class="nav-item"><button type="button" style="float: right" class="btn btn-danger btn-sm del-step-btn">删除</button></li>
            </ul>
        </div>
        <div class="card-body step-content collapse"  id="step-content-${(step.id)!''}">
            <div class="tab-content">
                <div class="tab-pane active">
                    <form class="step-form">
                        <input type="hidden" name="id" value="${(step.id)!''}">
                        <#include 'step.ftlh' />
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

