package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.*;
import com.fantasy.attr.storage.dao.AttributeDao;
import com.fantasy.attr.storage.dao.AttributeVersionDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AttributeVersionService {

    @Autowired
    private AttributeVersionDao attributeVersionDao;

    @Autowired
    private AttributeDao attributeDao;

    public List<AttributeVersion> search(List<PropertyFilter> filters, String orderBy, String order, int size) {
        return this.attributeVersionDao.find(filters, orderBy, order, 0, size);
    }

    public Pager<AttributeVersion> findPager(Pager<AttributeVersion> pager, List<PropertyFilter> filters) {
        return attributeVersionDao.findPager(pager, filters);
    }

    public AttributeVersion save(String targetClassName, String number, Attribute... attributes) {
        return this.save(targetClassName, number, Arrays.asList(attributes));
    }

    public AttributeVersion save(String targetClassName, Attribute... attributes) {
        return this.save(targetClassName, Arrays.asList(attributes));
    }

    public AttributeVersion save(String targetClassName, List<Attribute> attributes) {
        AttributeVersion version = new AttributeVersion();
        version.setTargetClassName(targetClassName);
        version.setNumber("");
        version.setType(AttributeVersion.Type.custom);
        version.setAttributes(attributes);
        for (Attribute attribute : version.getAttributes()) {
            attributeDao.save(attribute);
        }
        this.attributeVersionDao.save(version);
        return version;
    }

    public AttributeVersion save(String targetClassName, String number, List<Attribute> attributes) {
        AttributeVersion version = this.attributeVersionDao.findUnique(Restrictions.eq("targetClassName", targetClassName), Restrictions.eq("number", number));
        if (version == null) {
            version = new AttributeVersion();
            version.setTargetClassName(targetClassName);
            version.setNumber(number);
            version.setType(AttributeVersion.Type.ext);
        }
        if (version.getAttributes() == null) {
            version.setAttributes(new ArrayList<Attribute>());
        }
        for (Attribute attribute : attributes) {
            Attribute oldAttribute = ObjectUtil.find(version.getAttributes(), "code", attribute.getCode());
            if (oldAttribute == null) {
                attributeDao.save(attribute);
                version.getAttributes().add(attribute);
            } else {
                BeanUtil.copyProperties(oldAttribute, attribute);
                attributeDao.save(oldAttribute);
            }
        }
        List<Attribute> removeAttribute = new ArrayList<Attribute>();
        for (Attribute attribute : version.getAttributes()) {
            if (ObjectUtil.find(attributes, "code", attribute.getCode()) == null) {
                removeAttribute.add(attribute);
            }
        }
        for (Attribute attribute : removeAttribute) {
            attributeDao.delete(attribute.getId());
            ObjectUtil.remove(version.getAttributes(), "code", attribute.getCode());
        }
        this.attributeVersionDao.save(version);
        return version;
    }

    public AttributeVersion get(Long id) {
        AttributeVersion version = this.attributeVersionDao.get(id);
        ObjectUtil.sort(version.getAttributes(), "id", "asc");
        return version;
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.attributeVersionDao.delete(id);
        }
    }

    public List<AttributeVersion> getVersions(Class<?> entityClass) {
        return this.attributeVersionDao.find(Restrictions.eq("targetClassName", entityClass.getName()));
    }


    public List<AttributeVersion> getVersions(List<PropertyFilter> filter, String orderBy, String order, int size) {
        return this.attributeVersionDao.find(filter, orderBy, order, 0, size);
    }

    public AttributeVersion findUniqueByTargetClassName(String targetClassName) {
        AttributeVersion version = this.attributeVersionDao.findUnique(Restrictions.eq("targetClassName", targetClassName), Restrictions.eq("type", AttributeVersion.Type.custom));
        if (version == null) {
            return null;
        }
        AttributeVersion _rev = BeanUtil.copyProperties(new AttributeVersion(), version);
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (Attribute attribute : version.getAttributes()) {
            Hibernate.initialize(attribute);
            attributes.add(BeanUtil.copyProperties(new Attribute(), attribute));
            AttributeType attributeType = attribute.getAttributeType();
            Hibernate.initialize(attributeType);
            Hibernate.initialize(attributeType.getConverter());
            Converter converter = BeanUtil.copyProperties(new Converter(), attributeType.getConverter());
            attributeType = BeanUtil.copyProperties(new AttributeType(), attributeType);
            attributeType.setConverter(converter);
            attributes.get(attributes.size() - 1).setAttributeType(attributeType);
        }
        _rev.setAttributes(attributes);
        return _rev;
    }

    /**
     * 通过 version id 加载全部版本相关的完整数据
     *
     * @param className 版本对应的 class
     * @param number    版本号
     * @return AttributeVersion
     */
    public AttributeVersion findUniqueByTargetClassName(String className, String number) {
        AttributeVersion version = this.attributeVersionDao.findUnique(Restrictions.eq("targetClassName", className), Restrictions.eq("number", number));
        if (version == null) {
            return null;
        }
        AttributeVersion _rev = BeanUtil.copyProperties(new AttributeVersion(), version);
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (Attribute attribute : version.getAttributes()) {
            Hibernate.initialize(attribute);
            attributes.add(BeanUtil.copyProperties(new Attribute(), attribute));
            AttributeType attributeType = attribute.getAttributeType();
            Hibernate.initialize(attributeType);
            Hibernate.initialize(attributeType.getConverter());
            Converter converter = BeanUtil.copyProperties(new Converter(), attributeType.getConverter());
            attributeType = BeanUtil.copyProperties(new AttributeType(), attributeType);
            attributeType.setConverter(converter);
            attributes.get(attributes.size() - 1).setAttributeType(attributeType);
        }
        _rev.setAttributes(attributes);
        return _rev;
    }

    public static AttributeVersion version(Long id) {
        AttributeVersionService service = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        return service.get(id);
    }

    /**
     * 获取class 原有属性及版本属性 名称
     *
     * @param className
     * @return
     */
    public static List<String> classAllAttrs(Long id, String className) {
        List<String> classAttrs = new ArrayList<String>();
        AttributeVersionService service = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        //版本属性
        AttributeVersion version = service.get(id);
        if (version.getAttributes() != null) {
            for (Attribute attr : version.getAttributes()) {
                classAttrs.add(attr.getCode());
            }
        }
        //class 原有属性
        String[] classAttrArray = ObjectUtil.toFieldArray(ClassUtil.getPropertys(ClassUtil.forName(className)), "name", String.class);
        if (classAttrArray != null) {
            for (String name : classAttrArray) {
                classAttrs.add(name);
            }
        }
        return classAttrs;
    }


    /**
     * 获取版本列表
     *
     * @return List<AttributeVersion>
     */
    public List<AttributeVersion> getAttributeVersions() {
        return this.attributeVersionDao.find();
    }

    /**
     * 静态获取版本列表
     *
     * @return List<AttributeVersion>
     */
    public static List<AttributeVersion> listAttributeVersions() {
        AttributeVersionService versionService = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        return versionService.getAttributeVersions();
    }

}
