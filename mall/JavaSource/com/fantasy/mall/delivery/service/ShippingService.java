package com.fantasy.mall.delivery.service;

import com.fantasy.common.order.Order;
import com.fantasy.common.order.OrderService;
import com.fantasy.common.order.OrderServiceFactory;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.dao.DeliveryItemDao;
import com.fantasy.mall.delivery.dao.DeliveryTypeDao;
import com.fantasy.mall.delivery.dao.ShippingDao;
import com.fantasy.mall.delivery.event.context.ShippingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShippingService {

    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private DeliveryTypeDao deliveryTypeDao;
    @Autowired
    private DeliveryItemDao deliveryItemDao;
    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired
    private ApplicationContext applicationContext;

    public Pager<Shipping> findPager(Pager<Shipping> pager, List<PropertyFilter> filters) {
        return this.shippingDao.findPager(pager, filters);
    }

    public Order getOrder(Long id) {
        Shipping shipping = this.shippingDao.get(id);
        OrderService orderDetailsService = orderServiceFactory.getOrderService(shipping.getOrderType());
        return orderDetailsService.loadOrderBySn(shipping.getOrderSn());
    }

    /**
     * 发货信息
     *
     * @param deliveryTypeId 配送方式
     * @param orderSn        订单SN
     * @param orderType      订单类型
     * @param items          物流项
     * @return Shipping
     */
    public Shipping save(Long deliveryTypeId, String orderSn, String orderType, List<DeliveryItem> items) {
        //初始化发货信息
        Shipping shipping = new Shipping();
        shipping.setOrderSn(orderSn);
        shipping.setOrderType(orderType);
        shipping.setDeliveryType(new DeliveryType(deliveryTypeId));
        shipping.setDeliveryItems(items);
        //获取订单信息
        OrderService orderDetailsService = orderServiceFactory.getOrderService(orderType);
        Order order = orderDetailsService.loadOrderBySn(orderSn);
        DeliveryType deliveryType = deliveryTypeDao.get(deliveryTypeId);
        shipping.setDeliveryType(deliveryType);
        // 初始化快递信息
        shipping.setDeliveryTypeName(deliveryType.getName());
        shipping.setDeliveryCorpName(deliveryType.getDefaultDeliveryCorp().getName());
        shipping.setDeliveryCorpUrl(deliveryType.getDefaultDeliveryCorp().getUrl());
        // 添加收货地址信息
        shipping.setShipName(order.getShipAddress().getName());
        shipping.setShipAreaStore(order.getShipAddress().getAreaStore());
        shipping.setShipAddress(order.getShipAddress().getAddress());
        shipping.setShipZipCode(order.getShipAddress().getZipCode());
        shipping.setShipMobile(order.getShipAddress().getMobile());

        shipping = this.shippingDao.save(shipping);

        // 初始化物流项
        for (DeliveryItem item : shipping.getDeliveryItems()) {
            item.initialize(ObjectUtil.find(order.getOrderItems(), "sn", item.getSn()));
            item.setShipping(shipping);
            this.deliveryItemDao.save(item);
        }
        applicationContext.publishEvent(new ShippingEvent(shipping, order));
        return shipping;
    }

    public Shipping get(Long id) {
        return this.shippingDao.get(id);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.shippingDao.delete(id);
        }
    }

}
