package org.jfantasy.framework.dao;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class MultiDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = Logger.getLogger(MultiDataSource.class);

    private CatalogConverter catalogConverter;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (ObjectUtil.isNull(catalogConverter)){
            catalogConverter = new SimpleCatalogConverter();
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        String catalog = getCatalog();
        if (ObjectUtil.isNull(getDataSource())) {
            LOGGER.error("没有匹配到DataSource:" + connection);
        }
        if (StringUtil.isNotBlank(catalog)) {
            setCatalog(connection, catalog);
        }
        return connection;
    }

    private org.jfantasy.framework.dao.annotations.DataSource getDataSource() {
        return MultiDataSourceManager.getManager().peek();
    }

    private void setCatalog(Connection connection, String catalog) {
        try {
            connection.setCatalog(catalogConverter.convert(catalog));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public String getCatalog() {
        org.jfantasy.framework.dao.annotations.DataSource dataSource = getDataSource();
        if ((ObjectUtil.isNull(dataSource)) || (StringUtil.isBlank(dataSource.catalog()))) {
            return null;
        }
        return dataSource.catalog();
    }

    @SuppressWarnings("unchecked")
    protected Object determineCurrentLookupKey() {
        org.jfantasy.framework.dao.annotations.DataSource dataSource = getDataSource();
        if (ObjectUtil.isNull(dataSource)) {
            LOGGER.error("没有匹配到DataSource,将使用默认数据源!");
            return null;
        }
        if (containsDataSource(dataSource.name())){
            return dataSource.name();
        }
        try {
            DataSource object = (DataSource) SpringContextUtil.getBean(dataSource.name());
            Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) ClassUtil.getValue(this, "resolvedDataSources");
            Object lookupKey = resolveSpecifiedLookupKey(dataSource.name());
            resolvedDataSources.put(lookupKey, object);
            return dataSource.name();
        } catch (BeansException e) {
            LOGGER.error("没有匹配到DataSource,将使用默认数据源!", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private boolean containsDataSource(String key) {
        Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) ClassUtil.getValue(this, "resolvedDataSources");
        return resolvedDataSources.containsKey(resolveSpecifiedLookupKey(key));
    }

    public void setCatalogConverter(CatalogConverter catalogConverter) {
        this.catalogConverter = catalogConverter;
    }

    public static interface CatalogConverter {

        public String convert(String catalog);

    }

    public static class SimpleCatalogConverter implements CatalogConverter {

        public String convert(String catalog) {
            return catalog;
        }

    }

}