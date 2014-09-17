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
<form id="saveForm" action="${request.contextPath}/admin/swp/page/data-save.do" method="post">
    <table class="formTable mb3">
    	<@s.hidden name="pages.id" value="#parameters['pageId']" />
        <caption>数据添加<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">数据类型:</td>
            <td class="formItem_content">
                <@s.select name="type" list="@com.fantasy.swp.bean.Data$Type@values()" listValue="value"  cssClass="ui_input_text w200" />
            </td>
            <td class="formItem_title w100">作用范围</td>
            <td class="formItem_content">
            	<@s.select name="scope" list="@com.fantasy.swp.bean.Data$Scope@values()" listValue="value"  cssClass="ui_input_text w200" />
            </td>
        </tr>
        <tr>
        	<td class="formItem_title">缓存时间:</td>
        	<td class="formItem_content" colspan="3" ><@s.textfield name="cacheInterval" cssClass="w300" /></td>
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
