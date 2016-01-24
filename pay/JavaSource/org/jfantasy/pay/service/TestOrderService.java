package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.common.service.AreaService;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestOrderService implements OrderService {

    protected final Log LOG = LogFactory.getLog(TestOrderService.class);

    @Autowired
    private AreaService areaService;

    @Override
    public String[] types() {
        return new String[]{"test"};
    }

    @Override
    public Order loadOrder(final String sn) {
        return new Order() {

            @Override
            public String getSN() {
                return sn;
            }

            @Override
            public String getType() {
                return "test";
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

    public void payFailure(Payment payment) {
        LOG.debug("订单支付失败");
    }

    public void paySuccess(Payment payment) {
        LOG.debug("订单支付成功");
    }

    public OrderUrls getOrderUrls() {
        OrderUrls urls = new OrderUrls();
        urls.setResultUrl("{{serverUrl}}/pays/{{orderSn}}/result");
        urls.setDetailsUrl("{{serverUrl}}/pays/{{orderSn}}/details");
        return urls;
    }


}
