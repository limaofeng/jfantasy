package com.fantasy.mall.delivery.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.DeliveryCorp;
import com.fantasy.mall.delivery.dao.DeliveryCorpDao;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeliveryCorpService {

    @Autowired
    private DeliveryCorpDao deliveryCorpDao;

    /**
     * 列表查询
     *
     * @param filters 过滤条件
     * @return List<DeliveryCorp>
     */
    public List<DeliveryCorp> find(List<PropertyFilter> filters) {
        return this.deliveryCorpDao.find(filters);
    }

    /**
     * 保存
     */
    public DeliveryCorp save(DeliveryCorp deliveryCorp) {
        return this.deliveryCorpDao.save(deliveryCorp);
    }

    /**
     * 根据主键获取id
     *
     * @param id 物流公司id
     * @return DeliveryCorp
     */
    public DeliveryCorp get(Long id) {
        return this.deliveryCorpDao.get(id);
    }

    /**
     * 批量删除
     *
     * @param ids 物流公司 ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.deliveryCorpDao.delete(id);
        }
    }

    public Pager<DeliveryCorp> findPager(Pager<DeliveryCorp> pager, List<PropertyFilter> filters) {
        return this.deliveryCorpDao.findPager(pager, filters);
    }

    public DeliveryCorp findUnique(Criterion... criterions) {
        return this.deliveryCorpDao.findUnique(criterions);
    }

}
