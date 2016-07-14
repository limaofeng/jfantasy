package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.dao.CardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardDao cardDao;

    public Card get(String id) {
        return this.cardDao.get(id);
    }

    public Pager<Card> findPager(Pager<Card> pager, List<PropertyFilter> filters) {
        return this.cardDao.findPager(pager,filters);
    }

}
