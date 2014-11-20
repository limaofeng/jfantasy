package com.fantasy.framework.ws.util;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.Pager.Order;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WebServiceUtil {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Pager<T> toPager(com.fantasy.framework.ws.util.PagerDTO pager, Class<T> clazz) {
        Pager page = new Pager();
        page.setPageSize(pager.getPageSize());
        page.setOrderBy(pager.getOrderBy());
        page.setCurrentPage(pager.getCurrentPage());
        // page.setTotalCount(pager.getTotalCount());
        // page.setTotalPage(pager.getTotalPage());
        page.setOrder(pager.getOrder());
        return page;
        //TODO ObjectUtil.copy(new Pager(), pager, "pageItems");
    }

    public static com.fantasy.framework.ws.util.PagerDTO toPager(com.fantasy.framework.ws.util.PagerDTO pager, Pager<?> daoPager) {
//        ObjectUtil.copy(pager, daoPager, "pagerItems");
        if (daoPager.getOrders() != null) {
            List<String> sts = new ArrayList<String>();
            for (Order order : daoPager.getOrders()) {
                sts.add(order.name());
            }
            pager.setOrders(sts.toArray(new String[sts.size()]));
        }
        if (daoPager.getOrder() != null) {
            pager.setOrder(daoPager.getOrder().name());
        }
        pager.setPageItems(JSON.serialize(daoPager.getPageItems()));
        return pager;
    }

    public static <T> Pager<T> toPager(PagerResult pager, Class<T> clazz) {
        return null;
//        return ObjectUtil.copy(new Pager(), pager, "pageItems");
    }

    public static <T> PagerResult<T> toPager(Pager<?> daoPager, PagerResult<T> pager, Class<T> clazz) {

        return pager;
    }

    public static List<PropertyFilter> toFilters(PropertyFilterDTO[] filterDTOs) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        for (PropertyFilterDTO filterDTO : filterDTOs) {
            filters.add(new PropertyFilter(filterDTO.getFilterName(), filterDTO.getPropertyValue()));
        }
        return filters;
    }

    public static List<PropertyFilter> toFilters(PropertyFilterDTO[] filterDTOs, Class<?> clazz) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        for (PropertyFilterDTO filterDTO : filterDTOs) {
            String filterName = filterDTO.getFilterName();
            String propertyNameStr = StringUtils.substringAfter(filterDTO.getFilterName(), "_");
            String[] propertyNames = propertyNameStr.split("_OR_");
            for (String propertyName : propertyNames) {
                Field field = ClassUtil.getDeclaredField(clazz, propertyName);
                if (field == null) {
                    continue;
                }
                Mapping mapping = ClassUtil.getDeclaredField(clazz, propertyName).getAnnotation(Mapping.class);
                if (mapping == null || StringUtil.isBlank(mapping.value())) {
                    continue;
                }
                filterName = RegexpUtil.replace(filterName, propertyName, mapping.value());
            }
            filters.add(new PropertyFilter(filterName, filterDTO.getPropertyValue()));
        }
        return filters;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<?> list, Class<T> clazz) {
        return (T[]) JSON.deserialize(JSON.serialize(list), ClassUtil.newInstance(clazz, 0).getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(T obj) {
        if (obj == null)
            return obj;
        return (T) JSON.deserialize(JSON.serialize(obj), obj.getClass());
    }

    public static <T> T toBean(Object obj, Class<T> newClass) {
        if (obj == null)
            return null;
        return (T) JSON.deserialize(JSON.serialize(obj), newClass);
    }

}
