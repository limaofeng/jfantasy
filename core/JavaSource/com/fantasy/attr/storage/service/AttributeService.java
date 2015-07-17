package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.dao.AttributeDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 商品属性 service
 */
@Service
@Transactional
public class AttributeService {

	@Autowired
	private AttributeDao attributeDao;
	/**
	 * 保存属性
	 * @param attribute
	 * @return
	 */
	public Attribute save(Attribute attribute) {
		return attributeDao.save(attribute);
	}
	/**
	 * 根据code查找属性
	 * @param code
	 * @return
	 */
	public Attribute findByCode(String code) {
		Attribute attribute = attributeDao.findUnique(Restrictions.eq("code", code));
		Hibernate.initialize(attribute);
		return attribute;
	}
	
	/**
	 * 商品属性编码唯一
	 * @param code
	 * @param id
	 * @return
	 */
	public boolean attributeCodeUnique(String code, Long id) {
		Attribute attribute = attributeDao.findUniqueBy("code", code);
		return (attribute==null) || attribute.getId().equals(id);
	}
	
	/**
	 * 获取所有属性
	 * @return
	 */
	public List<Attribute> getAll(){
		return attributeDao.getAll();
	}
	
	/**
	 * 分页查询
	 * @param pager
	 * @param filters
	 * @return
	 */
	public Pager<Attribute> findPager(Pager<Attribute> pager, List<PropertyFilter> filters) {
		return attributeDao.findPager(pager, filters);
	}
	/**
	 * 删除
	 * @param ids
	 */
	public void delete(Long... ids) {
		for (Long id : ids) {
			this.attributeDao.delete(id);
		}
	}
	/**
	 * 根据ID获取属性
	 * @param id
	 * @return
	 */
	public Attribute get(Long id) {
		Attribute shopconfig = attributeDao.get(id);
		Hibernate.initialize(shopconfig);
		return shopconfig;
	}
	
	/**
	 * 获取所有属性
	 * @return
	 */
	public static List<Attribute> allAttribute(){
		return SpringContextUtil.getBeanByType(AttributeService.class).getAll();
	}
}
