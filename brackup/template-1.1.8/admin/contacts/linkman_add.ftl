<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var win = $.dialog.open.origin;
        $("#saveForm").ajaxForm(function(data){
            win.$('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success",
                callback:function(){
                }
            });
            setTimeout("$.dialog.close();",10);
        });
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/contacts/savelinkman.do" method="post">
<table class="formTable">
<tbody>
<tr>
    <td class="formItem_title" style="width: 125px;">姓名 :</td>
    <td class="formItem_content"><input name="name" type="text" value="" class=" w" /></td>
    <td class="formItem_content" style="width: 3px;"></td>
</tr>
<tr>
    <td class="formItem_title w100">性别 :</td>
    <td class="formItem_content w">
        <@s.radio name="sex" value="'male'" list="@com.fantasy.security.bean.enums.Sex@values()" listValue="value"/>
    </td>
    <td class="formItem_content" style="width: 120px;"></td>
</tr>
<tr>
    <td class="formItem_title ">电话 :</td>
    <td class="formItem_content"><input name="mobile" type="text" value="" class=" w"></td>
    <td class="formItem_content"></td>
</tr>
<tr>
    <td class="formItem_title ">E-mail:</td>
    <td class="formItem_content"><input name="email" type="text" value="" class=" w"></td>
    <td class="formItem_content"></td>
</tr>
<tr>
    <td class="formItem_title ">部门 :</td>
    <td class="formItem_content"><input name="department" type="text" value="" class=" w"></td>
    <td class="formItem_content"></td>
</tr>
<tr>
    <td class="formItem_title ">职务:</td>
    <td class="formItem_content"><input name="job" type="text" value="" class=" w"></td>
    <td class="formItem_content"></td>
</tr>
<tr>
    <td class="formItem_title ">所属联系人分组:</td>
    <td class="formItem_content">
        <@s.checkboxlist name="groups.id" list="@com.fantasy.contacts.service.AddressBookService@getGroups()" listKey="id" listValue="name"/>
    </td>
    <td class="formItem_content"></td>
</tr>
<tr>
    <td class="formItem_title ">备注:</td>
<td class="formItem_content"><@s.textarea name="description" cssClass="w" cssStyle="height:80px;"/></td>
    <td class="formItem_content"></td>
</tr>
<tr>
    <td class="formItem_title"></td>
    <td class="formItem_content">
        <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
        <a class="ui_button close" href="javascript:$.dialog.close();" style="margin-left: 5px;">取消</a></td>
    <td class="formItem_content"></td>
</tr>
</tbody>
</table>
</form>
</@override>
<@extends name="../base.ftl"/>