<#if fieldErrors??><#t/>
    <#assign eKeys = fieldErrors.keySet()><#t/>
    <#assign eKeysSize = eKeys.size()><#t/>
    <#assign doneStartUlTag=false><#t/>
    <#assign doneEndUlTag=false><#t/>
    <#assign haveMatchedErrorField=false><#t/>
    <#if (fieldErrorFieldNames?size > 0) ><#t/>
        <#list fieldErrorFieldNames as fieldErrorFieldName><#t/>
            <#list eKeys as eKey><#t/>
                <#if (eKey = fieldErrorFieldName)><#t/>
                    <#assign haveMatchedErrorField=true><#t/>
                    <#assign eValue = fieldErrors[fieldErrorFieldName]><#t/>
                    <#if (haveMatchedErrorField && (!doneStartUlTag))><#t/>
                    <#lt/><${tagName}<#rt/>
                        <#if parameters.id?if_exists != ""> id="${parameters.id?html}"</#if><#t/>
                        <#if parameters.cssClass??> class="errorMessage ${parameters.cssClass?html} <#if (parameters.cssErrorClass??)>${parameters.cssErrorClass?html}</#if>"<#rt/><#elseif !(parameters.cssClass??) && (parameters.cssErrorClass??)> class="errorMessage ${parameters.cssErrorClass?html}"</#if><#t/>                      
                        <#if parameters.cssStyle??> style="${parameters.cssStyle?html}"<#rt/></#if><#t/>
                     ><#t/>
                    <#assign doneStartUlTag=true><#t/>
                    </#if><#t/>
                    <#assign eEachValue = eValue[0]><#t/>
                    <#if parameters.escape>${eEachValue!?html}<#else>${eEachValue!}</#if><#t/>
                </#if><#t/>
            </#list><#t/>
        </#list><#t/>
        <#if (haveMatchedErrorField && (!doneEndUlTag))><#t/>
        <#lt/></${tagName}>
            <#assign doneEndUlTag=true><#t/>
        </#if><#t/>
        <#if (!haveMatchedErrorField)><#t/>
		<#lt/><${tagName}<#rt/>
			<#if parameters.id?if_exists != ""> id="${parameters.id?html}"</#if><#t/>
			<#if parameters.cssClass??> class="${parameters.cssClass?html}"</#if><#t/>
            <#if parameters.cssStyle??> style="${parameters.cssStyle?html}"</#if><#t/>
		><#t/>
        <#lt/></${tagName}>
   		</#if><#t/>
    </#if><#t/>
    <#else><#t/>
        <#if (!haveMatchedErrorField)><#t/>
		<#lt/><${tagName}<#rt/>
			<#if parameters.id?if_exists != ""> id="${parameters.id?html}"<#t/></#if>
			<#if parameters.cssClass??> class="${parameters.cssClass?html}"<#t/></#if>
            <#if parameters.cssStyle??> style="${parameters.cssStyle?html}"<#t/></#if>
		><#t/>
        <#lt/></${tagName}>
    </#if><#t/>
</#if><#t/>