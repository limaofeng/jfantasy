<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        
        $('#pager').pager($('#searchForm'),page.pageSize,$('#template_view').view().on("add",function(data){
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
<div id="template_list" style="margin: 3px;">
    <form id="searchForm" action="${request.contextPath}/admin/swp/template/search.do" method="post">
        <table class="formTable mb3">
            <caption>模板查询条件</caption>
            <tbody>
            <tr>
                <td class="formItem_title w100">名称：</td>
                <td class="formItem_content"><@s.textfield name="LIKES_name" cssClass="ui_input_text w200" /></td>
            </tr>
            </tbody>
        </table>
    </form>
    <a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
    <a class="ui_button" href="${request.contextPath}/admin/swp/template/add.do" ajax="{type:'html',target:'closest(\'#template_list\')'}">添加</a>
    <a class="ui_button batchDelete" href="${request.contextPath}/admin/swp/template/delete.do" >删除</a>
    <span id="template_check_info"></span>
    <table id="template_view" class="formTable mb3 listTable w600">
        <caption>查询结果列表</caption>
        <thead>
        <tr>
            <th style="width:30px;"><input id="allChecked" type="checkbox" checkAll=".select_msg"></th>
            <th class="sort" orderBy="id">模板名称</th>
            <th style="width:300px;" orderBy="type" class="sort">模板类型</th>
             <th style="width:300px;" orderBy="filePath" class="sort">模板存放位置</th>
        </tr>
        </thead>
        <tbody>
        <tr class="template" name="default" >
            <td><input type="checkbox" class="select_msg" name="select_msg" value="{id}"/></td>
            <td>
                <a href="${request.contextPath}/admin/swp/template/view.do?id={id}" ajax="{type:'html',target:'closest(\'#template_list\')'}">{name}</a>
                <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                    <a class="delete" href="${request.contextPath}/admin/swp/template/delete.do">删除</a>/
                    <a href="${request.contextPath}/admin/swp/template/edit.do?id={id}" ajax="{type:'html',target:'closest(\'#template_list\')'}">修改</a>
                </div>
            </td>
            <td>{type}</td>
            <td>{filePath}</td>
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