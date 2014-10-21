<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){

        $("input[name='jobInfo.type']").change(function(){
            var type = $("input[name='jobInfo.type']:checked").val();
            if(type=="simple"){
                $(".task-simple").show();
                $("#task-cron").hide();
            }else if(type=="cron"){
                $(".task-simple").hide();
                $("#task-cron").show();
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

    <@s.form id="saveForm" namespace="/system/schedule" action="add_save" method="post" cssClass="center-margin">
            <div id="normal-tabs-1">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.group"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.textfield name="jobInfo.group" id="group"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.name"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.textfield name="jobInfo.name" id="name"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.jobClass"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.textfield name="jobInfo.jobClass" id="jobClass"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.type"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.radio name="jobInfo.type" list=r"#{'simple':'简单规则','cron':'cron表达式'}" value="'simple'"  cssClass="trigger-type"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row task-simple">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.rate"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.textfield name="jobInfo.rate" id="rate"/><@s.text name="schedule.add.minutes"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row task-simple">
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.times"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.textfield name="jobInfo.times" id="times"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-row" id="task-cron" style="display: none;" >
                            <div class="form-label col-md-3">
                                <label for="">
                                    <@s.text name="schedule.add.cronExpression"/>
                                </label>
                            </div>
                            <div class="form-input col-md-9">
                                <div>
                                    <@s.textfield name="jobInfo.cronExpression" id="cronExpression"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-row" style="text-align: center;">
            <div>
                <div style="float: left;padding-right: 50px;padding-left: 27px;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 menu-save4"  onclick="$('#saveForm').submit();return false;" title=" <@s.text name="schedule.button.save"/>"  >
                            <span class="glyph-icon icon-separator">
                                 <i class="glyph-icon icon-save"></i>
                            </span>
                             <span class="button-content">
                                 <@s.text name="schedule.button.save"/>
                             </span>
                    </a>
                </div>
                <div style="float: left;">
                    <a href="javascript:void(0);" class="btn medium primary-bg radius-all-4 switch menu-view back-page "  title=" <@s.text name="schedule.button.return"/>" >
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
