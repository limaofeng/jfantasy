package org.jfantasy.attr.storage.service;

import ognl.TypeConverter;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.attr.storage.bean.Converter;
import org.jfantasy.attr.storage.dao.ConverterDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConverterService {

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
        return this.converterDao.save(converter);
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
        return SpringContextUtil.getBeanByType(ConverterService.class).find();
    }

    public void save(Class<? extends TypeConverter> clazz, String name, String description) {
        Converter converter = ObjectUtil.defaultValue(this.converterDao.findUnique(Restrictions.eq("typeConverter", clazz.getName())), new Converter());
        converter.setName(name);
        converter.setDescription(description);
        converter.setTypeConverter(clazz.getName());
        this.converterDao.save(converter);
    }
}
