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

    public static EvaluationContext createEvaluationContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("DateUtil", DateUtil.class);
        context.setVariable("SequenceInfo", SequenceInfo.class);
        context.setVariable("StringUtil", StringUtil.class);
        return context;
    }

    public static EvaluationContext createEvaluationContext(Map<String, Object> data) {
        EvaluationContext context = createEvaluationContext();
        for (Map.Entry<String, Object> entry : data.entrySet()){
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }

    public static EvaluationContext createEvaluationContext(Object object) {
        StandardEvaluationContext context = new StandardEvaluationContext(object);
        context.setVariable("DateUtil", DateUtil.class);
        context.setVariable("SequenceInfo", SequenceInfo.class);
        context.setVariable("StringUtil", StringUtil.class);
        return context;
    }

    public static EvaluationContext createEvaluationContext(Object object, Map<String, Object> data) {
        EvaluationContext context = createEvaluationContext(object);
        for (Map.Entry<String, Object> entry : data.entrySet()){
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }

    public static Expression getExpression(String el) {
        SpelExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(el);
    }

}
