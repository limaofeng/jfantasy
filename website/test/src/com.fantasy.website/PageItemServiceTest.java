package org.jfantasy.website;

import org.apache.struts2.views.JspSupportServlet;
import org.jfantasy.attr.storage.service.AttributeVersionService;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import org.jfantasy.website.bean.Page;
import org.jfantasy.website.bean.PageItem;
import org.jfantasy.website.bean.PageItemData;
import org.jfantasy.website.service.PageItemDataService;
import org.jfantasy.website.service.PageItemService;
import org.jfantasy.website.service._PageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"spring/applicationContext.xml"})
public class PageItemServiceTest extends StrutsSpringJUnit4TestCase {
    @Autowired
    private PageItemService pageItemService;
    @Autowired
    private PageItemDataService pageItemDataService;
    @Autowired
    private _PageService pageService;
    @Autowired
    private PageActionTest pageActionTest;
    @Autowired
    private AttributeVersionService attributeVersionService;
//    @Autowired
//    private ArticleService articleService;

    @Before
    public void setUp() throws Exception {
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

//    @Test
    public void testSave() throws Exception {
        PageItem pageItem = new PageItem();
        pageItem.setFile("/file/test_file.html");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_name","PAGE_JUNIT_TEST"));
        List<Page> pages = pageService.find(filters);
        if(pages==null || pages.size()<=0){
            pageActionTest.setUp();
            pageActionTest.testSave();
            pages = pageService.find(filters);
        }
        pageItem.setPage(pages.get(pages.size()-1));
        pageItem.setContent("PAGEITEM_JUNIT_TEST");

        List<PageItemData> pageItemDatas = new ArrayList<PageItemData>();
        PageItemData pageItemData = new PageItemData();
        filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title","ARTICLE_JUNIT_TEST"));

//        Article article = new Article();
//        List<Article> articles = articleService.find(filters);
//        if(articles.size()<=0){
//            article.setTitle("ARTICLE_JUNIT_TEST");
//            article.setSummary("xxxx");
//            Pager<AttributeVersion> pager = attributeVersionService.findPager(new Pager<AttributeVersion>(),new ArrayList<PropertyFilter>());
//            if(pager.getPageItems()!=null && pager.getPageItems().size()>0){
//                article.setVersion(pager.getPageItems().get(0));
//            }
//            this.articleService.save(article);
//        }else{
//            article = articles.get(0);
//        }
//        pageItemData.setBeanId(article.getId() + "");
//        pageItemData.setClassName(Article.class.getName());
        pageItemData.setPageItem(pageItem);
        pageItemDatas.add(pageItemData);
        pageItem.setPageItemDatas(pageItemDatas);
        this.pageItemService.save(pageItem);
        this.pageItemDataService.save(pageItemData);
    }

    @Test
    public void testDelete(){
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_content","PAGEITEM_JUNIT_TEST"));
        List<PageItem> pageItems = this.pageItemService.find(filters);
        if(pageItems.size()<=0){
            return;
        }
        Long[] ids = new Long[pageItems.size()];
        for(int i=0; i<pageItems.size(); i++){
            PageItem pageItem = pageItems.get(i);
            ids[i] = pageItem.getId();
        }
        this.pageItemService.delete(ids);
    }
}
