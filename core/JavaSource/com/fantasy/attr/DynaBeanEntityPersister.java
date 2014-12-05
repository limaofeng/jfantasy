package com.fantasy.attr;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.QueryException;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.CascadeStyles;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.Type;

public class DynaBeanEntityPersister extends SingleTableEntityPersister implements EntityPersister {

    public DynaBeanEntityPersister(PersistentClass persistentClass, EntityRegionAccessStrategy cacheAccessStrategy, NaturalIdRegionAccessStrategy naturalIdRegionAccessStrategy, SessionFactoryImplementor factory, Mapping mapping) throws HibernateException {
        super(persistentClass, cacheAccessStrategy, naturalIdRegionAccessStrategy, factory, mapping);
    }

    public DynaBeanEntityPersister(EntityBinding entityBinding, EntityRegionAccessStrategy cacheAccessStrategy, NaturalIdRegionAccessStrategy naturalIdRegionAccessStrategy, SessionFactoryImplementor factory, Mapping mapping) throws HibernateException {
        super(entityBinding, cacheAccessStrategy, naturalIdRegionAccessStrategy, factory, mapping);
    }

    public int countSubclassProperties() {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery()) {
            return super.countSubclassProperties() + dynaBeanQuery.countSubclassProperties();
        }
        return super.countSubclassProperties();
    }

    public Type getSubclassPropertyType(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        int count = super.countSubclassProperties();
        if (dynaBeanQuery.isDynamicQuery() && i >= count) {
            return dynaBeanQuery.getSubclassPropertyType(i - count,this.getFactory());
        }
        return super.getSubclassPropertyType(i);
    }

    public String[] toColumns(String columnQualifier, int propertyIndex) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        int count = super.countSubclassProperties();
        if (dynaBeanQuery.isDynamicQuery() && propertyIndex >= count) {
            return dynaBeanQuery.toColumns(columnQualifier, propertyIndex - count);
        }
        return super.toColumns(columnQualifier, propertyIndex);
    }

    public boolean isSubclassPropertyNullable(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        return dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties() || super.isSubclassPropertyNullable(i);
    }

    public int getSubclassPropertyTableNumber(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties()) {
            return 0;
        }
        return super.getSubclassPropertyTableNumber(i);
    }

    public String getSubclassPropertyTableName(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties()) {
            return super.getSubclassPropertyTableName(0);
        }
        return super.getSubclassPropertyTableName(i);
    }

    public String[] toColumns(String alias, String propertyName) throws QueryException {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.inColumn(propertyName)) {
            return dynaBeanQuery.toColumns(alias, propertyName);
        }
        return super.toColumns(alias, propertyName);
    }

    public String getSubclassPropertyName(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties()) {
            return dynaBeanQuery.getSubclassPropertyName(i-super.countSubclassProperties());
        }
        return super.getSubclassPropertyName(i);
    }

    public FetchMode getFetchMode(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties()) {
            return FetchMode.DEFAULT;
        }
        return super.getFetchMode(i);
    }

    public CascadeStyle getCascadeStyle(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties()) {
            return CascadeStyles.NONE;
        }
        return super.getCascadeStyle(i);
    }

    public String[] getSubclassPropertyColumnNames(int i) {
        DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
        if (dynaBeanQuery.isDynamicQuery() && i >= super.countSubclassProperties()) {
            return dynaBeanQuery.getSubclassPropertyColumnNames(i - super.countSubclassProperties());
        }
        return super.getSubclassPropertyColumnNames(i);
    }

    @Override
    public Type toType(String propertyName) throws QueryException {
        try {
            return super.toType(propertyName);
        } catch (QueryException e) {
            DynaBeanQuery dynaBeanQuery = DynaBeanQueryManager.getManager().peek();
            if (dynaBeanQuery.inColumn(propertyName)) {
                return dynaBeanQuery.toType(propertyName,this.getFactory());
            }
            throw e;
        }
    }


}
