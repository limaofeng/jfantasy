package com.fantasy.mall.order.converter;

import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.attr.storage.bean.Converter;
import com.fantasy.attr.storage.service.AttributeService;
import com.fantasy.attr.storage.service.AttributeTypeService;
import com.fantasy.attr.storage.service.AttributeVersionService;
import com.fantasy.attr.storage.service.ConverterService;
import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.common.service.AreaService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.mall.delivery.bean.DeliveryCorp;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.service.DeliveryService;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.service.OrderService;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OrderTypeConverterTest {

    private final static Log logger = LogFactory.getLog(OrderTypeConverterTest.class);

    @Resource
    private AttributeVersionService attributeVersionService;
    @Resource
    private ConverterService converterService;
    @Resource
    private AttributeTypeService attributeTypeService;
    @Resource
    private AttributeService attributeService;
    @Resource
    private OrderService orderService;
    @Resource
    private DeliveryService deliveryService;
    @Resource
    private AreaService areaService;

    @Before
    public void setUp(){
        this.tearDown();

        Converter converter = new Converter();
        converter.setName("测试转换器");
        converter.setTypeConverter(OrderTypeConverter.class.getName());
        converter.setDescription("test");
        converterService.save(converter);

        converter = converterService.findUnique(Restrictions.eq("description", "test"), Restrictions.eq("typeConverter", OrderTypeConverter.class.getName()));
        logger.debug(converter);
        Assert.assertNotNull(converter);

        AttributeType attributeType = new AttributeType();
        attributeType.setName("测试数据类");
        attributeType.setDataType(Order.class.getName());
        attributeType.setForeignKey("sn");
        attributeType.setConverter(converter);
        attributeType.setDescription("");
        attributeTypeService.save(attributeType);

        attributeType = attributeTypeService.findUnique(Restrictions.eq("name", "测试数据类"));
        logger.debug(attributeType);
        Assert.assertNotNull(attributeType);

        Attribute attribute = new Attribute();
        attribute.setCode("testOrder");
        attribute.setName("测试 order 类型字段");
        attribute.setDescription("");
        attribute.setAttributeType(attributeType);
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        attributeService.save(attribute);

        AttributeVersion version = new AttributeVersion();
        version.setNumber("1.0.beta");
        version.setTargetClassName(Order.class.getName());
        version.setAttributes(new ArrayList<Attribute>());
        version.getAttributes().add(attribute);
        attributeVersionService.save(version);

        DeliveryCorp corp = new DeliveryCorp();
        corp.setName("测试物流公司");
        corp.setUrl("http://test.jfantasy.org");
        corp.setDescription("test");
        deliveryService.save(corp);

        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setMethod(DeliveryType.DeliveryMethod.cashOnDelivery);
        deliveryType.setName("测试配送方式");
        deliveryType.setDefaultDeliveryCorp(corp);
        deliveryType.setDescription("test");
        deliveryService.save(deliveryType);
    }

    @After
    public void tearDown(){
        //删除订单测试数据
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_orderType","TEST"));
        List<Order> orders = orderService.find(filters,"sn","asc");
        this.orderService.delete(ObjectUtil.toFieldArray(orders, "id", Long.class));

        //删除配送信息
        this.deliveryService.deleteDeliveryType(ObjectUtil.toFieldArray(deliveryService.findDeliveryTypePager(new Pager<DeliveryType>(), new ArrayList<PropertyFilter>() {
            {
                add(new PropertyFilter("EQS_description", "test"));
            }
        }).getPageItems(),"id",Long.class));
        this.deliveryService.deleteDeliveryCorp(ObjectUtil.toFieldArray(deliveryService.findDeliveryCorpPager(new Pager<DeliveryCorp>(),new ArrayList<PropertyFilter>(){
            {
                add(new PropertyFilter("EQS_description","test"));
            }
        }).getPageItems(),"id",Long.class));

        // 删除测试数据版本
        AttributeVersion version = attributeVersionService.getVersion(Order.class, "1.0.beta");
        if (version == null) {
            for(Converter converter : converterService.find(Restrictions.eq("description", "test"))){
                this.converterService.delete(converter.getId());
            }
        }else {
            for (Attribute attribute : version.getAttributes()) {
                this.converterService.delete(attribute.getAttributeType().getConverter().getId());
            }
            this.attributeVersionService.delete(version.getId());
        }

    }

    @Test
    public void testConvertValue() throws Exception {
        Order subOrder = new Order();
        subOrder.setOrderType("TEST");

        List<DeliveryType> deliveryTypes = deliveryService.findDeliveryTypePager(new Pager<DeliveryType>(), new ArrayList<PropertyFilter>() {
            {
                add(new PropertyFilter("EQS_description", "test"));
            }
        }).getPageItems();

        if(!deliveryTypes.isEmpty()) {
            OgnlUtil.getInstance().setValue("deliveryType.id", subOrder, deliveryTypes.get(0).getId());
        }

        subOrder.setShipName("林暮春");
        subOrder.setShipMobile("15921884771");
        subOrder.setShipPhone("021-xxxxxxx");
        subOrder.setShipAreaStore(JSON.serialize(areaService.get("310104")));
        subOrder.setShipAddress("田林路140号28好楼G09室");
        subOrder.setShipZipCode("200000");

        orderService.submitOrder(subOrder);

        //保存扩展的 order 对象
        Order order = VersionUtil.createDynaBean(Order.class,"1.0.beta");
        order.setOrderType("TEST");

        if(!deliveryTypes.isEmpty()) {
            OgnlUtil.getInstance().setValue("deliveryType.id", order, deliveryTypes.get(0).getId());
        }

        order.setShipName("林暮春");
        order.setShipMobile("15921884771");
        order.setShipPhone("021-xxxxxxx");
        order.setShipAreaStore(JSON.serialize(areaService.get("310104")));
        order.setShipAddress("田林路140号28好楼G09室");
        order.setShipZipCode("200000");

        AttributeType attributeType = order.getVersion().getAttributes().get(0).getAttributeType();
        OgnlUtil ognlUtil = VersionUtil.getOgnlUtil(attributeType);
        ognlUtil.setValue("testOrder", order, subOrder.getSn());
        Assert.assertNotNull(ognlUtil.getValue("testOrder",order));

        orderService.submitOrder(order);

        logger.debug(order);

        Order getOrder = orderService.get(order.getSn());
        Assert.assertEquals(subOrder.getSn(),ognlUtil.getValue("testOrder.sn", getOrder));

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_testOrder.sn",subOrder.getSn()));
        List<Order> orders = orderService.find(filters,"id","asc");
        Assert.assertEquals(1,orders.size());

        filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_testOrder.sn",subOrder.getSn()));
        orders = orderService.find(filters,"id","asc");
        Assert.assertEquals(1,orders.size());
    }

}