package org.jfantasy.system.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.system.bean.DataDictionary;
import org.jfantasy.system.bean.DataDictionaryKey;
import org.jfantasy.system.bean.DataDictionaryType;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataDictionaryDao extends HibernateDao<DataDictionary, DataDictionaryKey> {

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_type");
        if (filter == null) {// 如果没有设置分类条件，默认为根
            String[] codes = getRootDataDictionaryTypeCodes();
            if (codes.length > 1) {
                filter = new PropertyFilter("INS_type", codes);
            } else {
                filter = new PropertyFilter("EQS_type.code", codes[0]);
            }
        }
        Criterion likePathCriterion;
        // 将 code 转为 path like 查询
        if (filter.getMatchType() == PropertyFilter.MatchType.IN) {
            Disjunction disjunction = Restrictions.disjunction();
            for (String code : filter.getPropertyValue(String[].class)) {
                DataDictionaryType ddt = (DataDictionaryType) this.getSession().get(DataDictionaryType.class, code);
                disjunction.add(Restrictions.sqlRestriction(" {alias}.type in (select code from sys_dd_type where path like ? )", ddt.getPath() + "%", StringType.INSTANCE));
            }
            likePathCriterion = disjunction;
        } else {
            DataDictionaryType ddt = (DataDictionaryType) this.getSession().get(DataDictionaryType.class, (Serializable) filter.getPropertyValue());
            likePathCriterion = Restrictions.sqlRestriction(" {alias}.type in (select code from sys_dd_type where path like ? )", ddt.getPath() + "%", StringType.INSTANCE);
        }
        filters = filters == null ? new ArrayList<PropertyFilter>() : filters;
        return ObjectUtil.join(super.buildPropertyFilterCriterions(filters), likePathCriterion);
    }

    @SuppressWarnings("unchecked")
    private String[] getRootDataDictionaryTypeCodes() {
        String ids = ObjectUtil.toString(this.getSession().createCriteria(DataDictionaryType.class).add(Restrictions.isNull("parent")).list(), "code", ";");
        return StringUtil.tokenizeToStringArray(ids);
    }

}
