package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Card;
import org.jfantasy.member.dao.CardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mem.cardService")
public class CardService {

    @Autowired
    private CardDao cardDao;

    public Pager<Card> findPager(Pager<Card> pager, List<PropertyFilter> filters) {
        return this.cardDao.findPager(pager, filters);
    }

}
