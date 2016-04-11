package org.jfantasy.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:backup/testconfig/spring/applicationContext.xml"})
public class HelloIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testPublishEvent() {
        applicationContext.publishEvent(new ContentEvent("今年是龙年的博客更新了"));
        /*
        User user = new User();
        user.setUsername("测试用户名");
        applicationContext.publishEvent(new RegisterEvent(user));
        applicationContext.publishEvent(new ContentOrderEvent(new Order() {
            @Override
            public String getSN() {
                return "123456";
            }

            @Override
            public String getType() {
                return "test";
            }

            @Override
            public String getSubject() {
                return "subject";
            }

            @Override
            public BigDecimal getTotalFee() {
                return BigDecimal.ONE;
            }

            @Override
            public BigDecimal getPayableFee() {
                return BigDecimal.ONE;
            }

            @Override
            public boolean isPayment() {
                return false;
            }

            @Override
            public List<OrderItem> getOrderItems() {
                return new ArrayList<OrderItem>();
            }

            @Override
            public ShipAddress getShipAddress() {
                return new ShipAddress();
            }
        }));
        */
    }

}

