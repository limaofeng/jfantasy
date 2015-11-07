package com.fantasy.common.order;

import com.fantasy.common.service.AreaService;
import com.fantasy.payment.bean.Payment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestOrderService extends AbstractOrderService implements InitializingBean {

    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired
    private AreaService areaService;

    @Override
    public void afterPropertiesSet() throws Exception {
        orderServiceFactory.register("test", this);
    }

    @Override
    public Order loadOrderBySn(final String sn) {
        return new Order() {

            @Override
            public String getSN() {
                return sn;
            }

            @Override
            public String getType() {
                return "TEST";
            }

            @Override
            public String getSubject() {
                return "测试订单";
            }

            @Override
            public BigDecimal getTotalFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public BigDecimal getPayableFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public boolean isPayment() {
                return true;
            }

            @Override
            public List<OrderItem> getOrderItems() {
                return new ArrayList<OrderItem>() {
                    {
                        this.add(new OrderItem() {
                            @Override
                            public String getSn() {
                                return "SN000001";
                            }

                            @Override
                            public String getName() {
                                return "这个是测试订单项";
                            }

                            @Override
                            public Integer getQuantity() {
                                return 1;
                            }
                        });
                    }
                };
            }

            @Override
            public ShipAddress getShipAddress() {
                ShipAddress address = new ShipAddress();
                address.setArea(areaService.get("430103"));
                address.setName("王五");
                address.setAddress("天心区308号");
                address.setMobile("159218471");
                address.setZipCode("415000");
                return address;
            }

        };
    }

    @Override
    public void payFailure(Payment payment) {
        LOG.debug("订单支付失败");
    }

    @Override
    public void paySuccess(Payment payment) {
        LOG.debug("订单支付成功");
    }

    @Override
    public OrderUrls getOrderUrls() {
        OrderUrls urls = new OrderUrls();
        urls.setResultUrl("{{serverUrl}}/pays/{{orderSn}}/result");
        urls.setDetailsUrl("{{serverUrl}}/pays/{{orderSn}}/details");
        return urls;
    }


}
