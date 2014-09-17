<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
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
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/security/role_save.do" method="post">
    <table class="formTable">
    <caption>角色编辑<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">角色名称 :</td>
            <td class="formItem_content"><@s.textfield name="role.name" cssClass="w" autocomplete="off"/></td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">角色代码 :</td>
            <td class="formItem_content w">
                <@s.textfield name="role.code" cssClass="w" disabled="true"/>
                <@s.hidden name="role.code"/>
            </td>
            <td class="formItem_content" style="width: 120px;"></td>
        </tr>
        <tr>
            <td class="formItem_title">是否启用:</td>
            <td class="formItem_content">
                <@s.radio name="role.enabled" list=r"#{true:'启用',false:'禁用'}" value="%{role.enabled}"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">备注 :</td>
            <td class="formItem_content"><@s.textarea name="role.description" cssStyle="height: 150px;" cssClass="w" rows="" cols=""/></td>
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
</@override>
<@extends name="../dialog.ftl"/>