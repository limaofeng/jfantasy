package com.fantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JdbcUtil extends JdbcUtils {

	private static Log logger = LogFactory.getLog(JdbcUtil.class);

	public static void rollback(Connection con) {
		try {
			if (con != null){
                con.rollback();
            }
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static <T extends Exception> void rollback(Connection con, T re) throws T {
		try {
			con.rollback();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw re;
		}
	}

}
