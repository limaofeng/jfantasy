package com.fantasy.cms.ws.client;

import com.fantasy.cms.ws.dto.ArticleCategoryDTO;
import com.fantasy.cms.ws.dto.ArticleDTO;
import com.fantasy.cms.ws.dto.ArticlePagerResult;
import com.fantasy.com.ws.client.ArticleService;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.uitl.Constants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArticleServiceTest {

    private ArticleService articleService;

    @Before
    public void init() throws Exception {
        articleService = new ArticleService();
        articleService.setEndPointReference(Constants.END_POINT_REFERENCE);
        articleService.setTargetNamespace("http://ws.cms.fantasy.com");
        articleService.setAxis2xml("classpath:axis2.xml");
        articleService.afterPropertiesSet();
    }


    @Test
    public void testFindPager(){
        PagerDTO pager = new PagerDTO();// 设置每页显示的数据条数
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        filters.add(new PropertyFilterDTO("EQS_category.code", "x"));
        // 调用接口查询
        ArticlePagerResult pagination = articleService.findPager(pager, filters.toArray(new PropertyFilterDTO[filters.size()]));
    }

    @Test
    public void testFindcategory() {
        // 直接调用无参数接口
        for (ArticleCategoryDTO articleCategory : articleService.categorys()) {
            System.out.println(articleCategory.getCode()+"----"+articleCategory.getName()+"\n");
        }

    }

    @Test
    public  void  testqueryArtcleById(){
        ArticleDTO articleDTO = this.articleService.findArticleById(Long.valueOf("1"));
        System.out.print(articleDTO.getContent());
    }


}
