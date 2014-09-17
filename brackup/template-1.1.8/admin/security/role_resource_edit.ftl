<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success",
            });
            $('.back-page').backpage();
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/security/role_save.do" method="post">
    <table class="formTable">
    	<caption>资源授权<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">角色名称 :</td>
            <td class="formItem_content">
            	<@s.textfield name="role.name" cssClass="w" disabled="true"/>
            </td>
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
            <td class="formItem_title ">授权 :</td>
            <td class="formItem_content">
            	<@s.checkboxlist name="role.resources.id" list="allResources" listKey="id" listValue="name" value="%{@com.fantasy.framework.util.common.ObjectUtil@toFieldArray(role.getResources(), 'id', @java.lang.Long@class)}"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
                <a href="###" class="ui_button back-page"  style="margin-left: 5px;">取消</a></td>
            <td class="formItem_content"></td>
        </tr>
        </tbody>
    </table>
</form>