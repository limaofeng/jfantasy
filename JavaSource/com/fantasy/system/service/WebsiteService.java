package com.fantasy.system.service;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.FileManagerService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.system.bean.Setting;
import com.fantasy.system.bean.Website;
import com.fantasy.system.dao.SettingDao;
import com.fantasy.system.dao.WebsiteDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WebsiteService implements InitializingBean {

    private static final Log logger = LogFactory.getLog(WebsiteService.class);

    @Resource
    private WebsiteDao websiteDao;
    @Resource
    private SettingDao settingDao;
    @Resource
    private FileManagerService fileManagerService;

    @Override
    public void afterPropertiesSet() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 初始化商品根目录
            StringBuffer log = new StringBuffer("初始化默认站点");
            Website webSite = this.findUniqueByKey("haolue");
            if (webSite == null) {
                webSite = new Website();
                webSite.setKey("haolue");
                webSite.setName("上海昊略信息技术有限公司");
                webSite.setWeb("http://haolue.jfantasy.org");
            }
            // 添加默认文件管理器
            FileManagerConfig config = fileManagerService.get(webSite.getKey() + "-default");
            if (config == null) {
                fileManagerService.save(config = FileManagerConfig.newInstance(webSite.getKey() + "-default", "默认文件管理器", "/home/" + webSite.getKey(), "默认文件管理器,请勿删除"));
            }
            webSite.setDefaultFileManager(config);

            // 添加上传文件管理器
            FileManagerConfig ufm = fileManagerService.get(webSite.getKey() + "-upload");
            if (ufm == null) {
                fileManagerService.save(ufm = FileManagerConfig.newInstance(webSite.getKey() + "-upload", "默认文件上传管理器", FileManagerConfig.newInstance(webSite.getKey() + "-default"), "默认文件上传管理器"));
            }
            webSite.setDefaultUploadFileManager(ufm);

            this.websiteDao.save(webSite);
            // 初始化参数
            List<Setting> settings = new ArrayList<Setting>();
            settings.add(Setting.newInstance(webSite, "网站头部标题", "title", "昊略信息技术有限公司网站", ""));
            settings.add(Setting.newInstance(webSite, "所有权", "copyright", "2014版权所有 All Right Reserved", ""));
            settings.add(Setting.newInstance(webSite, "欢迎页面标题", "wel_tle", "欢迎使昊略信息技术有限公司-网站后台管理系统", ""));
            settings.add(Setting.newInstance(webSite, "欢迎页面描述", "wel_des", "昊略-网站后台管理系统***************************", ""));
            settings.add(Setting.newInstance(webSite, "版本", "wel_ver", "V1.0.1", "版本号"));
            settings.add(Setting.newInstance(webSite, "所有权", "wel_copyr", "@copyright 2014", "所有权"));
            settings.add(Setting.newInstance(webSite, "网站图标", "icon", "/images/rex04.ico", "标题上的icon图片"));
            settings.add(Setting.newInstance(webSite, "应用地址", "serverUrl", "http://localhost:8080", ""));

            for (Setting setting : settings) {
                if (webSite.getSettings() == null || ObjectUtil.find(webSite.getSettings(), "key", setting.getKey()) == null) {
                    settingDao.save(setting);
                }
            }

            logger.debug(log);
        } finally {
            transactionManager.commit(status);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Website findUniqueByKey(String key) {
        Website website = this.websiteDao.findUniqueBy("key", key);
        for(Setting setting : website.getSettings()){
            Hibernate.initialize(setting);
        }
        Hibernate.initialize(website.getRootMenu());
        return website;
    }

    public Website get(Long id) {
        Website website = websiteDao.get(id);
        Hibernate.initialize(website);
        return website;
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.websiteDao.delete(id);
        }
    }

    public Pager<Website> findPager(Pager<Website> pager, List<PropertyFilter> filters) {
        return websiteDao.findPager(pager, filters);
    }

    public Website save(Website website) {
        websiteDao.save(website);
        return website;
    }

    public boolean websiteCodeUnique(String key, Long id) {
        Website website = this.websiteDao.findUniqueBy("key", key);
        return (website == null) || website.getId().equals(id);
    }

    public List<Website> getAll(){
        return this.websiteDao.getAll();
    }

    /**
     * 获取列表
     *
     * @return
     */
    public List<Website> listWebsite() {
        return this.websiteDao.find(new Criterion[0], "id", "asc");
    }

    /**
     * 静态获取列表
     *
     * @return
     */
    public static List<Website> websiteList() {
        WebsiteService websiteService = SpringContextUtil.getBeanByType(WebsiteService.class);
        return websiteService.listWebsite();
    }
}
