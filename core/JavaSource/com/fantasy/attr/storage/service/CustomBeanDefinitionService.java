package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.Attribute;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CustomBeanDefinitionService {

    /**
     * 保存 自定义bean 定义
     *
     * @param simpleName classSimpleName
     * @param name       中文描述
     * @param attributes 自定义字段
     */
    public void save(String simpleName, String name, List<Attribute> attributes) {
    }

}
