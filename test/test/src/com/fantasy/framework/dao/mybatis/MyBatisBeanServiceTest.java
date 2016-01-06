package org.jfantasy.framework.dao.mybatis;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.test.bean.MyBatisBean;
import org.jfantasy.test.service.MyBatisBeanService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MyBatisBeanServiceTest {

    private static final Log logger = LogFactory.getLog(MyBatisBeanServiceTest.class);

    @Autowired
    private MyBatisBeanService tMyBatisbeanService;

    @Before
    public void setUp() throws Exception {
        this.testInsert();
    }

    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

    @Test
    public void testFindPager() {
        Pager<MyBatisBean> pager = new Pager<MyBatisBean>();
        MyBatisBean myBatisBean = new MyBatisBean();
        myBatisBean.setValue("test");
        pager = tMyBatisbeanService.findPager(pager, myBatisBean);
        logger.debug("pager : " + pager);
        for (int i = 1; i <= pager.getTotalPage(); i++) {
            pager.setCurrentPage(i);
            pager.setFirst(0);
            pager = tMyBatisbeanService.findPager(pager, myBatisBean);
            logger.debug("显示第" + i + "页的数据:" + pager);
            for (MyBatisBean t : pager.getPageItems()) {
                logger.debug(t);
            }
        }
        logger.debug("pager : " + pager);
    }

    @Test
    public void testFindSimplePager() {
        Pager<MyBatisBean> pager = tMyBatisbeanService.findSimplePager(new Pager<MyBatisBean>(), "test");
        logger.debug("pager : " + pager);
        for (int i = 1; i <= pager.getTotalPage(); i++) {
            logger.debug("显示第" + i + "页的数据:" + pager);
            for (MyBatisBean t : pager.getPageItems()) {
                logger.debug(t);
            }
        }
        logger.debug("pager : " + pager);
    }

    @Test
    public void testSelectAll() {
        List<MyBatisBean> myBatisBeanList = tMyBatisbeanService.selectAll();
        logger.debug("size : " + myBatisBeanList.size());
        for (MyBatisBean t : myBatisBeanList) {
            logger.debug(t);
        }
        myBatisBeanList =  tMyBatisbeanService.selectMultiParameters("test", "test");
        logger.debug("size : " + myBatisBeanList.size());
        for (MyBatisBean t : myBatisBeanList) {
            logger.debug(t);
        }
    }

    @Test
    public void testUpdate() {
        MyBatisBean testbean = new MyBatisBean();
        testbean.setKey("test");
        testbean.setValue("test-value");
        int count = tMyBatisbeanService.update(testbean);
        logger.debug("影响行数：" + count + "\n");
        Assert.assertEquals(count, 1);

        testbean = tMyBatisbeanService.get("test");
        Assert.assertEquals(testbean.getValue(), "test-value");
    }

    public void testDelete() {
        int count = tMyBatisbeanService.delete("test");
        logger.debug("影响行数：" + count + "\n");

        for (int i = 1; i <= 30; i++) {
            count = tMyBatisbeanService.delete(i + "");
            logger.debug("影响行数：" + count + "\n");
        }

    }

    public void testInsert() {
        this.testDelete();

        MyBatisBean testbean = new MyBatisBean();
        testbean.setKey("test");
        testbean.setValue("test");
        int count = tMyBatisbeanService.insert(testbean);
        logger.debug("影响行数：" + count + "\n");
        Assert.assertEquals(count, 1);

        for (int i = 1; i <= 30; i++) {
            testbean = new MyBatisBean();
            testbean.setKey(i + "");
            testbean.setValue("test");
            count = tMyBatisbeanService.insert(testbean);
            logger.debug("影响行数：" + count + "\n");
            Assert.assertEquals(count, 1);
        }

    }

    @Test
    public void testAntPathMatcher() throws IOException {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        for (Resource resource : patternResolver.getResources("classpath:org.jfantasy/**/dao/*-Mapper.xml")) {
            logger.debug(resource);
        }
        String currPath = "/Users/lmf/framework/core/target/test-classes/org.jfantasy/test/";
        String fullPattern = "/Users/lmf/framework/core/target/test-classes/org.jfantasy/test/**/*-Mapper.xml";
        logger.debug(new AntPathMatcher().matchStart(fullPattern, currPath));
    }

}
