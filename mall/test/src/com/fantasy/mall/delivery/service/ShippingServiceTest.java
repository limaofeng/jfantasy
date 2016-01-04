package org.jfantasy.mall.delivery.service;

import org.jfantasy.mall.delivery.bean.DeliveryCorp;
import org.jfantasy.mall.delivery.bean.DeliveryItem;
import org.jfantasy.mall.delivery.bean.DeliveryType;
import org.jfantasy.mall.delivery.bean.Shipping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ShippingServiceTest {

    private static final Log LOG = LogFactory.getLog(ShippingServiceTest.class);

    @Autowired
    private ShippingService shippingService;
    @Autowired
    private DeliveryTypeService deliveryTypeService;
    @Autowired
    private DeliveryCorpService deliveryCorpService;

    @Before
    public void setUp() throws Exception {
        DeliveryCorp corp = deliveryCorpService.findUnique(Restrictions.eq("name", "测试物流公司"));
        if(corp == null) {
            corp = new DeliveryCorp();
            corp.setName("测试物流公司");
            corp.setDescription("测试物流公司");
            corp.setUrl("http://test.corp.com");
            corp = deliveryCorpService.save(corp);
        }

        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setName("TEST");
        deliveryType.setMethod(DeliveryType.DeliveryMethod.deliveryAgainstPayment);
        deliveryType.setFirstWeight(50);
        deliveryType.setFirstWeightPrice(BigDecimal.ONE);
        deliveryType.setContinueWeight(10);
        deliveryType.setContinueWeightPrice(BigDecimal.ONE);
        deliveryType.setDefaultDeliveryCorp(corp);
        deliveryTypeService.save(deliveryType);
    }

    @After
    public void tearDown() throws Exception {
        DeliveryType deliveryType = deliveryTypeService.findUnique(Restrictions.eq("name", "TEST"));
        if (deliveryType != null) {
            this.deliveryTypeService.delete(deliveryType.getId());
        }
    }

    @Test
    public void testSave() throws Exception {
        DeliveryType deliveryType = deliveryTypeService.findUnique(Restrictions.eq("name", "TEST"));
        Shipping shipping = shippingService.save(deliveryType.getId(), "SN001", "TEST", new ArrayList<DeliveryItem>() {
            {
                DeliveryItem deliveryItem = new DeliveryItem();
                deliveryItem.setSn("SN000001");
                deliveryItem.setQuantity(1);
                this.add(deliveryItem);
            }
        });
        LOG.debug(shipping);
    }
}