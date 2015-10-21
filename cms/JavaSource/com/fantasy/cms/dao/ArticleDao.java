package com.fantasy.cms.dao;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import com.fantasy.framework.util.common.ObjectUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleDao extends HibernateLuceneDao<Article, Long> {

    @Autowired
    private ArticleCategoryDao categoryDao;

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_category.code");
        if (filter == null) {// 如果没有设置分类条件，默认为根
            String[] codes = getRootArticleCategoryCodes();
            if (codes.length > 1) {
                filter = new PropertyFilter("INS_category.code", codes);
            } else if (codes.length == 1)  {
                filter = new PropertyFilter("EQS_category.code", codes[0]);
            }
        }
        Criterion likePathCriterion = null;
        // 将 code 转为 path like 查询
        if(filter!= null ) {
            if (filter.getMatchType() == PropertyFilter.MatchType.IN) {
                Disjunction disjunction = Restrictions.disjunction();
                for (String code : filter.getPropertyValue(String[].class)) {
                    ArticleCategory category = (ArticleCategory) this.getSession().get(ArticleCategory.class, code);
                    disjunction.add(Restrictions.like("category.path", category.getPath(), MatchMode.START));
                }
                likePathCriterion = disjunction;
            } else {
                ArticleCategory category = (ArticleCategory) this.getSession().get(ArticleCategory.class, (Serializable) filter.getPropertyValue());
                if (category != null) {
                    likePathCriterion = Restrictions.like("category.path", category.getPath(), MatchMode.START);
                } else {
                    likePathCriterion = Restrictions.eq("category.code", filter.getPropertyValue());
                }
            }
        }
        List<PropertyFilter> newFilters = filters == null ? new ArrayList<PropertyFilter>() : filters;
        Criterion[] criterions = super.buildPropertyFilterCriterions(newFilters);
        criterions = ObjectUtil.join(criterions,likePathCriterion == null ? new Criterion[0]: new Criterion[]{likePathCriterion});
        return criterions;
    }

    private String[] getRootArticleCategoryCodes() {
        return ObjectUtil.toFieldArray(this.categoryDao.find(Restrictions.isNull("parent")), "code", String.class);
    }

}
