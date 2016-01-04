package org.jfantasy.mall.stock.interceptor;

import org.springframework.beans.factory.annotation.Autowired;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.jfantasy.mall.goods.service.ProductService;
import org.jfantasy.mall.stock.bean.Stock;

/**
 * 变更库存(类似保存入库单)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-2 下午2:09:46
 * @version 1.0
 */
@Component
@Aspect
public class StockInterceptor {

	@Autowired
	private ProductService productService;

	/**
	 * 更新product占用数量
	 * 
	 * @功能描述 订单提交完成后，更新
	 * @param point
	 */
	@After("execution(public * org.jfantasy.mall.stock.service.StockService.save(..))")
	@Transactional
	public void replenish(JoinPoint point) {
		Stock stock = (Stock) point.getArgs()[0];
		this.productService.replenish(stock.getProduct().getId(), stock.getStatus() ? stock.getChangeNumber() : -stock.getChangeNumber());
	}

}
