package com.fantasy.framework.dao.mybatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class NullTypeHandler implements TypeHandler<Object> {
	
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}

	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return null;
	}

	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return null;
	}

	public void setParameter(PreparedStatement ps, int columnIndex, Object parameter, JdbcType jdbcType) throws SQLException {
	}
}
