package com.fantasy.mall.order.interceptor;

import com.fantasy.mall.cart.service.ShopCart;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.bean.OrderItem;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单提交后
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-2 下午1:45:51
 */
@Component
@Aspect
public class OrderSubmitInterceptor {

    @Autowired
    private ProductService productService;

    /**
     * 订单提交后刷新购物车
     *
     * @param point JoinPoint
     */
    //@After("execution(public * com.fantasy.mall.order.service.OrderService.submitOrder(..))")
    //@Transactional
    public void freshCart(JoinPoint point) {
        Order order = (Order) point.getArgs()[0];
        for (OrderItem orderItem : order.getOrderItems()) {
            ShopCart.current().removeItem(orderItem.getSn(), orderItem.getProductQuantity());
        }
    }

    /**
     * 更新product占用数量
     * 订单提交完成后，更新
     *
     * @param point JoinPoint
     */
    //@After("execution(public * com.fantasy.mall.order.service.OrderService.submitOrder(..))")
    //@Transactional
    public void freezeStore(JoinPoint point) {
        Order order = (Order) point.getArgs()[0];
        for (OrderItem orderItem : order.getOrderItems()) {
            productService.freezeStore(orderItem.getProduct().getId(), orderItem.getProductQuantity());
        }
    }

}
