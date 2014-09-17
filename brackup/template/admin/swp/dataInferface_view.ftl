<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
    <table class="formTable mb3">
        <caption>数据详细<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
        	<td class="formItem_title w100">key</td>
        	<td class="formItem_content">
        		<@s.property value="face.key" cssClass="w300" />
        	</td>
        	<td class="formItem_title w100">code:</td>
            <td class="formItem_content">
            	<@s.property value="face.code" cssClass="w300" />
            </td>
        </tr>
        <tr>
            <td class="formItem_title">数据名称:</td>
            <td class="formItem_content" colspan="3"><@s.property value="face.name"/></td>
        </tr>
        <tr>
        	<td class="formItem_title">javaType:</td>
        	<td class="formItem_content"><@s.property value="face.dataType"/></td>
        	<td class="formItem_title">defaultValue</td>
        	<td class="formItem_content"><@s.property value="face.defaultValue"/></td>
        </tr>
        </tbody>
    </table>
