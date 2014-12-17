package com.fantasy.mall.order.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.service.DeliveryService;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.member.bean.Receiver;
import com.fantasy.mall.member.service.ReceiverService;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.bean.Order.OrderStatus;
import com.fantasy.mall.order.bean.Order.PaymentStatus;
import com.fantasy.mall.order.bean.Order.ShippingStatus;
import com.fantasy.mall.order.bean.OrderItem;
import com.fantasy.mall.order.dao.OrderDao;
import com.fantasy.mall.order.dao.OrderItemDao;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.service.DataDictionaryService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单管理
 */
@Service
@Transactional
public class OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private ReceiverService receiverService;
    @Resource
    private ProductService productService;
    @Resource
    private DeliveryService deliveryService;

    private static final String defaultCashOnDeliveryName = "货到付款";

    public Pager<Order> findPager(Pager<Order> pager, List<PropertyFilter> filters) {
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        return orderDao.findPager(pager, filters);
    }

    /**
     * 前台中心未完成的订单
     *
     * @param filters 过滤条件
     * @param orderBy 排序字段
     * @param order   排序方向
     * @return {List<order>}
     */
    public List<Order> find(List<PropertyFilter> filters, String orderBy, String order) {
        return this.orderDao.find(filters, orderBy, order);
    }

    /**
     * 查询订单
     *
     * @param filters 过滤条件
     * @param orderBy 排序字段
     * @param order   排序方向
     * @param size    条数
     * @return List<Order>
     */
    public List<Order> find(List<PropertyFilter> filters, String orderBy, String order, Integer size) {
        return this.orderDao.find(filters, orderBy, order, 0, size);
    }

    /**
     * 订单保存
     *
     * @param order 订单对象
     * @return {Order}
     */
    public Order save(Order order) {
        this.orderDao.save(order);
        return order;
    }

    /**
     * 新订单
     *
     * @param order 订单对象
     */
    public void submitOrder(Order order) {
        // 初始订单相关状态
        order.setOrderStatus(OrderStatus.unprocessed);// 初始订单状态
        order.setPaymentStatus(PaymentStatus.unpaid);// 初始支付状态
        order.setShippingStatus(ShippingStatus.unshipped);// 初始发货状态
        // 初始化收货人信息
        if (ObjectUtil.isNotNull(order.getReceiverId())) {
            Receiver receiver = receiverService.get(order.getReceiverId());
            order.setShipName(receiver.getName());// 收货人姓名
            order.setShipAreaStore(receiver.getAreaStore());// 收货地区存储
            order.setShipAddress(receiver.getAddress());// 收货地址
            order.setShipZipCode(receiver.getZipCode());// 收货邮编
            order.setShipPhone(receiver.getPhone());// 收货电话
            order.setShipMobile(receiver.getMobile());// 收货手机
        }
        // 初始化订单项信息
        BigDecimal totalProductPrice = new BigDecimal(0);
        int totalProductQuantity = 0, totalProductWeight = 0;
        for (OrderItem item : order.getOrderItems()) {
            item.initialize(productService.get(item.getSn()));
            totalProductPrice = totalProductPrice.add(item.getSubtotalPrice());
            totalProductQuantity += item.getProductQuantity();
            totalProductWeight += item.getSubtotalWeight();
            item.setOrder(order);
        }
        order.setTotalProductWeight(totalProductWeight);// 订单商品总重量
        order.setTotalProductQuantity(totalProductQuantity);// 订单商品数量
        order.setTotalProductPrice(totalProductPrice);// 订单商品总价
        // 初始化配置信息
        DeliveryType deliveryType = deliveryService.getDeliveryType(order.getDeliveryType().getId());
        order.setDeliveryTypeName(deliveryType.getName());
        order.setDeliveryType(deliveryType);
        if (deliveryType.getMethod() == DeliveryType.DeliveryMethod.deliveryAgainstPayment) {
            BigDecimal deliveryFee = deliveryType.getFirstWeightPrice();
            if (order.getTotalProductWeight() > deliveryType.getFirstWeight() && deliveryType.getContinueWeightPrice().intValue() > 0) {// 如果订单重量大于配送首重量且配送方式续重方式价格大于0时,计算快递费
                Integer weigth = order.getTotalProductWeight() - deliveryType.getFirstWeight();
                Integer number;
                if (weigth % deliveryType.getContinueWeight() == 0) {
                    number = weigth / deliveryType.getContinueWeight();
                } else {
                    number = weigth / deliveryType.getContinueWeight() + 1;
                }
                deliveryFee = deliveryFee.add(deliveryType.getContinueWeightPrice().multiply(BigDecimal.valueOf(number)));
            }
            order.setDeliveryFee(deliveryFee);// 快递费用
        } else {
            order.setDeliveryFee(BigDecimal.valueOf(0));
        }
        // 初始化支付信息
        order.setPaidAmount(new BigDecimal(0));// 已付金额
        order.setPaymentFee(new BigDecimal(0));
        // 初始化支付信息(创建订单时只判断是否为货到付款)
        if (DeliveryType.DeliveryMethod.cashOnDelivery == deliveryType.getMethod()) {
            order.setPaymentFee(new BigDecimal(0));// 支付费
            DataDictionary config = DataDictionaryService.get("deliveryMethod", DeliveryType.DeliveryMethod.cashOnDelivery.name());
            order.setPaymentConfigName(config == null ? defaultCashOnDeliveryName : StringUtil.defaultValue(config.getName(), defaultCashOnDeliveryName));
            order.setPaymentConfig(null);
        } else {
            order.setPaymentConfigName("支付功能尚未实现");
        }
        // 订单所属人
        if (SpringSecurityUtils.getCurrentUser() instanceof MemberUser) {
            order.setMember(SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser());
        }
        //解决附言不能为空的问题
        if (StringUtil.isBlank(order.getMemo())) {
            order.setMemo("");
        }
        order.setTotalAmount(order.getTotalProductPrice().add(order.getDeliveryFee()));// 订单总金额(商品金额+邮费)
        this.orderDao.save(order);
        for (OrderItem item : order.getOrderItems()) {
            this.orderItemDao.save(item);
        }
    }

    /**
     * 根据id 批量删除订单
     *
     * @param ids 订单Ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.orderDao.delete(id);
        }
    }

    /**
     * 根据 编号 获取对象
     *
     * @param sn 订单编号
     * @return {Order}
     */
    public Order get(String sn) {
        return this.findUnique(Restrictions.eq("sn", sn));
    }

    /**
     * 根据 criterions 获取对象
     *
     * @param criterions 筛选条件
     * @return {Order}
     */
    public Order findUnique(Criterion... criterions) {
        Order order = this.orderDao.findUnique(criterions);
        if (order == null) {
            return null;
        }
        Hibernate.initialize(order);
        for (OrderItem item : order.getOrderItems()) {
            Hibernate.initialize(item);
        }
        for (Shipping item : order.getShippings()) {
            Hibernate.initialize(item);
        }
        return order;
    }

    /**
     * 返回订单列表
     *
     * @param filters 过滤条件
     * @return {List<Order>}
     */
    public List<Order> get(List<PropertyFilter> filters) {
        return this.orderDao.find(filters);
    }

    /**
     * 获取order
     *
     * @param id 订单id
     * @return {Order}
     */
    public Order get(Long id) {
        Order order = this.orderDao.get(id);
        Hibernate.initialize(order);
        for (OrderItem item : order.getOrderItems()) {
            Hibernate.initialize(item);
        }
        for (Shipping item : order.getShippings()) {
            Hibernate.initialize(item);
        }
        return order;
    }

    /**
     * 订单已处理
     */
    public void processed(Long[] ids) {
        for (Long id : ids) {
            Order order = this.orderDao.get(id);
            order.setOrderStatus(Order.OrderStatus.processed);
            this.orderDao.save(order);
        }
    }

    /**
     * 订单作废
     */
    public void invalid(Long[] ids) {
        for (Long id : ids) {
            Order order = this.orderDao.get(id);
            order.setOrderStatus(Order.OrderStatus.invalid);
            this.orderDao.save(order);
        }
    }

    /**
     * 订单发货
     *
     * @param shipping 订单发货操作
     */
    public void shipping(Long orderId, Shipping shipping) {
        Order order = this.orderDao.get(orderId);
        order.setShippingStatus(Order.ShippingStatus.partShipped);
        boolean shipped = true;
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getProductQuantity().equals(orderItem.getDeliveryQuantity())) {
                continue;
            }
            DeliveryItem deliveryItem = ObjectUtil.find(shipping.getDeliveryItems(), "sn", orderItem.getSn());
            if (deliveryItem != null) {
                orderItem.setDeliveryQuantity(orderItem.getDeliveryQuantity() + deliveryItem.getQuantity());
                this.orderItemDao.save(orderItem);
            }
            if (shipped && !orderItem.getProductQuantity().equals(orderItem.getDeliveryQuantity())) {
                shipped = false;
            }
        }
        if (shipped) {
            order.setShippingStatus(Order.ShippingStatus.shipped);
        }
        this.orderDao.save(order);
    }

}
