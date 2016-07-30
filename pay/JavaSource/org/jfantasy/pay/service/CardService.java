package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.enums.CardStatus;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.dao.AccountDao;
import org.jfantasy.pay.dao.CardDao;
import org.jfantasy.pay.event.CardBindEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

    private final static Log LOG = LogFactory.getLog(PayService.class);

    @Autowired
    private CardDao cardDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private LogService logService;
    @Autowired
    private ApplicationContext applicationContext;

    public Card get(String id) {
        return this.cardDao.get(id);
    }

    public Pager<Card> findPager(Pager<Card> pager, List<PropertyFilter> filters) {
        return this.cardDao.findPager(pager, filters);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Card bind(String owner, String id, String password) {
        Card card = this.cardDao.findUnique(Restrictions.eq("no", id));
        if (card == null) {
            throw new NotFoundException("卡不存在");
        }
        if (card.getStatus() != CardStatus.activated) {
            throw new ValidationException(101.1f, "卡状态不正确");
        }
        Account account = this.accountDao.findUnique(Restrictions.eq("owner", owner));
        if (account == null) {
            throw new ValidationException(101.2f, "账号不存在!");
        }
        /*
        if (account.getStatus() != AccountStatus.activated) {
            throw new ValidationException(101.3f, "账号未激活!");
        }*/
        if (!card.getSecret().equals(password)) {
            throw new ValidationException(101.4f, "密钥错误");
        }
        card.setStatus(CardStatus.used);
        card.setOwner(owner);
        this.cardDao.update(card);
        //记录日志
        this.logService.log(OwnerType.card, card.getNo(), "build", "绑定卡");
        //触发事件
        this.applicationContext.publishEvent(new CardBindEvent(card));
        return card;
    }

    public Card save(Card card) {
        card.setSecret("123456");//TODO 密码生成规则
        card.setStatus(CardStatus.sleep);

        this.cardDao.save(card);
        //记录日志
        this.logService.log(OwnerType.card, card.getNo(), "create", "卡添加");
        return card;
    }

    void release(Long id) {
        for (Card card : this.cardDao.find(Restrictions.eq("batch.id", id))) {
            card.setStatus(CardStatus.activated);
            //记录日志
            this.logService.log(OwnerType.card, card.getNo(), "activated", "卡生效");
        }
    }

    void cancel(Long id) {
        for (Card card : this.cardDao.find(Restrictions.eq("batch.id", id))) {
            card.setStatus(CardStatus.invalid);
            //记录日志
            this.logService.log(OwnerType.card, card.getNo(), "invalid", "卡失效");
        }
    }
}
