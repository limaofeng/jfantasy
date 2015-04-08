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
        CustomBean _customBean = new CustomBean();
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        _customBean.setId(customBean.getId());
        if (_customBean.getId() == null) {
            _customBean.setDefinition(definition);
            _customBean.setVersion(version.getId());
            customBeanDao.save(_customBean);
            _customBean.setAttributeValues(attributeValues);
            customBean.setId(_customBean.getId());
        }else{
            attributeValues = this.customBeanDao.get(customBean.getId()).getAttributeValues();
        }
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = ObjectUtil.find(attributeValues, "attribute.code", attribute.getCode());
            if (attributeValue == null && OgnlUtil.getInstance().getValue(attribute.getCode(), customBean)!=null) {
                attributeValue = new AttributeValue();
                attributeValue.setAttribute(attribute);
                attributeValue.setValue(OgnlUtil.getInstance().getValue(attribute.getCode(), customBean)==null?null:OgnlUtil.getInstance().getValue(attribute.getCode(), customBean).toString());
                attributeValue.setVersion(version);
                attributeValue.setTargetId(_customBean.getId());
                attributeValues.add(attributeValue);
            }
            String value = VersionUtil.getOgnlUtil(attribute.getAttributeType()).getValue(attribute.getCode(), customBean, String.class);
            if (StringUtil.isNotBlank(value)) {
                attributeValue.setValue(value);
            }
        }
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
