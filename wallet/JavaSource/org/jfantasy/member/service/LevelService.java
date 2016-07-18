package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Level;
import org.jfantasy.member.dao.LevelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LevelService {

    @Autowired
    private LevelDao levelDao;

    public Pager<Level> search(Pager<Level> pager, List<PropertyFilter> filters) {
        return levelDao.findPager(pager, filters);
    }

    public Level get(Long id) {
        return this.levelDao.get(id);
    }

    @Transactional
    public Level save(Level level) {
        return this.levelDao.save(level);
    }

    @Transactional
    public Level update(Level level) {
        this.levelDao.update(level);
        return level;
    }

    @Transactional
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.levelDao.delete(id);
        }
    }

}
