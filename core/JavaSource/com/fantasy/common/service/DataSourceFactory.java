package com.fantasy.common.service;

import com.fantasy.common.bean.JdbcConfig;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;

@Service
public class DataSourceFactory {

    private final static ConcurrentMap<Long, DataSource> dataSourceCache = new ConcurrentHashMap<Long, DataSource>();

    private Map<String, String> drivers = new HashMap<String, String>();
    private Map<String, String> driverUrls = new HashMap<String, String>();

    @Resource
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
                ProxoolDataSource dataSource = new ProxoolDataSource();
                dataSource.setDriver(drivers.get(config.getType().name()));
                dataSource.setDriverUrl(RegexpUtil.replace(driverUrls.get(config.getType().name()), "\\{a-zA-Z0-9\\}", new RegexpUtil.AbstractReplaceCallBack() {
                    @Override
                    public String doReplace(String text, int index, Matcher matcher) {
                        return String.valueOf(OgnlUtil.getInstance().getValue(text, config));
                    }
                }));
                dataSource.setUser(config.getUsername());
                dataSource.setPassword(config.getPassword());
                dataSource.setAlias("datasource-alias-" + config.getId());
                dataSourceCache.put(config.getId(), dataSource);
            }
        }
    }

}
