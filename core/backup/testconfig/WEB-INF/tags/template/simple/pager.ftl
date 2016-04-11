<#assign s=JspTaglibs["/WEB-INF/tlds/struts-tags.tld"]/>
<#--<div class="about_page">
    <ul>
    	<@s.iterator value="@com.fantasy.framework.util.common.PagerUtil@getPageArray(parameters.currentPage,parameters.totalPage,3)" var="page">
	  			<@s.if test="#page=='...'">
	    			<li style="border:none;" ><@s.property value="page" /></li>
				</@s.if>
				<@s.elseif test="parameters.currentPage==#page">
					<li><a class="current_page" href="<@s.property value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','${page}')" />"><@s.property value="page" /></a></li>
				</@s.elseif>
	    		<@s.else>
	    			<li><a href="<@s.property value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','${page}')" />"><@s.property value="page" /></a></li>
	    		</@s.else>
	    </@s.iterator>
    </ul>
</div>-->
<script type="text/javascript">
	$(function(){
		$('#go').click(function(){
			$('#go').attr('href','<@s.property escapeHtml="false" value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','{page}')" />'.replace(/\{page\}/g,$('#currentPage').val()));
		});
	});
</script>
<div class="about_page">
	<@s.if test="parameters.currentPage==1">
		<div class="up_page"><a href="###">上一页</a></div>
	</@s.if>
	<@s.else>
		<div class="up_page"><a href="<@s.property value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','${parameters.currentPage-1}')" />">上一页</a></div>
	</@s.else>
	<ul>
		<@s.iterator value="@com.fantasy.framework.util.common.PagerUtil@getPageArray(parameters.currentPage,parameters.totalPage,3)" var="page">
	    	<@s.if test="#page=='...'">
	    		<li style="border:none;"><@s.property value="page" /></li>
	    	</@s.if>
	       <@s.elseif test="parameters.currentPage==#page">
				<li><a class="current_page" href="<@s.property value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','${page}')" />"><@s.property value="page" /></a></li>
		   </@s.elseif>
		    <@s.else>
		    	<li><a href="<@s.property value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','${page}')" />"><@s.property value="page" /></a></li>
		    </@s.else>
	    </@s.iterator>
    </ul>
    <@s.if test="parameters.currentPage==parameters.totalPage">
    	<div class="down_page"><a href="###">下一页</a></div>
    </@s.if>
    <@s.else>
		<div class="down_page"><a href="<@s.property value="@com.fantasy.framework.util.common.PagerUtil@getPagerUrl('${request.getRequestURI()}','${request.getQueryString()!''}','${parameters.currentPage+1}')" />">下一页</a></div>
	</@s.else>
    <div class=" floatL col666">到</div>
    <div class="jump_page"><input type="text" id="currentPage"><a title="go" href="" id="go"></a></div>
</div>


