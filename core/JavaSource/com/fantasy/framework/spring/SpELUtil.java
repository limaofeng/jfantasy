package com.fantasy.framework.spring;

import com.fantasy.framework.dao.mybatis.keygen.util.SequenceInfo;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class SpELUtil {

    public static EvaluationContext createEvaluationContext(Object object) {
        StandardEvaluationContext context = new StandardEvaluationContext(object);
        context.setVariable("DateUtil", DateUtil.class);
        context.setVariable("SequenceInfo", SequenceInfo.class);
        context.setVariable("StringUtil", StringUtil.class);
        return context;
    }

    public static EvaluationContext createEvaluationContext(Object object, Map<String, Object> data) {
        EvaluationContext context = createEvaluationContext(object);
        for (Map.Entry<String, Object> entry : data.entrySet())
            context.setVariable(entry.getKey(), entry.getValue());
        return context;
    }

    public static Expression getExpression(String el) {
        SpelExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(el);
    }

    public static void main(String[] args) {
//		StandardEvaluationContext context = new StandardEvaluationContext(object);
//		context.setVariable("DateUtil", DateUtil.class);
//		context.setVariable("SequenceInfo", SequenceInfo.class);
//		context.setVariable("StringUtil", StringUtil.class);
//		return expression.getValue(context, String.class);


//		SpelExpressionParser parser = new SpelExpressionParser();
//		
//		Expression expression = parser.parseExpression("'SN_' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(123, 5)");
//		User user = new User();
//		user.setUsername("xxxx");
//		
//		StandardEvaluationContext context = new StandardEvaluationContext(user);
//		context.setVariable("DateUtil", DateUtil.class);
//		context.setVariable("StringUtil", StringUtil.class);
//		
//		System.out.println(expression.getValue(context,String.class));

//		Goods goods = new Goods();
//		goods.setMarketable(true);
//		
//		SpelExpressionParser parser = new SpelExpressionParser();
//		System.out.println(getExpression(" marketable = true ").getValue(createEvaluationContext(goods),boolean.class));

    }


}
