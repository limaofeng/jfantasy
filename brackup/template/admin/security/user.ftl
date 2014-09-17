<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#user_view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
                deleteMethod([data.id]);
                return false;
            });
        })).setJSON(page);
        var deleteMethod = $('.batchDelete').batchExecute($(".checkBox_userId"),$('#pager').pager(),'id','确认删除用户[{username}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
    });
</script>
</@override>
<@override name="container">
<form id="searchForm" action="${request.contextPath}/admin/security/user_search.do" method="post">
    <table class="formTable mb3">
        <caption>用户查询条件</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">登陆名称:</td>
            <td class="formItem_content"><input type="text" name="filters.LIKES_username" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">真实姓名:</td>
            <td class="formItem_content"><input type="text" name="filters.LIKES_details.name" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">Email:</td>
            <td class="formItem_content" style="width:300px;"><input type="text" name="filters.LIKES_details.email" class="ui_input_text w input_w193"/></td>
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
<a class="ui_button add" href="${request.contextPath}/admin/security/user_add.do" ajax="{type:'html',target:'closest(\'#container\')'}">添加</a>
<a class="ui_button batchDelete">批量删除</a>
<span id="user_check_info"></span>
<table id="user_view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input checkAll=".checkBox_userId" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#user_check_info'}" /></th>
        <th class="sort" orderBy="username">登陆名称</th>
        <th class="sort" orderBy="details.name">真实姓名</th>
        <th class="sort" orderBy="details.email">Email</th>
        <th class="sort" orderBy="lastLoginTime">最后登录时间</th>
        <th>拥有角色</th>
        <th style="width:35%;">备注</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkBox_userId" type="checkbox" value="{id}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;">{username}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/security/user_delete.do?ids={id}">删除</a>/
                <a href="${request.contextPath}/admin/security/user_edit.do?id={id}" ajax="{type:'html',target:'closest(\'#container\')'}">编辑</a>
            </div>
        </td>
        <td>{details.name}</td>
        <td>{details.email}</td>
        <td>{lastLoginTime:format('yyyy-MM-dd HH:mm:ss')}</td>
        <td title="{roles}">{roles:ellipsis(30,'...')}</td>
        <td>{details.description}</td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="7">暂无数据</td></tr>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="7">
            <div id="pager" class="paging digg"></div>
        </td>
    </tr>
    </tfoot>
</table>
</@override>
<@extends name="../base.ftl"/>