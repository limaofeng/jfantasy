package org.jfantasy.pay.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.product.order.OrderService;

/**
 * 实现一个简单的  OrderDetailsService 封装实现
 */
public abstract class AbstractOrderService implements OrderService {

    protected final Log LOG = LogFactory.getLog(this.getClass());

}
