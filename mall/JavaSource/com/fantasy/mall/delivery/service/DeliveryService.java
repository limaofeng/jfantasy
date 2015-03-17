package com.fantasy.mall.delivery.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.mall.delivery.bean.*;
import com.fantasy.mall.delivery.bean.DeliveryType.DeliveryMethod;
import com.fantasy.mall.delivery.dao.DeliveryCorpDao;
import com.fantasy.mall.delivery.dao.DeliveryItemDao;
import com.fantasy.mall.delivery.dao.DeliveryTypeDao;
import com.fantasy.mall.delivery.dao.ShippingDao;
import com.fantasy.mall.order.bean.Order;
import com.fantasy.mall.order.service.OrderService;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: lmf Date: 13-11-20 Time: 下午5:06 To change this template use File | Settings | File Templates.
 */
@Service("mall.service.DeliveryService")
@Transactional
public class DeliveryService {

    @Autowired
    private DeliveryCorpDao deliveryCorpDao;
    @Autowired
    private DeliveryTypeDao deliveryTypeDao;
    @Autowired
    private DeliveryItemDao deliveryItemDao;

    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private OrderService orderService;

    public List<DeliveryType> listDeliveryType() {
        return this.deliveryTypeDao.getAll();
    }

    public DeliveryType getDeliveryType(Long id) {
        return deliveryTypeDao.get(id);
    }

    /**
     * 列表查询
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return Pager<DeliveryCorp>
     */
    public Pager<DeliveryCorp> findDeliveryCorpPager(Pager<DeliveryCorp> pager, List<PropertyFilter> filters) {
        return this.deliveryCorpDao.findPager(pager, filters);
    }

    /**
     * 保存
     */
    public DeliveryCorp save(DeliveryCorp deliveryCorp) {
        this.deliveryCorpDao.save(deliveryCorp);
        return deliveryCorp;
    }

    /**
     * 根据主键获取id
     *
     * @param id 物流公司id
     * @return DeliveryCorp
     */
    public DeliveryCorp getDeliveryCorp(Long id) {
        return this.deliveryCorpDao.get(id);
    }

    /**
     * 批量删除
     *
     * @param ids 物流公司 ids
     */
    public void deleteDeliveryCorp(Long[] ids) {
        for (Long id : ids) {
            this.deliveryCorpDao.delete(id);
        }
    }

    /**
     * 获取列表
     *
     * @return List<DeliveryCorp>
     */
    public List<DeliveryCorp> listDeliveryCorp() {
        return this.deliveryCorpDao.find(new Criterion[0], "sort", "asc");
    }

    /**
     * 配送列表
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return Pager<DeliveryType>
     */
    public Pager<DeliveryType> findDeliveryTypePager(Pager<DeliveryType> pager, List<PropertyFilter> filters) {
        return this.deliveryTypeDao.findPager(pager, filters);
    }

    /**
     * 配送保存
     *
     * @param deliveryType 配送对象
     * @return DeliveryType
     */
    public DeliveryType save(DeliveryType deliveryType) {
        if (deliveryType.getFirstWeight() == null) {
            deliveryType.setFirstWeight(0);
        }
        if (deliveryType.getFirstWeightPrice() == null) {
            deliveryType.setFirstWeightPrice(BigDecimal.ZERO);
        }
        if (deliveryType.getContinueWeight() == null) {
            deliveryType.setContinueWeight(0);
        }
        if (deliveryType.getContinueWeightPrice() == null) {
            deliveryType.setContinueWeightPrice(BigDecimal.ZERO);
        }
        this.deliveryTypeDao.save(deliveryType);
        return deliveryType;
    }

    /**
     * 配送删除
     *
     * @param ids 配送类型
     */
    public void deleteDeliveryType(Long... ids) {
        for (Long id : ids) {
            this.deliveryTypeDao.delete(id);
        }
    }

    /**
     * 发货信息
     *
     * @param shipping 必输项: order.id,deliveryType.id,deliverySn，deliveryFee，deliveryItems 可选输入项：memo
     */
    public void save(Shipping shipping) {
        Order order = orderService.get(shipping.getOrder().getId());
        shipping.setOrder(order);
        DeliveryType deliveryType = this.getDeliveryType(shipping.getDeliveryType().getId());
        shipping.setDeliveryType(deliveryType);
        // 初始化快递信息
        shipping.setDeliveryTypeName(deliveryType.getName());
        shipping.setDeliveryCorpName(deliveryType.getDefaultDeliveryCorp().getName());
        shipping.setDeliveryCorpUrl(deliveryType.getDefaultDeliveryCorp().getUrl());
        // 添加收货地址信息
        shipping.setShipName(order.getShipName());
        shipping.setShipAreaStore(order.getShipAreaStore());
        shipping.setShipAddress(order.getShipAddress());
        shipping.setShipZipCode(order.getShipZipCode());
        shipping.setShipPhone(order.getShipPhone());
        shipping.setShipMobile(order.getShipMobile());
        // 初始化物流项
        for (DeliveryItem item : shipping.getDeliveryItems()) {
            item.initialize(ObjectUtil.find(order.getOrderItems(), "sn", item.getSn()));
            item.setShipping(shipping);
        }
        this.shippingDao.save(shipping);
        for (DeliveryItem item : shipping.getDeliveryItems()) {
            this.deliveryItemDao.save(item);
        }
    }

    /**
     * 退货信息
     *
     * @param reship 退货信息
     */
    public void save(Reship reship) {

    }

    public List<DeliveryType> listDeliveryType(DeliveryMethod method) {
        return this.deliveryTypeDao.find(Restrictions.eq("method", method));
    }

    /**
     * 获取发货商品信息列表
     *
     * @param id id
     * @return Shipping
     */
    public Shipping getShipping(Long id) {
        Shipping shipping = this.shippingDao.get(id);
        for (DeliveryItem deliveryItem : shipping.getDeliveryItems()) {
            Hibernate.initialize(deliveryItem);
        }
        return shipping;
    }

    /**
     * 获取配送方式
     *
     * @param deliveryMethod 配送方式
     * @return List<DeliveryType>
     */
    public static List<DeliveryType> deliveryTypes(DeliveryType.DeliveryMethod deliveryMethod) {
        DeliveryService deliveryService = SpringContextUtil.getBeanByType(DeliveryService.class);
        return deliveryService.listDeliveryType(deliveryMethod);
    }

    /**
     * 静态获取列表
     *
     * @return List<DeliveryCorp>
     */
    public static List<DeliveryCorp> deliveryCorps() {
        DeliveryService deliveryCorpService = SpringContextUtil.getBeanByType(DeliveryService.class);
        return deliveryCorpService.listDeliveryCorp();
    }

    public static List<DeliveryType> deliveryTypes() {
        DeliveryService deliveryCorpService = SpringContextUtil.getBeanByType(DeliveryService.class);
        return deliveryCorpService.listDeliveryType();
    }

}
