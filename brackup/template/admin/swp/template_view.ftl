<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<table class="formTable mb3">
    <caption>模板详细
    	<a href="${request.contextPath}/admin/swp/page/face-index.do?EQL_template.id=${template.id}" target="after:closest('.ajax-load-div')" style="margin-left:50px;">模板数据</a>
    	<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a>
    </caption>
    <tbody>
    <tr>
        <td class="formItem_title w100">模板名称:</td>
        <td class="formItem_content"><@s.property value="template.name"/></td>
        <td class="formItem_title w100">模板类型:</td>
        <td class="formItem_content"><@s.property value="template.type.value"/></td>
        
    </tr>
    <tr>
    	<td class="formItem_title">备注:</td>
    	<td colspan="3"><@s.property value="template.description"/></td>
    </tr>
    </tbody>
</table>
