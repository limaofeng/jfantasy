package com.fantasy.cms.ws.client;

import com.fantasy.cms.ws.dto.ArticleCategoryDTO;
import com.fantasy.cms.ws.dto.ArticleDTO;
import com.fantasy.cms.ws.dto.ArticlePagerResult;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.uitl.Constants;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CmsServiceTest {

    private CmsService cmsService;

    @Before
    public void init() throws Exception {
        cmsService = new CmsService();
        cmsService.setEndPointReference(Constants.END_POINT_REFERENCE);
        cmsService.setTargetNamespace("http://ws.cms.fantasy.com");
        cmsService.setAxis2xml("classpath:axis2.xml");
        cmsService.afterPropertiesSet();
    }


    @Test
    public void testFindPager(){
        PagerDTO pager = new PagerDTO();// 设置每页显示的数据条数
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        //filters.add(new PropertyFilterDTO("EQS_category.code", "wxtx"));
        // 调用接口查询
        ArticlePagerResult pagination = cmsService.findPager(pager, filters.toArray(new PropertyFilterDTO[filters.size()]));
        Assert.assertNotNull(pagination);
    }


    @Test
    public void testFind(){
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        filters.add(new PropertyFilterDTO("EQS_category.code", "abcd"));
        ArticleDTO[] articleDTOs = cmsService.find(filters.toArray(new PropertyFilterDTO[filters.size()]),"id","desc",10);
        Assert.assertNotNull(articleDTOs);
    }


    @Test
    public void testFindcategory() {
        Assert.assertNotNull(cmsService.categorys());

    }

    @Test
    public  void  testqueryArtcleById(){
        PagerDTO pager = new PagerDTO();// 设置每页显示的数据条数
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        ArticlePagerResult pagination = cmsService.findPager(pager, filters.toArray(new PropertyFilterDTO[filters.size()]));
        Assert.assertNotNull("文章数据为空",pagination.getPageItems());
            for(ArticleDTO dto:pagination.getPageItems()){
                ArticleDTO articleDTO = this.cmsService.findArticleById(dto.getId());
                Assert.assertNotNull(articleDTO);
            }
    }


    @Test
    public void getArticleCategoryDtoByCode(){
        ArticleCategoryDTO[] articleCategoryDTOs = this.cmsService.getArticleCategoryDtoByCode("root");
    }


}
