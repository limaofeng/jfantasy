<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($('#searchForm'),page.pageSize,$('#config_view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
                deleteMethod([data.configKey]);
                return false;
            });
        })).setJSON(page);
        //查询表单异步操作
        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'configKey','确认删除配置项[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
            Fantasy.config.reload();
        });
    });
</script>
</@override>
<@override name="container">
<form id="searchForm" action="${request.contextPath}/admin/system/config/search.do" method="post">
    <table class="formTable mb3">
        <caption>配置项查询条件</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">类别分类：</td>
            <td class="formItem_content">
                <@s.select name="EQS_type" list="@com.fantasy.system.service.ConfigService@types()" headerKey="" headerValue="--全部--" listKey="code" listValue="name" cssClass="w200"/>
            </td>
            <td class="formItem_title w100">上级分组编码：</td>
            <td class="formItem_content"><input type="text" name="EQS_parent.code" class="ui_input_text w input_w193"></td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content w210"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">配置编号：</td>
            <td class="formItem_content w210"><input type="text" name="EQS_code" class="ui_input_text w input_w193"></td>
            <td class="formItem_title w100">配置名称：</td>
            <td class="formItem_content w210"><input type="text" name="LIKES_name" class="ui_input_text w input_w193"></td>
            <td class="formItem_title w100"></td>
            <td class="formItem_content w210"></td>
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
<a class="ui_button" href="${request.contextPath}/admin/system/config/add.do" ajax="{type:'dialog',otherSettings:{title:'添加配置项',width:450,height:450}}">添加</a>
<a class="ui_button batchDelete" href="${request.contextPath}/admin/system/config/delete.do">批量删除</a>
<span id="config_check_info"></span>
<table id="config_view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input id="allChecked" checkAll=".configId" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#config_check_info'}" /></th>
        <th style="width:250px;" class="sort" orderBy="name">名称</th>
        <th style="width:250px;" class="sort" orderBy="code">编码</th>
        <th class="sort" orderBy="type" style="width:120px;">类别</th>
        <th style="width:230px;">父级配置项</th>
        <th style="width:70px;" class="sort" orderBy="sort">默认排序</th>
        <th>描述</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default">
        <td><input class="configId" type="checkbox" value="{configKey}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/system/config/delete.do?keys={type}:{code}">删除</a>/
                <a href="${request.contextPath}/admin/system/config/edit.do?key={type}:{code}" ajax="{type:'dialog',otherSettings:{title:'编辑配置项:{name}',width:450,height:450}}">编辑</a>
            </div>
        </td>
        <td title="{code}">{code:ellipsis(20,'...')}</td>
        <td>{type:configTypeName()}</td>
        <td>{parentName}</td>
        <td>{sort}</td>
        <td>{description}</td>
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