<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){

        var selectType = $("input[name='triggerType']:checked").val();
        if(selectType=="1"){
            $("#trigger-simple").show();
            $("#trigger-cron").hide();
        }else if(selectType=="2"){
            $("#trigger-simple").hide();
            $("#trigger-cron").show();
        }

        $("input[name='triggerType']").change(function(){
            var type = $("input[name='triggerType']:checked").val();
            if(type=="1"){
                $("#trigger-simple").show();
                $("#trigger-cron").hide();
                $("#trigger-cron input").each(function(){
                    $(this).val("");
                });
            }else if(type=="2"){
                $("#trigger-simple").hide();
                $("#trigger-cron").show();
                $("#trigger-simple input").each(function(){
                    $(this).val("");
                });
            }
        });

        $("#saveForm").ajaxForm({
            success :function(data){
                $('#pager').pager().reload();
                $.msgbox({
                    msg : " <@s.text name="schedule.save.success"/>",
                    type : "success"
                });
                $page$.backpage();
            }
        });
    });
</script>

<div class="pad10L pad10R">
    <div class="example-box">
    <@s.form id="saveForm" namespace="/system/schedule" action="edit_save" method="post" cssClass="center-margin">
            <@s.hidden name="jobKey" value="%{queryGroup}"+"."+"%{queryJobName}" />
            <@s.hidden name="queryGroup" value="%{queryGroup}"/>
            <@s.hidden name="queryJobName" value="%{queryJobName}"/>
            <div id="normal-tabs-1">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.edit.group"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.property value="queryGroup" />
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.edit.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.property value="queryJobName"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.edit.jobClass"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.property value="jobDetail.jobClass.name"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.edit.triggerType"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.radio name="triggerType" list=r"#{'1':'简单规则','2':'cron表达式'}" value="%{triggerType}"  cssClass="trigger-type"/>
                                </div>
                            </div>
                        </div>
                        <span id="trigger-simple">
                            <@s.if test="@com.fantasy.framework.util.common.StringUtil@isNotBlank(trigger.repeatInterval)">
                             <div class="form-row">
                                 <div class="form-label col-md-3">
                                     <label for="">
                                         <@s.text name="schedule.edit.repeatInterval"/>
                                     </label>
                                 </div>
                                 <div class="form-input col-md-9">
                                     <div>
                                         <@s.property value="trigger.repeatInterval/60000" />
                                         <@s.if test="@com.fantasy.framework.util.common.StringUtil@isNotBlank(trigger.repeatInterval)">/</@s.if>
                                         <@s.property value="trigger.repeatCount" />  <@s.text name="schedule.edit.repeatCount"/>

                                     </div>
                                 </div>
                             </div>
                             </@s.if>
                            <div class="form-row">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.rate"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="rate" value="%{trigger.repeatInterval/60000}" id="rate"/>(分钟)
                                    </div>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.times"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="times" value="%{trigger.repeatCount}" id="times"/>
                                    </div>
                                </div>
                            </div>
                        </span>
                        <span id="trigger-cron">
                            <@s.if test="@com.fantasy.framework.util.common.StringUtil@isNotBlank(trigger.cronExpression)">
                            <div class="form-row" id="task-cron">
                                 <div class="form-label col-md-3">
                                     <label for="">
                                         <@s.text name="schedule.edit.cronExpression"/>
                                     </label>
                                 </div>
                                 <div class="form-input col-md-9">
                                     <div>
                                         <@s.property value="%{trigger.cronExpression}" />
                                     </div>
                                 </div>
                            </div>
                            </@s.if>
                            <div class="form-row" id="task-cron">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.secondField"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="secondField" value="%{@com.fantasy.schedule.service.ScheduleService@cron(trigger.cronExpression,0)}" />
                                    </div>
                                </div>
                            </div>
                            <div class="form-row" id="task-cron">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.minutesField"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="minutesField" value="%{@com.fantasy.schedule.service.ScheduleService@cron(trigger.cronExpression,1)}" />
                                    </div>
                                </div>
                            </div>
                            <div class="form-row" id="task-cron">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.hourField"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="hourField" value="%{@com.fantasy.schedule.service.ScheduleService@cron(trigger.cronExpression,2)}" />
                                    </div>
                                </div>
                            </div>
                            <div class="form-row" id="task-cron">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.dayField"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="dayField" value="%{@com.fantasy.schedule.service.ScheduleService@cron(trigger.cronExpression,3)}" />
                                    </div>
                                </div>
                            </div>
                            <div class="form-row" id="task-cron">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.monthField"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="monthField" value="%{@com.fantasy.schedule.service.ScheduleService@cron(trigger.cronExpression,4)}" />
                                    </div>
                                </div>
                            </div>
                            <div class="form-row" id="task-cron">
                                <div class="form-label col-md-3">
                                    <label for="">
                                        <@s.text name="schedule.edit.weekField"/>
                                    </label>
                                </div>
                                <div class="form-input col-md-9">
                                    <div>
                                        <@s.textfield name="weekField" value="%{@com.fantasy.schedule.service.ScheduleService@cron(trigger.cronExpression,5)}" />
                                    </div>
                                </div>
                            </div>
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-row" style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;padding-left: 27px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title="<@s.text name="schedule.button.save"/>"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                    <@s.text name="schedule.button.save"/>
                             </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title="<@s.text name="schedule.button.return"/>" >
                            <span class="glyph-icon icon-separator">
                                  <i class="glyph-icon icon-reply"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="schedule.button.return"/>
                             </span>
                    </a>
                </div>
            </div>
        </div>
    </@s.form>
    </div>
</div>
