package org.jfantasy.pay.service;


import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.Card;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.CardType;
import org.jfantasy.pay.bean.enums.CardBatchStatus;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.bean.enums.Usage;
import org.jfantasy.pay.dao.CardBatchDao;
import org.jfantasy.pay.dao.CardDesignDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardBatchService {

    @Autowired
    private CardDesignDao cardDesignDao;
    @Autowired
    private CardBatchDao cardBatchDao;
    @Autowired
    private CardService cardService;
    @Autowired
    private LogService logService;

    public Pager<CardBatch> findPager(Pager<CardBatch> pager, List<PropertyFilter> filters) {
        return loadLogs(cardBatchDao.findPager(pager, filters));
    }

    public CardBatch get(Long id) {
        return loadLogs(this.cardBatchDao.get(id));
    }

    @Transactional
    public CardBatch save(CardBatch batch) {
        if (this.cardBatchDao.findUnique(Restrictions.eq("no", batch.getNo())) != null) {
            throw new RestException("批次信息已经存在,不能重复保存");
        }
        CardDesign design = cardDesignDao.get(batch.getCardDesign().getKey());
        batch.setCardType(design.getCardType());
        batch.setCardDesign(design);
        batch.setStatus(CardBatchStatus.draft);
        return this.cardBatchDao.save(batch);
    }

    @Transactional
    public CardBatch update(CardBatch batch, boolean patch) {
        if (this.cardBatchDao.get(batch.getId()).getStatus() != CardBatchStatus.draft) {
            throw new RestException("只有草拟中的批次可以修改信息");
        }
        CardDesign design = cardDesignDao.get(batch.getCardDesign().getKey());
        batch.setCardType(design.getCardType());
        batch.setCardDesign(design);
        batch.setStatus(CardBatchStatus.draft);
        return this.cardBatchDao.update(batch, patch);
    }

    @Transactional
    public List<Card> make(Long id, String notes) {
        CardBatch batch = this.cardBatchDao.get(id);
        if (batch.getStatus() != CardBatchStatus.draft) {
            throw new RestException("生成卡片失败!");
        }
        CardDesign design = batch.getCardDesign();
        CardType type = batch.getCardType();
        BigDecimal amount = design.getAmount();
        Usage usage = design.getUsage();
        List<Card> cards = new ArrayList<>();
        for (int i = 1, length = batch.getQuantity(); i <= length; i++) {
            Card card = new Card();
            card.setNo(batch.getNo() + StringUtil.addZeroLeft(i + "", 4));//TODO 卡号生成规则
            card.setBatch(batch);
            card.setType(type);
            card.setUsage(usage);
            card.setDesign(design);
            card.setAmount(amount);
            card.setExtras(design.getExtras());
            cards.add(cardService.save(card));
        }
        batch.setStatus(CardBatchStatus.make);
        //保存操作记录
        this.logService.log(OwnerType.card_batch, batch.getId(), "make", notes);
        return cards;
    }

    @Transactional
    public CardBatch release(Long id, String notes) {
        CardBatch batch = this.cardBatchDao.get(id);
        if (batch.getStatus() != CardBatchStatus.make) {
            throw new RestException("激活卡片失败!");
        }
        this.cardService.release(id);
        batch.setStatus(CardBatchStatus.released);
        //保存操作记录
        this.logService.log(OwnerType.card_batch, batch.getId(), "release", notes);
        return this.cardBatchDao.save(batch);
    }

    @Transactional
    public CardBatch cancel(Long id, String notes) {
        CardBatch batch = this.cardBatchDao.get(id);
        if (batch.getStatus() == CardBatchStatus.released) {
            throw new RestException("卡已经发布,不能取消");
        }
        this.cardService.cancel(id);
        batch.setStatus(CardBatchStatus.canceled);
        //保存操作记录
        this.logService.log(OwnerType.card_batch, batch.getId(), "cancel", notes);
        return this.cardBatchDao.save(batch);
    }

    private Pager<CardBatch> loadLogs(Pager<CardBatch> pager) {
        loadLogs(pager.getPageItems());
        return pager;
    }

    private List<CardBatch> loadLogs(List<CardBatch> designs) {
        for (CardBatch design : designs) {
            this.loadLogs(design);
        }
        return designs;
    }

    private CardBatch loadLogs(CardBatch batch) {
        if (batch != null) {
            batch.setLogs(this.logService.logs(OwnerType.card_batch, batch.getNo()));
        }
        return batch;
    }

}
