package org.jfantasy.mall.delivery.rest;

import junit.framework.Assert;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.mall.delivery.bean.DeliveryCorp;
import org.jfantasy.mall.delivery.bean.DeliveryType;
import org.jfantasy.mall.delivery.rest.form.DeliveryItemForm;
import org.jfantasy.mall.delivery.rest.form.ShippingForm;
import org.jfantasy.mall.delivery.service.DeliveryCorpService;
import org.jfantasy.mall.delivery.service.DeliveryTypeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ShippingControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private DeliveryTypeService deliveryTypeService;
    @Autowired
    private DeliveryCorpService deliveryCorpService;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DeliveryCorp corp = deliveryCorpService.findUnique(Restrictions.eq("name", "测试物流公司"));
        if(corp == null) {
            corp = new DeliveryCorp();
            corp.setName("测试物流公司");
            corp.setDescription("测试物流公司");
            corp.setUrl("http://test.corp.com");
            corp = deliveryCorpService.save(corp);
        }

        DeliveryType deliveryType = deliveryTypeService.findUnique(Restrictions.eq("name", "TEST"));
        if(deliveryType == null) {
            deliveryType = new DeliveryType();
            deliveryType.setName("TEST");
            deliveryType.setMethod(DeliveryType.DeliveryMethod.deliveryAgainstPayment);
            deliveryType.setFirstWeight(50);
            deliveryType.setFirstWeightPrice(BigDecimal.ONE);
            deliveryType.setContinueWeight(10);
            deliveryType.setContinueWeightPrice(BigDecimal.ONE);
            deliveryType.setDefaultDeliveryCorp(corp);
            deliveryTypeService.save(deliveryType);
        }
    }

    @After
    public void tearDown() throws Exception {
        DeliveryType deliveryType = deliveryTypeService.findUnique(Restrictions.eq("name", "TEST"));
        if (deliveryType != null) {
            this.deliveryTypeService.delete(deliveryType.getId());
        }
    }

    public void testSearch() throws Exception {

    }

    public void testView() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {
        DeliveryType deliveryType = deliveryTypeService.findUnique(Restrictions.eq("name", "TEST"));

        ShippingForm shipping = new ShippingForm();
        OgnlUtil.getInstance().setValue("deliveryTypeId", shipping, deliveryType.getId());
        shipping.setOrderSn("SN000001");
        shipping.setOrderType("TEST");

        shipping.setItems(new ArrayList<DeliveryItemForm>() {
            {
                DeliveryItemForm deliveryItem = new DeliveryItemForm();
                deliveryItem.setSn("SN000001");
                deliveryItem.setQuantity(1);
                this.add(deliveryItem);
            }
        });

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/delivery/shippings").contentType(MediaType.APPLICATION_JSON).content(JSON.serialize(shipping))).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    public void testDelete() throws Exception {

    }
}