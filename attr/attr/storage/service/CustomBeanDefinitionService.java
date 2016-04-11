package org.jfantasy.attr.storage.service;

import org.hibernate.criterion.Criterion;
import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.attr.storage.bean.CustomBeanDefinition;
import org.jfantasy.attr.storage.dao.CustomBeanDefinitionDao;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CustomBeanDefinitionService {

    @Autowired
    private CustomBeanDefinitionDao customBeanDefinitionDao;
    @Autowired
    private AttributeVersionService attributeVersionService;

    /**
     * 保存 自定义bean 定义
     *
     * @param className  className
     * @param name       中文描述
     * @param attributes 自定义字段
     */
    public CustomBeanDefinition save(String className, String name, Attribute... attributes) {
        return this.save(className, name, Arrays.asList(attributes));
    }

    /**
     * 保存 自定义bean 定义
     *
     * @param className  className
     * @param name       中文描述
     * @param attributes 自定义字段
     */
    public CustomBeanDefinition save(String className, String name, List<Attribute> attributes) {
        AttributeVersion version = attributeVersionService.save(className, attributes);
        CustomBeanDefinition definition = ObjectUtil.defaultValue(this.findUniqueByClassName(className), new CustomBeanDefinition());
        definition.setVersion(version);
        definition.setName(name);
        definition.setClassName(className);
        definition.setAttributes(version.getAttributes());
        return customBeanDefinitionDao.save(definition);
    }

    public CustomBeanDefinition findUniqueByClassName(String className) {
        return customBeanDefinitionDao.findUniqueBy("className", className);
    }

    public void delete(String className) {
        CustomBeanDefinition definition = findUniqueByClassName(className);
        if (definition != null) {
            customBeanDefinitionDao.delete(definition.getId());
        }
    }

    public void delete(Long id) {
        customBeanDefinitionDao.delete(id);
    }

    public List<CustomBeanDefinition> find(Criterion... criterions) {
        return this.customBeanDefinitionDao.find(criterions);
    }
}
