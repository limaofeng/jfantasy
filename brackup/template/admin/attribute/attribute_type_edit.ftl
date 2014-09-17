<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "修改成功",
                icon : "success"
            });
            $('.back-page').click();
        });
        $('.saveFormBut').click(function(e){
	        $('#saveForm').submit();
	        return stopDefault(e);
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/attribute/type/save.do" method="post">
	<@s.hidden name="attributeType.id"/>
    <table class="formTable">
    	<caption>编辑自定义属性类型<a href="#" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
	        <tr>
	            <td class="formItem_title" style="width: 125px;">类型名称 :</td>
	            <td class="formItem_content"><@s.textfield name="attributeType.name" cssClass=" w"/></td>
	            <td class="formItem_content" style="width: 3px;"></td>
	        </tr>
	        <tr>
	            <td class="formItem_title" style="width: 125px;">对应的java类型 :</td>
	            <td class="formItem_content"><@s.textfield name="attributeType.dataType" cssClass=" w"/></td>
	            <td class="formItem_content" style="width: 3px;"></td>
	        </tr>
	        <tr>
	            <td class="formItem_title ">类型转换器 :</td>
	            <td class="formItem_content"><@s.textfield name="attributeType.typeConverter" cssClass=" w"/></td>
	            <td class="formItem_content"></td>
	        </tr>
	        <tr>
	            <td class="formItem_title ">描述 :</td>
	            <td class="formItem_content"><@s.textarea name="attributeType.description" style="height: 150px;" cssClass=" w"/></td>
	            <td class="formItem_content"></td>
	        </tr>
	        <tr>
	            <td class="formItem_title"></td>
	            <td class="formItem_content">
	                <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
	                <a class="ui_button close" href="javascript:$.dialog.close();" style="margin-left: 5px;">取消</a></td>
	            <td class="formItem_content"></td>
	        </tr>
        </tbody>
    </table>
</form>