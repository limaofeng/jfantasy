package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.Converter;
import com.fantasy.attr.storage.dao.ConverterDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConverterService {

    private static final Log logger = LogFactory.getLog(ConverterService.class);

    @Autowired
    private ConverterDao converterDao;

    /**
     * 分页查询转换器
     *
     * @param pager
     * @param filters
     * @return
     */
    public Pager<Converter> findPager(Pager<Converter> pager, List<PropertyFilter> filters) {
        return this.converterDao.findPager(pager, filters);
    }

    public Converter save(Converter converter) {
        this.converterDao.save(converter);
        return converter;
    }

    public Object get(Long id) {
        return this.converterDao.get(id);
    }

    public void delete(Long... ids) {
        for (long id : ids) {
            this.converterDao.delete(id);
        }
    }

    public Converter findUnique(Criterion... criterions) {
        return this.converterDao.findUnique(criterions);
    }

    public List<Converter> find(Criterion... criterions) {
        return this.converterDao.find(criterions);
    }

    public List<Converter> find() {
        return this.converterDao.find();
    }

    public static List<Converter> findAll() {
        ConverterService service = SpringContextUtil.getBeanByType(ConverterService.class);
        return service.find();
    }

    public void save(Class<? extends TypeConverter> clazz, String name, String description) {
        Converter converter = ObjectUtil.defaultValue(this.converterDao.findUnique(Restrictions.eq("typeConverter", clazz.getName())), new Converter());
        converter.setName(name);
        converter.setDescription(description);
        converter.setTypeConverter(clazz.getName());
        this.converterDao.save(converter);
    }
}
