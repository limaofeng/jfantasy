package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.dao.TemplateDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("swp.page.templateService")
@Transactional
public class TemplateService {

    @Resource(name = "swp.page.template")
    private TemplateDao templateDao;

    public Pager<Template> findPager(Pager<Template> pager, List<PropertyFilter> filters) {
        return this.templateDao.findPager(pager, filters);
    }

    public void save(Template template) {
        this.templateDao.save(template);
    }

    public Template get(Long id) {
        return this.templateDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.templateDao.delete(id);
        }
    }

}

