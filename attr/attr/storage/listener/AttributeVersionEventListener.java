package org.jfantasy.attr.storage.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;
import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.attr.storage.dao.AttributeDao;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AttributeVersionEventListener implements DeleteEventListener {
    private final static Log LOGGER = LogFactory.getLog(AttributeVersionEventListener.class);
    @Autowired
    private AttributeDao attributeDao;

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        Class<?> clazz = ClassUtil.forName(event.getEntityName());
        clazz = ObjectUtil.defaultValue(clazz, event.getObject().getClass());
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            LOGGER.debug(">>>>>>>");
        }
    }

    //删除 Version 时 删除 Attribute
    @Override
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        Class<?> clazz = ClassUtil.forName(event.getEntityName());
        assert clazz != null;
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            AttributeVersion version = (AttributeVersion) event.getObject();
            for (Attribute attribute : version.getAttributes()) {
                if (!attribute.getNotTemporary()) {
                    attributeDao.delete(attribute.getId());
                }
            }
        }
    }

}
