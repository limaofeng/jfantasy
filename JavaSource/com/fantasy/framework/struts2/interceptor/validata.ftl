$(function(){
	jQuery.formValidator.initConfig({formid:"${formId}",alertmessage:false,onerror:function(msg){
	}});
<#assign fName = 'null'><#t> 
<#assign index = 0><#t> 
<#list validators as fvalidator><#t>
	<#if (( fName!='null' && fvalidator.fieldName != fName))><#t>
		});
	});	
	</#if><#t>
	<#if fvalidator.fieldName != fName><#t> 
		
		<#assign nameIndex = fvalidator.fieldName.indexOf('*')><#t>
		<#if nameIndex gt -1 ><#t> 
	$('#${formId}').find('[name^=${fvalidator.fieldName.substring(0,nameIndex-1)}]').filter(function(){			
		return /${fvalidator.fieldName.replaceAll('(\\[|\\])', '\\\\$1').replaceAll('\\*', '\\\\d+')}/.test(this.name);
	}).each(function(){
		<#else>
	/*添加非空标志 *
	$('#${formId}').find('[name=${fvalidator.fieldName}]').each(function(){
		<#if (fvalidator.validatorType == 'requiredstring' || fvalidator.validatorType == 'required')>
			if(!$(this).next().hasClass('required'))
				$(this).after('<span class="required"></span>');
		</#if>
	});
	*/
	$('#${formId}').find('[name=${fvalidator.fieldName}]').each(function(){
		</#if><#t>
		$(this).formValidator({
			<#if  fvalidator.validatorType != 'requiredstring' && fvalidator.validatorType != 'required'><#t> 
			empty:true,
			</#if>
			show : function(elem,tip){
	    		<#if iErrorClass??>elem.removeClass('${iErrorClass}');</#if>
			},
			focus : function(elem,tip){
				<#if iErrorClass??>elem.removeClass('${iErrorClass}');</#if>
				tip.removeClass().html('&nbsp;');
			},
			failure : function(elem,tip){
				//<#if iErrorClass??>elem.addClass('${iErrorClass}');</#if>
				//<#if errorClass??>tip.removeClass('onError').addClass('${errorClass}')</#if>
			},
			success : function(elem,tip){
				<#if iErrorClass??>elem.removeClass('${iErrorClass}');</#if>
				<#if correctClass??>tip.removeClass().addClass('${correctClass}').show()</#if>
			}
	</#if>
		<#if fvalidator.class.superclass.name == 'com.fantasy.framework.struts2.validator.AjaxValidatorSupport'><#t> 
		}).ajaxValidator({
			url : '${request.contextPath}/ajaxValidator.do',
			type : 'POST',
			data : {
				'validatorType':'${fvalidator.validatorType}'${(fvalidator.dataNames?size>0)?string(',','')}
				<#list fvalidator.dataNames as dataName>
				'${dataName}':$('[name=${dataName}]').val()${(fvalidator.dataNames?size > dataName_index + 1)?string(',','')}
				</#list>
				},
			datatype : 'json',
			processdata : true,
			isvalid : true,
			success : function(data){
				return data['success'];
			},
			onerror : "${fvalidator.defaultMessage}"
		<#elseif fvalidator.validatorType == 'stringlength'>
		}).inputValidator({
			<#if fvalidator.maxLength != -1><#t> 
			max:${(fvalidator.maxLength)?c},
			</#if>
			<#if fvalidator.minLength != -1><#t> 
			min:${(fvalidator.minLength)?c},
			</#if>
			onerror:"${fvalidator.defaultMessage}"
		<#else>
		}).regexValidator( {		
		<#if fvalidator.validatorType == 'regex'><#t> 
			regexp : "${fvalidator.expression.replaceAll('\\\\', '\\\\\\\\')}",
		<#else>
			regexp : "${(fvalidator.validatorType=='requiredstring')?string("notempty",fvalidator.validatorType)}",
			datatype : "enum",
		</#if>			
			onerror : "${fvalidator.defaultMessage}"
		</#if>
	<#if ((index + 1) = validators.size())><#t>
		});
	});	
	</#if><#t>
	<#assign fName = fvalidator.fieldName><#t>
	<#assign index = index+1><#t>
</#list>

});