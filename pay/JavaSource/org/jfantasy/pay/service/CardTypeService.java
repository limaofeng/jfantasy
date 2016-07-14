package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.CardType;
import org.jfantasy.pay.dao.CardTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardTypeService {

    @Autowired
    private CardTypeDao cardTypeDao;

    public Pager<CardType> findPager(Pager<CardType> pager, List<PropertyFilter> filters) {
        return this.cardTypeDao.findPager(pager, filters);
    }

    public CardType get(String id) {
        return this.cardTypeDao.get(id);
    }

    @Transactional
    public CardType save(CardType type) {
        return this.cardTypeDao.save(type);
    }
}
