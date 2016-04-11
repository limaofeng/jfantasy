package org.jfantasy.framework.dao.hibernate.spi;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.internal.AbstractSaveEventListener;
import org.hibernate.event.spi.EventSource;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.jfantasy.framework.dao.hibernate.generator.SequenceGenerator;
import org.jfantasy.framework.dao.hibernate.util.TypeFactory;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IdentifierGeneratorUtil {

    private final static ConcurrentMap<Class<?>, Map<String, IdentifierGenerator>> generatorCache = new ConcurrentHashMap<Class<?>, Map<String, IdentifierGenerator>>();

    public static boolean contains(Class<?> entityClass) {
        if (generatorCache.containsKey(entityClass)) {
            return true;
        }
        generatorCache.put(entityClass, new HashMap<String, IdentifierGenerator>());
        return false;
    }

    public static Set<Map.Entry<String, IdentifierGenerator>> getIdentifierGenerators(Class<?> entityClass) {
        return generatorCache.get(entityClass).entrySet();
    }

    public static void initialize(AbstractSaveEventListener.EntityState entityState, EventSource eventSource,  Object object,IdentifierGeneratorFactory identifierGeneratorFactory) {
        if (entityState == AbstractSaveEventListener.EntityState.TRANSIENT) {
            Class<?> entityClass = object.getClass();
            if (!IdentifierGeneratorUtil.contains(entityClass)) {// 获取实体中,需要自动生成的字段
                Field[] fields = ClassUtil.getDeclaredFields(entityClass, GenericGenerator.class);
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(Id.class)) {
                        GenericGenerator annotGenerator = field.getAnnotation(GenericGenerator.class);
                        Properties properties = new Properties();
                        properties.put(SequenceGenerator.KEY_NAME, entityClass.getAnnotation(Table.class).name() + ":" + field.getAnnotation(Column.class).name());
                        for (Parameter parameter : annotGenerator.parameters()) {
                            properties.put(parameter.name(), parameter.value());
                            IdentifierGenerator generator = identifierGeneratorFactory.createIdentifierGenerator(annotGenerator.strategy(), TypeFactory.basic(field.getType().getName()), properties);
                            generatorCache.get(entityClass).put(field.getName(), generator);
                        }
                    }
                }
            }
            // 调用IdentifierGenerator生成
            for (Map.Entry<String, IdentifierGenerator> entry : IdentifierGeneratorUtil.getIdentifierGenerators(entityClass)) {
                OgnlUtil.getInstance().setValue(entry.getKey(), object, entry.getValue().generate(eventSource, object));
            }
        }
    }

    public static boolean reassociateIfUninitializedProxy(Object object, SessionImplementor source) {
        return source.getPersistenceContext().reassociateIfUninitializedProxy( object );
    }

}
