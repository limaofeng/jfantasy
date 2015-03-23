package com.fantasy.test.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.test.bean.TyBatisbean;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class TyBatisbeanServiceTest {

    private static final Log logger = LogFactory.getLog(TyBatisbeanServiceTest.class);

    @Autowired
    private TMyBatisbeanService tMyBatisbeanService;

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
        Pager<TyBatisbean> pager = new Pager<TyBatisbean>();
        TyBatisbean tyBatisbean = new TyBatisbean();
        tyBatisbean.setValue("test");
        pager = tMyBatisbeanService.findPager(pager, tyBatisbean);
        logger.debug("pager : " + pager);
        for (int i = 1; i <= pager.getTotalPage(); i++) {
            pager.setCurrentPage(i);
            pager.setFirst(0);
            pager = tMyBatisbeanService.findPager(pager, tyBatisbean);
            logger.debug("显示第" + i + "页的数据:" + pager);
            for (TyBatisbean t : pager.getPageItems()) {
                logger.debug(t);
            }
        }
        logger.debug("pager : " + pager);
    }

    @Test
    public void testFindSimplePager() {
        Pager<TyBatisbean> pager = tMyBatisbeanService.findSimplePager(new Pager<TyBatisbean>(), "test");
        logger.debug("pager : " + pager);
        for (int i = 1; i <= pager.getTotalPage(); i++) {
            logger.debug("显示第" + i + "页的数据:" + pager);
            for (TyBatisbean t : pager.getPageItems()) {
                logger.debug(t);
            }
        }
        logger.debug("pager : " + pager);
    }

    @Test
    public void testSelectAll() {
        List<TyBatisbean> tyBatisbeanList = tMyBatisbeanService.selectAll();
        logger.debug("size : " + tyBatisbeanList.size());
        for (TyBatisbean t : tyBatisbeanList) {
            logger.debug(t);
        }
        tyBatisbeanList =  tMyBatisbeanService.selectMultiParameters("test", "test");
        logger.debug("size : " + tyBatisbeanList.size());
        for (TyBatisbean t : tyBatisbeanList) {
            logger.debug(t);
        }
    }

    @Test
    public void testUpdate() {
        TyBatisbean testbean = new TyBatisbean();
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

        TyBatisbean testbean = new TyBatisbean();
        testbean.setKey("test");
        testbean.setValue("test");
        int count = tMyBatisbeanService.insert(testbean);
        logger.debug("影响行数：" + count + "\n");
        Assert.assertEquals(count, 1);

        for (int i = 1; i <= 30; i++) {
            testbean = new TyBatisbean();
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
        for (Resource resource : patternResolver.getResources("classpath:com/fantasy/**/dao/*-Mapper.xml")) {
            logger.debug(resource);
        }
        String currPath = "/Users/lmf/framework/core/target/test-classes/com/fantasy/test/";
        String fullPattern = "/Users/lmf/framework/core/target/test-classes/com/fantasy/test/**/*-Mapper.xml";
        logger.debug(new AntPathMatcher().matchStart(fullPattern, currPath));
    }

}
