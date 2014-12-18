package com.fantasy.framework.ws.util;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
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
        page.setOrder(pager.getOrder());
        return page;
    }

    public static <T extends PagerResult> T toPagerResult(Pager<?> daoPager, T pager) {
        pager.setCurrentPage(daoPager.getCurrentPage());
        pager.setTotalCount(daoPager.getTotalCount());
        pager.setTotalPage(daoPager.getTotalPage());
        pager.setOrderBy(daoPager.getOrderBy());
        String[] orders = new String[daoPager.getOrders().length];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = daoPager.getOrders()[i].name();
        }
        pager.setOrders(orders);
        pager.setOrder(daoPager.getOrder().name());
        return pager;
    }

    public static <T extends PagerResult> T toPagerResult(Pager<?> daoPager, T pager, Object[] dtos) {
        toPagerResult(daoPager, pager).setPageItems(dtos);
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

}
