package com.fantasy.cms.ws.server;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.cms.ws.IArticleService;
import com.fantasy.cms.ws.dto.ArticleCategoryDTO;
import com.fantasy.cms.ws.dto.ArticleDTO;
import com.fantasy.cms.ws.dto.ArticlePagerResult;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ArticleWebService implements IArticleService {

    @Resource
    private CmsService cmsService;

    @Override
    public ArticlePagerResult findPager(PagerDTO pagerDTO, PropertyFilterDTO[] filters) {
        Pager<Article> pager = cmsService.findPager(WebServiceUtil.toPager(pagerDTO, Article.class), WebServiceUtil.toFilters(filters));
        ArticlePagerResult articlePagerResult = WebServiceUtil.toBean(pager, ArticlePagerResult.class);
        ArticleDTO[] articleDTOs = WebServiceUtil.toArray(pager.getPageItems(), ArticleDTO.class);
        for (Article article : pager.getPageItems()) {
            ObjectUtil.find(articleDTOs, "id", article.getId()).setContent(article.getContent().toString());
        }
        articlePagerResult.setPageItems(articleDTOs);
        return articlePagerResult;
    }

    @Override
    public ArticleCategoryDTO[] categorys() {
        List<ArticleCategory> category = cmsService.getCategorys();
        return WebServiceUtil.toArray(category, ArticleCategoryDTO.class);
    }

    @Override
    public ArticleDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
        return new ArticleDTO[0];
    }

    @Override
    public ArticleDTO findArticleById(Long id) {
        Article article = this.cmsService.get(id);
        ArticleDTO articleDTO = WebServiceUtil.toBean(article, ArticleDTO.class);
        articleDTO.setSummary(article.getSummary().toString());
        articleDTO.setContent(article.getContent().toString());
        return articleDTO;
    }
}
