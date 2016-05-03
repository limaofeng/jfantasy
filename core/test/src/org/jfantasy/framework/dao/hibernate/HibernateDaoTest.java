package org.jfantasy.framework.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.springboot.ApplicationTest;
import org.jfantasy.springboot.bean.Article;
import org.jfantasy.springboot.dao.ArticleDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class HibernateDaoTest {

    private static final Log LOG = LogFactory.getLog(HibernateDaoTest.class);

    @Autowired
    private ArticleDao articleDao;

    private Article article = new Article();

    @Before
    public void setUp() throws Exception {
        article.setAuthor("HibernateDaoTest");
        article.setTitle("HibernateDao 测试");
        article.setSummary("HibernateDao Summary UUID = " + UUID.randomUUID());
        article.setContent("HibernateDao Content");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    @Transactional
    public void save() throws Exception {
        Article newArticle = articleDao.save(article);
        Assert.assertNotNull("ID未生成", newArticle.getId());
        Assert.assertNotNull("SN未生成", newArticle.getSn());
        Assert.assertEquals(article, newArticle);

        newArticle = BeanUtil.copyProperties(new Article(), articleDao.save(article));
        newArticle.setTitle(newArticle.getTitle() + " - merge");

        Assert.assertNotNull(newArticle.getContent());
        newArticle.setContent(null);

        Article mergeArticle = this.articleDao.save(newArticle, true);

        Assert.assertNotNull(mergeArticle.getContent());//使用合并模式,为null的对象不会合并到数据库中
    }

    @Test
    @Transactional
    public void merge() throws Exception {
        Article newArticle = BeanUtil.copyProperties(new Article(), articleDao.save(article));
        newArticle.setTitle(newArticle.getTitle() + " - merge");

        Assert.assertNotNull(newArticle.getContent());
        newArticle.setContent(null);

        Article mergeArticle = this.articleDao.merge(newArticle);

        Assert.assertNull(mergeArticle.getContent());

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void merge1() throws Exception {

    }

    @Test
    public void persist() throws Exception {

    }

    @Test
    @Transactional
    public void delete() throws Exception {

    }

    @Test
    public void delete1() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void load() throws Exception {

    }

    @Test
    @Transactional
    public void getAll() throws Exception {
        Assert.assertNotNull(articleDao.getAll());

        Assert.assertNotNull(articleDao.getAll(new Pager<Article>()));

        Assert.assertNotNull(articleDao.getAll("title", true));
    }

    @Test
    public void findBy() throws Exception {

    }

    @Test
    public void findUniqueBy() throws Exception {

    }

    @Test
    public void findByIds() throws Exception {

    }

    @Test
    public void findByIds1() throws Exception {

    }

    @Test
    public void find() throws Exception {

    }

    @Test
    public void find1() throws Exception {

    }

    @Test
    @Transactional
    public void findSQL() throws Exception {
        Session session = this.articleDao.getSession();
        SQLQuery query = session.createSQLQuery("select id,'mjjjj' from website.test_article");
        List objectList = query.list();
        LOG.debug(objectList.getClass());
        LOG.debug(JSON.serialize(objectList));
    }

    @Test
    public void findSQL1() throws Exception {

    }

    @Test
    public void findUnique() throws Exception {

    }

    @Test
    public void findUnique1() throws Exception {

    }

    @Test
    public void batchExecute() throws Exception {

    }

    @Test
    public void batchExecute1() throws Exception {

    }

    @Test
    public void batchSQLExecute() throws Exception {

    }

    @Test
    public void batchSQLExecute1() throws Exception {

    }

    @Test
    public void createQuery() throws Exception {

    }

    @Test
    public void createQuery1() throws Exception {

    }

    @Test
    public void createSQLQuery() throws Exception {

    }

    @Test
    public void createSQLQuery1() throws Exception {

    }

    @Test
    public void findUnique2() throws Exception {

    }

    @Test
    public void createDetachedCriteria() throws Exception {

    }

    @Test
    public void getIdName() throws Exception {

    }

    @Test
    public void getIdName1() throws Exception {

    }

    @Test
    public void getAll2() throws Exception {

    }

    @Test
    public void findPager() throws Exception {

    }

    @Test
    public void findPager1() throws Exception {

    }

    @Test
    public void findPager2() throws Exception {

    }

    @Test
    public void findBy1() throws Exception {

    }

    @Test
    public void find2() throws Exception {

    }

    @Test
    public void find3() throws Exception {

    }

    @Test
    public void find4() throws Exception {

    }

    @Test
    public void find5() throws Exception {

    }

    @Test
    public void find6() throws Exception {

    }

    @Test
    public void find7() throws Exception {

    }

    @Test
    public void find8() throws Exception {

    }

    @Test
    public void find9() throws Exception {

    }

    @Test
    public void findPager3() throws Exception {

    }

    @Test
    public void getAlias() throws Exception {

    }

    @Test
    public void isPropertyUnique() throws Exception {

    }

    @Test
    public void count() throws Exception {

    }

    @Test
    public void count1() throws Exception {

    }

    @Test
    public void sqlRestriction() throws Exception {

    }

    @Test
    public void find10() throws Exception {

    }

    @Test
    public void findUnique3() throws Exception {

    }

    @Test
    public void find11() throws Exception {

    }

}