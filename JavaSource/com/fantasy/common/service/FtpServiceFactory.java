package com.fantasy.common.service;

import com.fantasy.common.bean.FtpConfig;
import com.fantasy.framework.service.FTPService;
import com.fantasy.framework.spring.SpringContextUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * FtpServiceFactory
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-5 下午4:57:11
 * @version 1.0
 */
@Service
@Transactional
@Lazy(false)
public class FtpServiceFactory implements InitializingBean {

	@Resource
	private FtpConfigService ftpConfigService;

	private final static ConcurrentMap<Long, FTPService> ftpServiceCache = new ConcurrentHashMap<Long, FTPService>();

	/**
	 * 应用启动时加载并初始化全部的FtpService
	 * 
	 * @功能描述
	 * @throws Exception
	 */
	public void afterPropertiesSet() throws Exception {
		PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setReadOnly(true);
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			for (FtpConfig config : ftpConfigService.getAll()) {
				this.initialize(config);
			}
		} finally {
			transactionManager.commit(status);
		}
	}

	public static String getFtpServiceBeanId(Long configId) {
		return "ftpService-" + configId;
	}

	private FTPService initialize(FtpConfig config) {
		Map<String, Object> propertyValues = new HashMap<String, Object>();
		propertyValues.put("hostname", config.getHostname());
		propertyValues.put("port", config.getPort());
		propertyValues.put("username", config.getUsername());
		propertyValues.put("password", config.getPassword());
		propertyValues.put("controlEncoding", config.getControlEncoding());
		SpringContextUtil.registerBeanDefinition(getFtpServiceBeanId(config.getId()), FTPService.class, propertyValues);
		return SpringContextUtil.getBean(getFtpServiceBeanId(config.getId()),FTPService.class);
	}

	/**
	 * 更新FtpService连接信息
	 * 
	 * @功能描述
	 * @param config
	 */
	public void updateFtpService(FtpConfig config) {
		FTPService ftpService = getFtpService(config.getId());
		if (ftpService != null) {
			ftpService.setHostname(config.getHostname());
			ftpService.setPort(config.getPort());
			ftpService.setUsername(config.getUsername());
			ftpService.setPassword(config.getPassword());
			ftpService.setControlEncoding(config.getControlEncoding());
		}
	}

	/**
	 * 删除一个指定的FtpService
	 * 
	 * @功能描述
	 * @param config
	 */
	public void removeFtpService(FtpConfig config) {
		if (ftpServiceCache.containsKey(config.getId())) {
			SpringContextUtil.removeBeanDefinition(getFtpServiceBeanId(config.getId()));
			ftpServiceCache.remove(config.getId());
		}
	}

	/**
	 * 通过Id获取FtpService对象
	 * 
	 * @功能描述
	 * @param id
	 * @return
	 */
	public FTPService getFtpService(Long id) {
		if (!ftpServiceCache.containsKey(id)) {
			ftpServiceCache.put(id, this.initialize(ftpConfigService.get(id)));
		}
		return ftpServiceCache.get(id);
	}

	public static class FtpConfigEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

		private static final long serialVersionUID = -8410630987365469163L;

		@Resource
		private FtpServiceFactory ftpServiceFactory;

        @Override
        public boolean requiresPostCommitHanding(EntityPersister persister) {//TODO 啥意思
            return false;
        }

		public void onPostInsert(PostInsertEvent event) {
			if (event.getEntity() instanceof FtpConfig) {
				ftpServiceFactory.initialize((FtpConfig) event.getEntity());
			}
		}

        public void onPostUpdate(PostUpdateEvent event) {
			if (event.getEntity() instanceof FtpConfig) {
				ftpServiceFactory.updateFtpService((FtpConfig) event.getEntity());
			}
		}

		public void onPostDelete(PostDeleteEvent event) {
			if (event.getEntity() instanceof FtpConfig) {
				ftpServiceFactory.removeFtpService((FtpConfig) event.getEntity());
			}
		}
	}

}
