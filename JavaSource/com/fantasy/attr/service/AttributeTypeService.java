package com.fantasy.attr.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.dao.AttributeTypeDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;

/**
 * 商品属性类型service
 * 
 * @author mingliang
 * 
 */
@Service
@Transactional
public class AttributeTypeService implements InitializingBean {

	private static final Log logger = LogFactory.getLog(AttributeTypeService.class);

	@Resource
	private AttributeTypeDao attributeTypeDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			// 初始化基础分类
			Class<?>[] defaultClass = new Class[] { int.class, long.class, double.class, Date.class, BigDecimal.class, String.class, int[].class, long[].class, double[].class, Date[].class, BigDecimal[].class, String[].class };
			for (Class<?> clazz : defaultClass) {
				AttributeType attributeType = attributeTypeDao.findUniqueBy("dataType", clazz.getName());
				if (attributeType == null) {
					StringBuffer log = new StringBuffer("初始化属性类型:" + clazz);
					attributeType = new AttributeType();
					attributeType.setName(clazz.getSimpleName());
					attributeType.setDataType(clazz.getName());
					attributeType.setConverter(null);
					attributeType.setDescription("基本数据类型:" + clazz);
					attributeTypeDao.save(attributeType);
					logger.debug(log);
				}
			}
			// 初始化商品图片目录
		} finally {
			transactionManager.commit(status);
		}
	}

	public Pager<AttributeType> findPager(Pager<AttributeType> pager, List<PropertyFilter> filters) {
		return attributeTypeDao.findPager(pager, filters);
	}

	public AttributeType save(AttributeType attributeType) {
		attributeTypeDao.save(attributeType);
		return attributeType;
	}

	public AttributeType get(Long id) {
		AttributeType attributeType = attributeTypeDao.get(id);
		Hibernate.initialize(attributeType);
		return attributeType;
	}

	public void delete(Long[] ids) {
		for (Long id : ids) {
			this.attributeTypeDao.delete(id);
		}
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
