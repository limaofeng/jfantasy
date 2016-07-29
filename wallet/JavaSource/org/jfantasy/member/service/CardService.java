package org.jfantasy.member.service;

import org.hibernate.criterion.Restrictions;
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

    public List<Card> findByWallet(Long walletId) {
        return this.cardDao.find(Restrictions.eq("wallet.id",walletId));
    }

    public int count(Long id) {
        return this.cardDao.count(Restrictions.eq("wallet.id",id));
    }

}
