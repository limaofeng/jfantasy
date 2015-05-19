package com.fantasy.cms.ws.server;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.cms.ws.ICmsService;
import com.fantasy.cms.ws.dto.ArticleCategoryDTO;
import com.fantasy.cms.ws.dto.ArticleDTO;
import com.fantasy.cms.ws.dto.ArticlePagerResult;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Component
public class CmsWebService implements ICmsService {

    @Autowired
    private CmsService cmsService;

    @Override
    public ArticlePagerResult findPager(PagerDTO pagerDTO, PropertyFilterDTO[] filters) {
        List<PropertyFilter> propertyFilters = WebServiceUtil.toFilters(filters);
        Pager<Article> pager = this.cmsService.findPager(WebServiceUtil.toPager(pagerDTO, Article.class), propertyFilters);
        List<Article> articles = pager.getPageItems();
        return WebServiceUtil.toPagerResult(pager, new ArticlePagerResult(), asArray(articles.toArray(new Article[articles.size()])));
    }

    @Override
    public ArticleCategoryDTO[] categorys() {
        return asArray(cmsService.getCategorys("root"));
    }

    @Override
    public ArticleDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
        List<PropertyFilter> propertyFilters = WebServiceUtil.toFilters(filters);
        List<Article> articles =this.cmsService.find(propertyFilters, orderBy, order, size);
        return asArray(articles.toArray(new Article[articles.size()]));
    }

    @Override
    public ArticleDTO findArticleById(Long id) {
        return asDto(this.cmsService.get(id));
    }

    @Override
    public ArticleCategoryDTO[] getArticleCategoryByCode(String code) {
        return asArray(this.cmsService.getCategorys(code));
    }

    public static ArticleCategoryDTO asDto(ArticleCategory category) {
        ArticleCategoryDTO categoryDTO = new ArticleCategoryDTO();
        categoryDTO.setCode(category.getCode());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setSort(category.getSort());
        categoryDTO.setLayer(category.getLayer());
        return categoryDTO;
    }

    public static ArticleCategoryDTO[] asArray(List<ArticleCategory> categorys) {
        ArticleCategoryDTO[] dtos = new ArticleCategoryDTO[categorys.size()];
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = asDto(categorys.get(i));
        }
        return dtos;
    }

    public static ArticleDTO asDto(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setSummary(article.getSummary());
        articleDTO.setKeywords(article.getKeywords());
        if (article.getContent() != null) {
            articleDTO.setContent(article.getContent().getText());
        }
        articleDTO.setAuthor(article.getAuthor());
        articleDTO.setReleaseDate(article.getReleaseDate());
        if (ClassUtil.getProperty(article.getClass(),"articleImage") != null) {//OgnlUtil.getInstance().getValue("articleImage", article) != null
            FileDetail fileDetail = (FileDetail) OgnlUtil.getInstance().getValue("articleImage", article);
            articleDTO.setArticleImageStore(fileDetail.getAbsolutePath());
        }
        return articleDTO;
    }

    public static ArticleDTO[] asArray(Article[] articles) {
        ArticleDTO[] dtos = new ArticleDTO[articles.length];
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = asDto(articles[i]);
        }
        return dtos;
    }
}
