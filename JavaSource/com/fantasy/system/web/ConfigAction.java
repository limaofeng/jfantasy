package com.fantasy.system.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.system.bean.Config;
import com.fantasy.system.bean.ConfigKey;
import com.fantasy.system.bean.ConfigType;
import com.fantasy.system.service.ConfigService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class ConfigAction extends ActionSupport {

    private static final long serialVersionUID = -3470618157257096733L;

    @Resource
    private transient ConfigService configService;

    /**
     * 配置项类别管理
     *
     * @功能描述
     * @return
     */
    public String types() {
        this.attrs.put("types", this.configService.allTypes());
        return SUCCESS;
    }

    /**
     * 跳转配置项列表
     *
     * @功能描述
     * @return
     */
    public String index() {
        this.search(new Pager<Config>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 配置项分页条件查询
     *
     * @功能描述
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Config> pager, List<PropertyFilter> filters) {
        pager = this.configService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    /**
     * 获取配置项树结构
     *
     * @功能描述
     * @param type
     * @param parentCode
     * @return
     */
    public String tree(String type, String parentCode) {
        this.attrs.put(ROOT, this.configService.tree(type, parentCode));
        return JSONDATA;
    }

    /**
     * 保存
     *
     * @功能描述 新增和修改都适用
     * @param config
     * @return
     */
    public String save(Config config) {
        this.configService.save(config);
        this.attrs.put(ROOT, config);
        return JSONDATA;
    }

    /**
     * 根据Id查询配置项信息
     *
     * @功能描述
     * @param key
     * @return
     */
    public String edit(ConfigKey key) {
        this.attrs.put("config", this.configService.get(key));
        return SUCCESS;
    }

    /**
     * 保存配置项分类
     *
     * @功能描述
     * @param configType
     * @return
     */
    public String saveType(ConfigType configType) {
        this.configService.save(configType);
        this.attrs.put(ROOT, configType);
        return JSONDATA;
    }

    public String deletetType(String... codes) {
        this.configService.deleteType(codes);
        return JSONDATA;
    }

    /**
     * 通过configKey返回对应的List
     *
     * @功能描述
     * @param configKey
     * @return
     */
    public String list(String configKey) {
        this.attrs.put("configs", ConfigService.list(configKey));
        return SUCCESS;
    }

    /**
     * 删除配置项
     *
     * @功能描述
     * @param configKeys 数据字典 key
     * @return {String}
     */
    public String delete(ConfigKey... configKeys) {
        this.configService.delete(configKeys);
        return JSONDATA;
    }

}
