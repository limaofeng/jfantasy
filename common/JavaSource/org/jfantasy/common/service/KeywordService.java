package org.jfantasy.common.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.common.bean.Keyword;
import org.jfantasy.common.dao.KeywordDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.lucene.BuguSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KeywordService extends BuguSearcher<Keyword> {

    @Autowired
    private KeywordDao keywordDao;

    public Pager<Keyword> findPager(Pager<Keyword> pager, List<PropertyFilter> filters) {
        return keywordDao.findPager(pager, filters);
    }

    public Keyword get(Long id) {
        return keywordDao.get(id);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            keywordDao.delete(id);
        }
    }

    public Keyword save(String type, String keyword) {
        Keyword _keyword = this.keywordDao.findUnique(Restrictions.eq("words", keyword), Restrictions.eq("type", type), Restrictions.eq("owner", "public"));
        return _keyword != null ? _keyword : this.keywordDao.save(new Keyword(type, keyword));
    }

    public Keyword save(String type, String keyword, String owner) {
        Keyword _keyword = this.keywordDao.findUnique(Restrictions.eq("words", keyword), Restrictions.eq("type", type), Restrictions.eq("owner", owner));
        return _keyword != null ? _keyword : this.keywordDao.save(new Keyword(type, keyword, owner));
    }

    public Keyword save(Keyword keyword) {
        return this.keywordDao.save(keyword);
    }

}
