package com.fantasy.attr.storage.listener;

import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.attr.storage.dao.AttributeDao;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AttributeVersionEventListener implements DeleteEventListener {

    @Autowired
    private AttributeDao attributeDao;

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        Class<?> clazz = ClassUtil.forName(event.getEntityName());
        clazz = ObjectUtil.defaultValue(clazz, event.getObject().getClass());
        if (AttributeVersion.class.isAssignableFrom(clazz)) {
            System.out.println(">>>>>>>");
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
