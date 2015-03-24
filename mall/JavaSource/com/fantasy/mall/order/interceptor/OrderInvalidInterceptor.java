package com.fantasy.mall.order.interceptor;

import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.bean.OrderItem;
import com.fantasy.mall.order.service.OrderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单失效(关闭)
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-25 下午3:43:18
 */
@Component
@Aspect
public class OrderInvalidInterceptor {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    /**
     * 订单关闭时，还购买商品的占用数量
     *
     * @param point {}
     */
    //@After("execution(public * com.fantasy.mall.order.service.OrderService.invalid(..))")
    //@Transactional
    public void revertFreezeStore(JoinPoint point) {
        for (Long orderId : (Long[]) point.getArgs()[0]) {
            if (orderId == null)
                continue;
            Order order = orderService.get(orderId);
            if (order == null)
                continue;
            for (OrderItem orderItem : order.getOrderItems()) {
                productService.freezeStore(orderItem.getProduct().getId(), -orderItem.getProductQuantity());
            }
        }
    }

}
