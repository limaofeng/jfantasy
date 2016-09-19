package org.jfantasy.framework.dao.hibernate.generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.jfantasy.framework.spring.SpELUtil;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.Serializable;
import java.util.Properties;

public class SerialNumberGenerator implements IdentifierGenerator, Configurable {

	private SpelExpressionParser parser = new SpelExpressionParser();
	private Expression expression;

	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		this.expression = parser.parseExpression(params.getProperty("expression"));
	}

	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		return expression.getValue(SpELUtil.createEvaluationContext(object), String.class);
	}

}
