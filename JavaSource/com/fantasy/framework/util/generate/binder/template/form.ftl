package ${package};

<#list imports as import>
import ${import};
</#list>

import ${beanClass};

/**
 * ${table.comments} FormBean
 * 
 * @author 复蓝软件
 */
@SuppressWarnings("serial")
public class ${table.simpleName}Form implements java.io.Serializable {
	<#assign cols = table.listColumn()>
    <#list cols as col>
	<#if col.getComments() != "">
	/**
	 * ${col.getComments()}
	 */
	</#if> 
    private ${util.toSimpleJavaType(util.toJavaType(col.getDataType()))} ${util.toJavaName(col.columnName)};
    </#list>
    
    public ${table.simpleName}Form(){
    }
    
    <#list cols as col>
	<#assign propName = util.toJavaName(col.columnName)>
	/**
	 * 设置  ${col.getComments()}
	 * @param ${propName}
	 */
    public void set${util.upperCaseFirst(propName)}(${util.toSimpleJavaType(util.toJavaType(col.getDataType()))} ${propName}){
    	this.${propName} = ${propName};
    }
    
	/**
	 * 获取  ${col.getComments()}
	 * @return ${util.toJavaType(col.getDataType())}
	 */
    public ${util.toSimpleJavaType(util.toJavaType(col.getDataType()))} get${util.upperCaseFirst(propName)}(){
    	return this.${propName};
    }
    
    </#list>
    public ${util.toSimpleJavaType(beanClass)} to${util.toSimpleJavaType(beanClass)}(){
    	${util.toSimpleJavaType(beanClass)} ${util.lowerCaseFirst(util.toSimpleJavaType(beanClass))} = new ${util.toSimpleJavaType(beanClass)}();
    	<#list cols as col>
    	<#assign propName = util.toJavaName(col.columnName)>
    	${util.lowerCaseFirst(util.toSimpleJavaType(beanClass))}.set${util.upperCaseFirst(propName)}(this.get${util.upperCaseFirst(propName)}());
    	</#list>
		return ${util.lowerCaseFirst(util.toSimpleJavaType(beanClass))};
    }
    
}