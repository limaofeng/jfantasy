package com.fantasy.framework.lucene;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class BuguParser {

    private static final Logger logger = Logger.getLogger(BuguParser.class);

    /**
     * 单个字段匹配
     *
     * @param field 字段
     * @param value 匹配值
     * @return Query
     */
    public static Query parseTerm(String field, String value) {
        Term t = new Term(field, value);
        return new TermQuery(t);
    }

    /**
     * 单个字段匹配 - 匹配开始
     *
     * @param field 匹配字段
     * @param value 匹配值
     * @return Query
     */
    public static Query parseTermPrefix(String field, String value) {
        Term t = new Term(field, value);
        return new PrefixQuery(t);
    }

    /**
     * 单个字段模糊检索
     *
     * @param field 字段
     * @param value 比较值
     * @return Query
     */
    public static Query parse(String field, String value) {
        return parse(field, value, QueryParser.Operator.OR);
    }

    /**
     * 单个字段模糊检索
     *
     * @param field 字段
     * @param value 值
     * @param op    匹配方式 包含 and 与 or
     * @return Query
     */
    public static Query parse(String field, String value, QueryParser.Operator op) {
        QueryParser parser = new QueryParser(BuguIndex.getInstance().getVersion(),field, BuguIndex.getInstance().getAnalyzer());
        parser.setDefaultOperator(op);
        return parse(parser, value);
    }

    /**
     * 多个字段模糊检索同一个值
     *
     * @param fields 多个字段
     * @param value  值
     * @return Query
     */
    public static Query parse(String[] fields, String value) {
        return parse(fields, value, QueryParser.Operator.OR);
    }

    /**
     * 多条件查询
     *
     * @param fields 多个字段
     * @param values 多个条件
     * @return Query
     */
    public static Query parse(String[] fields, String[] values) {
        Assert.isTrue(fields.length != 0, "param fields length must be greater than 1 ");
        Assert.isTrue(fields.length == values.length, "length of fields and values must be equal");
        BooleanClause.Occur[] clauses = new BooleanClause.Occur[fields.length];
        if (fields.length == values.length) {
            for (int i = 0; i < fields.length; i++) {
                clauses[i] = BooleanClause.Occur.SHOULD;
            }
        }
        return parse(fields, clauses, values);
    }

    /**
     * 一次性查询多个条件
     *
     * @param fields 多个字段
     * @param value  匹配值
     * @param op     匹配方式 包含 and 与 or
     * @return Query
     */
    public static Query parse(String[] fields, String value, QueryParser.Operator op) {
        QueryParser parser = new MultiFieldQueryParser(BuguIndex.getInstance().getVersion(),fields, BuguIndex.getInstance().getAnalyzer());
        parser.setDefaultOperator(op);
        return parse(parser, value);
    }

    /**
     * 多字段查询
     *
     * @param fields 多个字段
     * @param occurs 条件连接方式，类似于 and 与 or 、not
     * @param value  匹配字符串
     * @return Query
     */
    public static Query parse(String[] fields, BooleanClause.Occur[] occurs, String value) {
        Query query = null;
        try {
            query = MultiFieldQueryParser.parse(BuguIndex.getInstance().getVersion(),value, fields, occurs, BuguIndex.getInstance().getAnalyzer());
        } catch (ParseException ex) {
            logger.error("MultiFieldQueryParser can not parse the value " + value, ex);
        }
        return query;
    }

    /**
     * 多字段查询
     *
     * @param fields 多个字段
     * @param occurs 条件连接方式，类似于 and 与 or 、not
     * @param values 匹配多个字符串
     * @return Query
     */
    public static Query parse(String[] fields, BooleanClause.Occur[] occurs, String[] values) {
        Query query = null;
        try {
            query = MultiFieldQueryParser.parse(BuguIndex.getInstance().getVersion(),values, fields, occurs, BuguIndex.getInstance().getAnalyzer());
        } catch (ParseException ex) {
            logger.error("MultiFieldQueryParser can not parse the values " + Arrays.toString(values), ex);
        }
        return query;
    }

    /**
     * 多条件查询
     *
     * @param queries 将多个 Query 对象转为 一个
     * @return Query
     */
    public static Query parse(Query... queries) {
        Assert.isTrue(queries.length != 0, "param queries length must be greater than 1 ");
        BooleanQuery query = new BooleanQuery();
        for (Query querys : queries) {
            query.add(querys, BooleanClause.Occur.MUST);
        }
        return query;
    }

//	BooleanClause.Occur.MUST表示and,
//	BooleanClause.Occur.MUST_NOT表示not,
//	BooleanClause.Occur.SHOULD表示or.

    /**
     * 多条件查询
     *
     * @param queries 要合并的查询
     * @param occurs  条件连接方式，类似于 and 与 or 、not
     * @return Query
     */
    public static Query parse(Query[] queries, BooleanClause.Occur[] occurs) {
        Assert.isTrue(queries.length != 0, "param queries length must be greater than 1 ");
        Assert.isTrue(queries.length == occurs.length, "length of queries and occurs must be equal");
        BooleanQuery query = new BooleanQuery();
        for (int i = 0; i < queries.length; i++) {
            query.add(queries[i], occurs[i]);
        }
        return query;
    }

    /**
     * 使用 QueryParser 进行查询
     *
     * @param parser parser
     * @param value  匹配的值
     * @return Query
     */
    private static Query parse(QueryParser parser, String value) {
        Query query = null;
        try {
            query = parser.parse(value);
        } catch (ParseException ex) {
            logger.error("Can not parse the value " + value, ex);
        }
        return query;
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
    public static Query parse(String field, int value) {
        return NumericRangeQuery.newIntRange(field, value, value, true, true);
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     * @return Query
     */
    public static Query parse(String field, int minValue, int maxValue) {
        return NumericRangeQuery.newIntRange(field, minValue, maxValue, true, true);
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
    public static Query parse(String field, long value) {
        return NumericRangeQuery.newLongRange(field, value, value, true, true);
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     * @return Query
     */
    public static Query parse(String field, long minValue, long maxValue) {
        return NumericRangeQuery.newLongRange(field, minValue, maxValue, true, true);
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
    public static Query parse(String field, float value) {
        return NumericRangeQuery.newFloatRange(field, value, value, true, true);
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     */
    public static Query parse(String field, float minValue, float maxValue) {
        return NumericRangeQuery.newFloatRange(field, minValue, maxValue, true, true);
    }

    /**
     * 单个字段检索 - 数值
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
    public static Query parse(String field, double value) {
        return NumericRangeQuery.newDoubleRange(field, value, value, true, true);
    }

    /**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     * @return Query
     */
    public static Query parse(String field, double minValue, double maxValue) {
        return NumericRangeQuery.newDoubleRange(field, minValue, maxValue, true, true);
    }

    /**
     * 单个字段检索 - 时间区间
     *
     * @param field 匹配字段
     * @param begin 开始时间
     * @param end   结束时间
     * @return Query
     */
    public static Query parse(String field, Date begin, Date end) {
        long beginTime = begin.getTime();
        long endTime = end.getTime();
        return parse(field, beginTime, endTime);
    }

    /**
     * 单个字段检索 - 时间区间
     *
     * @param field 匹配字段
     * @param begin 开始时间
     * @param end   结束时间
     * @return Query
     */
    public static Query parse(String field, Timestamp begin, Timestamp end) {
        long beginTime = begin.getTime();
        long endTime = end.getTime();
        return parse(field, beginTime, endTime);
    }

    /**
     * 单个字段检索
     *
     * @param field 匹配字段
     * @param value boolean值
     * @return Query
     */
    public static Query parse(String field, boolean value) {
        if (value) {
            return parse(field, "true");
        }
        return parse(field, "false");
    }

    /**
     * 单个字段模糊检索
     *
     * @param field 字段
     * @param value 比较值
     * @return Query
     */
    public static Query parse(String field, char value) {
        return parse(field, String.valueOf(value));
    }
}
