package org.jfantasy.framework.lucene.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.lucene.dao.LuceneDao;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.springboot.ApplicationTest;
import org.jfantasy.springboot.dao.ArticleDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class HibernateLuceneDaoTest {

    private final static Log LOG = LogFactory.getLog(HibernateLuceneDaoTest.class);

    private LuceneDao dao;

    @Autowired
    private ArticleDao articleDao;

    @Before
    public void setUp() throws Exception {
        dao = SpringContextUtil.registerBeanDefinition("articleDaoLuceneDao",HibernateLuceneDao.class,new Object[]{articleDao});//new HibernateLuceneDao(articleDao);
        LOG.debug(dao);
    }

    @Test
    public void testArticle() {
        LOG.debug("count =>" + dao.count());
    }

}