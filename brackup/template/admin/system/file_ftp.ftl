<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        $('#view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
                deleteMethod([data.id]);
                return false;
            });
        }).setJSONUrl('${request.contextPath}/admin/mall/goods/product_search.do').setJSON(Fantasy.decode('<@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(ftps)" escapeHtml="false"/>'));

        $('.edit').toEdit($('#allChecked'),{target:'closest(\'#page_list\')'});

        var deleteMethod = $('.batchDelete').batchExecute($("#productAllChecked"),$('#view').view(),'id','确认要删除产品[{name}]？',function(){
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
    <table class="formTable mb3">
        <caption>FTP站点查询</caption>
    </table>
    <div style="padding-top:2px;padding-bottom: 3px;">
        <a class="ui_button add" href="${request.contextPath}/admin/system/file/ftp_add.do" ajax="{type:'html',target:'closest(\'#page_list\')'}">添加</a>
        <a class="ui_button edit" href="${request.contextPath}/admin/system/file/ftp_edit.do?id={id}">编辑</a>
        <a class="ui_button batchDelete" href="${request.contextPath}/admin/system/file/ftp_delete.do">删除</a>
        <span id="check_info"></span>
    </div>
    <table id="view" class="formTable mb3 listTable">
        <caption>查询结果列表</caption>
        <thead>
        <tr>
            <th style="width: 30px;"><input id="allChecked" type="checkbox" checkAll=".checkId" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}" ></th>
            <th style="width:250px;" class="sort" orderBy="name">名称</th>
            <th style="width:250px;" class="sort" orderBy="code">站点地址</th>
            <th>描述</th>
        </tr>
        </thead>
        <tbody>
        <tr align="center" class="template" name="default">
            <td><input type="checkbox" class="checkId" value="{id}"/></td>
            <td>
                <span style="float:left;padding-left: 20px;"><a href="${request.contextPath}/admin/system/file/ftp_view.do?id={id}" ajax="{type:'html',target:'closest(\'#page_list\')'}">{name}</a></span>
                <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                    <a class="delete" href="${request.contextPath}/admin/system/file/ftp_delete.do?id={id}">删除</a>/
                    <a href="${request.contextPath}/admin/system/file/ftp_edit.do?id={id}" ajax="{type:'html',target:'closest(\'#page_list\')'}">编辑</a>
                </div>
            </td>
            <td>{hostname}:{port}</td>
            <td>{description}</td>
        </tr>
        <tr class="empty"><td class="norecord" colspan="4">暂无数据</td></tr>
        </tbody>
    </table>
</div>
</@override>
<@extends name="../base.ftl"/>