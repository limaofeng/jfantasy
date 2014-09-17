package com.fantasy.security.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.Role;
import com.fantasy.security.dao.RoleDao;

@Service("fantasy.auth.RoleService")
@Transactional
public class RoleService implements InitializingBean {

	private static final Log logger = LogFactory.getLog(RoleService.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		StringBuffer log = new StringBuffer("初始化系统默认系统管理员角色信息");
		PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			Role role = get("SYSTEM");
			if (role == null) {
				role = new Role();
				role.setCode("SYSTEM");
				role.setName("系统管理员");
				role.setEnabled(true);
				role.setDescription("系统默认管理员");
				save(role);
			}
		} finally {
			transactionManager.commit(status);
		}
		logger.debug(log);
	}

	@Resource
	private RoleDao roleDao;

	public List<Role> getAll() {
		return roleDao.findBy("enabled", true);
	}

	public Pager<Role> findPager(Pager<Role> pager, List<PropertyFilter> filters) {
		return this.roleDao.findPager(pager, filters);
	}

	public void save(Role role) {
		roleDao.save(role);
	}

	public Role get(String id) {
		return this.roleDao.get(id);
	}

	public void delete(String[] ids) {
		for (String code : ids) {
			this.roleDao.delete(code);
		}
	}

	public static List<Role> list() {
		RoleService roleService = SpringContextUtil.getBeanByType(RoleService.class);
		return roleService.getAll();
	}

	public static String[] getRoleCodes(List<Role> roles) {
		return ObjectUtil.toFieldArray(roles, "code", String.class);
	}

}