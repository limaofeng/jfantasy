package org.jfantasy.mall.order.converter;

import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.mall.order.bean.Order;
import org.jfantasy.mall.order.service.OrderService;
import ognl.DefaultTypeConverter;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Member;
import java.util.Map;

/**
 * 用于将 order 类作为动态属性时的转换方法。
 */
public class OrderTypeConverter extends DefaultTypeConverter {

    @Autowired
    private OrderService orderService;

    @Transactional
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (toType == Order.class) {
            return orderService.get((String) value);
        } else if (value instanceof Order && toType == String.class) {
            return OgnlUtil.getInstance().getValue("sn", value);
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }

}
