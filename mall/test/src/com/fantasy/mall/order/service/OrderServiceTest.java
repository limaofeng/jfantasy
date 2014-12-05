package com.fantasy.mall.order.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.order.bean.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindPager() throws Exception {
        Pager<Order> pager = new Pager<Order>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_orderType","medical"));
        filters.add(new PropertyFilter("EQS_shipName",""));
        filters.add(new PropertyFilter("LIKES_sn_OR_shipName","2"));
        Pager<Order> orderPager = this.orderService.findPager(pager,filters);
        assertNotNull(orderPager);
    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testSubmitOrder() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testProcessed() throws Exception {

    }

    @Test
    public void testInvalid() throws Exception {

    }

    @Test
    public void testShipping() throws Exception {

    }
}