package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.dao.CardDesignDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardDesignService {

    @Autowired
    private CardDesignDao cardDesignDao;

    public CardDesign get(String id) {
        return this.cardDesignDao.get(id);
    }

    public Pager<CardDesign> findPager(Pager<CardDesign> pager, List<PropertyFilter> filters) {
        return this.cardDesignDao.findPager(pager, filters);
    }

    @Transactional
    public CardDesign save(CardDesign design) {
        return this.cardDesignDao.save(design);
    }

}
