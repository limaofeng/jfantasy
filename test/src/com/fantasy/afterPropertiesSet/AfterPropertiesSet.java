package com.fantasy.afterPropertiesSet;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.Converter;
import com.fantasy.attr.typeConverter.PrimitiveTypeConverter;
import com.fantasy.common.bean.FtpConfig;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.payment.product.*;
import com.fantasy.payment.service.TestPaymentOrderService;
import com.fantasy.security.bean.Menu;
import com.fantasy.security.bean.Role;
import com.fantasy.swp.bean.*;
import com.fantasy.system.bean.DataDictionaryType;
import com.fantasy.system.bean.Setting;
import com.fantasy.system.bean.Website;
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


    public void AreaService() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int count = areaDao.count();
            if (count <= 0) {
                // 加载菜单sql脚本
                InputStream is = AreaService.class.getResourceAsStream("/file/area.sql");
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


    /**
     * 应用启动时加载并初始化全部的FtpService
     *
     * @功能描述
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


    public void PaymentConfiguration() throws Exception {

        //支付宝即时交易
        if (ObjectUtil.find(this.paymentProducts, "id", "alipayDirect") == null) {
            AlipayDirect alipayDirect = new AlipayDirect();
            alipayDirect.setId("alipayDirect");
            alipayDirect.setName("支付宝（即时交易）");
            alipayDirect.setBargainorIdName("合作身份者ID");
            alipayDirect.setBargainorKeyName("安全校验码");
            alipayDirect.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            alipayDirect.setLogoPath("/template/tocer/images/payment/alipay_direct_icon.gif");
            alipayDirect.setDescription("支付宝即时交易，付款后立即到账，无预付/年费，单笔费率阶梯最低0.7%，无流量限制。 <a href=\"https://www.alipay.com/himalayas/practicality_customer.htm?customer_external_id=C4393933195131654818&market_type=from_agent_contract&pro_codes=61F99645EC0DC4380ADE569DD132AD7A\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(alipayDirect);
        }

        //支付宝担保交易
        if (ObjectUtil.find(this.paymentProducts, "id", "alipayPartner") == null) {
            AlipayPartner alipayPartner = new AlipayPartner();
            alipayPartner.setId("alipayPartner");
            alipayPartner.setName("支付宝（担保交易）");
            alipayPartner.setBargainorIdName("合作身份者ID");
            alipayPartner.setBargainorKeyName("安全校验码");
            alipayPartner.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            alipayPartner.setLogoPath("/template/tocer/images/payment/alipay_partner_icon.gif");
            alipayPartner.setDescription("支付宝担保交易，买家先付款到支付宝，支付宝收到买家付款后即时通知卖家发货，买家收到货物满意后通知支付宝付款给卖家。 <a href=\"https://www.alipay.com/himalayas/practicality_customer.htm?customer_external_id=C4393933195131654818&market_type=from_agent_contract&pro_codes=61F99645EC0DC4380ADE569DD132AD7A\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(alipayPartner);
        }

        //财付通即时交易
        if (ObjectUtil.find(this.paymentProducts, "id", "tenpayDirect") == null) {
            TenpayDirect tenpayDirect = new TenpayDirect();
            tenpayDirect.setId("tenpayDirect");
            tenpayDirect.setName("财付通（即时交易）");
            tenpayDirect.setBargainorIdName("商户号");
            tenpayDirect.setBargainorKeyName("安全校验码");
            tenpayDirect.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            tenpayDirect.setLogoPath("/template/tocer/images/payment/tenpay_direct_icon.gif");
            tenpayDirect.setDescription("中国领先的在线支付平台，致力于为互联网用户和企业提供安全、便捷、专业的在线支付服务。 <a href=\"http://union.tenpay.com/mch/mch_register.shtml?sp_suggestuser=admin@shopxx.net\" class=\"red\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(tenpayDirect);
        }

        //财付通（担保交易）
        if (ObjectUtil.find(this.paymentProducts, "id", "tenpayPartner") == null) {
            TenpayPartner tenpayPartner = new TenpayPartner();
            tenpayPartner.setId("tenpayPartner");
            tenpayPartner.setName("财付通（担保交易）");
            tenpayPartner.setBargainorIdName("商户号");
            tenpayPartner.setBargainorKeyName("安全校验码");
            tenpayPartner.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            tenpayPartner.setLogoPath("/template/tocer/images/payment/tenpay_partner_icon.gif");
            tenpayPartner.setDescription("中国领先的在线支付平台，致力于为互联网用户和企业提供安全、便捷、专业的在线支付服务。 <a href=\"http://union.tenpay.com/mch/mch_register.shtml?sp_suggestuser=admin@shopxx.net\" class=\"red\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
            this.paymentProducts.add(tenpayPartner);
        }

        //易宝支付
        if (ObjectUtil.find(this.paymentProducts, "id", "yeepay") == null) {
            Yeepay yeepay = new Yeepay();
            yeepay.setId("yeepay");
            yeepay.setName("易宝支付");
            yeepay.setBargainorIdName("商户编号");
            yeepay.setBargainorKeyName("密钥");
            yeepay.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            yeepay.setLogoPath("/template/tocer/images/payment/yeepay_icon.gif");
            yeepay.setDescription("中国领先的独立第三方支付平台，致力于为广大商家和消费者提供“安全、简单、快乐”的专业电子支付解决方案和服务。");
            this.paymentProducts.add(yeepay);
        }

        //快钱
        if (ObjectUtil.find(this.paymentProducts, "id", "pay99bill") == null) {
            Pay99bill pay99bill = new Pay99bill();
            pay99bill.setId("pay99bill");
            pay99bill.setName("快钱");
            pay99bill.setBargainorIdName("账户号");
            pay99bill.setBargainorKeyName("密钥");
            pay99bill.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
            pay99bill.setLogoPath("/template/tocer/images/payment/pay99bill_icon.gif");
            pay99bill.setDescription("快钱是国内领先的独立第三方支付企业，旨在为各类企业及个人 提供安全、便捷和保密的综合电子支付服务。");
            this.paymentProducts.add(pay99bill);
        }

        //支付订单service
        if(!this.paymentOrderServices.containsKey("test")){
            this.paymentOrderServices.put("test", SpringContextUtil.createBean(TestPaymentOrderService.class,SpringContextUtil.AUTOWIRE_BY_TYPE));
        }

    }



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
                InputStream is = MenuService.class.getResourceAsStream("/file/auth_menu.sql");
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


    @Override
    public void RoleService() throws Exception {
        StringBuffer log = new StringBuffer("初始化系统默认系统管理员角色信息");
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Role role = get("SYSTEM");
            if (role == null) {
                role = new Role();
                role.setCode("SYSTEM");
                role.setName("系统管理员");
                role.setEnabled(true);
                role.setDescription("系统默认管理员");
                save(role);
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
            DataDictionaryType ddt = getDataDictionaryType("root");
            if (ddt == null) {
                StringBuffer log = new StringBuffer("初始化数据字典分类跟目录");
                ddt = new DataDictionaryType();
                ddt.setCode("root");
                ddt.setName("数据字典分类");
                save(ddt);
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




}
