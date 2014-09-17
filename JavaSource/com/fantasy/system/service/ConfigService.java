package com.fantasy.system.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.bean.Config;
import com.fantasy.system.bean.ConfigKey;
import com.fantasy.system.bean.ConfigType;
import com.fantasy.system.dao.ConfigDao;
import com.fantasy.system.dao.ConfigTypeDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("fantasy.sys.ConfigService")
@Transactional
public class ConfigService implements InitializingBean {

    @Resource
    private ConfigDao configDao;
    @Resource
    private ConfigTypeDao configTypeDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO 检查并添加网站常量
    }

    @Cacheable(value = "fantasy.sys.ConfigService.ConfigType", key = "'allTypes'")
    public List<ConfigType> allTypes() {
        return configTypeDao.getAll();
    }

    @Cacheable(value = "fantasy.sys.ConfigService.Config", key = "'allConfigs'")
    public List<Config> allConfigs() {
        return configDao.find(new Criterion[0], "sort", "asc");
    }

    public Config get(ConfigKey key) {
        return configDao.get(key);
    }

    public Config get(String key) {
        String[] array = key.split(":");
        return configDao.get(ConfigKey.newInstance(array[1], array[0]));
    }

    /**
     * 查询配置项分类
     *
     * @param name     like查询
     * @param showsize 返回结果条数，默认15条
     * @return
     * @功能描述
     */
    @Cacheable(value = "fantasy.sys.ConfigService.ConfigType", key = "#name + #showsize")
    public List<ConfigType> types(String name, int showsize) {
        showsize = showsize == 0 ? 15 : showsize;
        if (StringUtil.isBlank(name)) {
            return configTypeDao.find(new Criterion[0], 0, showsize);
        } else {
            return configTypeDao.find(new Criterion[]{Restrictions.like("name", name)}, 0, showsize);
        }
    }

    public ConfigType getConfigType(String code) {
        return configTypeDao.findUniqueBy("code", code);
    }

    /**
     * 通过配置项分类及配置项CODE返回配置项
     *
     * @param type 类型
     * @param code 配置项CODE，返回的List顺序与codes的顺序一致
     * @return {Config}
     * @功能描述
     */
    @Cacheable(value = "fantasy.sys.ConfigService.Config", key = "'getUnique' + #type + #code")
    public Config getUnique(String type, String code) {
        return configDao.findUnique(Restrictions.eq("type", type), Restrictions.eq("code", code));
    }

    /**
     * 通过分类及上级编码查询配置项
     *
     * @param type       分类
     * @param parentCode 上级编码
     * @return
     * @功能描述
     */
    public List<Config> list(String type, String parentCode) {
        Criterion[] criterions = new Criterion[]{Restrictions.eq("type", type)};
        if (StringUtil.isNotBlank(parentCode)) {
            criterions = ObjectUtil.join(criterions, Restrictions.eq("parent.code", parentCode));
        }
        return configDao.find(criterions, "sort", "asc");
    }

    /**
     * 分页查询方法
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return
     * @功能描述
     */
    public Pager<Config> findPager(Pager<Config> pager, List<PropertyFilter> filters) {
        return this.configDao.findPager(pager, filters);
    }

    /**
     * 上级编码名称 <br/>
     * 格式如：配置一级名称>配置二级名称>配置三级名称
     *
     * @param type 分类
     * @param code 编码
     * @return
     * @功能描述
     */
    @Cacheable(value = "fantasy.sys.ConfigService.Config", key = "'getParentName' + #type + #code")
    public String getParentName(String type, String code) {
        if (StringUtil.isBlank(code))
            return "";
        Config config = configDao.findUnique(Restrictions.eq("type", type), Restrictions.eq("code", code));
        if (StringUtil.isBlank(config))
            return "";
        return "";
    }

    /**
     * 添加及更新配置项
     *
     * @param config
     * @功能描述
     */
    @CacheEvict(value = {"fantasy.sys.ConfigService.Config"}, allEntries = true)
    public void save(Config config) {
        this.configDao.save(config);
    }

    /**
     * 添加及更新配置项分类方法
     *
     * @param configType
     * @功能描述
     */
    @CacheEvict(value = {"fantasy.sys.ConfigService.ConfigType", "fantasy.sys.ConfigService.Config"}, allEntries = true)
    public void save(ConfigType configType) {
        this.configTypeDao.save(configType);
    }

    /**
     * 删除配置项
     *
     * @param keys 数据字段 key
     * @功能描述
     */
    @CacheEvict(value = {"fantasy.sys.ConfigService.Config"}, allEntries = true)
    public void delete(ConfigKey... keys) {
        for (ConfigKey key : keys) {
            this.configDao.delete(this.get(key));
        }
    }

    @CacheEvict(value = {"fantasy.sys.ConfigService.ConfigType", "fantasy.sys.ConfigService.Config"}, allEntries = true)
    public void deleteType(String... codes) {
        for (String code : codes) {
            ConfigType configType = this.configTypeDao.get(code);
            if (configType == null)
                continue;
            for (Config config : configType.getConfigs()) {
                this.configDao.batchExecute("delete from Config where type = ? and code = ? ", configType.getCode(), config.getCode());
            }
            this.configTypeDao.batchExecute("delete from ConfigType where code = ?", configType.getCode());
        }
    }

    /**
     * 返回配置项的树结构数据
     *
     * @param type       分类
     * @param parentCode 上级编码，为空返回整个分类的树结构
     * @return
     * @功能描述
     */
    @Cacheable(value = "fantasy.sys.ConfigService.Config", key = "'tree' + #type + #parentCode")
    public List<Config> tree(String type, String parentCode) {
        List<Config> configs = configDao.find(new Criterion[]{Restrictions.eq("type", type)}, "sort", "asc");
        Map<String, List<Config>> configMap = new HashMap<String, List<Config>>();
        List<Config> roots = new ArrayList<Config>();
        for (Config config : configs) {
            if (StringUtil.isBlank(config.getParent())) {
                roots.add(config);
            } else {
                if (!configMap.containsKey(config.getParent().getCode())) {
                    configMap.put(config.getParent().getCode(), new ArrayList<Config>());
                }
                configMap.get(config.getParent().getCode()).add(config);
            }
        }
        return loadConfigs(roots, configMap);
    }

    /**
     * tree的辅助方法
     *
     * @param configs
     * @param configMap
     * @return
     * @功能描述
     */
    private List<Config> loadConfigs(List<Config> configs, Map<String, List<Config>> configMap) {
        if (ObjectUtil.isNull(configs))
            return new ArrayList<Config>();
        for (Config config : configs) {
            config.setChildren(loadConfigs(configMap.get(config.getCode()), configMap));
            configMap.remove(config.getCode());
        }
        return (List<Config>) ObjectUtil.sort(configs, "sort", "asc");
    }

    /**
     * 根据分类获取配置项
     *
     * @param configKey
     * @return
     * @功能描述 static method
     */
    public static List<Config> list(String configKey) {
        ConfigService configService = (ConfigService) SpringContextUtil.getBean("fantasy.sys.ConfigService");
        List<Config> configs = new ArrayList<Config>();
        for (Config config : configService.allConfigs()) {
            // 如果带: 返回子配置项， 否则返回分类对应的全部配置项
            if (!StringUtil.defaultValue(configKey, "").contains(":") ? config.getType().equals(configKey) : (config.getParentKey() != null && configKey.equals(config.getParentKey().toString()))) {
                configs.add(config);
            }
        }
        return configs;
    }

    /**
     * 获取所有的配置项类型
     *
     * @return
     * @功能描述 static method
     */
    public static List<ConfigType> types() {
        return ((ConfigService) SpringContextUtil.getBean("fantasy.sys.ConfigService")).allTypes();
    }

    /**
     * 根据配置项类型和编码获取配置项
     *
     * @param type
     * @param code
     * @return
     * @功能描述 static method
     */
    public static Config get(String type, String code) {
        ConfigService configService = (ConfigService) SpringContextUtil.getBean("fantasy.sys.ConfigService");
        return configService.getUnique(type, code);
    }

    public static String getName(String type, String code) {
        Config config = get(type, code);
        return config == null ? type + ":" + code : config.getName();
    }

}
