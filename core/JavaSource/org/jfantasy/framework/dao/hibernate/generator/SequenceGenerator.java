package org.jfantasy.framework.dao.hibernate.generator;

import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Properties;

/**
 * 自定义序列生成器
 * 
 * @author 李茂峰
 * @since 2013-1-14 下午02:07:25
 * @version 1.0
 */
public class SequenceGenerator implements IdentifierGenerator, Configurable {

	@Autowired
	private DataBaseKeyGenerator baseKeyGenerator;

	public final static String KEY_NAME = "keyName";

	private String keyName;

	@Override
	public void configure(Type type, Properties params, Dialect d) throws MappingException {
		this.keyName =  StringUtil.defaultValue(params.getProperty(KEY_NAME),params.getProperty("target_table")+":"+params.getProperty("target_column")).toLowerCase();
	}

	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		if (ObjectUtil.isNull(this.baseKeyGenerator)) {
			SpringContextUtil.autowireBean(this);
		}
		return this.baseKeyGenerator.nextValue(StringUtil.defaultValue(keyName, ClassUtil.getRealClass(object).getName()));
	}

}