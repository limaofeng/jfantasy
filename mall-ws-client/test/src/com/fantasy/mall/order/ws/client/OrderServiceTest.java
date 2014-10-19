package com.fantasy.mall.order.ws.client;


import com.fantasy.mall.goods.ws.client.GoodsService;
import com.fantasy.mall.goods.ws.dto.GoodsDTO;
import com.fantasy.mall.order.ws.dto.DeliveryTypeDTO;
import com.fantasy.mall.order.ws.dto.OrderDTO;
import com.fantasy.mall.order.ws.dto.OrderItemDTO;
import com.fatnasy.ws.Constants;
import org.junit.Before;
import org.junit.Test;

public class OrderServiceTest {


    private OrderService orderService;
    private  GoodsService goodsService;
    @Before
    public void init() throws Exception {
        goodsService = new GoodsService();
        goodsService.setEndPointReference(Constants.END_POINT_REFERENCE);
        goodsService.setTargetNamespace("http://ws.goods.mall.fantasy.com");
        goodsService.afterPropertiesSet();
        orderService = new OrderService();
        orderService.setEndPointReference(Constants.END_POINT_REFERENCE);
        orderService.setTargetNamespace("http://ws.order.mall.fantasy.com");
        orderService.afterPropertiesSet();


    }


    @Test
    public void testSave() {
        OrderDTO orderDTO = new OrderDTO();
        //MemberDTO member=new MemberDTO();
        //member=memberService.findUniqueByUsername("7897");
    //    Long id = Long.valueOf("82");
      // GoodsDTO goods =  goodsService.getGoodsById(id);
        OrderItemDTO[] OrderItemDTOs=new OrderItemDTO[1];
        OrderItemDTOs[0]=new OrderItemDTO();
       // OrderItemDTOs[0].setGoods(goods);
       // OrderItemDTOs[0].setName("ccc");
       //OrderItemDTOs[0].setProductQuantity(2);
       // OrderItemDTOs[0].setDeliveryQuantity(3);
      //  OrderItemDTOs[0].setId(3l);
        OrderItemDTOs[0].setSn("SN_2014041100013");
      //  OrderItemDTOs[0].setSn(goods.getSn());
        orderDTO.setOrderItems(OrderItemDTOs);
        orderDTO.setShipName("cc");
        orderDTO.setShipAreaStore("sadasd");// 收货人姓名
        orderDTO.setShipAddress("asdsad");// 收货地区存储
        orderDTO.setShipZipCode("asdasd");// 收货地址
        orderDTO.setShipPhone("asdasd");// 收货邮编
        orderDTO.setShipMobile("asdad");// 收货手机



        DeliveryTypeDTO  deliveryTypeDTO= new DeliveryTypeDTO();
        deliveryTypeDTO.setId(2l);
        deliveryTypeDTO.setName("123");
        deliveryTypeDTO.setMethod("deliveryAgainstPayment");

        orderDTO.setDeliveryTypeDTO(deliveryTypeDTO);


        orderService.submitOrder(orderDTO);
        // intentionOrderDTO.setMember(member);
       // OrderDTO intention = orderService
      //  System.out.println("intention.getName()"+intention.getId());
        //System.out.println("intention.getGoods()"+intention.getGoods());
        //	System.out.println("intention.getMember().getNickName()"+intention.getMember().getNickName());
       // System.out.println("intention.getGoods().getSn()"+intention.getOrderItems()[0].getGoods().getSn());


    }



}
