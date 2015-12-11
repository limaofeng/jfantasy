/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.config.CachingConfig.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月3日 上午9:54:53 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月3日    |    Administrator     |     Created 
 */
package com.fantasy.framework.spring.mvc.config;

import org.apache.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/** 
 *Description: <启用缓存>. <br>
 *<p>
	<使用说明>
 </p>
 *Makedate:2014年9月3日 上午9:54:53 
 * @author Administrator  
 * @version V1.0                             
 */
@Configuration
@EnableCaching
public class CachingConfig {
	private static final Logger logger = Logger.getLogger(CachingConfig.class);
//	@Bean
//	public CacheManager cacheManager() {
//		logger.info("SimpleCacheManager");
//		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
//
//		ConcurrentMapCache mapCache_default = new ConcurrentMapCache("default");
//
//		ConcurrentMapCache mapCache_commonCache = new ConcurrentMapCache(
//				"commonCache");
//
//		Set<Cache> caches = new HashSet<Cache>();
//		caches.add(mapCache_default);
//		caches.add(mapCache_commonCache);
//
//		simpleCacheManager.setCaches(caches);
//
//		return simpleCacheManager;
//	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
		ehCacheManagerFactoryBean.setAcceptExisting(true);
		ehCacheManagerFactoryBean.setCacheManagerName("appCache");
		return ehCacheManagerFactoryBean;
	}

	@Bean
	public CacheManager cacheManager() {
		logger.info("EhCacheCacheManager");
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
		return cacheManager;
	}
}


