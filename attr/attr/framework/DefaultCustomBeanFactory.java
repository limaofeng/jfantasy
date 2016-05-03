package org.jfantasy.attr.framework;

import com.fasterxml.jackson.core.type.TypeReference;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.jfantasy.attr.framework.converter.PrimitiveTypeConverter;
import org.jfantasy.attr.framework.util.AttributeTypeUtils;
import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.attr.storage.bean.AttributeType;
import org.jfantasy.attr.storage.bean.AttributeValue;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.attr.storage.service.AttributeTypeService;
import org.jfantasy.attr.storage.service.AttributeVersionService;
import org.jfantasy.attr.storage.service.ConverterService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.FantasyClassLoader;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.framework.util.asm.Property;
import org.jfantasy.framework.util.cglib.CglibUtil;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认的自定义Bean工厂
 */
@Component
//@Lazy(false)
@Scope(proxyMode = ScopedProxyMode.NO)
public class DefaultCustomBeanFactory implements CustomBeanFactory/*, InitializingBean*/ {

    private static final Log LOG = LogFactory.getLog(DefaultCustomBeanFactory.class);

    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private ConverterService converterService;

    private ConcurrentMap<String, AttributeVersion> versions = new ConcurrentHashMap<String, AttributeVersion>();

    public void afterPropertiesSet() throws Exception {
        long start = System.currentTimeMillis();
        JdbcUtil.transaction(new JdbcUtil.Callback<Void>() {
            @Override
            public Void run() {
                    DefaultCustomBeanFactory.this.initAttributeTypes();
                    for (AttributeVersion version : attributeVersionService.getAttributeVersions()) {
                        if (ClassUtil.forName(version.getTargetClassName()) == null) {
                            LOG.debug("target:" + version.getTargetClassName() + "\t" + version.getNumber());
                            continue;
                        }
                        makeClass(version);
                    }
                return null;
            }
        }, TransactionDefinition.PROPAGATION_REQUIRED);
        LOG.error("\n初始化 CustomBeanFactory 耗时:" + (System.currentTimeMillis() - start) + "ms");
    }

    public void initAttributeTypes() {
        converterService.save(PrimitiveTypeConverter.class, "基本数据类型转换器", "class");

        List<AttributeType> attributeTypes = new ArrayList<AttributeType>();

        attributeTypes.add(AttributeTypeUtils.primitive("Integer", Integer.class));
        attributeTypes.add(AttributeTypeUtils.primitive("Integer[]", Integer[].class));
        attributeTypes.add(AttributeTypeUtils.primitive("Float", Float.class));
        attributeTypes.add(AttributeTypeUtils.primitive("Float[]", Float[].class));
        attributeTypes.add(AttributeTypeUtils.primitive("String", String.class));
        attributeTypes.add(AttributeTypeUtils.primitive("String[]", String[].class));
        attributeTypes.add(AttributeTypeUtils.primitive("Date", Date.class));
        attributeTypes.add(AttributeTypeUtils.primitive("Date[]", Date[].class));

        for (AttributeType attributeType : attributeTypes) {
            AttributeType eAttributeType = attributeTypeService.findUniqueByJavaType(attributeType.getDataType());
            if (eAttributeType != null) {
                attributeType.setId(eAttributeType.getId());
            }
            attributeTypeService.save(attributeType);
        }
    }

    public Class makeClass(AttributeVersion version) {
        try {
            String className = version.getClassName();
            String superClass = version.getType() == AttributeVersion.Type.custom ? Object.class.getName() : version.getTargetClassName();
            List<Property> properties = new ArrayList<Property>();
            for (Attribute attribute : version.getAttributes()) {
                String attrClassName = attribute.getAttributeType().getDataType();
                Class<?> javaType = ClassUtil.forName(attrClassName);
                if (javaType == null) {
                    LOG.debug(" javaType : " + attrClassName + " load Failure ！ ");
                    //如果class为数组的话，取原对象
                    boolean array = RegexpUtil.isMatch(attrClassName, "^\\[L[a-zA-Z._]+;$");
                    javaType = makeClass(RegexpUtil.replace(attrClassName, "^\\[L|;$", ""));
                    javaType = array ? Array.newInstance(javaType, 0).getClass() : javaType;
                }
                if (javaType == null) {
                    continue;
                }
                Property property = new Property(attribute.getCode(), javaType);
                properties.add(property);
                Hibernate.initialize(attribute.getAttributeType().getConverter());
            }
            Class[] iters = new Class[0];
            if (version.getType() == AttributeVersion.Type.custom) {
                properties.add(new Property("id", Long.class));
                iters = new Class[]{CustomBean.class};
            }
            Class clazz = AsmUtil.makeClass(className, superClass, iters, properties.toArray(new Property[properties.size()]));
            LOG.debug("dynaBeanClass:" + clazz);
            return clazz;
        } finally {
            this.versions.put(version.getClassName(), version);
        }
    }

    private <T> Class<T> makeClass(String attrClassName) {
        AttributeVersion version = attributeVersionService.findUniqueByTargetClassName(attrClassName);
        if (version != null) {
            return makeClass(version);
        }
        int index = attrClassName.indexOf(ClassUtil.CGLIB_CLASS_SEPARATOR);
        if (index != -1) {
            version = attributeVersionService.findUniqueByTargetClassName(attrClassName.substring(0, index), attrClassName.substring(index + ClassUtil.CGLIB_CLASS_SEPARATOR.length()));
            if (version != null) {
                return makeClass(version);
            }
        }
        return null;
    }

