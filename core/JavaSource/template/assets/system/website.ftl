<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="system.website.title"/>
<small>
    <@s.text name="system.website.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var list=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(list)" escapeHtml="false"/>;
        var websiteDetails = $('#website-dialog').form();
        var websitelist = $("#websitelist").view({url:'<@s.url namespace="/system/website" action="search"/>'}).on('add',function(data){
            $('.edit',this.target).click(function(e){
                openDialog(data);
            });

            $('.delete',this.target).click(function(e){
                var href = $(this).attr('href');
                jQuery.dialog.confirm("<@s.text name="system.website.delete.alert"/>", function () {
                    $.post(href,{},function(data){
                        websitelist.reload();
                        $.msgbox({
                            msg : "<@s.text name="system.website.delete.success"/>",
                            type : "success"
                        });
                    });
                });
                return stopDefault(e);
            });
        }).setJSON(list);
        $("#add").click(function(){
            openDialog({});
        });
        var openDialog = function(json){
            websiteDetails.update(json);
            $( "#website-dialog" ).dialog({
                modal: !0,
                dialogClass: "modal-dialog",
                overlayClass: "bg-white opacity-60",
                width:722,
                height:532,
                buttons: [
                    {
                        text: "<@s.text name="system.website.button.save"/>",
                        click: function() {
                            var _$dialog = $(this);
                            $.post('<@s.url namespace="/system/website" action="save"/>',websiteDetails.getData(),function(){
                                websitelist.reload();
                                $.msgbox({
                                    msg : " <@s.text name="system.website.save.success"/>",
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
            <a title="<@s.text name="system.website.button.add"/>" class="btn medium primary-bg dd-add" href="javascript:void(0)" id="add">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="system.website.button.add"/>
            </span>
            </a>
        </div>
        <div class="row" id="websitelist">
                <div class="col-md-4 template" name="default">
                    <div class="content-box">
                        <h3 class="content-box-header ui-state-default">
                            <span class="float-left">{name}</span>
                        </h3>
                        <div class="content-box-wrapper" style="height: 80px;">
                            <div>{defaultFileManager.id}</div>
                            <div>{web}</div>
                            <div>
                                <a href="<@s.url namespace="/system/website" action="delete?ids={id}"/>" class="font-red delete" style="float: right;" title="<@s.text name="system.website.button.delete"/>"> <i class="glyph-icon icon-remove mrg5R"></i></a>
                                <a href="<@s.url namespace="/system/website/setting" action="index?EQL_website.id={id}"/>" style="float: right;padding-right: 10px;" title="<@s.text name="system.website.button.view"/>" target="after:closest('#page-content')"> <i class="glyph-icon icon-list-ul"></i></a>
                                <a href="javascript:void(0);" class="edit" style="float: right;padding-right: 10px;" title="<@s.text name="system.website.button.edit"/>"><i class="glyph-icon icon-edit mrg5R"></i></a>
                            </div>

                        </div>
                    </div>
                </div>
        </div>
        <#include "website_dialog.ftl">
    </div>
</div>
</@override>
<@extends name="../wrapper.ftl"/>