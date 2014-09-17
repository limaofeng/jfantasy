<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperClass}">
	
	<#assign pk = table.primaryKey>
	<#assign pkName = util.toJavaName(pk.columnName)>
	<#assign cols = table.getColumns()>
	<!-- resultMap -->
	<resultMap id="${util.toSimpleJavaType(table.simpleName)}ResultMap" type="${beanClass}">
		<#list cols as col>
	   		<#if pk.columnName == col.columnName>
	   	<id property="${util.toJavaName(col.columnName)}" column="${col.columnName}"/>
	   		<#else>
	   	<result property="${util.toJavaName(col.columnName)}" column="${col.columnName}"/>
        	</#if>
		</#list>
	</resultMap>

	
	
	<!-- 查询sql脚本(多条记录) -->
	<select id="get${util.toSimpleJavaType(table.simpleName)}s" parameterType="map" resultType="${beanClass}">
		SELECT
			<#list cols as col>
			<#if 0<col_index>,<#else> </#if>[${col.columnName}]
			</#list>
		FROM [${table.tableName}]
		<where>
			<#list cols as col>
		   		<#if pk.columnName != col.columnName>
	        <if test=" ${util.toJavaName(col.columnName)} != null ">AND [${col.columnName}] = #${'{'+util.toJavaName(col.columnName)+'}'} </if>
	        	</#if>
			</#list>
		</where>
	</select>
	

</mapper>