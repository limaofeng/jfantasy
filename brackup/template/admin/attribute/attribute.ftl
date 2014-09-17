<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
             	console.log(data.id);
                deleteMethod([data.id]);
                return false;
            });
        })).setJSON(page);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'id','确认删除店铺[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
    });
</script>
</@override>
<@override name="container">
<div id="page_list">
<form id="searchForm" action="${request.contextPath}/admin/attr/goodsAttribute_search.do" method="post">
    <table class="formTable mb3">
        <caption>查询条件</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">属性名称:</td>
            <td class="formItem_content"><input type="text" name="LIKES_name" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">属性编码:</td>
            <td class="formItem_content"><input type="text" name="LIKES_code" class="ui_input_text w input_w193"/></td>
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
<a class="ui_button add" href="${request.contextPath}/admin/attr/goodsAttribute_add.do" ajax="{type:'html',target:'closest(\'#page_list\')'}">添加</a>
<a class="ui_button batchDelete" href="${request.contextPath}/admin/attr/goodsAttribute_delete.do">批量删除</a>
<span id="check_info"></span>
<table id="view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input id="allChecked" checkAll=".checkall" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}" /></th>
        <th class="sort" orderBy="name">名称</th>
        <th class="sort" orderBy="attributeType.name">属性类型</th>
        <th>描述</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkall" type="checkbox" value="{id}"/></td>
        <td>
            <span style="float:left;padding-left: 50px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/attr/goodsAttribute_delete.do?ids={id}">删除</a>/
                <a href="${request.contextPath}/admin/attr/goodsAttribute_edit.do?id={id}" ajax="{type:'html',target:'closest(\'#page_list\')'}">编辑</a>
            </div>
        </td>
        <td>{attributeType.name}</td>
        <td>{description}</td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="4">暂无数据</td></tr>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="4">
            <div id="pager" class="paging digg"></div>
        </td>
    </tr>
    </tfoot>
</table>
</div>
</@override>
<@extends name="../base.ftl"/>