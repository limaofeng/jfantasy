package com.fantasy.common.service;

import com.fantasy.common.bean.HotKeywords;
import com.fantasy.common.bean.Keywords;
import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.common.dao.HotKeywordsDao;
import com.fantasy.common.dao.KeywordsDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 热门搜索关键词
 */
@Service
@Transactional
public class KeywordService {

    @Autowired
    private KeywordsDao keywordsDao;
    @Autowired
    private HotKeywordsDao hotKeywordsDao;

    /**
     * 获取热门关键字
     *
     * @param key      标示
     * @param timeUnit 时间单位
     * @param time     时间
     * @param size     返回数据集条数
     * @return String[]
     */
    public String[] getKeywords(String key, TimeUnit timeUnit, String time, int size) {
        List<HotKeywords> hotSearch = this.hotKeywordsDao.find(new Criterion[]{Restrictions.eq("key", key), Restrictions.eq("timeUnit", timeUnit), Restrictions.eq("time", time)}, "hitCount", "desc", 0, size);
        return ObjectUtil.toFieldArray(hotSearch, "Keywords", String.class);
    }

    /**
     * 关键词
     *
     * @param key
     * @param text
     * @功能描述 该功能会自动将关键词保存到 HotKeywords
     */
    @SuppressWarnings("unchecked")
    public List<String> analyze(String key, String text) {
        if (StringUtil.isBlank(text)) {
            return Collections.EMPTY_LIST;
        }
        List<String> keywords = ObjectUtil.analyze(text);
        for (String words : keywords) {
            this.addHotKeywords(key, words);
        }
        return keywords;
    }

    public void addKeywords(String keywords) {

    }

    public void removeKeywords(String keywords) {

    }

    /**
     * 保存单个关键字
     *
     * @param key
     * @param keywords
     * @功能描述
     */
    public HotKeywords addHotKeywords(String key, String keywords) {
        String time = DateUtil.format("yyyyMMdd");
        HotKeywords hotKeywords = this.hotKeywordsDao.findUnique(Restrictions.eq("key", key), Restrictions.eq("keywords", keywords), Restrictions.eq("timeUnit", TimeUnit.day), Restrictions.eq("time", time));
        if (hotKeywords == null) {
            hotKeywords = new HotKeywords();
            hotKeywords.setKey(key);
            hotKeywords.setKeywords(keywords);
            hotKeywords.setTimeUnit(TimeUnit.day);
            hotKeywords.setTime(time);
            hotKeywords.setHitCount(0);
        }
        hotKeywords.setHitCount(hotKeywords.getHitCount() + 1);
        return this.hotKeywordsDao.save(hotKeywords);
    }

    public Pager<Keywords> findPager(Pager<Keywords> pager, List<PropertyFilter> filters) {
        return this.keywordsDao.findPager(pager, filters);
    }

}
