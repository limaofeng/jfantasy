package com.fantasy.mall.delivery.service;

import com.fantasy.common.order.Order;
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
import com.fantasy.common.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Pager<Shipping> findPager(Pager<Shipping> pager, List<PropertyFilter> filters) {
        return this.shippingDao.findPager(pager, filters);
    }

    public Order getOrder(Long id) {
        Shipping shipping = this.shippingDao.get(id);
        OrderService orderDetailsService = orderServiceFactory.get(shipping.getOrderType());
        return orderDetailsService.loadOrderBySn(shipping.getOrderSn());
    }

    /**
     * 发货信息
     *
     * @param shipping 必输项: order.id,deliveryType.id,deliverySn，deliveryFee，deliveryItems 可选输入项：memo
     */
    public Shipping save(Shipping shipping) {
        OrderService orderDetailsService = orderServiceFactory.get(shipping.getOrderType());
        Order order = orderDetailsService.loadOrderBySn(shipping.getOrderSn());
        DeliveryType deliveryType = deliveryTypeDao.get(shipping.getDeliveryType().getId());
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
        shipping.setShipPhone(order.getShipAddress().getPhone());
        shipping.setShipMobile(order.getShipAddress().getMobile());
        // 初始化物流项
        for (DeliveryItem item : shipping.getDeliveryItems()) {
            item.initialize(ObjectUtil.find(order.getOrderItems(), "sn", item.getSn()));
            item.setShipping(shipping);
        }
        shipping = this.shippingDao.save(shipping);
        for (DeliveryItem item : shipping.getDeliveryItems()) {
            this.deliveryItemDao.save(item);
        }
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
