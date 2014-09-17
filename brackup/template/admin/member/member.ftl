<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        //列表初始化
        var page = <@s.property value="@com.fantasy.framework.util.jackson.JSON@serialize(pager)" escapeHtml="false"/>;
        $('#pager').pager($("#searchForm"),page.pageSize,$('#member_view').view().on('add',function(data){
            this.target.find('.view').ajax({type:'html',target:'closest(\'#member_list\')',callback:function(type, target){target.initialize();}});
            this.target.find('.edit').ajax({type:'html',target:'closest(\'#member_list\')',callback:function(type, target){target.initialize();}});
            this.target.find('.delete').click(function(){
                deleteMethod([data.id]);
                return false;
            });
        })).setJSON(page);

        //添加产品
        $('.member-add').data('href',$('.member-add').attr('href')).ajax({type:'html',target:'closest(\'#member_list\')',callback:function(type, target){target.initialize();}});

        //参数1:jqueyr匹配所有可能删除行
        //参数2:翻页pager对象
        //参数3:bean主键名称
        //参数4:删除时弹出提示{username}希望弹出提示显示的字段，一般通过id删除，但提示语是用户名
        //参数5:删除成功后的回调方法
        //return 一个方法，该方法可以提供手动删除时使用注意第40行使用了
        var deleteMethod = $('.batchDelete').batchExecute($("#allCheckBox"),$('#pager').pager(),'id','确认删除用户[{username}]？',function(){
            top.$.msgbox({
                msg : "删除成功!",
                icon : "success"
            });
        });
    });
</script>
</@override>
<@override name="container">
<div id="member_list" style="margin: 3px;">
    <form id="searchForm" action="${request.contextPath}/admin/member/search.do" method="post">
        <table class="formTable mb3">
            <caption>用户查询条件</caption>
            <tbody>
            <tr>
                <td class="formItem_title w100">用户登录名称:</td>
                <td class="formItem_content"><@s.textfield name="LIKES_username" cssClass="w100" /></td>
                <td class="formItem_title w100">用户显示昵称:</td>
                <td class="formItem_content"><@s.textfield name="LIKES_nickName" cssClass="w100"/></td>
                <td class="formItem_title w100">是否vip:</td>
                <td class="formItem_content">
                    <@s.select name="EQB_details.vip" list=r"#{true:'是',false:'否' }" headerKey="" headerValue="--全部--" cssClass="w100"/>
                </td>
            </tr>
            <tr>
                <td class="formItem_title">性别:</td>
                <td class="formItem_content">
                    <@s.select name="EQE_details.sex" list=r"#{'male':'男','female':'女' }" headerKey="" headerValue="--全部--" cssClass="w100"/>
                </td>
                <td class="formItem_title">积分:</td>
                <td class="formItem_content">
                    <@s.textfield cssStyle="width:50px;" name="GEI_details.score"/>至<@s.textfield cssStyle="width:50px;" name="LEI_details.score"/>
                </td>
                <td class="formItem_title">是否启用:</td>
                <td class="formItem_content">
                    <@s.select name="EQB_enabled" list=r"#{true:'是',false:'否' }" headerKey="" headerValue="--全部--" cssClass="w100"/>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
    <a class="ui_button" onclick="$('#searchForm').submit()">查询</a>
    <!-- ajax="{type:'dialog',otherSettings:{title:'添加用户',width:450,height:600}}" -->
    <a class="ui_button member-add add" href="${request.contextPath}/admin/member/add.do" >添加</a>
    <a class="ui_button batchDelete" href="${request.contextPath}/admin/member/delete.do">批量禁用</a>
    <span id="check_info"></span>
    <table id="member_view" class="formTable mb3 listTable">
        <caption>查询结果列表</caption>
        <thead>
        <tr>
            <th style="width:30px;"><input id="allCheckBox" checkAll=".checkBoxAll" type="checkbox" checktip="{message:'您选中了{num}条记录',tip:'#check_info'}" /></th>
            <th style="width:250px;" class="sort" orderBy="username">用户登录名称</th>
            <th class="sort" orderBy="nickName">用户显示昵称</th>
            <th>vip</th>
            <th>积分</th>
            <th>性别</th>
            <th>email</th>
            <th>是否启用</th>
        </tr>
        </thead>
        <tbody>
        <tr align="center" class="template" name="default" >
            <td><input class="checkBoxAll" type="checkbox" value="{id}"/></td>
            <td>
                <a style="float:left;padding-left: 20px;" class="view" href="${request.contextPath}/admin/member/view.do?id={id}" >{username}</a>
                <div style="float:right;padding-right:5px;" toggle="closest('tr')">
                    <a class="delete" href="###">删除</a>/
                    <a class="edit" href="${request.contextPath}/admin/member/edit.do?id={id}">编辑</a>
                </div>
            </td>
            <td>{nickName}</td>
            <td>{details.vip:dict({'true':'是','false':'否'})}</td>
            <td>{details.score}</td>
            <td>{details.sex:dict({'male':'男','female':'女'})}</td>
            <td>{details.email}</td>
            <td>{enabled:dict({'true':'是','false':'否'})}</td>
        </tr>
        <tr class="empty"><td class="norecord" colspan="8">暂无数据</td></tr>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="8">
                <div id="pager" class="paging digg"></div>
            </td>
        </tr>
        </tfoot>
    </table>
</div>
</@override>
<@extends name="../base.ftl"/>