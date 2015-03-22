package com.fantasy.attr.storage.service;

import com.fantasy.attr.framework.util.VersionUtil;
import com.fantasy.attr.storage.bean.*;
import com.fantasy.attr.storage.dao.CustomBeanDao;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
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

    public void save(com.fantasy.attr.framework.CustomBean customBean) {
        CustomBeanDefinition definition = customBeanDefinitionService.findUniqueByClassName(customBean.getClass().getName());
        if (definition == null) {
            return;
        }
        AttributeVersion version = attributeVersionService.findUniqueByTargetClassName(customBean.getClass().getName());
        if (version == null) {
            return;
        }
        CustomBean _customBean = customBean.getId() != null ? customBeanDao.get(customBean.getId()) : new CustomBean();
        if (_customBean == null) {
            _customBean = new CustomBean();
            _customBean.setId(null);
        }
        _customBean.setDefinition(definition);
        _customBean.setVersion(version.getId());
        if (_customBean.getId() == null) {
            customBeanDao.save(_customBean);
        }
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(_customBean.getAttributeValues(), "attribute.code", attribute.getCode());
            if (attributeValue == null) {
                attributeValue = new AttributeValue();
                attributeValue.setAttribute(attribute);
                attributeValue.setValue(OgnlUtil.getInstance().getValue(attribute.getCode(), customBean).toString());
                attributeValue.setVersion(version);
                attributeValue.setTargetId(_customBean.getId());
            }
            String value = VersionUtil.getOgnlUtil(attribute.getAttributeType()).getValue(attribute.getCode(), customBean, String.class);
            if (StringUtil.isNotBlank(value)) {
                attributeValue.setValue(value);
                attributeValues.add(attributeValue);
            }
        }
        _customBean.setAttributeValues(attributeValues);
        customBeanDao.save(_customBean);
        customBean.setId(_customBean.getId());
    }

    public void save() {

    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            customBeanDao.delete(id);
        }
    }

    public com.fantasy.attr.framework.CustomBean get(Long id) {
        CustomBean _customBean = customBeanDao.get(id);
        if (_customBean == null) {
            return null;
        }
        com.fantasy.attr.framework.CustomBean customBean = ClassUtil.newInstance(_customBean.getDefinition().getClassName(), com.fantasy.attr.framework.CustomBean.class);
        customBean.setId(_customBean.getId());
        AttributeVersion version = attributeVersionService.findUniqueByTargetClassName(customBean.getClass().getName());
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(_customBean.getAttributeValues(), "attribute.code", attribute.getCode());
            if (attributeValue != null) {
                VersionUtil.getOgnlUtil(attribute.getAttributeType()).setValue(attribute.getCode(), customBean, attributeValue.getValue());
            }
        }
        return customBean;
    }

}
