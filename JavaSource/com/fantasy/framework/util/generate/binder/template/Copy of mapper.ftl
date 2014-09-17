package ${package};

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aia.cn.isp.core.dao.Pagination;

import ${IMapper};
import ${beanClass};

/**
 * ${table.comments}   Mapper
 * 
 * @author 复蓝软件
 */
<#assign pk = table.primaryKey>
<#assign pkName = util.toJavaName(pk.columnName)>
@Repository
public interface ${table.simpleName}Mapper extends ${util.toSimpleJavaType(IMapper)}{

	/**
	 * 查询	${table.comments}  的所有数据
	 * @param Map<String, String> map
	 * @return java.util.List<${beanClass}>
	 */
	public List<${util.toSimpleJavaType(table.simpleName)}> get${util.toSimpleJavaType(table.simpleName)}s(@Param("map") Map<String, String> map);

}