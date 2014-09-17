package ${package};

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aia.cn.isp.core.dao.Pager;
import ${beanClass};
import ${serviceClass};
import aia.cn.isp.core.util.jackson.JSON;

<#assign pk = table.primaryKey>
<#assign pkName = util.toJavaName(pk.columnName)>
<#assign cols = table.listColumn()>
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml"})
public class ${util.toSimpleJavaType(serviceClass)}Test {

	private final static Log LOG = LogFactory.getLog(${util.toSimpleJavaType(serviceClass)}Test.class);

	@Autowired
	private ${util.toSimpleJavaType(serviceClass)} ${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))};
	
	/**
	 * 测试	新增	${table.comments}   方法
	 * @param ${util.lowerCaseFirst(table.simpleName)}
	 */
	@Test
	public void create${util.toSimpleJavaType(table.simpleName)}(){
		${util.toSimpleJavaType(table.simpleName)} ${util.lowerCaseFirst(table.simpleName)} = new ${util.toSimpleJavaType(table.simpleName)}();
		<#list cols as col>
    	<#assign propName = util.toJavaName(col.columnName)>
    	${util.lowerCaseFirst(util.toSimpleJavaType(table.simpleName))}.set${util.upperCaseFirst(propName)}(null);
    	</#list>
		${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))}.create${util.toSimpleJavaType(table.simpleName)}(${util.lowerCaseFirst(table.simpleName)});
	}

	/**
	 * 测试	修改	${table.comments}   方法
	 * @param ${util.lowerCaseFirst(table.simpleName)}
	 */
	@Test
	public void update${util.toSimpleJavaType(table.simpleName)}(){
		${util.toSimpleJavaType(table.simpleName)} ${util.lowerCaseFirst(table.simpleName)} = new ${util.toSimpleJavaType(table.simpleName)}();
		<#list cols as col>
    	<#assign propName = util.toJavaName(col.columnName)>
    	${util.lowerCaseFirst(util.toSimpleJavaType(table.simpleName))}.set${util.upperCaseFirst(propName)}(null);
    	</#list>
		${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))}.update${util.toSimpleJavaType(table.simpleName)}(${util.lowerCaseFirst(table.simpleName)});
	}
	
	/**
	 * 测试	查询	${table.comments}   方法
	 * @param ${pkName}
	 * @@return ${beanClass}
	 */
	@Test
	public void get${util.toSimpleJavaType(table.simpleName)}(){
		${util.toSimpleJavaType(util.toJavaType(pk.getDataType()))} ${pkName} = null;
		LOG.debug(JSON.serialize(${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))}.get${util.toSimpleJavaType(table.simpleName)}(${pkName})));
	}

	/**
	 * 测试	查询	${table.comments}  的所有数据   方法
	 * @param ${util.lowerCaseFirst(table.simpleName)}
	 * @return java.util.List<${beanClass}>
	 */
	@Test
	public void get${util.toSimpleJavaType(table.simpleName)}s(){
		${util.toSimpleJavaType(table.simpleName)} ${util.lowerCaseFirst(table.simpleName)} = new ${util.toSimpleJavaType(table.simpleName)}();
		LOG.debug(JSON.serialize(${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))}.get${util.toSimpleJavaType(table.simpleName)}s(${util.lowerCaseFirst(table.simpleName)})));
	}
	
	/**
	 * 测试	分页查询	${table.comments}  的所有数据   方法
	 * @param pager
	 * @param ${util.lowerCaseFirst(table.simpleName)}
	 * @return aia.cn.isp.core.dao.Pager<${beanClass}>
	 */
	@Test
	public void list${util.toSimpleJavaType(table.simpleName)}(){
		Pager<${util.toSimpleJavaType(table.simpleName)}> pager = new Pager<${util.toSimpleJavaType(table.simpleName)}>(10);
		${util.toSimpleJavaType(table.simpleName)} ${util.lowerCaseFirst(table.simpleName)} = new ${util.toSimpleJavaType(table.simpleName)}();
		LOG.debug(JSON.serialize(${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))}.list${util.toSimpleJavaType(table.simpleName)}(pager,${util.lowerCaseFirst(table.simpleName)})));
	}
	
	/**
	 * 测试	删除	${table.comments}   方法
	 * @param ${pkName}
	 */
	@Test
	public void delete${util.toSimpleJavaType(table.simpleName)}(){
		${util.toSimpleJavaType(util.toJavaType(pk.getDataType()))} ${pkName} = null;
		${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))}.delete${util.toSimpleJavaType(table.simpleName)}(${pkName});
	}
}