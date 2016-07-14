package org.jfantasy.pay.service;


import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.enums.CardBatchStatus;
import org.jfantasy.pay.dao.CardBatchDao;
import org.jfantasy.pay.dao.CardDesignDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardBatchService {

    @Autowired
    private CardDesignDao cardDesignDao;
    @Autowired
    private CardBatchDao cardBatchDao;

    public Pager<CardBatch> findPager(Pager<CardBatch> pager, List<PropertyFilter> filters) {
        return cardBatchDao.findPager(pager, filters);
    }

    public CardBatch get(String id) {
        return this.cardBatchDao.get(id);
    }

    @Transactional
    public CardBatch save(CardBatch batch) {
        if (get(batch.getNo()) != null) {
            throw new RestException("批次信息已经存在,不能重复保存");
        }
        CardDesign design = cardDesignDao.get(batch.getCardDesign().getKey());
        batch.setCardType(design.getCardType());
        batch.setCardDesign(design);
        batch.setStatus(CardBatchStatus.Draft);
        return this.cardBatchDao.save(batch);
    }

    public CardBatch update(CardBatch batch) {
        if (batch.getStatus() != CardBatchStatus.Draft) {
            throw new RestException("只有草拟中的批次可以修改信息");
        }
        this.cardBatchDao.update(batch);
        return batch;
    }

}
