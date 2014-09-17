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
    <input type="hidden" name="id" value="${linkman.id}"/>
    <input type="hidden" name="book.id" value="${linkman.book.id}"/>
    <table class="formTable">
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">姓名 :</td>
            <td class="formItem_content"><@s.textfield name="name" value="linkman.name" cssClass="w" /></td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title w100">性别 :</td>
            <td class="formItem_content w">
                <@s.radio name="sex" value="linkman.sex" list="@com.fantasy.security.bean.enums.Sex@values()" listValue="value"/>
            </td>
            <td class="formItem_content" style="width: 120px;"></td>
        </tr>
        <tr>
            <td class="formItem_title ">电话 :</td>
            <td class="formItem_content"><@s.textfield name="mobile" value="linkman.mobile" cssClass="w" autocomplete="off"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">E-mail:</td>
            <td class="formItem_content"><@s.textfield name="email" value="linkman.email" cssClass="w" autocomplete="off"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">部门 :</td>
            <td class="formItem_content"><@s.textfield name="department" value="linkman.department" cssClass="w"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">职务:</td>
            <td class="formItem_content"><@s.textfield name="job" value="linkman.job" cssClass="w" autocomplete="off"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">所属联系人分组:</td>
            <td class="formItem_content">
                <@s.checkboxlist value="linkman.groupIds" name="groups.id" list="@com.fantasy.contacts.service.AddressBookService@getGroups()" listKey="id" listValue="name"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">备注:</td>
            <td class="formItem_content"><@s.textarea name="description" value="linkman.description" cssClass="w" cssStyle="height:80px;"/></td>
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