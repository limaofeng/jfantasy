<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
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
        
        $('.saveFormButs').click(function(){
        	var fileManagerSelect = $('#fileManagerSelect').val();
        	if(fileManagerSelect==null || fileManagerSelect==' '){
        		alert('文件管理器不能为空！');
        		return false;
        	}else{
	        	$('#saveForm').submit();
	        	return false;
        	}
        });
    });
</script>
<form id="saveForm" action="${request.contextPath}/admin/system/website/website_save.do" method="post">
    <table class="formTable">
    	<caption>添加站点<a href="###" class="back-page" style="float:right;padding-right:50px;">返回</a></caption>
        <tbody>
        <tr>
        	<td class="formItem_title w100">网站编码(唯一):</td>
            <td class="formItem_content"><input name="website.key" type="text" class="view-field w250" ><@s.fielderror fieldName="key"/></td>
            <td class="formItem_title w100">网站名称:</td>
            <td class="formItem_content"><input name="website.name" type="text" class="view-field w250"/></td>
        </tr>
        <tr>
       	 	<td class="formItem_title">网址:</td>
            <td class="formItem_content">
            	<input name="website.web" type="text" class="view-field w250"/>
            </td>
            <td class="formItem_title w100">文件管理器:</td>
            <td class="formItem_content">
            	<@s.select id="fileManagerSelect" name="website.defaultFileManager.id" headerKey=" " headerValue="--全部--" list="@com.fantasy.file.service.FileManagerFactory@getFileManagers()" listKey="id" listValue="name" style="width:150px;"/>
            	<a href="/website/admin/system/file/add.do" target="after:closest('.ajax-load-div')" style="padding-left:5px;">新增</a>
            </td>
        </tr>
        <tr>
            <td class="formItem_title w100">文件上传管理器:</td>
            <td class="formItem_content" colspan="3">
            	<@s.select id="fileManagerSelect" name="website.defaultUploadFileManager.id" headerKey=" " headerValue="--全部--" list="@com.fantasy.file.service.FileManagerFactory@getFileManagers()" listKey="id" listValue="name" style="width:150px;"/>
            	<a href="/website/admin/system/file/add.do" target="after:closest('.ajax-load-div')" style="padding-left:5px;">新增</a>
            </td>
        </tr>
        </tbody>
    </table>
	<table id="saveFormButs" class="formTable mb3">
		<tr>
			<td class="formItem_title w100"></td>
			<td class="formItem_content"><a class="ui_button saveFormButs">保存</a></td>
		</tr>
	</table>
</form>