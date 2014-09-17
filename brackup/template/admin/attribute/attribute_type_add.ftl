<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
            $('#pager').pager().reload();
            top.$.msgbox({
                msg : "保存成功",
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
<table class="formTable">
	<caption>添加自定义属性类型<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
    <tbody>
        <tr>
            <td class="formItem_title" style="width: 125px;">名称 :</td>
            <td class="formItem_content"><@s.textfield name="name" cssClass=" w"/></td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title" style="width: 125px;">对应的java类型 :</td>
            <td class="formItem_content"><@s.textfield name="dataType" cssClass=" w"/></td>
            <td class="formItem_content" style="width: 3px;"></td>
        </tr>
        <tr>
            <td class="formItem_title ">类型转换器 :</td>
            <td class="formItem_content"><@s.textfield name="converter" cssClass=" w"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title ">描述 :</td>
            <td class="formItem_content"><@s.textfield name="description" cssClass=" w" cssStyle="height: 150px;"/></td>
            <td class="formItem_content"></td>
        </tr>
        <tr>
            <td class="formItem_title"></td>
            <td class="formItem_content">
                <a class="ui_button saveFormBut" href="javascript:void(0);" onclick="return false;">保存</a>
            <td class="formItem_content"></td>
        </tr>
    </tbody>
</table>
</form>