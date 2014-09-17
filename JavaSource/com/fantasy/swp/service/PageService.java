package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.swp.bean.*;
import com.fantasy.swp.dao.PageDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PageService implements InitializingBean {

    @Resource
    private PageDao pageDao;

    @Override
    public void afterPropertiesSet() throws Exception {
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

    public Pager<Page> findPager(Pager<Page> pager, List<PropertyFilter> filters) {
        return this.pageDao.findPager(pager, filters);
    }

    public void save(Page page) {
        this.pageDao.save(page);
    }

    public Page get(Long id) {
        return this.pageDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.pageDao.delete(id);
        }
    }

    public void generation(Long id) {
    }

}

