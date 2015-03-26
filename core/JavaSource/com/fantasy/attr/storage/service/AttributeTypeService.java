package com.fantasy.attr.storage.service;

import com.fantasy.attr.framework.util.TypeConverterUtils;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.dao.AttributeTypeDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品属性类型service
 *
 * @author mingliang
 */
@Service
@Transactional
public class AttributeTypeService {

    private static final Log logger = LogFactory.getLog(AttributeTypeService.class);

    @Autowired
    private AttributeTypeDao attributeTypeDao;

    public Pager<AttributeType> findPager(Pager<AttributeType> pager, List<PropertyFilter> filters) {
        return attributeTypeDao.findPager(pager, filters);
    }

    public AttributeType save(AttributeType attributeType) {
        attributeTypeDao.save(attributeType);
        return attributeType;
    }

    public AttributeType save(Class<?> javaType, String name, String description, Class<? extends TypeConverter> converter) {
        AttributeType attributeType = this.attributeTypeDao.findUniqueBy("dataType", javaType);
        if (attributeType == null) {
            attributeType = new AttributeType();
        }
        attributeType.setName(name);
        attributeType.setDataType(javaType.getName());
        attributeType.setConverter(TypeConverterUtils.getTypeConverter(converter));
        attributeType.setDescription(description);
        this.attributeTypeDao.save(attributeType);
        return attributeType;
    }

    public AttributeType get(Long id) {
        AttributeType attributeType = attributeTypeDao.get(id);
        Hibernate.initialize(attributeType);
        return attributeType;
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.attributeTypeDao.delete(id);
        }
    }

    public AttributeType findUniqueByJavaType(Class<?> javaClass) {
        return this.attributeTypeDao.findUniqueBy("dataType", javaClass.getName());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public AttributeType findUniqueByJavaType(String javaClassName) {
        return this.attributeTypeDao.findUniqueBy("dataType", javaClassName);
    }

    public AttributeType findUnique(Criterion... criterions) {
        return this.attributeTypeDao.findUnique(criterions);
    }

    public List<AttributeType> getAll() {
        return this.attributeTypeDao.getAll();
    }

    /**
     * 获取所有商品属性类型
     *
     * @return
     */
    public static List<AttributeType> allAttributeType() {
        List<AttributeType> types = SpringContextUtil.getBeanByType(AttributeTypeService.class).getAll();
        return types;
    }


}
