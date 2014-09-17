<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#role_view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
                deleteRole([data.code]);
                return false;
            });
        })).setJSON(page);
        $('.batchDelete').click(function(){
            var codes=$(".checkBox_roleId:checked").val();
            deleteRole(codes);
            return false;
        });
        var deleteRole = function(codes){
            if(codes){
                var rolenames = [];
                codes.each(function(){
                    var row = $('#role_view').view().find("code",this.toString());
                    rolenames.push(row.data.name);
                });
                jQuery.dialog.confirm("确认删除角色["+rolenames+"]？",function(){
                    $.post('${request.contextPath}/admin/security/role_delete.do',{ids:codes},function(data){
                        $('#pager').pager().reload();
                        top.$.msgbox({
                            msg : "删除成功!",
                            icon : "success",
                            callback:function(){
                            }
                        });
                    });
                });
            }else{
                top.$.msgbox({
                    msg : "您没有选中记录!",
                    icon : "warning",
                    callback:function(){
                    }
                });
            }
        };
    });
</script>
</@override>
<@override name="container">
<form id="searchForm" action="${request.contextPath}/admin/security/role_search.do" method="post">
    <table class="formTable mb3">
        <caption>用户查询条件</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">角色名称:</td>
            <td class="formItem_content"><input type="text" name="LIKES_name" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">角色编码:</td>
            <td class="formItem_content"><input type="text" name="LIKES_code" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content" style="width:300px;"></td>
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
<a class="ui_button add" href="${request.contextPath}/admin/security/role_add.do" ajax="{type:'html',target:'closest(\'#container\')'}">添加</a>
<span id="role_check_info"></span>
<a class="ui_button batchDelete" style="display: none;">批量删除</a>
<table id="role_view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input checkAll=".checkBox_roleId" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#role_check_info'}" /></th>
        <th class="sort" orderBy="name">角色名称</th>
        <th class="sort" orderBy="code">角色代码</th>
        <th class="sort" orderBy="enabled">是否启用</th>
        <th style="width:35%;">备注</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkBox_roleId" type="checkbox" value="{code}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/security/role_delete.do?ids={code}">删除</a>/
                <a href="${request.contextPath}/admin/security/role_edit.do?id={code}" ajax="{type:'html',target:'closest(\'#container\')'}">编辑</a>/
                <a href="${request.contextPath}/admin/security/role_resource_edit.do?id={code}" ajax="{type:'html',target:'closest(\'#container\')'}">授权</a>
            </div>
        </td>
        <td>{code}</td>
        <td>{enabled:dict({'true':'启用','false':'禁用'})}</td>
        <td>{description}</td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="5">暂无数据</td></tr>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="5">
            <div id="pager" class="paging digg"></div>
        </td>
    </tr>
    </tfoot>
</table>
</@override>
<@extends name="../base.ftl"/>