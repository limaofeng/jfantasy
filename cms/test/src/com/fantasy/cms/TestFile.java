package com.fantasy.cms;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.jackson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class TestFile {

    @Resource
    private CmsService cmsService;

    @Test
    public void findPager() {
        Pager<Article> pager = cmsService.findPager(new Pager<Article>(), new ArrayList<PropertyFilter>());
        System.out.println(JSON.serialize(pager));
    }

    public static void main(String[] args) {
        System.out.println(ClassUtil.newInstance(Integer.class, "8080"));
    }

}
