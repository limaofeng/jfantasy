package com.fantasy.attr.framework;

import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.attr.storage.service.AttributeVersionService;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            List<AttributeVersion> versions = attributeVersionService.getAttributeVersions();
            for (AttributeVersion version : versions) {
                if (ClassUtil.forName(version.getTargetClassName()) == null) {
                    LOG.debug("target:" + version.getTargetClassName());
                    continue;
                }
                makeClass(version);
            }
        } finally {
            transactionManager.commit(status);
        }
    }

    public static Class makeClass(AttributeVersion version) {
        String className = version.getClassName();
        String superClass = version.getTargetClassName();
        List<Property> properties = new ArrayList<Property>();
        for (Attribute attribute : version.getAttributes()) {
            final Property property = new Property(attribute.getCode(), ClassUtil.forName(attribute.getAttributeType().getDataType()));
            properties.add(property);
        }
        Class clazz = AsmUtil.makeClass(className, superClass, properties.toArray(new Property[properties.size()]));
        LOG.debug("dynaBeanClass:" + clazz);
        return clazz;
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
