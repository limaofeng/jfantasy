<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
    <table class="formTable mb3">
    	<@s.hidden name="page.id" value="#parameters['pageId']" />
        <caption>page数据查看<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">数据类型:</td>
            <td class="formItem_content"><@s.property value="data.type.value"/></td>
            <td class="formItem_title w100">作用范围</td>
            <td class="formItem_content"><@s.property value="data.scope.value"/></td>
        </tr>
        <tr>
        	<td class="formItem_title">缓存时间:</td>
        	<td class="formItem_content" colspan="3" ><@s.property value="data.cacheInterval"/></td>
        </tr>
        </tbody>
    </table>
