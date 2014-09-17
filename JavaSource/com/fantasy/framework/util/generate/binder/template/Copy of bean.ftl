package ${package};

import org.apache.ibatis.type.Alias;

<#list imports as import>
import ${import};
</#list>

/**
 * ${table.comments}
 * 
 * @author 复蓝软件
 */
@Alias("${table.simpleName}")
@SuppressWarnings("serial")
public class ${table.simpleName} implements java.io.Serializable {
	<#assign cols = table.listColumn()>
    <#list cols as col>
	<#if col.getComments() != "">
	/**
	 * ${col.getComments()}
	 */
	</#if> 
    private ${util.toSimpleJavaType(util.toJavaType(col.getDataType()))} ${util.toJavaName(col.columnName)};
    </#list>
    
    public ${table.simpleName}(){
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
}