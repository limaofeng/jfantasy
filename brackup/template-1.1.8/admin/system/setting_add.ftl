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
    });
</script>
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/system/website/setting_save.do" method="post">
    <table class="formTable">
    	<@s.hidden name="website.id" value="%{#parameters['websiteId']}"/>
        <tbody>
        <tr>
            <td class="formItem_title" style="width: 100px;">编码:</td>
            <td class="formItem_content">
            	<input name="key" type="text" value="" class="w200" autocomplete="off"/>
            	<@s.fielderror fieldName="key"/>
            </td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
        	<td class="formItem_title ">值 :</td>
            <td class="formItem_content">
                <input name="value" type="text"  value="" class="w200" autocomplete="off"/>
            </td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">名称:</td>
            <td class="formItem_content"><input name="name" type="text" value="" class="w200"></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">描述 :</td>
            <td class="formItem_content"><textarea name="description" style="height: 150px;" class="w200" rows="" cols=""></textarea></td>
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
<@extends name="../dialog.ftl"/>