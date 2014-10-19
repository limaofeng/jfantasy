package com.fantasy.mall.order.ws;


import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.mall.order.ws.dto.OrderDTO;

public interface IOrderService {

    /**
     * 查询订单
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return {PagerDTO}
     */
    public PagerDTO findPager(PagerDTO pager, PropertyFilterDTO[] filters);

    /**
     * 通过订单编号获得订单信息
     *
     * @param sn 订单编码
     * @return {OrderDTO}
     */
    public OrderDTO getSN(String sn);


    /**
     * 新订单生产方法
     *
     * @param order 订单信息
     */
    public void submitOrder(OrderDTO order);
}
