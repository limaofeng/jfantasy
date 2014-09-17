<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
<@s.text name="file.ftp.title"/>
<small>
    <@s.text name="file.ftp.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var list=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(list)" escapeHtml="false"/>;
        var ftpDetails = $('#ftp-dialog').form();
        var ftplist = $("#ftplist").view({url:'<@s.url namespace="/system/ftp" action="search"/>'}).on('add',function(data){
            $('.edit',this.target).click(function(e){
                openDialog(data);
            });

            $('.delete',this.target).click(function(e){
                var href = $(this).attr('href');
                jQuery.dialog.confirm("<@s.text name="file.ftp.delete.alert"/>", function () {
                    $.post(href,{},function(data){
                        ftplist.reload();
                        $.msgbox({
                            msg : " <@s.text name="file.ftp.delete.success"/>",
                            type : "success"
                        });
                    });
                });
                return stopDefault(e);
            });
        }).setJSON(list);
        $("#add").click(function(){
            openDialog({port:"21",defaultDir:"/"});
        });
        var openDialog = function(json){
            console.log(json);
            ftpDetails.update(json);
            $( "#ftp-dialog" ).dialog({
                modal: !0,
                dialogClass: "modal-dialog",
                overlayClass: "bg-white opacity-60",
                width:722,
                buttons: [
                    {
                        text: "<@s.text name="file.ftp.button.save"/>",
                        click: function() {
                            var _$dialog = $(this);
                            $.post('<@s.url namespace="/system/ftp" action="save"/>',ftpDetails.getData(),function(){
                                ftplist.reload();
                                $.msgbox({
                                    msg : " <@s.text name="file.ftp.save.success"/>",
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
            <a title="<@s.text name="file.ftp.button.add"/>" class="btn medium primary-bg dd-add" href="javascript:void(0)" id="add">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="file.ftp.button.add"/>
            </span>
            </a>
        </div>
        <div class="row" id="ftplist">
            <div class="col-md-4 template" name="default">
                <div class="content-box">
                    <h3 class="content-box-header ui-state-default">
                        <span class="float-left">{name}</span>
                    </h3>
                    <div class="content-box-wrapper" style="height: 80px;">
                        <div>{username}</div>
                        <div>{hostname}:：{port}</div>
                        <div>
                            <a href="<@s.url namespace="/system/ftp" action="delete?ids={id}"/>" class="font-red delete" style="float: right;" title="<@s.text name="file.ftp.button.delete"/>"> <i class="glyph-icon icon-remove mrg5R"></i></a>
                            <a href="javascript:void(0);" class="edit" style="float: right;padding-right: 10px;" title="<@s.text name="file.ftp.button.edit"/>"><i class="glyph-icon icon-edit mrg5R"></i></a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <#include "ftp_dialog.ftl">
    </div>
</div>
</@override>
<@extends name="../wrapper.ftl"/>