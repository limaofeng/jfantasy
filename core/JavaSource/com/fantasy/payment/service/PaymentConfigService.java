package com.fantasy.payment.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.dao.PaymentConfigDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Service
@Transactional
public class PaymentConfigService {

    @Autowired
    private PaymentConfigDao paymentConfigDao;
    /**
     * findPager
     *
     * @param pager 分页对象
     * @param filters 筛选条件
     * @return {pager}
     */
    public Pager<PaymentConfig> findPager(Pager<PaymentConfig> pager, List<PropertyFilter> filters) {
        return this.paymentConfigDao.findPager(pager, filters);
    }

    public List<PaymentConfig> find(List<PropertyFilter> filters) {
        return this.paymentConfigDao.find(filters);
    }

    /**
     * 保存
     *
     * @param config 支付配置信息
     * @return {paymentConfig}
     */
    public PaymentConfig save(PaymentConfig config) {
        this.paymentConfigDao.save(config);
        return config;
    }

    /**
     * 获取
     *
     * @param id 配置id
     * @return {paymentConfig}
     */
    public PaymentConfig get(Long id) {
        return this.paymentConfigDao.get(id);
    }

    /**
     * 删除
     *
     * @param ids 支付配置ids
     */
    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.paymentConfigDao.delete(id);
        }
    }

}

