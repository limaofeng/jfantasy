package org.jfantasy.common.dao;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.jfantasy.common.bean.Area;
import org.jfantasy.common.bean.enums.AreaTag;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AreaDao extends HibernateDao<Area, String> {

    public Integer getSortMax() {
        Query query = this.createQuery("select max(sort) from " + Area.class.getName());
        Object result = query.uniqueResult();
        return result == null ? 1 : Integer.valueOf(result.toString());
    }

    @Override
    protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "INE_tags");
        Criterion[] criterions = super.buildPropertyFilterCriterions(filters);
        if (filter != null) {
            List<Criterion> predicates = new ArrayList<>();
            for (AreaTag tag : filter.getPropertyValue(AreaTag[].class)) {
                predicates.add(Restrictions.sqlRestriction("{alias}.tags like ?", tag.name(), StringType.INSTANCE));
            }
            criterions = ObjectUtil.join(criterions, Restrictions.or(predicates.toArray(new Criterion[predicates.size()])));
        }
        return criterions;
    }

}
