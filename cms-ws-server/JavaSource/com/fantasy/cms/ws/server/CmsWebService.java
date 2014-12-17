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
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CmsWebService implements ICmsService {

    @Resource
    private CmsService cmsService;

    @Override
    public ArticlePagerResult findPager(PagerDTO pagerDTO, PropertyFilterDTO[] filters) {
        Pager<Article> pager = new Pager<Article>();
        PagerUitl.statusPager(pagerDTO,pager);
        List<PropertyFilter> propertyFilters =  WebServiceUtil.toFilters(filters);
        pager = this.cmsService.findPager(pager,propertyFilters);
        ArticlePagerResult articlePagerResult = new ArticlePagerResult();
        articlePagerResult.setTotalCount(pager.getTotalCount());
        articlePagerResult.setTotalPage(pager.getTotalPage());
        articlePagerResult.setPageSize(pager.getPageSize());
        articlePagerResult.setCurrentPage(pager.getCurrentPage());
        articlePagerResult.setOrder(pager.getOrder().toString());
        List<Article> articleList = pager.getPageItems();
        ArticleDTO[] articleDTOs = new ArticleDTO[articleList.size()];
        for(int i=0;i<articleList.size();i++){
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(articleList.get(i).getId());
            articleDTO.setTitle(articleList.get(i).getTitle());
            articleDTO.setSummary(articleList.get(i).getSummary());
            articleDTO.setKeywords(articleList.get(i).getKeywords());
            if(articleList.get(i).getContent()!=null){
                articleDTO.setContent(articleList.get(i).getContent().getText());
            }
            articleDTO.setAuthor(articleList.get(i).getAuthor());
            articleDTO.setReleaseDate(articleList.get(i).getReleaseDate());
            if(OgnlUtil.getInstance().getValue("articleImage",articleList.get(i))!=null){
                FileDetail fileDetail = (FileDetail)OgnlUtil.getInstance().getValue("articleImage",articleList.get(i));
                articleDTO.setArticleImageStore(fileDetail.getAbsolutePath());
            }
            articleDTOs[i] = articleDTO;
        }
        articlePagerResult.setPageItems(articleDTOs);
        return articlePagerResult;
    }

    @Override
    public ArticleCategoryDTO[] categorys() {
        List<ArticleCategory> category = cmsService.getCategorys("root");
        ArticleCategoryDTO[] articleCategoryDTOs = new ArticleCategoryDTO[category.size()];
        for(int i=0;i<category.size();i++){
            ArticleCategoryDTO categoryDTO = new ArticleCategoryDTO();
            categoryDTO.setCode(category.get(i).getCode());
            categoryDTO.setName(category.get(i).getName());
            categoryDTO.setEngname(category.get(i).getEgname());
            categoryDTO.setDescription(category.get(i).getDescription());
            categoryDTO.setSort(category.get(i).getSort());
            categoryDTO.setLayer(category.get(i).getLayer());
            articleCategoryDTOs[i] = categoryDTO;
        }
        return articleCategoryDTOs;
    }

    @Override
    public ArticleDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
        List<PropertyFilter> propertyFilters =  WebServiceUtil.toFilters(filters);
        List<Article> articleList = this.cmsService.find(propertyFilters,orderBy,order,size);
        ArticleDTO[] articleDTOs = new ArticleDTO[articleList.size()];
        for(int i=0;i<articleList.size();i++){
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(articleList.get(i).getId());
            articleDTO.setTitle(articleList.get(i).getTitle());
            articleDTO.setSummary(articleList.get(i).getSummary());
            articleDTO.setKeywords(articleList.get(i).getKeywords());
            if(articleList.get(i).getContent()!=null){
                articleDTO.setContent(articleList.get(i).getContent().getText());
            }
            articleDTO.setAuthor(articleList.get(i).getAuthor());
            articleDTO.setReleaseDate(articleList.get(i).getReleaseDate());
            if(OgnlUtil.getInstance().getValue("articleImage",articleList.get(i))!=null){
                FileDetail fileDetail = (FileDetail)OgnlUtil.getInstance().getValue("articleImage",articleList.get(i));
                articleDTO.setArticleImageStore(fileDetail.getAbsolutePath());
            }
            articleDTOs[i] = articleDTO;
        }
        return articleDTOs;
    }

    @Override
    public ArticleDTO findArticleById(Long id) {
        Article article = this.cmsService.get(id);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setSummary(article.getSummary());
        articleDTO.setKeywords(article.getKeywords());
        if(article.getContent()!=null){
            articleDTO.setContent(article.getContent().getText());
        }
        articleDTO.setAuthor(article.getAuthor());
        articleDTO.setReleaseDate(article.getReleaseDate());
        if(OgnlUtil.getInstance().getValue("articleImage",article)!=null){
            FileDetail fileDetail = (FileDetail)OgnlUtil.getInstance().getValue("articleImage",article);
            articleDTO.setArticleImageStore(fileDetail.getAbsolutePath());
        }

        return articleDTO;
    }

    @Override
    public ArticleCategoryDTO[] getArticleCategoryDtoByCode(String code) {
        List<ArticleCategory> articleCategoryList = this.cmsService.getCategorys(code);
        ArticleCategoryDTO[] articleCategoryDTOs = new ArticleCategoryDTO[articleCategoryList.size()];
        for(int i=0;i<articleCategoryList.size();i++){
            ArticleCategoryDTO categoryDTO = new ArticleCategoryDTO();
            categoryDTO.setCode(articleCategoryList.get(i).getCode());
            categoryDTO.setName(articleCategoryList.get(i).getName());
            categoryDTO.setEngname(articleCategoryList.get(i).getEgname());
            categoryDTO.setDescription(articleCategoryList.get(i).getDescription());
            categoryDTO.setSort(articleCategoryList.get(i).getSort());
            categoryDTO.setLayer(articleCategoryList.get(i).getLayer());
            articleCategoryDTOs[i] = categoryDTO;
        }
        return articleCategoryDTOs;
    }
}
