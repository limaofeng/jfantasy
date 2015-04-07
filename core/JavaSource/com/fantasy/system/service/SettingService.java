package com.fantasy.system.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.system.bean.Setting;
import com.fantasy.system.dao.SettingDao;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Lazy(false)
public class SettingService {

    @Autowired
    private SettingDao settingDao;

    public Setting get(Long id) {
        Setting setting = settingDao.get(id);
        Hibernate.initialize(setting);
        return setting;
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.settingDao.delete(id);
        }
    }

    public Pager<Setting> findPager(Pager<Setting> pager, List<PropertyFilter> filters) {
        return settingDao.findPager(pager, filters);
    }

    public Setting save(Setting setting) {
        settingDao.save(setting);
        return setting;
    }

    public boolean settingCodeUnique(String key, Long websiteId, Long id) {
        Setting setting = this.settingDao.findUnique(Restrictions.eq("key", key), Restrictions.eq("website.id", websiteId));
        return (setting == null) || setting.getId().equals(id);
    }

    public List<Setting> queryNotExitSetting(Long websiteId) {
        List<Setting> preset = this.preset();
        List<Setting> exitSetting = this.settingDao.find(Restrictions.eq("website.id", websiteId), Restrictions.in("key", ObjectUtil.toFieldArray(preset, "key", String.class)));
        for (Setting setting : exitSetting) {
            ObjectUtil.remove(preset, "key", setting.getKey());
        }
        return preset;

    }

    /**
     * 获取设置
     *
     * @param code
     * @param websiteId
     * @return
     */
    public Setting findUnique(String code, Long websiteId) {
        return this.settingDao.findUnique(Restrictions.eq("key", code), Restrictions.eq("website.id", websiteId));
    }

    public List<Setting> preset() {
        return new ArrayList<Setting>(Arrays.asList(new Setting[]{
                Setting.newInstance("system", "系统名称"),
                Setting.newInstance("systemVersion", "系统版本"),
                Setting.newInstance("copyright", "所有权"),
                Setting.newInstance("icon", "网站图标"),
                Setting.newInstance("title", "网站头部标题"),
                Setting.newInstance("serverUrl", "应用地址"),
                Setting.newInstance("cms", "cms文章栏目"),
                Setting.newInstance("goods", "商品分类")
        }));
    }
}
