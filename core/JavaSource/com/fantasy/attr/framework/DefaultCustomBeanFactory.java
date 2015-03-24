package com.fantasy.attr.framework;

import com.fantasy.attr.framework.converter.PrimitiveTypeConverter;
import com.fantasy.attr.framework.util.AttributeTypeUtils;
import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.attr.storage.service.AttributeTypeService;
import com.fantasy.attr.storage.service.AttributeVersionService;
import com.fantasy.attr.storage.service.ConverterService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.FantasyClassLoader;
import com.fantasy.framework.util.asm.AsmUtil;
import com.fantasy.framework.util.asm.Property;
import com.fantasy.framework.util.common.ClassUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 默认的自定义Bean工厂
 */
@Component
@Lazy(false)
public class DefaultCustomBeanFactory implements CustomBeanFactory, InitializingBean {

    private static final Log LOG = LogFactory.getLog(DefaultCustomBeanFactory.class);

    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private AttributeTypeService attributeTypeService;
    @Autowired
    private ConverterService converterService;

    @Override
    public void afterPropertiesSet() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            this.initAttributeTypes();
            List<AttributeVersion> versions = attributeVersionService.getAttributeVersions();
            for (AttributeVersion version : versions) {
                if (ClassUtil.forName(version.getTargetClassName()) == null) {
                    LOG.debug("target:" + version.getTargetClassName());
                    continue;
                }
                if (ClassUtil.forName(version.getClassName()) != null) {
                    continue;
                }
                makeClass(version);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            transactionManager.rollback(status);
        }
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
            AttributeType _attributeType = attributeTypeService.findUniqueByJavaType(attributeType.getDataType());
            if (_attributeType != null) {
                attributeType.setId(_attributeType.getId());
            }
            attributeTypeService.save(attributeType);
        }
    }

    public Class makeClass(AttributeVersion version) {
        String className = version.getClassName();
        String superClass = version.getType() == AttributeVersion.Type.custom ? Object.class.getName() : version.getTargetClassName();
        List<Property> properties = new ArrayList<Property>();
        for (Attribute attribute : version.getAttributes()) {
            String attrClassName = attribute.getAttributeType().getDataType();
            Class<?> javaType = ClassUtil.forName(attrClassName);
            if (javaType == null) {
                LOG.debug(" javaType : " + attrClassName + " load Failure ！ ");
                javaType = makeClass(attrClassName);
            }
            if (javaType == null) {
                continue;
            }
            properties.add(new Property(attribute.getCode(), javaType));
        }
        Class[] iters = new Class[0];
        if (version.getType() == AttributeVersion.Type.custom) {
            properties.add(new Property("id", Long.class));
            iters = new Class[]{CustomBean.class};
        }
        Class clazz = AsmUtil.makeClass(className, superClass, iters, properties.toArray(new Property[properties.size()]));
        LOG.debug("dynaBeanClass:" + clazz);
        return clazz;
    }

    private Class<?> makeClass(String attrClassName) {
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
        FantasyClassLoader.getClassLoader().removeClass(version.getClassName());
    }

}
