<#list machineList as machine>
    <div class="timeline" machine-id="${machine.id}">
        <!-- timeline time label -->
        <div class="time-label">
            <span class="bg-gray machine-name">${machine.ip}</span>
        </div>
        <#list stepList as step >
            <!-- timeline item -->
            <div step-id="${step.id}"  >
                <i class="fas bg-gray step-state" animate="ok"></i>
                <div class="timeline-item" <#if step.isSkip==1>style="background-color: #d2cccc8a;opacity:0.2" </#if> >
                    <span class="time"><i class="fas fa-clock"></i> <span>00:00</span></span>
                    <h3 class="timeline-header task-name"  data-toggle="collapse" onclick="show('#log${machine.id}-${step.id}')">${step.name}</h3>
                    <div class="timeline-body" id="log${machine.id}-${step.id}" style="display:none;">
                        <pre><code class="bash task-log"></code></pre>
                    </div>
                </div>
            </div>
        </#list>
        <div><i class="fas fa-check bg-gray machine-state"  machine-id="${machine.id}"></i>
            <div class="timeline-item"></div>
        </div>

        <!-- END timeline item -->
    </div>
</#list>
