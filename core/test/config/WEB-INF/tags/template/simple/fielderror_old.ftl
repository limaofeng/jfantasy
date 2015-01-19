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
                    <span<#rt/>
                        <#if parameters.id?if_exists != "">
                                id="${parameters.id?html}"<#rt/>
                        </#if>
                        <#if parameters.cssClass??>
                                class="${parameters.cssClass?html}"<#rt/>
                            <#else>
                                class="errorMessage"<#rt/>
                        </#if>
                        <#if parameters.cssStyle??>
                                style="${parameters.cssStyle?html}"<#rt/>
                        </#if>
                            >
                        <#assign doneStartUlTag=true><#t/>
                    </#if><#t/>
                    <#list eValue as eEachValue><#t/>
                        <#if parameters.escape>${eEachValue!?html}<#else>${eEachValue!}</#if>
                    </#list><#t/>
                </#if><#t/>
            </#list><#t/>
        </#list><#t/>
        <#if (haveMatchedErrorField && (!doneEndUlTag))><#t/>
        </span>
            <#assign doneEndUlTag=true><#t/>
        </#if><#t/>
        <#else><#t/>
        <#if (eKeysSize > 0)><#t/>
        <span<#rt/>
            <#if parameters.cssClass??>
                    class="${parameters.cssClass?html}"<#rt/>
                <#else>
                    class="errorMessage"<#rt/>
            </#if>
            <#if parameters.cssStyle??>
                    style="${parameters.cssStyle?html}"<#rt/>
            </#if>
                >
            <#list eKeys as eKey><#t/>
                <#assign eValue = fieldErrors[eKey]><#t/>
                <#list eValue as eEachValue><#t/>
                    <#if parameters.escape>${eEachValue!?html}<#else>${eEachValue!}</#if>
                </#list><#t/>
            </#list><#t/>
        </span>
        </#if><#t/>
    </#if><#t/>
</#if><#t/>
