<#if parameters.type!='ajax'>
<#lt><script type="text/javascript" src="${parameters.contextPath}${parameters.namespace?if_exists}/${parameters.action?if_exists}.validata?1=1<#rt>
<#lt><#if parameters.formId??>&formId=${parameters.formId}</#if><#rt>
<#lt><#if parameters.iErrorClass??>&iErrorClass=${parameters.iErrorClass}</#if><#rt>
<#lt><#if parameters.errorClass??>&errorClass=${parameters.errorClass}</#if><#rt>
<#if parameters.correctClass??>&correctClass=${parameters.correctClass}</#if>" charset="${parameters.charset}" rel="forceLoad"></script><#rt>
<#else>
<script type="text/javascript">
	jQuery.getScript('${parameters.contextPath}${parameters.namespace?if_exists}/${parameters.action?if_exists}.validata?1=1<#rt>
<#lt><#if parameters.formId??>&formId=${parameters.formId}</#if><#rt>
<#lt><#if parameters.iErrorClass??>&iErrorClass=${parameters.iErrorClass}</#if><#rt>
<#lt><#if parameters.errorClass??>&errorClass=${parameters.errorClass}</#if><#rt>
<#if parameters.correctClass??>&correctClass=${parameters.correctClass}</#if>');
</script>
</#if>