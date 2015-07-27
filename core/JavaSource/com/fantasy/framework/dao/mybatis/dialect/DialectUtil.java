package com.fantasy.framework.dao.mybatis.dialect;

import com.fantasy.framework.util.regexp.RegexpUtil;

import java.util.regex.Matcher;

public class DialectUtil {

    /**
     * 关键字数组
     */
    private static String[] keywords = new String[]{"select", "top", "count", "from", "order", "by", "asc", "desc", "group", "where", "and"};

    private static String keywordRegexp = null;

    static {
        StringBuilder buffer = new StringBuilder("^(");
        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            buffer.append("(").append(keyword).append(")");
            if (i != keywords.length - 1) {
                buffer.append("|");
            }
        }
        buffer.append(")$");
        keywordRegexp = buffer.toString();
    }

    private static RegexpUtil.ReplaceCallBack antonymReplaceCallBack = new RegexpUtil.AbstractReplaceCallBack() {

        @Override
        public String doReplace(String text, int index, Matcher matcher) {
            if (RegexpUtil.find(text.toLowerCase(), "^((asc)|(desc))$")) {
                return "asc".equalsIgnoreCase(text) ? "desc" : "asc";
            }
            return text;
        }

    };

    /**
     * sql 排序取反
     *
     * @param over over
     * @return string
     */
    public static String antonymOver(String over) {
        return RegexpUtil.replace(over, "[a-zA-Z0-9_]{1,}", antonymReplaceCallBack);
    }

    /**
     * sql 美化
     * 查询关键字大写
     *
     * @param sql sql
     * @return sql
     */
    public static String pretty(String sql) {
        return RegexpUtil.replace(sql, "[a-zA-Z0-9_]{1,}", new RegexpUtil.AbstractReplaceCallBack() {

            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                if (RegexpUtil.find(text.toLowerCase(), keywordRegexp)) {
                    return text.toUpperCase();
                }
                return text;
            }

        });
    }

}
