package org.jfantasy.attr.storage.service;

import org.jfantasy.attr.framework.CustomBeanFactory;
import org.jfantasy.attr.storage.bean.*;
import org.jfantasy.attr.storage.dao.CustomBeanDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomBeanService {

    @Autowired
    private CustomBeanDao customBeanDao;
    @Autowired
    private CustomBeanDefinitionService customBeanDefinitionService;
    @Autowired
    private AttributeVersionService attributeVersionService;
    @Autowired
    private CustomBeanFactory customBeanFactory;

    public void save(org.jfantasy.attr.framework.CustomBean customBean) {
        CustomBeanDefinition definition = customBeanDefinitionService.findUniqueByClassName(customBean.getClass().getName());
        if (definition == null) {
            return;
        }
        AttributeVersion version = attributeVersionService.findUniqueByTargetClassName(customBean.getClass().getName());
        if (version == null) {
            return;
        }
        CustomBean customBeans = new CustomBean();
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        customBeans.setId(customBean.getId());
        if (customBeans.getId() == null) {
            customBeans.setDefinition(definition);
            customBeans.setVersion(version.getId());
            customBeans = customBeanDao.save(customBeans);
            customBeans.setAttributeValues(attributeValues);
            customBean.setId(customBeans.getId());
        } else {
            attributeValues = this.customBeanDao.get(customBean.getId()).getAttributeValues();
        }
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(attributeValues, "attribute.code", attribute.getCode());
            if (attributeValue == null && OgnlUtil.getInstance().getValue(attribute.getCode(), customBean) != null) {
                attributeValue = new AttributeValue();
                attributeValue.setAttribute(attribute);
                attributeValue.setValue(OgnlUtil.getInstance().getValue(attribute.getCode(), customBean) == null ? null : OgnlUtil.getInstance().getValue(attribute.getCode(), customBean).toString());
                attributeValue.setVersion(version);
                attributeValue.setTargetId(customBeans.getId());
                attributeValues.add(attributeValue);
            }
            String value = customBeanFactory.getOgnlUtil(attribute.getAttributeType()).getValue(attribute.getCode(), customBean, String.class);
            if (StringUtil.isNotBlank(value)) {
                assert attributeValue != null;
                attributeValue.setValue(value);
            }
        }
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            customBeanDao.delete(id);
        }
    }

    public Pager findPager(Pager<CustomBean> pager, List<PropertyFilter> filters) {
        return this.customBeanDao.findPager(pager, filters);
    }

    public org.jfantasy.attr.framework.CustomBean get(Long id) {
        CustomBean customBeans = customBeanDao.get(id);
        if (customBeans == null) {
            return null;
        }
        org.jfantasy.attr.framework.CustomBean customBean = ClassUtil.newInstance(customBeans.getDefinition().getClassName(), org.jfantasy.attr.framework.CustomBean.class);
        assert customBean != null;
        customBean.setId(customBeans.getId());
        AttributeVersion version = attributeVersionService.findUniqueByTargetClassName(customBean.getClass().getName());
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(customBeans.getAttributeValues(), "attribute.code", attribute.getCode());
            if (attributeValue != null) {
                customBeanFactory.getOgnlUtil(attribute.getAttributeType()).setValue(attribute.getCode(), customBean, attributeValue.getValue());
            }
        }
        return customBean;
    }

}
