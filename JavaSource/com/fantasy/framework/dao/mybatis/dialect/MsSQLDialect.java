package com.fantasy.framework.dao.mybatis.dialect;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;

import java.util.regex.Matcher;

/**
 * SqlServer 翻页方言
 * @功能描述 
 * @author 李茂峰
 * @since 2013-1-14 下午02:11:57
 * @version 1.0
 */
public class MsSQLDialect implements Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	private String version = "2005";

	public MsSQLDialect(String version) {
		this.version = version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLimitString(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder(sql.length() + 20);
		if ("2000".equals(version)) {
			sql = trim(sql);
			String over = StringUtil.defaultValue(RegexpUtil.parseFirst(sql, "(order|ORDER)[ ]+(by|BY)[ ]+([a-zA-Z0-9_, ]+)([ ]|[\r\n\t]){0,}((desc|DESC|asc|ASC)|[ ]|[\r\n\t]){0,}$"), "");
			if (StringUtil.isBlank(over)) {
				throw new RuntimeException("sqlserver2000 翻页必须添加 order by ? 条件");
			}
			sb.append("SELECT * FROM ( ");
			sb.append("SELECT TOP ").append(limit).append(" * FROM ( ");
			sb.append("SELECT TOP ").append(offset > 0 ? limit + offset : limit).append(" * FROM (");
			sb.append(RegexpUtil.replace(sql, "(order|ORDER)[ ]+(by|BY)[ ]+([a-zA-Z0-9_, ]+)([ ]|[\r\n\t]){0,}((desc|DESC|asc|ASC)|[ ]|[\r\n\t]){0,}$", ""));
			sb.append(" ) ");
			sb.append("A " ).append(filterOver(over, "A")).append("  ");
			sb.append(") B ").append(DialectUtil.antonymOver(filterOver(over, "B"))).append(") ");
			sb.append("C ").append(filterOver(over, "C"));
			return DialectUtil.pretty(sb.toString());
		} else {// ROW_NUMBER()翻页 2005以下的版本不支持
			sql = trim(sql);
			String over = StringUtil.defaultValue(RegexpUtil.parseFirst(sql, "(order|ORDER)[ ]+(by|BY)[ ]+([a-zA-Z0-9_, ]+)([ ]|[\r\n\t]){0,}((desc|DESC|asc|ASC)|[ ]|[\r\n\t]){0,}$"), "");
			if (StringUtil.isBlank(over)) {
				throw new RuntimeException("sqlserver2005 翻页必须添加 order by ? 条件");
			}
			if (!StringUtil.isBlank(over)) {
				sql = sql.replaceFirst(over, "");
			}
			if (offset > 0) {
				sb.append("select * from (");
				sb.append("select tab.*, ROW_NUMBER() OVER(" ).append(over.toUpperCase()).append(") as row_number from  ");
				sb.append("(");
				sb.append(sql);
				sb.append(") as tab ");
				sb.append(") as temp ").append(" where").append(" temp.row_number >").append(offset).append(" and ").append(" temp.row_number <= ").append(offset + limit).append(SQL_END_DELIMITER);
			} else {
				sb.append("select * from (");
				sb.append("select tab.*, ROW_NUMBER() OVER(" ).append(over.toUpperCase()).append(") as row_number from  ");
				sb.append("(");
				sb.append(sql);
				sb.append(") as tab ");
				sb.append(") as temp ").append(" where").append(" temp.row_number <= ").append(limit).append(SQL_END_DELIMITER);
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
		return DialectUtil.pretty("select count(1) from (" + trim(RegexpUtil.replace(sql, "(order|ORDER)[ ]+(by|BY)[ ]+([a-zA-Z0-9_, ]+)([ ]|[\r\n\t]){0,}((desc|DESC|asc|ASC)|[ ]|[\r\n\t]){0,}$", "")) + ") as countSql");
	}

	public String filterOver(String over, final String tableName) {

		return RegexpUtil.replace(over, "[a-zA-Z0-9_]{1,}", new RegexpUtil.AbstractReplaceCallBack() {

			@Override
			public String doReplace(String text, int index, Matcher matcher) {
				if (!RegexpUtil.find(text.toLowerCase(), "^((order)|(by)|(asc)|(desc))$")) {
					return tableName.concat(".").concat(text);
				}
				return text;
			}

		});

	}

}
