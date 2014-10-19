<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
短信配置
<small>
    URL
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var list=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(list)" escapeHtml="false"/>;
        var configDetails = $('#config-dialog').form();
        console.log(configDetails);
        var configlist = $("#configlist").view({url:'<@s.url namespace="/system/captchaConfig" action="search"/>'}).on('add',function(data){
            $('.edit',this.target).click(function(e){
                openDialog(data);
            });

            $('.delete',this.target).click(function(e){
                var href = $(this).attr('href');
                jQuery.dialog.confirm("是否删除该短信模版配置?", function () {
                    $.post(href,{},function(data){
                        configlist.reload();
                        $.msgbox({
                            msg : " 删除成功",
                            type : "success"
                        });
                    });
                });
                return stopDefault(e);
            });
        }).setJSON(list);
        $("#add").click(function(){
            openDialog({'randomWord':'0123456789','wordLength':'6','expires':'86400000','active':'300000','retry':'3'});
        });
        var openDialog = function(json){
            configDetails.update(json);
            $( "#config-dialog" ).dialog({
                modal: !0,
                dialogClass: "modal-dialog",
                overlayClass: "bg-white opacity-60",
                width:722,
                height:532,
                buttons: [
                    {
                        text: "保存",
                        click: function() {
                            var _$dialog = $(this);
                            $.post('<@s.url namespace="/system/captchaConfig" action="save"/>',configDetails.getData(),function(){
                                configlist.reload();
                                $.msgbox({
                                    msg : " 保存成功",
                                    type : "success"
                                });
                                _$dialog.dialog( "close" );
                            });
                        }
                    }
                ]
            });
        }

    });
</script>
</@override>
<@override name="pageContent">
<div class="example-box">
    <div class="example-code">
        <div id="searchFormPanel" class="button-panel pad5A">
            <a title="添加" class="btn medium primary-bg dd-add" href="javascript:void(0)" id="add">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                添加
            </span>
            </a>
        </div>
        <div class="row" id="configlist">
            <div class="col-md-4 template" name="default">
                <div class="content-box">
                    <h3 class="content-box-header ui-state-default">
                        <span class="float-left">{serverName}</span>
                    </h3>
                    <div class="content-box-wrapper" style="height: 80px;">
                        <div>{template}</div>
                        <div>
                            <a href="<@s.url namespace="/system/captchaConfig" action="delete?ids={id}"/>" class="font-red delete" style="float: right;" title="删除"> <i class="glyph-icon icon-remove mrg5R"></i></a>
                            <a href="<@s.url namespace="/system/captchaConfig" action="captcha_log?EQS_captchaConfig.id={id}"/>" style="float: right;padding-right: 10px;" title="验证码日志" target="after:closest('#page-content')"> <i class="glyph-icon icon-list-ul"></i></a>
                            <a href="<@s.url namespace="/system/captchaConfig" action="message_log?EQS_captchaConfig.id={id}"/>" style="float: right;padding-right: 10px;" title="消息日志" target="after:closest('#page-content')"> <i class="glyph-icon icon-list-ul"></i></a>
                            <a href="javascript:void(0);" class="edit" style="float: right;padding-right: 10px;" title="编辑"><i class="glyph-icon icon-edit mrg5R"></i></a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <#include "config_dialog.ftl">
    </div>
</div>
</@override>
<@extends name="../../wrapper.ftl"/>