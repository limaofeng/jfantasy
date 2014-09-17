<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>

<@override name="head">
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#user_view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
                deleteMethod([data.key]);
                return false;
            });
        })).setJSON(page);
        //参数1:jqueyr匹配所有可能删除行
        //参数2:翻页pager对象
        //参数3:bean主键名称
        //参数4:删除时弹出提示{username}希望弹出提示显示的字段，一般通过id删除，但提示语是用户名
        //参数5:删除成功后的回调方法
        //return 一个方法，该方法可以提供手动删除时使用注意第40行使用了
        var deleteMethod = $('.batchDelete').batchExecute($("#checkBox"),$('#pager').pager(),'key','确认ID[{key}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
    
        
    });
</script>
</@override>
<@override name="container">
<div class="page_list">
<form id="searchForm" action="${request.contextPath}/admin/system/file/directory_search.do" method="post">
    <table class="formTable mb3">
        <caption>用户查询条件</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">ID:</td>
            <td class="formItem_content"><input type="text" name="filters.LIKES_key" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">目录名称:</td>
            <td class="formItem_content"><input type="text" name="filters.LIKES_name" class="ui_input_text w input_w193"/></td>

          
           
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
<a class="ui_button add" href="${request.contextPath}/admin/system/file/directory_add.do"  ajax="{type:'html',target:'closest(\'.page_list\')'}">添加</a>
<a href="${request.contextPath}/admin/system/file/directory_delete.do" class="ui_button batchDelete">批量删除</a>
<span id="user_check_info"></span>
<table id="user_view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input checkAll=".checkBox_userId" id="checkBox"  type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#user_check_info'}" /></th>
        <th class="sort" orderBy="key">主键</th>
         <th class="sort" orderBy="name">目录名称</th>
   		 <th class="sort" orderBy="dirPath">对应的默认目录</th>
   		 <th class="sort" orderBy="fileManager.name">对应的文件管理器名称</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkBox_userId" type="checkbox" value="{key}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;">{key}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="###">删除</a>
                <a href="${request.contextPath}/admin/system/file/directory_edit.do?id={key}"  ajax="{type:'html',target:'closest(\'.page_list\')'}"">编辑</a>
            </div>
        </td>
        <td>{name}</td>
        <td>{dirPath}</td>
        <td>{fileManager.name}</td>
        
       
       
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