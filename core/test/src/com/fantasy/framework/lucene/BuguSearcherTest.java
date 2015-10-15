//package com.fantasy.framework.lucene;
//
//import com.fantasy.attr.framework.CustomBeanFactory;
//import com.fantasy.attr.storage.service.AttributeVersionService;
//import com.fantasy.framework.lucene.service.NewsService;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
//public class BuguSearcherTest {
//
//    @Autowired
//    private NewsService newsService;
//    @Autowired
//    private AttributeVersionService versionService;
//    @Autowired
//    private CustomBeanFactory customBeanFactory;
//    private static final Log LOG = LogFactory.getLog(BuguSearcherTest.class);
//
//    @Before
//    public void setUp() throws Exception {
////        versionService.save(News.class.getName(), "vt", AttributeUtils.string("test", "测试字段", "测试字段"));
////
////        String json = I18nUtil.decodeUnicode(HttpClientUtil.doGet("http://interface.sina.cn/dfz/jx/news/index.d.html?page=1&ch=zhengwen&cid=69603").getText());
////        List<Map<String, String>> list = OgnlUtil.getInstance().getValue("result.data.list", JSON.deserialize(json));
////
////        for (Map<String, String> article : list) {
////            News news = customBeanFactory.makeDynaBean(News.class, "vt");
////            assert news != null;
////            news.setTitle(OgnlUtil.getInstance().getValue("title", article).toString());
////            news.setSummary(OgnlUtil.getInstance().getValue("summary", article).toString());
////            OgnlUtil.getInstance().setValue("test", news, "测试");
////            newsService.save(news);
////        }
//    }
//
//    @After
//    public void tearDown() throws Exception {
////        newsService.deleteAll();
//    }
//
//    public void testSearch() throws Exception {
////        Thread.sleep(6 * 1000);
////        String value = "scxmgyz";
////        Query query = BuguParser.parseWildcard(new String[]{"title", "pinyin", "qpin"}, "*" + value + "*");
////        Pager<News> pager = newsService.search(new Pager<News>(), query);
////
////        LOG.debug("匹配的数据条数:" + pager.getTotalCount());
////
////        for (News news : pager.getPageItems()) {
////            LOG.debug(news.getTitle() + " >> ");// OgnlUtil.getInstance().getValue("test", news)
////        }
//
//    }
//
//
//}