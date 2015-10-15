package com.fantasy.afterPropertiesSet;

import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.Converter;
import com.fantasy.attr.storage.dao.AttributeTypeDao;
import com.fantasy.attr.storage.dao.ConverterDao;
import com.fantasy.attr.framework.converter.PrimitiveTypeConverter;
import com.fantasy.common.bean.FtpConfig;
import com.fantasy.common.dao.AreaDao;
import com.fantasy.common.service.AreaService;
import com.fantasy.common.service.FtpConfigService;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.security.bean.Menu;
import com.fantasy.security.bean.Role;
import com.fantasy.security.dao.MenuDao;
import com.fantasy.security.service.MenuService;
import com.fantasy.system.bean.DataDictionaryType;
import com.fantasy.system.bean.Setting;
import com.fantasy.system.bean.Website;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Created by hebo on 2014/9/19.
*/
public class AfterPropertiesSet {

    private static final Log logger = LogFactory.getLog(AfterPropertiesSet.class);

    private AttributeTypeDao attributeTypeDao;

    public void AttributeTypeService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 初始化基础分类
            Class<?>[] defaultClass = new Class[] { int.class, long.class, double.class, Date.class, BigDecimal.class, String.class, int[].class, long[].class, double[].class, Date[].class, BigDecimal[].class, String[].class };
            for (Class<?> clazz : defaultClass) {
                AttributeType attributeType = attributeTypeDao.findUniqueBy("dataType", clazz.getName());
                if (attributeType == null) {
                    StringBuffer log = new StringBuffer("初始化属性类型:" + clazz);
                    attributeType = new AttributeType();
                    attributeType.setName(clazz.getSimpleName());
                    attributeType.setDataType(clazz.getName());
                    attributeType.setConverter(null);
                    attributeType.setDescription("基本数据类型:" + clazz);
                    attributeTypeDao.save(attributeType);
                    logger.debug(log);
                }
            }
            // 初始化商品图片目录
        } finally {
            transactionManager.commit(status);
        }
    }

    private ConverterDao converterDao;

    public void ConverterService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 初始化基础分类
            Class<?>[] defaultClass = new Class[] { PrimitiveTypeConverter.class };
            for (Class<?> clazz : defaultClass) {
                Converter converter = converterDao.findUniqueBy("typeConverter", clazz.getName());
                if (converter == null) {
                    StringBuffer log = new StringBuffer("初始化基本数据类型转换器:" + clazz);
                    converter = new Converter();
                    converter.setName("int converter");
                    converter.setTypeConverter(clazz.getName());
                    converter.setDescription("基本数据类型转换器:" + clazz);
                    converterDao.save(converter);
                    logger.debug(log);
                }
            }
        } finally {
            transactionManager.commit(status);
        }
    }

    private AreaDao areaDao;

    public void AreaService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int count = areaDao.count();
            if (count <= 0) {
                // 加载菜单sql脚本
                InputStream is = AreaService.class.getResourceAsStream("/database/area.sql");
                FileUtil.readFile(is, new FileUtil.ReadLineCallback() {
                    @Override
                    public boolean readLine(String line) {
                        for (String sql : StringUtil.tokenizeToStringArray(line, "\n")) {
                            areaDao.batchSQLExecute(sql);
                        }
                        return true;
                    }
                });
                transactionManager.commit(status);
            }
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
        }
    }

    private FtpConfigService ftpConfigService;

    /**
     * 应用启动时加载并初始化全部的FtpService
     *
     * @throws Exception
     */
    public void FtpServiceFactory() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(true);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            for (FtpConfig config : ftpConfigService.getAll()) {
                this.initialize(config);
            }
        } finally {
            transactionManager.commit(status);
        }
    }

    private void initialize(FtpConfig config) {

    }

    private MenuDao menuDao;

    /**
     * 初始化菜单
     */
    public void MenuService() throws Exception {
        List<Menu> menus = menuDao.find();
        if (menus == null || menus.size() <= 0) {
            PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                // 加载菜单sql脚本
                InputStream is = MenuService.class.getResourceAsStream("/database/auth_menu.sql");
                FileUtil.readFile(is, new FileUtil.ReadLineCallback() {
                    @Override
                    public boolean readLine(String line) {
                        for (String sql : StringUtil.tokenizeToStringArray(line, "\n")) {
                            menuDao.batchSQLExecute(sql);
                        }
                        return true;
                    }
                });
                transactionManager.commit(status);
            } catch (RuntimeException e) {
                transactionManager.rollback(status);
            }
        }
    }


    public void RoleService() throws Exception {
        StringBuffer log = new StringBuffer("初始化系统默认系统管理员角色信息");
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Role role = null;//get("SYSTEM");
            if (role == null) {
                role = new Role();
                role.setCode("SYSTEM");
                role.setName("系统管理员");
                role.setEnabled(true);
                role.setDescription("系统默认管理员");
                //save(role);
            }
        } finally {
            transactionManager.commit(status);
        }
        logger.debug(log);
    }

    public void PageService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            /*
            //添加模板
            Template template = new Template();
            template.setName("测试页面");
            template.setType(Template.Type.freeMarker);
            template.setFilePath("template/test.ftl");
            template.setDescription("测试模板");
            //添加数据接口
            List<DataInferface> dataInferfaces = new ArrayList<DataInferface>();
            DataInferface dataInferface = new DataInferface();
            dataInferface.setKey("testData");
            dataInferface.setName("测试数据");
            dataInferface.setDefaultValue("xxxxx");
            dataInferface.setJavaType(String.class.getName());
            dataInferfaces.add(dataInferface);
            template.setDataInferfaces(dataInferfaces);
            //添加页面
            Page page = new Page();
            page.setTemplate(template);
            page.setName("测试页");
            //添加数据定义
            List<Data> datas = new ArrayList<Data>();
            Data data = new Data();
            data.setDataInferface(dataInferface);
            data.setScope(Data.Scope.page);
            data.setCacheInterval(10000l);
            datas.add(data);
            page.setDatas(datas);

            //添加数据解析器
            DataAnalyzer dataAnalyzer = new DataAnalyzer();
            dataAnalyzer.setName("数据解析器");
            dataAnalyzer.setClassName("xxxx");
            data.setDataAnalyzer(dataAnalyzer);

            //添加页面解析器
            PageAnalyzer pageAnalyzer = new PageAnalyzer();
            pageAnalyzer.setName("页面解析器");
            pageAnalyzer.setClassName("xxxxx");
            page.setPageAnalyzer(pageAnalyzer);
            */
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            transactionManager.rollback(status);
        }
    }


    public void DataDictionaryService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            DataDictionaryType ddt = null;//getDataDictionaryType("root");
            if (ddt == null) {
                StringBuffer log = new StringBuffer("初始化数据字典分类跟目录");
                ddt = new DataDictionaryType();
                ddt.setCode("root");
                ddt.setName("数据字典分类");
                //save(ddt);
                logger.debug(log);
            }
        } finally {
            transactionManager.commit(status);
        }
    }



    public void WebsiteService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 初始化商品根目录
            StringBuffer log = new StringBuffer("初始化默认站点");
            Website webSite = null;//this.findUniqueByKey("haolue");
            if (webSite == null) {
                webSite = new Website();
                webSite.setKey("haolue");
                webSite.setName("上海昊略信息技术有限公司");
                webSite.setWeb("http://haolue.jfantasy.org");
            }
            // 添加默认文件管理器
            FileManagerConfig config = null;//fileManagerService.get(webSite.getKey() + "-default");
            if (config == null) {
                //fileManagerService.save(config = FileManagerConfig.newInstance(webSite.getKey() + "-default", "默认文件管理器", "/home/" + webSite.getKey(), "默认文件管理器,请勿删除"));
            }
            webSite.setDefaultFileManager(config);

            // 添加上传文件管理器
            FileManagerConfig ufm = null;//fileManagerService.get(webSite.getKey() + "-upload");
            if (ufm == null) {
                //fileManagerService.save(ufm = FileManagerConfig.newInstance(webSite.getKey() + "-upload", "默认文件上传管理器", FileManagerConfig.newInstance(webSite.getKey() + "-default"), "默认文件上传管理器"));
            }
            webSite.setDefaultUploadFileManager(ufm);

//            this.websiteDao.save(webSite);
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
//                    settingDao.save(setting);
                }
            }

            logger.debug(log);
        } finally {
            transactionManager.commit(status);
        }
    }




}
