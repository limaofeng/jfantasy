<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<table class="formTable mb3">
    <caption>静态页详细    
    	<a href="#" class="back-page" style="float:right;padding-right:50px;">返回</a>
    	<a href="${request.contextPath}/admin/swp/page/data-index.do?EQL_pages.id=${page.id}" target="after:closest('.ajax-load-div')" style="margin-left:50px;">page数据</a>
    </caption>
    <tbody>
        <tr>
            <td class="formItem_title w100">page名称：</td>
            <td class="formItem_content"><@s.property value="page.name"/></td>
            <td class="formItem_title w100">page类型：</td>
            <td class="formItem_content"><@s.property value="page.type.value"/></td>
        </tr>
        <tr>
            <td class="formItem_title w100">模板：</td>
            <td class="formItem_content" colspan="3">
            	<a href="${request.contextPath}/admin/swp/page/template-view.do?id=<@s.property value="page.template.id"/>" target="after:closest('.ajax-load-div')"><@s.property value="page.template.name"/></a>
            </td>
        </tr>
    </tbody>
</table>
