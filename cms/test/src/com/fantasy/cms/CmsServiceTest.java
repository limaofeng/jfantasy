package com.fantasy.cms;


import com.fantasy.attr.util.VersionUtil;
import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CmsServiceTest {

    @Resource
    private CmsService cmsService;

    @Test
    @Transient
    public void findPager() {
        Pager<Article> pager = new Pager<Article>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_test","limaofeng"));
        long start = System.currentTimeMillis();
        pager = cmsService.findPager(pager, filters);
        System.err.println(" time : " + (System.currentTimeMillis() - start) + "s");
        start = System.currentTimeMillis();
        for(int i=0;i<10;i++) {
            pager = cmsService.findPager(pager, filters);
        }
        System.err.println(" time : " + (System.currentTimeMillis() - start) + "s");
        for(Article article : pager.getPageItems()){
            System.out.println(JSON.serialize(article));
        }
        System.out.println(" total count :" + pager.getTotalCount());
    }

    @Test
    @Transient
    public void save() {
        Article article = (Article)VersionUtil.makeDynaBean(1l);
        OgnlUtil.getInstance().setValue("test",article,"limaofeng");
        cmsService.save(article);
    }

}
