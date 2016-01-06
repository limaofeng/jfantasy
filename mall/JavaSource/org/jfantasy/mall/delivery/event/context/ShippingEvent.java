package org.jfantasy.mall.delivery.event.context;

import org.jfantasy.mall.delivery.bean.Shipping;
import org.jfantasy.pay.product.order.Order;
import org.springframework.context.ApplicationEvent;

public class ShippingEvent extends ApplicationEvent {

    public ShippingEvent(Shipping shipping, Order order) {
        super(new ShippingSource(shipping, order));
    }

    public static class ShippingSource {
        private Shipping shipping;
        private Order order;

        public ShippingSource(Shipping shipping, Order order) {
            this.shipping = shipping;
            this.order = order;
        }

        public Shipping getShipping() {
            return shipping;
        }

        public Order getOrder() {
            return order;
        }

        public String getOrderType() {
            return order.getType();
        }

    }

}
