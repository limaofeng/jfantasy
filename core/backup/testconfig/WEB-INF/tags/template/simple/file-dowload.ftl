<#if files.size() gt 0 >
	<#list files as f>
		<a href="${parameters.contextPath}/file/download${f.absolutePath}" target="hideIframe">${f.fileName}</a>;
	</#list>
<#else>
	无
</#if>