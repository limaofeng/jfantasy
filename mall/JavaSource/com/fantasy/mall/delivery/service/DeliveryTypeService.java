package com.fantasy.mall.delivery.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.bean.DeliveryType.DeliveryMethod;
import com.fantasy.mall.delivery.bean.Reship;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.dao.DeliveryTypeDao;
import com.fantasy.mall.delivery.dao.ShippingDao;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DeliveryTypeService {

    @Autowired
    private DeliveryTypeDao deliveryTypeDao;
    @Autowired
    private ShippingDao shippingDao;

    public List<DeliveryType> find(List<PropertyFilter> filters) {
        return this.deliveryTypeDao.find(filters);
    }

    public DeliveryType get(Long id) {
        return deliveryTypeDao.get(id);
    }

    /**
     * 配送列表
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return Pager<DeliveryType>
     */
    public Pager<DeliveryType> findPager(Pager<DeliveryType> pager, List<PropertyFilter> filters) {
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
        return this.deliveryTypeDao.save(deliveryType);
    }

    /**
     * 配送删除
     *
     * @param ids 配送类型
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.deliveryTypeDao.delete(id);
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

}
