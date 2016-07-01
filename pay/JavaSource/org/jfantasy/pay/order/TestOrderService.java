package org.jfantasy.pay.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.order.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TestOrderService implements OrderService {

    protected final Log LOG = LogFactory.getLog(TestOrderService.class);

    public final static String ORDER_TYPE = "test";

    private static TestOrderService instance = new TestOrderService();

    public static TestOrderService getInstance(){
        return instance;
    }

    private TestOrderService(){

    }

    @Override
    public String[] types() {
        return new String[]{ORDER_TYPE};
    }

    @Override
    public OrderDetails loadOrder(final OrderKey sn) {
        OrderDetails order = new OrderDetails();
        order.setType(ORDER_TYPE);
        order.setSubject("测试订单");
        order.setBody("商品详情");
        order.setTotalFee(BigDecimal.valueOf(0.01));
        order.setPayableFee(BigDecimal.valueOf(0.01));
        order.setPayment(true);
        order.setOrderItems(new ArrayList<OrderItem>() {
            {
                OrderItem item = new OrderItem();
                item.setSn("SN000001");
                item.setName("这个是测试订单项");
                item.setQuantity(1);
                this.add(item);
            }
        });
        return order;
    }

    @Override
    public void on(OrderKey key, PaymentDetails status, String message) {
        System.out.println(key + " - " + status + " - " + message);
    }

    @Override
    public void on(OrderKey key, RefundDetails status, String message) {
        System.out.println(key + " - " + status + " - " + message);
    }

}
