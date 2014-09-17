<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#linkman_view').view().on('add',function(data){
            this.target.find('.delete').click(function(){
                deleteLinkman([data.id]);
                return false;
            });
        })).setJSON(page);
        $('.batchDelete').click(function(){
            var ids=$(".checkBox_linkmanId:checked").val();
            deleteLinkman(ids);
            return false;
        });
        var deleteLinkman = function(ids){
            if(ids){
                var names = [];
                ids.each(function(){
                    var row = $('#linkman_view').view().find("id",this.toString());
                    names.push(row.data.name);
                });
                jQuery.dialog.confirm("确认删除用户["+names+"]？",function(){
                    $.post('${request.contextPath}/admin/contacts/deleteLinkman.do',{ids:ids},function(data){
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
<form id="searchForm" action="${request.contextPath}/admin/contacts/search.do" method="post">
    <table class="formTable mb3">
        <caption>联系人组查询条件</caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">联系人名称:</td>
            <td class="formItem_content"><input type="text" name="LIKES_name" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">公司:</td>
            <td class="formItem_content"><input type="text" name="LIKES_company" class="ui_input_text w input_w193"/></td>
            <td class="formItem_title w100">所属分组:</td>
            <td class="formItem_content"><@s.select name="EQL_groups.id" headerKey=" " headerValue="--全部--" list="@com.fantasy.contacts.service.AddressBookService@getGroups()" listKey="id" listValue="name"/></td>
        </tr>
        </tbody>
    </table>
</form>
<a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
<a class="ui_button add" href="${request.contextPath}/admin/contacts/linkman_add.do" ajax="{type:'dialog',otherSettings:{title:'添加联系人',width:450,height:420}}">添加</a>
<a class="ui_button batchDelete">批量删除</a>
<span id="linkman_check_info"></span>
<table id="linkman_view" class="formTable mb3 listTable">
    <caption>查询结果列表</caption>
    <thead>
    <tr>
        <th style="width: 30px;"><input checkAll=".checkBox_linkmanId" type="checkbox"  checktip="{message:'您选中了 {num} 条记录',tip:'#linkman_check_info'}"/></th>
        <th class="sort" orderBy="name" style="width: 200px;">姓名</th>
        <th class="sort" orderBy="sex" style="width: 50px;">性别</th>
        <th class="sort" orderBy="mobile">电话</th>
        <th class="sort" orderBy="email">E-mail</th>
        <th class="sort" orderBy="department">部门</th>
        <th class="sort" orderBy="job">职务</th>
        <th>所属分组</th>
        <th>备注</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" class="template" name="default" >
        <td><input class="checkBox_linkmanId" type="checkbox" value="{id}"/></td>
        <td>
            <span style="float:left;padding-left: 20px;">{name}</span>
            <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                <a class="delete" href="${request.contextPath}/admin/contacts/deleteLinkman.do?ids={id}">删除</a>/
                <a href="${request.contextPath}/admin/contacts/editlinkman.do?id={id}" ajax="{type:'dialog',otherSettings:{title:'编辑联系人:{name}',width:450,height:420}}">编辑</a>
            </div>
        </td>
        <td>{sex:sex()}</td>
        <td>{mobile}</td>
        <td>{email}</td>
        <td>{department}</td>
        <td>{job}</td>
        <td title="{groupNames}">{groupNames:ellipsis(20,'...')}</td>
        <td>{description:ellipsis(20,'...')}</td>
    </tr>
    <tr class="empty"><td class="norecord" colspan="9">暂无数据</td></tr>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="9">
            <div id="pager" class="paging digg"></div>
        </td>
    </tr>
    </tfoot>
</table>
</@override>
<@extends name="../base.ftl"/>