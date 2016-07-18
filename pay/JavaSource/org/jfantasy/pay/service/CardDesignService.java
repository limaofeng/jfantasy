package org.jfantasy.pay.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.enums.CardDesignStatus;
import org.jfantasy.pay.dao.CardDesignDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardDesignService {

    @Autowired
    private CardDesignDao cardDesignDao;
    @Autowired
    private LogService logService;

    public CardDesign get(String id) {
        return loadLogs(this.cardDesignDao.get(id));
    }

    public Pager<CardDesign> findPager(Pager<CardDesign> pager, List<PropertyFilter> filters) {
        return loadLogs(this.cardDesignDao.findPager(pager, filters));
    }

    @Transactional
    public CardDesign save(CardDesign design) {
        design.setStatus(CardDesignStatus.draft);
        return loadLogs(this.cardDesignDao.save(design));
    }


    @Transactional
    public CardDesign update(CardDesign batch) {
        this.cardDesignDao.update(batch);
        return batch;
    }

    @Transactional
    public CardDesign publish(String id, String notes) {
        CardDesign design = this.cardDesignDao.get(id);
        if (design.getStatus() != CardDesignStatus.draft) {
            throw new RestException("卡片发布失败!");
        }
        design.setStatus(CardDesignStatus.publish);
        this.update(design);
        //保存操作记录
        this.logService.log(design, notes);
        return design;
    }

    @Transactional
    public CardDesign unpublish(String id, String notes) {
        CardDesign design = this.cardDesignDao.get(id);
        if (design.getStatus() != CardDesignStatus.publish) {
            throw new RestException("卡片取消发布失败!");
        }
        design.setStatus(CardDesignStatus.unpublish);
        //保存操作记录
        this.logService.log(design, notes);
        design.setStatus(CardDesignStatus.draft);
        this.cardDesignDao.update(design);
        return design;
    }

    public CardDesign destroy(String id, String notes) {
        CardDesign design = this.cardDesignDao.get(id);
        if (design.getStatus() != CardDesignStatus.draft) {
            throw new RestException("卡片销毁失败!");
        }
        design.setStatus(CardDesignStatus.destroyed);
        this.update(design);
        //保存操作记录
        this.logService.log(design, notes);
        return design;
    }

    private Pager<CardDesign> loadLogs(Pager<CardDesign> pager) {
        loadLogs(pager.getPageItems());
        return pager;
    }

    private List<CardDesign> loadLogs(List<CardDesign> designs) {
        for (CardDesign design : designs) {
            this.loadLogs(design);
        }
        return designs;
    }

    private CardDesign loadLogs(CardDesign design) {
        design.setLogs(this.logService.logs(CardDesign.class, design.getKey()));
        return design;
    }

}