    @Override
    public Class loadVersion(AttributeVersion version) {
        return makeClass(version);
    }

    @Override
    public void removeVersion(AttributeVersion version) {
        try {
            FantasyClassLoader.getClassLoader().removeClass(version.getClassName());
        } finally {
            versions.remove(version.getClassName());
        }
    }

    public DynaBean makeDynaBean(DynaBean bean) {
        if (bean.getVersion() == null) {
            return bean;
        }
        DynaBean dynaBean = CglibUtil.newInstance(ClassUtil.getRealClass(bean), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o, objects);
            }
        });
        dynaBean.setTarget(bean);
        //DynaBean dynaBean = makeDynaBean(bean.getVersion().getTargetClassName(), bean.getVersion().getNumber());
        return dynaBean;//createDynaBean(dynaBean, bean);
    }

            public DynaBean makeDynaBean(String className, String number) {
                AttributeVersion version = getVersion(className, number);
                assert version != null;
                return createDynaBean(ClassUtil.forName(version.getClassName()), version);
            }

            private AttributeVersion getVersion(String className, String number) {
                return versions.get(className + ClassUtil.CGLIB_CLASS_SEPARATOR + number);
            }

            public DynaBean createDynaBean(Class clazz, AttributeVersion version) {
                final Class realClass =  ClassUtil.getRealClass(clazz);
                DynaBean target = (DynaBean)ClassUtil.newInstance(realClass);
                assert target != null;
                target.setVersion(version);
                DynaBean dynaBean = (DynaBean)CglibUtil.newInstance(clazz, new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        if(method.getName().equals("setTarget") || method.getName().equals("getTarget")){
                            return methodProxy.invokeSuper(o, objects);
                        }
                        Object target = o instanceof DynaBean ? ((DynaBean)o).getTarget() : null;
                        if(ClassUtil.hasMethod(realClass,method.getName(),method.getParameterTypes())){
                            return methodProxy.invoke(target, objects);
                        }else{
                            return methodProxy.invokeSuper(o, objects);
                        }
                    }
                });
                dynaBean.setTarget(target);
                return dynaBean;
            }

            private DynaBean newInstance(Class clazz) {
                return (DynaBean) ClassUtil.newInstance(clazz);
            }

            private DynaBean createDynaBean(DynaBean dynaBean, DynaBean bean) {
                dynaBean.setAttributeValues(new ArrayList<AttributeValue>());
                List<AttributeValue> attributeValue = StringUtil.isBlank(bean.getAttributeValueStore()) ? bean.getAttributeValues() : JSON.deserialize(bean.getAttributeValueStore(), new TypeReference<List<AttributeValue>>() {
                });
                for (Attribute attribute : bean.getVersion().getAttributes()) {
                    AttributeValue sourceValue = ObjectUtil.find(attributeValue, "attribute.code", attribute.getCode());
                    if (sourceValue != null && StringUtil.isNotBlank(sourceValue.getValue())) {
                        getOgnlUtil(attribute.getAttributeType()).setValue(attribute.getCode(), dynaBean, sourceValue.getValue());
                    }
                }
                BeanUtil.copyProperties(dynaBean, bean, "version", "attributeValues");
                return dynaBean;
            }

            public Attribute getAttribute(Class<?> entityClass, String propertyName) {
                for (AttributeVersion version : getVersionsByEntityClass(entityClass)) {
                    AttributeVersion attributeVersion = getVersion(entityClass.getName(), version.getNumber());
                    String simpleName = propertyName.contains(".") ? propertyName.substring(0, propertyName.indexOf(".")) : propertyName;
                    assert attributeVersion != null;
                    Attribute attribute = ObjectUtil.find(attributeVersion.getAttributes(), "code", simpleName);
                    if (attribute != null) {
                        org.jfantasy.framework.util.reflect.Property property = ClassUtil.getProperty(ClassUtil.forName(attribute.getAttributeType().getDataType()), RegexpUtil.replaceFirst(propertyName, simpleName + ".", ""));
                        if (property != null) {
                            return attribute;
                        }
                    }
                }
                return null;
            }

            private List<AttributeVersion> getVersionsByEntityClass(Class<?> entityClass) {
                List<AttributeVersion> _versions = new ArrayList<AttributeVersion>();
                for (AttributeVersion version : this.versions.values()) {
                    if (entityClass.getName().equals(version.getTargetClassName())) {
                        _versions.add(version);
                    }
                }
                return _versions;
            }

            public OgnlUtil getOgnlUtil(AttributeType attributeType) {
                if (!OgnlUtil.containsKey("attr-" + attributeType.getId())) {
                    OgnlUtil.getInstance("attr-" + attributeType.getId()).addTypeConverter(ClassUtil.forName(attributeType.getDataType()), (TypeConverter) SpringContextUtil.createBean(ClassUtil.forName(attributeType.getConverter().getTypeConverter()), SpringContextUtil.AutoType.AUTOWIRE_BY_TYPE));
                }
                return OgnlUtil.getInstance("attr-" + attributeType.getId());
            }

            @SuppressWarnings(value = "unchecked")
            @Override
            public <T extends DynaBean> T makeDynaBean(Class<T> clazz, String number) {
                return (T) makeDynaBean(clazz.getName(), number);
            }

        }
