package ${package};

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import aia.cn.isp.core.dao.Pagination;
import ${beanClass};
import ${mapperClass};

<#assign pk = table.primaryKey>
<#assign pkName = util.toJavaName(pk.columnName)>
/**
 * ${table.comments} Dao
 * 
 * @author 复蓝软件
 */
@Repository
public class ${table.simpleName}Dao {

	@Autowired
	private ${util.toSimpleJavaType(table.simpleName)}Mapper ${util.lowerCaseFirst(table.simpleName)}Mapper;

	/**
	 * 查询	${table.comments}  的所有数据
	 * @param Map<String, String>
	 * @return java.util.List<${beanClass}>
	 */
	public List<${util.toSimpleJavaType(table.simpleName)}> get${util.toSimpleJavaType(table.simpleName)}s(Map<String, String> map){
		return ${util.lowerCaseFirst(table.simpleName)}Mapper.get${util.toSimpleJavaType(table.simpleName)}s(map);
	}
	


}