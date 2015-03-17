package com.fantasy.mall.order.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.fantasy.mall.delivery.bean.DeliveryType;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.delivery.service.DeliveryService;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.member.service.ReceiverService;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.bean.OrderItem;
import com.fantasy.mall.order.service.OrderService;
import com.fantasy.member.bean.Member;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;

/**
 * 订单
 */
public class OrderAction extends ActionSupport {

    private static final long serialVersionUID = 5809261416684874733L;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ReceiverService receiverService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DeliveryService deliveryService;

    /**
     * 列表页面
     *
     * @return
     */
    public String index(Pager<Order> pager, List<PropertyFilter> filters) {
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 列表查询
     *
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Order> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, orderService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    public String view(Long id) {
        this.attrs.put("order", orderService.get(id));
        return SUCCESS;
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    public String edit(Long id) {
        this.attrs.put("order", orderService.get(id));
        return SUCCESS;
    }

    /**
     * 订单地址修改
     *
     * @param id
     * @return
     */
    public String shipedit(Long id) {
        this.attrs.put("order", orderService.get(id));
        return SUCCESS;
    }

    /**
     * 保存订单收货地址
     *
     * @param order
     * @return
     */
    public String save(Order order) {
        this.attrs.put(ROOT, orderService.save(order));
        return JSONDATA;
    }

    /**
     * 在《新订单》 通知发货 改变订单状态为已处理
     *
     * @param ids
     * @return
     */
    public String processed(Long[] ids) {
        this.orderService.processed(ids);
        return JSONDATA;
    }

    /**
     * 订单作废
     *
     * @param ids
     * @return
     */
    public String invalid(Long[] ids) {
        this.orderService.invalid(ids);
        return JSONDATA;
    }

    /**
     * 前台订单列表
     *
     * @return
     */
    public String order() {
        // 会员信息
        Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQL_member.id", member.getId().toString()));
        this.search(new Pager<Order>(), filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    private static final String ORDER_ITEMS_KEY = "TEMP_ORDER_ITEMS_KEY";

    /**
     * 订单确认
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public String confirm(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            items = (List<OrderItem>) this.session.get(ORDER_ITEMS_KEY);
            if (items == null) {
                return "cart";
            }
        } else {
            this.session.put(ORDER_ITEMS_KEY, items);
        }
        if (SpringSecurityUtils.getCurrentUser(MemberUser.class) != null) {
            Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
            // 收货地址信息
            this.attrs.put("receivers", this.receiverService.find(new Criterion[]{Restrictions.eq("member.id", member.getId())}, "isDefault", "desc"));
        }
        // 订单商品信息
        for (OrderItem item : items) {
            item.initialize(productService.get(item.getSn()));
        }
        this.attrs.put("items", items);
        // 配送方式
        this.attrs.put("deliveryTypes", deliveryService.listDeliveryType());
        return SUCCESS;
    }

    /**
     * 订单提交
     *
     * @param order
     * @param order
     * @return
     */
    public String submit(Order order) {
        this.orderService.submitOrder(order);
        this.session.remove(ORDER_ITEMS_KEY);
        this.attrs.put("sn", order.getSn());
        if (order.getDeliveryType().getMethod() == DeliveryType.DeliveryMethod.cashOnDelivery) {
            return SUCCESS;
        } else {
            return "payment";
        }
    }

    /**
     * 订单支付页面
     *
     * @param sn
     * @return
     */
    public String payment(String sn) {
        Order order = this.orderService.get(sn);
        if (Order.OrderStatus.unprocessed == order.getOrderStatus() && Order.PaymentStatus.unpaid == order.getPaymentStatus()) {// 判断支付状态为未支付
            this.attrs.put("order", order);
            return SUCCESS;
        }
        return ERROR;
    }

    /**
     * 订单提交完成
     *
     * @return
     */
    public String success(String sn) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("INS_sn", new String[]{sn}));
        List<Order> orders = this.orderService.find(filters, "createTime", "desc");
        this.attrs.put("orders", orders);
        return SUCCESS;
    }

    public String details(String sn) {
        this.attrs.put("order", this.orderService.get(sn));
        return SUCCESS;
    }
}
