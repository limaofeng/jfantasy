package com.fantasy.framework.dao.hibernate.event;

import com.fantasy.framework.dao.hibernate.util.TypeFactory;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使 GenericGenerator 注解支持非注解的字段生成
 * 
 * @author 李茂峰
 * @since 2013-10-9 下午10:12:33
 * @version 1.0
 */
public class PropertyGeneratorSaveOrUpdatEventListener extends DefaultSaveOrUpdateEventListener {

	private static final long serialVersionUID = -2369176546449741726L;

	private final ConcurrentMap<Class<?>, Map<String, IdentifierGenerator>> generatorCache = new ConcurrentHashMap<Class<?>, Map<String, IdentifierGenerator>>();

    private IdentifierGeneratorFactory identifierGeneratorFactory;

    public IdentifierGenerator createIdentifierGenerator(String strategy, Type type, Properties config){
        if(identifierGeneratorFactory == null){
            this.identifierGeneratorFactory = SpringContextUtil.getBeanByType(LocalSessionFactoryBean.class).getConfiguration().getIdentifierGeneratorFactory();
        }
        return identifierGeneratorFactory.createIdentifierGenerator(strategy,type,config);
    }

	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		final SessionImplementor source = event.getSession();
		final Object object = event.getObject();
		final Serializable requestedId = event.getRequestedId();
		if (requestedId != null) {
			if (object instanceof HibernateProxy) {
				((HibernateProxy) object).getHibernateLazyInitializer().setIdentifier(requestedId);
			}
		}
		if (!reassociateIfUninitializedProxy(object, source)) {
			final Object entity = source.getPersistenceContext().unproxyAndReassociate(object);
			EntityEntry entityEntry = source.getPersistenceContext().getEntry(entity);
			EntityState entityState = getEntityState(entity, entity.getClass().getName(), entityEntry, event.getSession());
			if (entityState == EntityState.TRANSIENT) {
				Class<?> entityClass = object.getClass();
				if (!generatorCache.containsKey(entityClass)) {// 获取实体中,需要自动生成的字段
					generatorCache.put(entityClass, new HashMap<String, IdentifierGenerator>());
					Field[] fields = ClassUtil.getDeclaredFields(entityClass, GenericGenerator.class);
					for (Field field : fields) {
						if (!field.isAnnotationPresent(Id.class)) {
							GenericGenerator annotGenerator = field.getAnnotation(GenericGenerator.class);
							Properties properties = new Properties();
							properties.put("entity_name", object.getClass().getName() + "_" + field.getName());
							for (Parameter parameter : annotGenerator.parameters()) {
								properties.put(parameter.name(), parameter.value());
							}
							IdentifierGenerator generator = createIdentifierGenerator(annotGenerator.strategy(), TypeFactory.basic(field.getType().getName()), properties);
							generatorCache.get(entityClass).put(field.getName(), generator);
						}
					}
				}
				// 调用IdentifierGenerator生成
				for (Map.Entry<String, IdentifierGenerator> entry : generatorCache.get(entityClass).entrySet()) {
					OgnlUtil.getInstance().setValue(entry.getKey(), object, entry.getValue().generate(event.getSession(), event.getEntity()));
				}
			}
		}
		super.onSaveOrUpdate(event);
	}

}
