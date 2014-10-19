package com.fantasy.mall.order.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.mall.order.ws.IOrderService;
import com.fantasy.mall.order.ws.dto.OrderDTO;

import java.util.List;


public class OrderService extends WebServiceClient implements IOrderService {

    public OrderService() {// 设置对应的接口名称
        super("OrderService");
    }


    @Override
    public PagerDTO findPager(PagerDTO pager, PropertyFilterDTO[] filters) {
        return (PagerDTO) super.invokeOption("findPager", new Object[] { pager, filters }, new Class[] { PagerDTO.class, List.class, OrderDTO.class });
    }

    @Override
    public OrderDTO getSN(String sn) {
        return super.invokeOption("getSN", new Object[] { sn}, OrderDTO.class);
    }

    @Override
    public void submitOrder(OrderDTO order) {
        super.invokeOption("submitOrder", new Object[]{order},new Class[]{});
    }
}
