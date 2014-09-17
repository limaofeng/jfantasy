<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<script type="text/javascript">
    $(function(){
        $("#saveForm").ajaxForm(function(data){
        	if(data.type=="url"){
        		$('#pager').pager().reload();
	            $(".back-page",$("#saveForm")).backpage();
        	}else{
        		data.isParent = true;
				resourceTree.addNodes(resourceTree.getSelectedNodes()[0], data);
				var node = resourceTree.getNodeByParam("id", data.id);
				$(".back-page").backpage();
				$('#'+node.tId + '_span').click();
        	}
        	top.$.msgbox({
				msg : "保存成功",
				icon : "success"
			});
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/security/resource/save.do" method="post">
	<@s.hidden name="parentResources.id" value="#parameters.id"/>
    <table class="formTable">
		<caption>资源添加<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
            <td class="formItem_title w100">名称 :</td>
            <td class="formItem_content"><@s.textfield name="name"/></td>
            <td class="formItem_title w100">类型 :</td>
            <td class="formItem_content">
            	<@s.select cssClass="w200" name="type" value="%{#parameters.type[0]}" list="@com.fantasy.security.bean.enums.ResourceType@values()" listValue="value"/>
            </td>
        </tr>
        <tr>
            <td class="formItem_title ">值:</td>
            <td class="formItem_content" colspan="3">
                <@s.textfield name="value" cssStyle="width:98%;" />
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100">是否启用 :</td>
            <td class="formItem_content" colspan="3">
            	<@s.radio name="enabled" value="true" list=r"#{true:'是',false:'否'}" />
            </td>
        </tr>
        <tr>
            <td class="formItem_title ">描述 :</td>
            <td class="formItem_content" colspan="3">
            	<@s.textarea name="description" cssStyle="width:98%;" />
            </td>
        </tr>
      </tbody>
    </table>
    <table class="formTable mb3">
        <tbody>
	        <tr>
	            <td class="formItem_title w100"> </td>
	            <td class="formItem_content">
	                <a class="ui_button" href="javascript:void(0);" onclick="$('#saveForm').submit();return false;">保存</a>
	            </td>
	        </tr>
        </tbody>
    </table>
</form>