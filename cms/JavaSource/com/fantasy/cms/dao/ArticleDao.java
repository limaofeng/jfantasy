package com.fantasy.cms.dao;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository("fantasy.cms.hibernate.ArticleDao")
public class ArticleDao extends HibernateLuceneDao<Article, Long> {

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_category.code");
        if (filter == null) {// 如果没有设置分类条件，默认为根
            String[] codes = getRootArticleCategoryCodes();
            if (codes.length > 1) {
                filter = new PropertyFilter("INS_category.code", codes);
            } else {
                filter = new PropertyFilter("EQS_category.code", codes[0]);
            }
        }
        Criterion likePathCriterion;
        // 将 code 转为 path like 查询
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
        List<PropertyFilter> newFilters = filters == null ? new ArrayList<PropertyFilter>() : filters;
        Criterion[] criterions = super.buildPropertyFilterCriterions(newFilters);
        criterions = ObjectUtil.join(criterions, likePathCriterion);
        return criterions;
    }

    @SuppressWarnings("unchecked")
    private String[] getRootArticleCategoryCodes() {
        String ids = ObjectUtil.toString(this.getSession().createCriteria(ArticleCategory.class).add(Restrictions.isNull("parent")).list(), "code", ";");
        return StringUtil.tokenizeToStringArray(ids);
    }

}
