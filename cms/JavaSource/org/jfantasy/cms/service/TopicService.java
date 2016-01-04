package org.jfantasy.cms.service;

import org.jfantasy.cms.bean.Topic;
import org.jfantasy.cms.dao.TopicDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专题服务
 */
@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicDao topicDao;

    public Pager<Topic> findPager(Pager<Topic> pager, List<PropertyFilter> filters) {
        return topicDao.findPager(pager, filters);
    }

    public void delete(String... codes) {
        for (String code : codes) {
            this.topicDao.delete(code);
        }
    }

    public Topic save(Topic topic) {
        return topicDao.save(topic);
    }

    public Topic get(String code) {
        return this.topicDao.get(code);
    }

}
