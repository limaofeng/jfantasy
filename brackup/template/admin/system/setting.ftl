<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<link rel="stylesheet" href="${request.contextPath}/static/css/pop.css"/>
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        var param = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(#parameters)" escapeHtml="false"/>;
        $('#pager-setting').pager($("#searchForm").resetForm(param),page.pageSize,$('#view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
             	console.log(data.id);
                deleteMethod([data.id]);
                return false;
            });
        })).setJSON(page).setPostData(param);
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager-setting').pager(),'id','确认删除店铺[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
        
        $('.searchForm').click(function(){
        	$('#searchForm').submit();
        	return false;
        });
    });
</script>
<div id="page_list">
<form id="searchForm" action="${request.contextPath}/admin/system/website/setting_search.do" method="post">
	<@s.hidden name="EQL_website.id"/>
    <table class="formTable mb3">
        <caption>查询条件<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">编码:</td>
            <td class="formItem_content"><input type="text" name="LIKES_key" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">名称:</td>
            <td class="formItem_content">
            	<input type="text" name="LIKES_name" class="ui_input_text w input_w193"/>
            </td>
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button searchForm">查询</a>
<a class="ui_button add" href="${request.contextPath}/admin/system/website/setting_add.do?websiteId=<@s.property value="#parameters['EQL_website.id']"/>" ajax="{type:'dialog',otherSettings:{title:'添加设置',width:450,height:400}}">添加</a>
<a class="ui_button batchDelete" href="${request.contextPath}/admin/system/website/setting_delete.do">批量删除</a>
<span id="user_check_info"></span>
<table id="view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input id="allChecked" checkAll=".checkall" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#user_check_info'}" /></th>
        <th class="sort" orderBy="name">名称</th>
        <th class="sort" orderBy="key">编码</th>
        <th>值</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkall" type="checkbox" value="{id}"/></td>
        <td>
            <span style="float:left;padding-left: 50px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a href="${request.contextPath}/admin/system/website/setting_edit.do?id={id}" ajax="{type:'dialog',otherSettings:{title:'添加设置',width:450,height:400}}">编辑</a>/
                <a class="delete" href="${request.contextPath}/admin/system/website/setting_delete.do?ids={id}">删除</a>
            </div>
        </td>
        <td>{key}</td>
        <td>{value}</td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="4">暂无数据</td></tr>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="4">
            <div id="pager-setting" class="paging digg"></div>
        </td>
    </tr>
    </tfoot>
</table>
</div>
