package com.fantasy.mall.delivery.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.dao.ShippingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShippingService {

    @Autowired
    private ShippingDao shippingDao;

    public Pager<Shipping> findPager(Pager<Shipping> pager, List<PropertyFilter> filters) {
        return this.shippingDao.findPager(pager, filters);
    }

    public Shipping get(Long id){
        return  this.shippingDao.get(id);
    }

    public void delete(Long[] ids){
        for(Long id:ids){
            this.shippingDao.delete(id);
        }
    }
}
