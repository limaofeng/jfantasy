<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
$('.saveForm').click(function(){
	$('#saveForm').submit();
	return false;
});
$("#saveForm").ajaxForm(function(data){
	$('#pager').pager().reload();
	top.$.msgbox({
		msg : "保存成功",
		icon : "success"
	});
	$(".back-page",$("#saveForm")).backpage();
});
</script>
<form id="saveForm" action="${request.contextPath}/admin/swp/template/save.do" method="post">
	<@s.hidden name="id" value="template.id"/>
    <table class="formTable mb3">
        <caption>模板添加<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">模板名称:</td>
            <td class="formItem_content">
                <@s.textfield name="name" value="%{template.name}"  />
            </td>
            <td class="formItem_title w100">模板类型:</td>
            <td class="formItem_content">
            	<@s.select name="type" list="@com.fantasy.swp.bean.Template$Type@values()" value="template.type" cssStyle="width: 135px;">
            	</@s.select>
            </td>
        </tr>
        <tr>
        	<td class="formItem_title">备注:</td>
        	<td rowspan="3"><@s.textarea name="description" value="%{template.description}" cssStyle="width:98%;" /></td>
        	<td class="formItem_title">模板文件存放的位置</td>
        	<td><@s.textfield name="filePath" value="%{template.filePath}"/></td>
        	
        </tr>
        
        
        </tbody>
    </table>
    <table class="formTable mb3">
        <tbody>
        <tr>
            <td class="formItem_content w100"></td>
            <td class="formItem_content"><a href="###" class="saveForm ui_button">保存</a></td>
        </tr>
        </tbody>
    </table>
</form>
