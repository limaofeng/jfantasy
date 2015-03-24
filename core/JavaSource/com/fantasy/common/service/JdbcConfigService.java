package com.fantasy.common.service;

import com.fantasy.common.bean.JdbcConfig;
import com.fantasy.common.dao.JdbcConfigDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class JdbcConfigService {

    @Autowired
    private JdbcConfigDao jdbcConfigDao;

    public List<JdbcConfig> find(){
        return this.jdbcConfigDao.find();
    }

    public List<JdbcConfig> find(List<PropertyFilter> filters) {
        return this.jdbcConfigDao.find(filters);
    }

    public JdbcConfig get(String id) {
        return this.jdbcConfigDao.findUniqueBy("id", id);
    }

    public static JdbcConfig getJdbc(String id) {
        JdbcConfigService jdbcConfigService = SpringContextUtil.getBeanByType(JdbcConfigService.class);
        return jdbcConfigService.get(id);
    }

    public Pager<JdbcConfig> findPager(Pager<JdbcConfig> pager, List<PropertyFilter> filters) {
        return this.jdbcConfigDao.findPager(pager, filters);
    }

    public JdbcConfig save(JdbcConfig jdbcConfig) {
        this.jdbcConfigDao.save(jdbcConfig);
        return jdbcConfig;
    }

    public Boolean testConnection(JdbcConfig jdbcConfig) {
        return true;
    }

    /**
     * 获取列表
     *
     * @return
     */
    public List<JdbcConfig> listJdbc() {
        return this.jdbcConfigDao.find(new Criterion[0], "id", "asc");
    }

    /**
     * 静态获取列表
     *
     * @return
     */
    public static List<JdbcConfig> jdbcConfigs() {
        JdbcConfigService jdbcConfigService = SpringContextUtil.getBeanByType(JdbcConfigService.class);
        return jdbcConfigService.listJdbc();
    }


    public void delete(Long... ids){
        for(Long id : ids){
            this.jdbcConfigDao.delete(id);
        }
    }

}

