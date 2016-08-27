package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.hibernate.criterion.Criterion;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.dao.PayConfigDao;
import org.jfantasy.pay.product.PayProductSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PayConfigService {

    @Autowired
    private PayConfigDao payConfigDao;
    @Autowired
    private PayProductConfiguration payProductConfiguration;

    /**
     * findPager
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return {pager}
     */
    public Pager<PayConfig> findPager(Pager<PayConfig> pager, List<PropertyFilter> filters) {
        return this.payConfigDao.findPager(pager, filters);
    }

    public List<PayConfig> find(List<PropertyFilter> filters) {
        return this.payConfigDao.find(filters);
    }

    public List<PayConfig> find(Criterion... criterions) {
        return this.payConfigDao.find(criterions, "sort", "asc");
    }

    public PayConfig findUnique(Criterion... criterions) {
        return this.payConfigDao.findUnique(criterions);
    }

    /**
     * 保存
     *
     * @param config 支付配置信息
     * @return {paymentConfig}
     */
    public PayConfig save(PayConfig config) {
        PayProductSupport payProduct = payProductConfiguration.loadPayProduct(config.getPayProductId());
        config.setPayMethod(payProduct.getPayMethod());
        return this.payConfigDao.save(config);
    }

    public PayConfig update(PayConfig config) {
        this.payConfigDao.update(config);
        return config;
    }

    /**
     * 获取
     *
     * @param id 配置id
     * @return {paymentConfig}
     */
    public PayConfig get(Long id) {
        return this.payConfigDao.get(id);
    }

    /**
     * 删除
     *
     * @param ids 支付配置ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.payConfigDao.delete(id);
        }
    }

}

