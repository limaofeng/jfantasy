package com.fantasy.framework.dao.mybatis.dialect;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;

/**
 * DB2 翻页方言
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:11:23
 * @version 1.0
 */
public class DB2SQLDialect implements Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	public String getLimitString(String sql, int offset, int limit) {
		sql = trim(sql);
		if (RegexpUtil.isMatch(trim(sql), "WITH(?i)")) {
			String with = RegexpUtil.parseFirst(sql, "(WITH(?i))[ ]+([a-zA-Z0-9]+)\\([^\\)]+\\)[ ]+(AS(?i))[ ]+");
			String temp = sql.substring(with.length());
			String newSql = "";
			int num = 0;
			char[] array = temp.toCharArray();
			for (int i = 0; i < array.length; i++) {
				char c = array[i];
				if (c == '(') {
					num++;
				} else if (c == ')') {
					num--;
				}
				if (num == 0) {
					with += temp.substring(0, i + 1);
					newSql = temp.substring(i + 1);
					break;
				}
			}
			return DialectUtil.pretty(with + " " + getLimitString(newSql, offset, limit));
		} else {
			String over = StringUtil.defaultValue(RegexpUtil.parseFirst(sql, "(order|ORDER)[ ]+(by|BY)[ ]+([a-zA-Z0-9_, ]+)([ ]|[\r\n\t]){0,}((desc|DESC|asc|ASC)|[ ]|[\r\n\t]){0,}$"), "");
			if (!StringUtil.isBlank(over)) {
				sql = sql.replaceFirst(over, "");
			}
			StringBuilder sb = new StringBuilder(sql.length() + 20);
			if (offset > 0) {
				sb.append("select * from (");
				sb.append("select tab.*, ROWNUMBER() OVER(").append(over.toUpperCase()).append(") as row_number from  ");
				sb.append("(");
				sb.append(sql);
				sb.append(") as tab ");
				sb.append(") as temp ").append(" where").append(" temp.row_number >").append(offset).append(" and ").append(" temp.row_number <= ").append(offset + limit);
			} else {
				sb.append("select * from (");
				sb.append("select tab.*, ROWNUMBER() OVER(").append(over.toUpperCase()).append(") as row_number from  ");
				sb.append("(");
				sb.append(sql);
				sb.append(") as tab ");
				sb.append(") as temp ").append(" where").append(" temp.row_number <= ").append(limit);
			}
			return DialectUtil.pretty(sb.toString());
		}
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}

	public String getCountString(String sql) {
		if (RegexpUtil.isMatch(trim(sql), "WITH(?i)")) {
			String with = RegexpUtil.parseFirst(sql, "(WITH(?i))[ ]+([a-zA-Z0-9]+)\\([^\\)]+\\)[ ]+(AS(?i))[ ]+");
			String temp = sql.substring(with.length());
			String newSql = "";
			int num = 0;
			char[] array = temp.toCharArray();
			for (int i = 0; i < array.length; i++) {
				char c = array[i];
				if (c == '(') {
					num++;
				} else if (c == ')') {
					num--;
				}
				if (num == 0) {
					with += temp.substring(0, i + 1);
					newSql = temp.substring(i + 1);
					break;
				}
			}
			return DialectUtil.pretty(with + " " + getCountString(newSql));
		} else {
			return DialectUtil.pretty("select count(1) from (" + trim(sql) + ") countsql");
		}
	}

}
