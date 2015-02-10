<img<#rt/>
<#if parameters.id?if_exists != "">
 id="${parameters.id?html}"<#rt/>
</#if>
<#if parameters.cssClass??>
 class="${parameters.cssClass?html}"<#rt/>
</#if>
<#if parameters.cssStyle??>
 style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#if parameters.title??>
 title="${parameters.title?html}"<#rt/>
</#if>
<#if parameters.src??>
 src="${parameters.src?html}"<#rt/>
</#if>
<#if parameters.alt??>
 alt="${parameters.alt?html}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
<#if parameters.ratio??> data-src="holder.js/${parameters.ratio?html}" </#if>/>