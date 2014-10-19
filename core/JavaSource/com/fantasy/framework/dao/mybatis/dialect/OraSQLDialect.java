package com.fantasy.framework.dao.mybatis.dialect;

import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.regexp.RegexpUtil.ReplaceCallBack;

import java.util.regex.Matcher;

/**
 * oracle 翻页方言
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:12:27
 * @version 1.0
 */
public class OraSQLDialect implements Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	protected static final String ORA_SQL_LIMIT = "select * from (select ora_a.*,rownum row_num from ({SQL}) ora_a ) ora_b where ora_b.row_num between {OFFSET} and {LIMIT}";

	public String getLimitString(final String sql, final int offset, final int limit) {
        return DialectUtil.pretty(RegexpUtil.replace(ORA_SQL_LIMIT, "\\{[A-Z]+\\}", new ReplaceCallBack() {

            public String replace(String group, int i, Matcher m) {
                if ("{SQL}".equals(group)) {
                    return trim(sql);
                } else if ("{OFFSET}".equals(group)) {
                    return String.valueOf(offset + 1);
                } else if ("{LIMIT}".equals(group)) {
                    return String.valueOf(offset > 0 ? limit + offset : limit);
                }
                return group;
            }

        }));
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}

	public String getCountString(String sql) {
		return DialectUtil.pretty("select count(1) from (" + trim(sql) + ") countsql");
	}

}
