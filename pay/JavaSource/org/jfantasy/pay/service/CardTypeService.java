package org.jfantasy.pay.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.bean.CardType;
import org.jfantasy.pay.dao.CardDesignDao;
import org.jfantasy.pay.dao.CardTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardTypeService {

    @Autowired
    private CardTypeDao cardTypeDao;
    @Autowired
    private CardDesignDao cardDesignDao;

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

    @Transactional
    public void delete(String... ids) {
        for (String id : ids) {
            CardType type = this.cardTypeDao.get(id);
            if(type == null){
                continue;
            }
            if(this.cardDesignDao.count(Restrictions.eq("cardType.key",type.getKey())) > 0){
                throw new RestException("已经存在设计稿的会员卡类型,不允许删除");
            }
            this.cardTypeDao.delete(id);
        }
    }
}
