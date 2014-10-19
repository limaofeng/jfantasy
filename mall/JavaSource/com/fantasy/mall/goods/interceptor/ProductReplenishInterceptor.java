package com.fantasy.mall.goods.interceptor;

import com.fantasy.mall.goods.service.GoodsService;
import com.fantasy.mall.goods.service.ProductService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 修改货品的库存数时
 */
@Component
@Aspect
public class ProductReplenishInterceptor {

    @Resource
    private ProductService productService;
    @Resource
    private GoodsService goodsService;

    /**
     * 货品调整库存时,更新商品的库存数量
     *
     * @功能描述
     * @param point
     */
    @After("execution(public * com.fantasy.mall.goods.service.ProductService.replenish(..))")
    @Transactional
    public void replenish(JoinPoint point) {
        Long productId = (Long) point.getArgs()[0];
        Integer quantity = (Integer) point.getArgs()[1];
        goodsService.replenish(productService.get(productId).getGoods().getId(), quantity);
    }

}
