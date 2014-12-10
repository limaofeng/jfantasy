package com.fantasy.framework.dao.hibernate;

import com.fantasy.framework.util.common.NumberUtil;
import com.fantasy.system.service.DataDictionaryService;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class HibernateDaoTest extends TestCase {

    private final static Log logger = LogFactory.getLog(HibernateDaoTest.class);

    @Resource
    public DataDictionaryService dataDictionaryService;

    @Test
    public void cache(){
        logger.debug("> Dao find 方法缓存测试");
        long start = System.currentTimeMillis();
        logger.debug(" 开始第一次查 >> ");
        dataDictionaryService.allTypes();
        logger.debug(" 第一次查询耗时：" + (System.currentTimeMillis() - start) + "ms");

        for(int i=2;i<10;i++) {
            start = System.currentTimeMillis();
            logger.debug(" 开始第"+ NumberUtil.toChinese(i) +"次查 >> ");
            dataDictionaryService.allTypes();
            logger.debug(" 第"+ NumberUtil.toChinese(i) +"次查询耗时：" + (System.currentTimeMillis() - start) + "ms");
        }

        logger.debug("\n\n\n> Dao get 方法缓存测试");


//        start = System.currentTimeMillis();
//        logger.debug(" 开始第一次查 >> ");
//        dataDictionaryService.get("root:test");
//        logger.debug(" 第一次查询耗时：" + (System.currentTimeMillis() - start) + "ms");
//
//        for(int i=2;i<10;i++) {
//            start = System.currentTimeMillis();
//            logger.debug(" 开始第"+ NumberUtil.toChinese(i) +"次查 >> ");
//            dataDictionaryService.get("root:test");
//            logger.debug(" 第"+ NumberUtil.toChinese(i) +"次查询耗时：" + (System.currentTimeMillis() - start) + "ms");
//        }
    }

}