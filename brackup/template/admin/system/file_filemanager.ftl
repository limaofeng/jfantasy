<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#view').view().on('add',function(data){
            if(data.type == 'local'){
                this.target.find('.type').html('普通存储方式');
                this.target.find('.storeInfo').html('文件地址:'+data.localDefaultDir);
            }
            this.target.find('.delete').click(function(){
                deleteMethod([data.id]);
                return false;
            });
        })).setJSON(page);

        var deleteMethod = $('.batchDelete').batchExecute($("#allChecked"),$('#pager').pager(),'id','确认要删除产品[{name}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
    });
</script>
</@override>
<@override name="container">
<div id="fm_list" style="margin: 3px;">
    <form id="searchForm" action="${request.contextPath}/admin/system/file/search.do" method="post">
        <table class="formTable mb3">
            <caption>文件管理器</caption>
        </table>
        <div style="padding-top:2px;padding-bottom:3px;">
            <a class="ui_button add" href="${request.contextPath}/admin/system/file/add.do" ajax="{type:'html',target:'closest(\'#fm_list\')'}">添加</a>
            <a class="ui_button batchDelete" href="${request.contextPath}/admin/system/file/delete.do">批量删除</a>
            <span id="check_info"></span>
        </div>
        <table id="view" class="formTable mb3 listTable">
            <caption>查询结果列表</caption>
            <thead>
            <tr>
                <th style="width: 30px;"><input id="allChecked" type="checkbox" checkAll=".checkId" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}" ></th>
                <th style="width:250px;" class="sort" orderBy="name">名称</th>
                <th style="width:250px;" class="sort" orderBy="code">类型</th>
                <th>本地存储信息</th>
                <th>描述</th>
            </tr>
            </thead>
            <tbody>
            <tr align="center" class="template" name="default">
                <td><input type="checkbox" class="checkId" value="{id}"/></td>
                <td>
                    <a style="float:left;padding-left: 20px;" class="view" href="${request.contextPath}/admin/system/file/view.do?id={id}"  ajax="{type:'html',target:'closest(\'#fm_list\')'}" >{name}</a>
                    <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                        <a class="edit" href="${request.contextPath}/admin/system/file/edit.do?id={id}" ajax="{type:'html',target:'closest(\'#fm_list\')'}" >编辑</a>/
                        <a class="delete" href="${request.contextPath}/admin/system/file/delete.do?id={id}">删除</a>
                    </div>
                </td>
                <td class="type">{type}</td>
                <td class="storeInfo"></td>
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
    </form>
</div>
</@override>
<@extends name="../base.ftl"/>