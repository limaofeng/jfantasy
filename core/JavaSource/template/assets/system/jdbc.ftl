<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="pageTitle">
    <@s.text name="common.jdbc.title" />
<small>
    <@s.text name="common.jdbc.description"/>
</small>
</@override>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var list=<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(list)" escapeHtml="false"/>;
        var portMap = {mySql:'3306',oracle:'1521',sqlServer:'1433',db2:'50000',jndi:'',dataSource:''};
        var jdbcDetails = $('#jdbc-dialog').form().on('change',function(data,template,row){
            row.get('type').target.bind('change',function(){
                row.setValue('port',portMap[$(this).val()]);
            }).change();
        });
        var jdbclist = $("#jdbclist").view({url:'<@s.url namespace="/system/jdbc" action="search"/>'}).on('add',function(data){
            $('.edit',this.target).click(function(e){
                openDialog(data);
            });
            $('.delete',this.target).click(function(e){
                var href = $(this).attr('href');
                jQuery.dialog.confirm("<@s.text name="common.jdbc.delete.alert" />", function () {
                    $.post(href,{},function(data){
                        jdbclist.reload();
                        $.msgbox({
                            msg : " <@s.text name="common.jdbc.delete.success" />",
                            type : "success"
                        });
                    });
                });
                return stopDefault(e);
            });
        }).setJSON(list);
        $("#add").click(function(){
            openDialog({'type':'mySql'});
        });
        var openDialog = function(json){
            jdbcDetails.update(json);
            $( "#jdbc-dialog" ).dialog({
                modal: !0,
                dialogClass: "modal-dialog",
                overlayClass: "bg-white opacity-60",
                width:722,
                buttons: [
                    {
                        text: "<@s.text name="common.jdbc.button.save" />",
                        click: function() {
                            var _$dialog = $(this);
                            $.post('<@s.url namespace="/system/jdbc" action="save"/>',jdbcDetails.getData(),function(){
                                jdbclist.reload();
                                $.msgbox({
                                    msg : " <@s.text name="common.jdbc.save.success" />",
                                    type : "success"
                                });
                                _$dialog.dialog( "close" );
                            });
                        }
                    },
                    {
                        text: "<@s.text name="common.jdbc.button.test" />",
                        click: function() {
                            $.post('${request.contextPath}/system/jdbc/test.do',jdbcDetails.getData(),function(data){
                                if(data['isConnection']){
                                    $.msgbox({
                                        msg : "<@s.text name="common.jdbc.test.success" />",
                                        type : "success"
                                    });
                                }else{
                                    $.msgbox({
                                        msg : "<@s.text name="common.jdbc.test.error" />",
                                        type : "success"
                                    });
                                }
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
            <a title="<@s.text name="common.jdbc.button.add" />" class="btn medium primary-bg dd-add" href="javascript:void(0)" id="add">
            <span class="button-content">
                <i class="glyph-icon icon-plus float:left"></i>
                <@s.text name="common.jdbc.button.add" />
            </span>
            </a>
        </div>
        <div class="row" id="jdbclist">
            <a href="javascript:void(0)">
                <div class="col-md-4 template" name="default">
                    <div class="content-box">
                        <h3 class="content-box-header ui-state-default">
                            <span class="float-left">{name}</span>
                        </h3>
                        <div class="content-box-wrapper" style="height: 70px;">
                            <div style="float: left;width:180px;">{username}</div>
                            <div style="float: left;width: 150px;">{database}</div>
                            <div>{hostname}:{port}

                                <a href="<@s.url namespace="/system/jdbc" action="delete?ids={id}"/>" class="font-red delete" style="float: right;padding-right: 10px;" title="<@s.text name="common.jdbc.button.delete" />"> <i class="glyph-icon icon-remove mrg5R"></i></a>
                                <a href="<@s.url namespace="/system/tools" action="plsql"/>" title="<@s.text name="common.jdbc.button.link" />" class="jdbc" style="float: right;padding-right: 10px;" target="after:closest('#page-content')"><i class=" glyph-icon icon-wrench"></i></a>
                                <a href="javascript:void(0);" class="edit" style="float: right;padding-right: 10px;" title="<@s.text name="common.jdbc.button.edit" />"><i class="glyph-icon icon-edit mrg5R"></i></a>
                            </div>

                        </div>
                    </div>
                </div>
            </a>
        </div>
        <#include "jdbc_edit.ftl">
    </div>
</div>
</@override>
<@extends name="../wrapper.ftl"/>