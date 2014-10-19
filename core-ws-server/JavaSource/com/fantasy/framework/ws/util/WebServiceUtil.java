package com.fantasy.framework.ws.util;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.Pager.Order;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.jackson.JSON;

import java.util.ArrayList;
import java.util.List;

public class WebServiceUtil {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Pager<T> toPager(com.fantasy.framework.ws.util.PagerDTO pager, Class<T> clazz) {
        return null;
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

    public static List<PropertyFilter> toFilters(com.fantasy.framework.ws.util.PropertyFilterDTO[] filters) {
        List<PropertyFilter> newFilters = new ArrayList<PropertyFilter>();
        for (com.fantasy.framework.ws.util.PropertyFilterDTO filter : filters) {
            newFilters.add(new PropertyFilter(filter.getFilterName(), filter.getPropertyValue()));
        }
        return newFilters;
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
