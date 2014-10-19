package ${package};

import java.util.List;
import java.util.Map;

<#list imports as import>
import ${import};
</#list>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${beanClass};
import ${daoClass};
import ${serviceClass};

<#assign pk = table.primaryKey>
<#assign pkName = util.toJavaName(pk.columnName)>
@Service
public class ${table.simpleName}ServiceImpl implements ${util.toSimpleJavaType(serviceClass)} {
	
	@Autowired
	private ${util.toSimpleJavaType(daoClass)} ${util.lowerCaseFirst(util.toSimpleJavaType(daoClass))};

	

	public List<${util.toSimpleJavaType(table.simpleName)}> get${util.toSimpleJavaType(table.simpleName)}s(Map<String, String> map){
		return ${util.lowerCaseFirst(util.toSimpleJavaType(daoClass))}.get${util.toSimpleJavaType(table.simpleName)}s(map);
	}
	
}
