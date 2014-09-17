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
<form id="saveForm" action="${request.contextPath}/admin/system/file/directory_update.do" method="post">
	<@s.hidden name="key" value="%{directory.key}"/>
    <table class="formTable">
    	<caption><a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
        	<td class="formItem_title w100">主键:</td>
            <td class="formItem_content"><@s.textfield name="key" value="%{directory.key}" cssClass="w240" disabled="true"/></td>
           	<td class="formItem_title w100">目录名称:</td>
            <td class="formItem_content"><@s.textfield name="name" value="%{directory.name}" cssClass="w250" /></td>
           
        </tr>
        <tr>
        	<td class="formItem_title w100">文件管理器:</td>
            <td class="formItem_content"> 
            	<@s.select name="fileManager.id" value="directory.fileManager.id" list="@com.fantasy.file.service.FileManagerFactory@getFileManagers()" listKey="id" headerKey="" headerValue="--请选择--" listValue="name" cssStyle="width:207px"/>
            </td>
       	 	<td class="formItem_title">对应的默认目录:</td>
            <td class="formItem_content" colspan="3"><@s.textfield name="dirPath" value="%{directory.dirPath}" cssClass="w250" /></td>
        </tr>
        </tbody>
    </table>
    
	<table id="saveFormButs" class="formTable mb3">
		<tr>
			<td class="formItem_title w100"></td>
			<td class="formItem_content"><a href="javascript:void(0);" onclick="$('#saveForm').submit();return false;" class="ui_button">保存</a></td>
		</tr>
	</table>
</form>