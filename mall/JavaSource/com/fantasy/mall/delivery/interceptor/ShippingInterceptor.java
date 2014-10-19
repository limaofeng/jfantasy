package com.fantasy.mall.delivery.interceptor;

import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.order.service.OrderService;
import com.fantasy.mall.stock.service.StockService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 发货拦截器
 */
@Component
@Aspect
public class ShippingInterceptor {

	@Resource
	private ProductService productService;
	@Resource
	private OrderService orderService;
	@Resource
	private StockService stockService;

	/**
	 * 发货后，同步修改库存信息
	 * 
	 * @param point
	 */
	@Transactional
	@After("execution(public * com.fantasy.mall.delivery.service.DeliveryService.save(com.fantasy.mall.delivery.bean.Shipping))")
	public void outStock(JoinPoint point) {
		Shipping shipping = (Shipping) point.getArgs()[0];
		stockService.outStock(shipping);
	}

	/**
	 * 发货后，更新商品占用数量
	 * 
	 * @param point
	 */
	@Transactional
	@After("execution(public * com.fantasy.mall.delivery.service.DeliveryService.save(com.fantasy.mall.delivery.bean.Shipping))")
	public void freezeStore(JoinPoint point) {
		Shipping shipping = (Shipping) point.getArgs()[0];
		for (DeliveryItem item : shipping.getDeliveryItems()) {
			productService.freezeStore(item.getProduct().getId(), -item.getQuantity());
		}
	}

	/**
	 * 发货后，判断订单是否发货完成
	 * 
	 * @param point
	 * @功能描述
	 */
	@Transactional
	@After("execution(public * com.fantasy.mall.delivery.service.DeliveryService.save(com.fantasy.mall.delivery.bean.Shipping))")
	public void refreshOrder(JoinPoint point) {
		Shipping shipping = (Shipping) point.getArgs()[0];
		orderService.shipping(shipping.getOrder().getId(), shipping);
	}

}
