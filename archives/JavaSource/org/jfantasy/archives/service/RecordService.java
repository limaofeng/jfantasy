package org.jfantasy.archives.service;

import org.jfantasy.archives.bean.Record;
import org.jfantasy.archives.dao.RecordDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordDao recordDao;

    public Pager<Record> findPager(Pager<Record> pager, List<PropertyFilter> filters) {
        return this.recordDao.findPager(pager, filters);
    }

    public Record get(Long id) {
        return this.recordDao.get(id);
    }

    @Transactional
    public Record save(Record record) {
        record.setNo("编号规则未定");
        return this.recordDao.save(record);
    }

}
