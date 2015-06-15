package com.fantasy.framework.lucene;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.httpclient.HttpClientUtil;
import com.fantasy.framework.lucene.bean.News;
import com.fantasy.framework.lucene.service.NewsService;
import com.fantasy.framework.util.common.I18nUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class BuguSearcherTest {

    @Autowired
    private NewsService newsService;

    private static final Log LOG = LogFactory.getLog(BuguSearcherTest.class);

    @Before
    public void setUp() throws Exception {

        String json = I18nUtil.decodeUnicode(HttpClientUtil.doGet("http://interface.sina.cn/dfz/jx/news/index.d.html?page=1&ch=zhengwen&cid=69603").getText());
        List<Map<String, String>> list = (List<Map<String, String>>) OgnlUtil.getInstance().getValue("result.data.list", JSON.deserialize(json));

        for (Map<String, String> article : list) {
            News news = new News();
            news.setTitle(OgnlUtil.getInstance().getValue("title", article).toString());
            news.setSummary(OgnlUtil.getInstance().getValue("summary", article).toString());
            newsService.save(news);
        }
    }

    @After
    public void tearDown() throws Exception {
        newsService.deleteAll();
    }

    @Test
    public void testSearch() throws Exception {
        Pager<News> pager = newsService.search(new Pager<News>(), BuguParser.parse(new String[]{"title","summary"}, "上海救援"),new BuguHighlighter(new String[]{"title"},"上海救援"));

        LOG.debug("匹配的数据条数:" + pager.getTotalCount());

        for(News news : pager.getPageItems()){
            LOG.debug(news.getTitle());
        }

    }


}