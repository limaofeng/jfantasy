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
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parseTerm(String field, String value) {
		Term t = new Term(field, value);
		return new TermQuery(t);
	}

    public static Query parseTermPrefix(String field, String value) {
        Term t = new Term(field, value);
        return new PrefixQuery(t);
    }

	/**
	 * 单个字段模糊检索
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, String value) {
		return parse(field, value, QueryParser.Operator.OR);
	}

	/**
	 * 单个字段模糊检索
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @param op
	 * @return
	 */
	public static Query parse(String field, String value, QueryParser.Operator op) {
		QueryParser parser = new QueryParser(BuguIndex.getInstance().getVersion(), field, BuguIndex.getInstance().getAnalyzer());
		parser.setDefaultOperator(op);
		return parse(parser, value);
	}

	/**
	 * 多个字段模糊检索同一个值
	 * 
	 * @功能描述
	 * @param fields
	 * @param value
	 * @return
	 */
	public static Query parse(String[] fields, String value) {
		return parse(fields, value, QueryParser.Operator.OR);
	}

	/**
	 * 多条件查询
	 * 
	 * @功能描述
	 * @param fields
	 * @param values
	 * @return
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
	 * @功能描述
	 * @param fields
	 * @param value
	 * @param op
	 * @return
	 */
	public static Query parse(String[] fields, String value, QueryParser.Operator op) {
		QueryParser parser = new MultiFieldQueryParser(BuguIndex.getInstance().getVersion(), fields, BuguIndex.getInstance().getAnalyzer());
		parser.setDefaultOperator(op);
		return parse(parser, value);
	}

	/**
	 * 
	 * @功能描述
	 * @param fields
	 * @param occurs
	 * @param value
	 * @return
	 */
	public static Query parse(String[] fields, BooleanClause.Occur[] occurs, String value) {
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(BuguIndex.getInstance().getVersion(), value, fields, occurs, BuguIndex.getInstance().getAnalyzer());
		} catch (ParseException ex) {
			logger.error("MultiFieldQueryParser can not parse the value " + value, ex);
		}
		return query;
	}

	public static Query parse(String[] fields, BooleanClause.Occur[] occurs, String[] values) {
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(BuguIndex.getInstance().getVersion(), values, fields, occurs, BuguIndex.getInstance().getAnalyzer());
		} catch (ParseException ex) {
			logger.error("MultiFieldQueryParser can not parse the values " + Arrays.toString(values), ex);
		}
		return query;
	}

	/**
	 * 多条件查询
	 * 
	 * @功能描述
	 * @param queries
	 * @return
	 */
	public static Query parse(Query... queries) {
		Assert.isTrue(queries.length != 0, "param queries length must be greater than 1 ");
		BooleanQuery query = new BooleanQuery();
		for (Query _query : queries) {
			query.add(_query, BooleanClause.Occur.MUST);
		}
		return query;
	}
	
//	BooleanClause.Occur.MUST表示and,
//	BooleanClause.Occur.MUST_NOT表示not,
//	BooleanClause.Occur.SHOULD表示or.

	/**
	 * 多条件查询
	 * 
	 * @功能描述
	 * @param queries
	 * @param occurs
	 * @return
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
	 * 
	 * @功能描述
	 * @param parser
	 * @param value
	 * @return
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
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, int value) {
		return NumericRangeQuery.newIntRange(field, Integer.valueOf(value), Integer.valueOf(value), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static Query parse(String field, int minValue, int maxValue) {
		return NumericRangeQuery.newIntRange(field, Integer.valueOf(minValue), Integer.valueOf(maxValue), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, long value) {
		return NumericRangeQuery.newLongRange(field, Long.valueOf(value), Long.valueOf(value), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static Query parse(String field, long minValue, long maxValue) {
		return NumericRangeQuery.newLongRange(field, Long.valueOf(minValue), Long.valueOf(maxValue), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, float value) {
		return NumericRangeQuery.newFloatRange(field, Float.valueOf(value), Float.valueOf(value), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static Query parse(String field, float minValue, float maxValue) {
		return NumericRangeQuery.newFloatRange(field, Float.valueOf(minValue), Float.valueOf(maxValue), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, double value) {
		return NumericRangeQuery.newDoubleRange(field, Double.valueOf(value), Double.valueOf(value), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static Query parse(String field, double minValue, double maxValue) {
		return NumericRangeQuery.newDoubleRange(field, Double.valueOf(minValue), Double.valueOf(maxValue), true, true);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Query parse(String field, Date begin, Date end) {
		long beginTime = begin.getTime();
		long endTime = end.getTime();
		return parse(field, beginTime, endTime);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Query parse(String field, Timestamp begin, Timestamp end) {
		long beginTime = begin.getTime();
		long endTime = end.getTime();
		return parse(field, beginTime, endTime);
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, boolean value) {
		if (value) {
			return parse(field, "true");
		}
		return parse(field, "false");
	}

	/**
	 * 
	 * @功能描述
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query parse(String field, char value) {
		return parse(field, String.valueOf(value));
	}
}
