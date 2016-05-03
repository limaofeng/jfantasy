package org.jfantasy.pay.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.order.entity.OrderDetails;
import org.jfantasy.pay.order.entity.OrderItem;
import org.jfantasy.rpc.annotation.ServiceExporter;

import java.math.BigDecimal;
import java.util.ArrayList;

@ServiceExporter(targetInterface = OrderService.class, debugAddress = "127.0.0.1:9091")
public class TestOrderService implements OrderService {

    protected final Log LOG = LogFactory.getLog(TestOrderService.class);

    @Override
    public String[] types() {
        return new String[]{"test"};
    }

    @Override
    public OrderDetails loadOrder(final String sn) {
        OrderDetails order = new OrderDetails();
        order.setType("test");
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
    public void on(String sn, PaymentStatus status, String message) {
        System.out.println(sn + " - " + status + " - " + message);
    }

    @Override
    public void on(String sn, RefundStatus status, String message) {
        System.out.println(sn + " - " + status + " - " + message);
    }

}
