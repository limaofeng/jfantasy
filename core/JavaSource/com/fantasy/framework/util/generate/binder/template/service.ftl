package ${package};

import java.util.List;
import java.util.Map;

<#list imports as import>
import ${import};
</#list>
import ${beanClass};

<#assign pk = table.primaryKey>
<#assign pkName = util.toJavaName(pk.columnName)>
/**
 * ${table.comments} Service
 * 
 * @author 复蓝软件
 */
public interface ${table.simpleName}Service {

	
	/**
	 * 查询	${table.comments}  的所有数据
	 * @param Map<String, String> map
	 * @return java.util.List<${beanClass}>
	 */
	public List<${util.toSimpleJavaType(table.simpleName)}> get${util.toSimpleJavaType(table.simpleName)}s(Map<String, String> map);
}
