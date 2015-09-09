package com.fantasy.mall.delivery.rest;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.service.DeliveryTypeService;
import junit.framework.Assert;
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

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSearch() throws Exception {

    }

    @Test
    public void testView() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {
        Shipping shipping = new Shipping();
        OgnlUtil.getInstance().setValue("deliveryType.id", shipping, deliveryTypeService.get(1L).getId());
        shipping.setOrderSn("SN000001");
        shipping.setOrderType("TEST");
        shipping.setMemo("测试数据");

        shipping.setDeliveryItems(new ArrayList<DeliveryItem>() {
            {
                DeliveryItem deliveryItem = new DeliveryItem();
                deliveryItem.setSn("SN000001");
                deliveryItem.setQuantity(1);
                this.add(deliveryItem);
            }
        });

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/delivery/shippings").contentType(MediaType.APPLICATION_JSON).content(JSON.serialize(shipping))).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDelete() throws Exception {

    }
}