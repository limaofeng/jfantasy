package com.fantasy.common.web;

import com.fantasy.common.bean.JdbcConfig;
import com.fantasy.common.service.JdbcConfigService;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class JdbcConfigAction extends ActionSupport {

    @Autowired
    private JdbcConfigService jdbcConfigService;

    public String index(List<PropertyFilter> filters) {
        this.search(filters);
        this.attrs.put("list", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.jdbcConfigService.find(filters));
        return JSONDATA;
    }


    public String edit(String id) {
        this.attrs.put("jdbc", this.jdbcConfigService.get(id));
        return JSONDATA;
    }

    public String save(JdbcConfig jdbcConfig) {
        this.jdbcConfigService.save(jdbcConfig);
        return JSONDATA;
    }

    public String test(JdbcConfig jdbcConfig) {
        this.attrs.put("flag", this.jdbcConfigService.testConnection(jdbcConfig));
        return JSONDATA;
    }

    public String delete(Long... ids ){
        this.jdbcConfigService.delete(ids);
        return JSONDATA;
    }

}
