package org.jfantasy.framework.dao.hibernate.event;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.proxy.HibernateProxy;
import org.jfantasy.framework.dao.hibernate.spi.IdentifierGeneratorUtil;

import java.io.Serializable;

/**
 * 使 GenericGenerator 注解支持非注解的字段生成
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-9 下午10:12:33
 */
public class PropertyGeneratorSaveOrUpdatEventListener extends DefaultSaveOrUpdateEventListener {

    private static final long serialVersionUID = -2369176546449741726L;

    private IdentifierGeneratorFactory identifierGeneratorFactory;

    public PropertyGeneratorSaveOrUpdatEventListener(IdentifierGeneratorFactory identifierGeneratorFactory){
        this.identifierGeneratorFactory = identifierGeneratorFactory;
    }

    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        final SessionImplementor source = event.getSession();
        final Object object = event.getObject();
        final Serializable requestedId = event.getRequestedId();
        if (requestedId != null && object instanceof HibernateProxy) {
            ((HibernateProxy) object).gethibernate_lazy_initializer().setIdentifier(requestedId);
        }
        if (!reassociateIfUninitializedProxy(object, source)) {
            final Object entity = source.getPersistenceContext().unproxyAndReassociate(object);
            EntityEntry entityEntry = source.getPersistenceContext().getEntry(entity);
            EntityState entityState = getEntityState(entity, entity.getClass().getName(), entityEntry, event.getSession());
            IdentifierGeneratorUtil.initialize(entityState,event.getSession(),object,identifierGeneratorFactory);
        }
        super.onSaveOrUpdate(event);
    }

}
