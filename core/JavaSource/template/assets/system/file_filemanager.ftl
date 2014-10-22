<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="file.filemanager.title"/>
<small>
    <@s.text name="file.filemanager.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var list=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(list)" escapeHtml="false"/>;
        var fileDetails = $('#file-dialog').form().on('change',function(data,template,row){
            row.get('type').target.bind('change',function(){
                $('#ftp').hide();
                $('#jdbc').hide();
                $('#local').show();
                if($(this).val()=="ftp"){
                    $('#local').hide();
                    $('#ftp').show().find('select').removeAttr("disabled");
                };
                if($(this).val()=="jdbc"){
                    $('#local').hide();
                    $('#jdbc').show().find('select').removeAttr("disabled");
                };
                if($(this).val()=="local"){
                    $('#local').show().find('textfield').removeAttr("disabled");
                };
            }).change();
        });
        var filelist = $("#filelist").view({url:'<@s.url namespace="/system/filemanager" action="search"/>'}).on('add',function(data){
            if(data.type == 'local'){
                this.target.find('.typeInfo').html('普通存储方式');
                this.target.find('.storeInfo').html('文件地址:'+data.localDefaultDir);
            }
            if(data.type == 'virtual'){
                this.target.find('.typeInfo').html('默认存储方式');
            }
            $('.edit',this.target).click(function(e){
                openDialog(data);
            });

            $('.delete',this.target).click(function(e){
                var href = $(this).attr('href');
                jQuery.dialog.confirm("<@s.text name="file.filemanager.delete.alert"/>", function () {
                    $.post(href,{},function(){
                        filelist.reload();
                        $.msgbox({
                            msg : " <@s.text name="file.filemanager.delete.success"/>",
                            type : "success"
                        });
                    });
                });
                return stopDefault(e);
            });
        }).setJSON(list);
        $("#add").click(function(){
            openDialog({'type':'local'});
        });
        var openDialog = function(json){
            fileDetails.update(json);
            $( "#file-dialog" ).dialog({
                modal: !0,
                dialogClass: "modal-dialog",
                overlayClass: "bg-white opacity-60",
                width:722,
                buttons: [
                    {
                        text: "<@s.text name="file.filemanager.button.save"/>",
                        click: function() {
                            var _$dialog = $(this);
                            $.post('<@s.url namespace="/system/filemanager" action="save"/>',fileDetails.getData(),function(){
                                filelist.reload();
                                $.msgbox({
                                    msg : " <@s.text name="file.filemanager.save.success"/>",
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
            <a title="<@s.text name="file.filemanager.button.add"/>" class="btn medium primary-bg dd-add" href="javascript:void(0)" id="add">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="file.filemanager.button.add"/>
            </span>
            </a>
        </div>
        <div class="row" id="filelist">
            <a href="javascript:void(0)">
                <div class="col-md-4 template" name="default">
                    <div class="content-box">
                        <h3 class="content-box-header ui-state-default">
                            <span class="float-left">{id}（{name}）</span>
                        </h3>
                        <div class="content-box-wrapper" style="height: 80px;">
                            <div class="typeInfo">{type}</div>
                            <div class="storeInfo"></div>
                            <div>
                                <a href="<@s.url namespace="/system/filemanager" action="delete?ids={id}"/>" class="font-red delete" style="float: right;padding-right: 10px;" title="<@s.text name="file.filemanager.button.delete"/>"> <i class="glyph-icon icon-remove mrg5R"></i></a>
                                <a href="javascript:void(0);" class="edit" style="float: right;padding-right: 10px;" title="<@s.text name="file.filemanager.button.edit"/>"><i class="glyph-icon icon-edit mrg5R"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </a>
        </div>
        <#include "file_filemanager_add.ftl">
    </div>
</div>
</@override>
<@extends name="../wrapper.ftl"/>