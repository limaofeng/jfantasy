<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
	$('.saveForm').click(function(){
		$('#saveForm').submit();
		return false;
	});

	$("#saveForm").ajaxForm(function(data){
		top.$.msgbox({
			msg : "保存成功",
			icon : "success"
		});
		var $back = $(".back-page",$("#saveForm")).backpage();
		$('#pager',$back.closest('.ajax-load-div')).pager().reload();
	});
</script>
<form id="saveForm" action="${request.contextPath}/admin/swp/page/face-save.do" method="post">
	<@s.hidden name="id" value="face.id" />
	<@s.hidden name="template.id" value="face.template.id" />
    <table class="formTable mb3">
        <caption>数据修改<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
        	<td class="formItem_title w100">key</td>
        	<td class="formItem_content">
        		<@s.textfield name="key" value="face.key" cssClass="w300" />
        	</td>
        	<td class="formItem_title w100">code:</td>
            <td class="formItem_content">
            	<@s.textfield name="code" value="face.code" cssClass="w300" />
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100">数据名称:</td>
            <td class="formItem_content" colspan="3">
                <@s.textfield name="name" value="face.name" cssStyle="width:98%;" />
            </td>
        </tr>
        <tr>
        	<td class="formItem_title">javaType:</td>
        	<td class="formItem_content"><@s.textfield name="dataType" value="face.dataType" cssClass="w300" /></td>
        	<td class="formItem_title">defaultValue</td>
        	<td class="formItem_content"><@s.textfield name="defaultValue" value="face.defaultValue" cssClass="w300" /></td>
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
