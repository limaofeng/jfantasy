<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
             $('.back-page').backpage();
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/security/user_save.do" method="post">
    <table class="formTable">
    <caption>用户添加<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">登陆用户 :</td>
            <td class="formItem_content"><input name="username" type="text" value="" class=" w" autocomplete="off"/></td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">登陆密码 :</td>
            <td class="formItem_content w"><input name="password" type="password" value="" class=" w" /></td>
            <td class="formItem_content" style="width: 120px;"></td>
        </tr>
        <tr>
            <td class="formItem_title ">显示名称 :</td>
            <td class="formItem_content"><input name="details.name" type="text" value="" class=" w"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">Email:</td>
            <td class="formItem_content"><input name="details.email" type="text" value="" class=" w"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">选择角色:</td>
            <td class="formItem_content">
                <@s.checkboxlist name="roles.code" list="@com.fantasy.security.service.RoleService@list()" listKey="code" listValue="name"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">备注 :</td>
            <td class="formItem_content"><textarea name="details.description" style="height: 150px;" class="w" rows="" cols=""></textarea></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
            <td class="formItem_content"></td>
        </tr>
        </tbody>
    </table>
</form>