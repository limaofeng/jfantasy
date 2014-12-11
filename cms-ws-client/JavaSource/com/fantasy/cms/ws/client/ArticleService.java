package com.fantasy.cms.ws.client;

import com.fantasy.cms.ws.IArticleService;
import com.fantasy.cms.ws.dto.ArticleCategoryDTO;
import com.fantasy.cms.ws.dto.ArticleDTO;
import com.fantasy.cms.ws.dto.ArticlePagerResult;
import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;

public class ArticleService extends WebServiceClient implements IArticleService {

    public ArticleService(){
        super("ArticleService");
    }

    @Override
    public ArticlePagerResult findPager(PagerDTO pagerDTO, PropertyFilterDTO[] filterDTOs) {
        return super.invokeOption("findPager",new Object[] { pagerDTO,filterDTOs },ArticlePagerResult.class);
    }


    @Override
    public ArticleCategoryDTO[] categorys() {
        return super.invokeOption("categorys",new Object[] { },ArticleCategoryDTO[].class);
    }

    @Override
    public ArticleDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
        return super.invokeOption("find",new Object[] { filters,orderBy,order,size},ArticleDTO[].class);
    }

    @Override
    public ArticleDTO findArticleById(Long id) {
        return super.invokeOption("findArticleById",new Object[] { id },ArticleDTO.class);
    }

    @Override
    public ArticleCategoryDTO[] getArticleCategoryDtoByCode(String code) {
        return super.invokeOption("getArticleCategoryDtoByCode",new Object[] { code },ArticleCategoryDTO[].class);
    }
}
