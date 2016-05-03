package org.jfantasy.common.service;

import org.jfantasy.common.bean.JdbcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class DataSourceFactory {

    private final static ConcurrentMap<Long, DataSource> dataSourceCache = new ConcurrentHashMap<Long, DataSource>();

    private Map<String, String> drivers = new HashMap<String, String>();
    private Map<String, String> driverUrls = new HashMap<String, String>();

    @Autowired
    private JdbcConfigService jdbcConfigService;

    public DataSourceFactory() {

    }

    public DataSource getDataSource(Long id) {
        if (dataSourceCache.isEmpty()) {
            this.initialize();
        }
        return dataSourceCache.get(id);
    }

    public void initialize() {
        for (final JdbcConfig config : this.jdbcConfigService.find()) {
            if (dataSourceCache.containsKey(config.getId())) {
                //TODO 数据库
            }
        }
    }

}
