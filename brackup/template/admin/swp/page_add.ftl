<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
$("#saveForm").ajaxForm(function(data){
	$('#pager').pager().reload();
	top.$.msgbox({
		msg : "保存成功",
		icon : "success"
	});
	$(".back-page",$("#saveForm")).backpage();
});
</script>
<form id="saveForm" action="${request.contextPath}/admin/swp/page/save.do" method="post">
    <table class="formTable mb3">
        <caption>添加新的静态页<a href="#" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">page名称：</td>
            <td class="formItem_content">
                <@s.textfield name="name" cssClass="w300"/>
            </td>
            <td class="formItem_title w100">page类型：</td>
            <td class="formItem_content">
                <@s.select name="type" list="@com.fantasy.swp.bean.Page$Type@values()" listValue="value" cssStyle="width: 135px;"/>
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100">选择模板：</td>
            <td class="formItem_content" colspan="3">
            	<@s.select cssStyle="width:200px;" name="template.id" list="@com.fantasy.swp.service.TemplateService@templates()" listKey="id" listValue="name" />
            </td>
        </tr>
        </tbody>
    </table>
    <table class="formTable mb3">
        <tbody>
	        <tr>
	            <td class="formItem_content w100"></td>
	            <td class="formItem_content">
	            	<a href="###" onclick="$('#saveForm').submit();return false;" class="ui_button">保存</a>
	            </td>
	        </tr>
        </tbody>
    </table>
<form>
