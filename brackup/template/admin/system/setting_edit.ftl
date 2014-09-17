<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
        var win = $.dialog.open.origin;
        $("#saveForm").ajaxForm(function(data){
            win.$('#pager-setting').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
                icon : "success"
            });
            setTimeout("$.dialog.close();",10);
        });
        $('.saveForm').click(function(){
        	$('#saveForm').submit();
        	return false;
        });
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/system/website/setting_save.do" method="post">
    <table class="formTable">
    	<@s.hidden name="setting.id" value="%{setting.id}"/>
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">编码:</td>
            <td class="formItem_content">
            	<@s.textfield name="setting.key" diabled="true"/>
            	<@s.fielderror fieldName="key"/>
            </td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
        	<td class="formItem_title ">值 :</td>
            <td class="formItem_content">
            	<@s.textfield name="setting.value"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">名称:</td>
            <td class="formItem_content">
            	<@s.textfield name="setting.name"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">描述 :</td>
            <td class="formItem_content">
            	<@s.textfield name="setting.description"  style="height: 150px;"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button saveForm">保存</a>
                <a class="ui_button close" href="javascript:$.dialog.close();" style="margin-left: 5px;">取消</a></td>
            <td class="formItem_content"></td>
        </tr>
        </tbody>
    </table>
</form>
</@override>
<@extends name="../dialog.ftl"/>