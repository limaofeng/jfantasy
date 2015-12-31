package org.jfantasy.express.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.express.bean.Express;
import org.jfantasy.express.dao.ExpressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpressService {

    @Autowired
    private ExpressDao expressDao;

    public Pager<Express> findPager(Pager<Express> pager, List<PropertyFilter> filters) {
        return this.expressDao.findPager(pager, filters);
    }

    public Express get(String id) {
        return this.expressDao.get(id);
    }

    public Express save(Express express) {
        return this.expressDao.save(express);
    }

    public void delete(String... ids) {
        for (String id : ids) {
            this.expressDao.delete(id);
        }
    }
}
