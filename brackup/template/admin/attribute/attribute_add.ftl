<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<@override name="head">
<script type="text/javascript">
    $(function(){
    	var _interval = setInterval(function(){
			if($('#saveFormButs','body').length == 0){
				clearInterval(_interval);
				return;
			}
			var _top = document.documentElement.clientHeight-$('#saveFormButs').height()-1;
			$('#saveFormButs').css({top:'',position:''});
			var top = $('#saveFormButs').offset().top;
			if(_top < top){
				$('#saveFormButs').css({top:_top,position:'fixed'});
			}
		}, 200);
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
</@override>
<@override name="container">
<form id="saveForm" action="${request.contextPath}/admin/attr/goodsAttribute_save.do" method="post">
    <table class="formTable">
    	<caption>添加属性<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
        	<td class="formItem_title w100">编码(唯一):</td>
            <td class="formItem_content"><input name="attribute.code" type="text" value="" ><@s.fielderror fieldName="code"/></td>
            <td class="formItem_title w100">名称:</td>
            <td class="formItem_content"><input name="attribute.name" type="text" value=""  autocomplete="off"/></td>
        </tr>
        <tr>
        	<td class="formItem_title w100">类型:</td>
            <td class="formItem_content" colspan="3">
            	<@s.select id="attributeTypeSelect" name="attribute.attributeType.id" headerKey="" headerValue="--全部--" list="@com.fantasy.attr.service.AttributeTypeService@getAttributeType()" listKey="id" listValue="name" style="width:150px;"/>
            </td>
        </tr>
        <tr>
       	 	<td class="formItem_title">描述:</td>
            <td class="formItem_content" colspan="3"><textarea name="attribute.description" style="height: 150px;width:98%;"></textarea></td>
        </tr>
        </tbody>
    </table>
</form>
<table id="saveFormButs" class="formTable mb3">
	<tr>
		<td class="formItem_title w100"></td>
		<td class="formItem_content"><a href="javascript:void(0);" onclick="$('#saveForm').submit();return false;" class="ui_button">保存</a></td>
	</tr>
</table>
</@override>
<@extends name="../dialog.ftl"/>