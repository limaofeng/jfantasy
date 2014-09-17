<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        
        $('#pager').pager($('#searchForm'),page.pageSize,$('#page_view').view().on("add",function(data){
            this.target.find('.delete').click(function(e){
                deleteMethod([data.id]);
                return stopDefault(e);
            });
        })).setJSON(page);
        
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'id','确认要删除[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });

    });
</script>
</@override>
<@override name="container">
<div id="page_list" style="margin: 3px;">
    <form id="searchForm" action="${request.contextPath}/admin/swp/page/search.do" method="post">
        <table class="formTable mb3">
            <caption>page查询条件</caption>
            <tbody>
            <tr>
                <td class="formItem_title w100">名称：</td>
                <td class="formItem_content"><@s.textfield name="LIKES_name" cssClass="ui_input_text w200" /></td>
            </tr>
            </tbody>
        </table>
    </form>
    <a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
    <a class="ui_button page-add" href="${request.contextPath}/admin/swp/page/add.do" ajax="{type:'html',target:'closest(\'#page_list\')'}">添加</a>
    <a class="ui_button batchDelete" href="${request.contextPath}/admin/swp/page/delete.do">删除</a>
    <span id="page_check_info"></span>
    <table id="page_view" class="formTable mb3 listTable w600">
        <caption>查询结果列表</caption>
        <thead>
        <tr>
            <th style="width:30px;"><input id="allChecked" type="checkbox" checkAll=".select_msg"></th>
            <th class="sort" orderBy="id" >page名称</th>
            <th style="width:300px;">page类型</th>
        </tr>
        </thead>
        <tbody>
        <tr align="center" class="template" name="default" >
            <td><input type="checkbox" class="select_msg" name="select_msg" value="{id}"/></td>
            <td>
                <a style="float:left;padding-left: 20px;" class="view" href="${request.contextPath}/admin/swp/page/view.do?id={id}" ajax="{type:'html',target:'closest(\'#page_list\')'}">{name}</a>
                <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                    <a class="delete" href="###">删除</a>/
                    <a class="edit" href="${request.contextPath}/admin/swp/page/edit.do?id={id}" ajax="{type:'html',target:'closest(\'#page_list\')'}">编辑</a>
                </div>
            </td>
            <td>{type}</td>
        </tr>
        <tr class="empty"><td class="norecord" colspan="3">暂无数据</td></tr>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="3">
                <div id="pager" class="paging digg"></div>
            </td>
        </tr>
        </tfoot>
    </table>
</div>
</@override>
<@extends name="../base.ftl"/>