package com.fantasy.framework.lucene;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.lucene.annotations.Indexed;
import com.fantasy.framework.lucene.backend.IndexChecker;
import com.fantasy.framework.lucene.backend.IndexReopenTask;
import com.fantasy.framework.lucene.cache.IndexWriterCache;
import com.fantasy.framework.lucene.cluster.ClusterConfig;
import com.fantasy.framework.lucene.dao.LuceneDao;
import com.fantasy.framework.spring.ClassPathScanner;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.PathUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BuguIndex implements InitializingBean {

	private static final Log logger = LogFactory.getLog(BuguIndex.class);

	private static BuguIndex instance;
	/**
	 * RAMBufferSizeMB
	 */
	private double bufferSizeMB = 16.0D;
	/**
	 * Lucene 版本
	 */
	private Version version = Version.LUCENE_36;
	/**
	 * 分词器
	 */
	private Analyzer analyzer = new StandardAnalyzer(this.version);
//	/**
//	 * 文件管理器
//	 */
//	@Deprecated
//	private LocalFileManager fileManager;
	/**
	 *索引文件的存放目录
	 */
	private String directoryPath;
	/**
	 * 集群配置
	 */
	private ClusterConfig clusterConfig;
	/**
	 * 线程池
	 */
	private ExecutorService executor;
	/**
	 * 线程池大小
	 */
	private int threadPoolSize = 10;
	/**
	 * 定时任务
	 */
	private ScheduledExecutorService scheduler;
	/**
	 * reopen 执行周期
	 */
	private long period = 30000L;

	private boolean rebuild = false;

	private Map<Class<?>, IndexRebuilder> indexRebuilders = new HashMap<Class<?>, IndexRebuilder>();

	private String[] packagesToScan = new String[] {};
	private HashMap<Class<?>, LuceneDao<?>> luceneDaos = new HashMap<Class<?>, LuceneDao<?>>();
	private HashMap<Class<?>, Class<?>> luceneDaoClass = new HashMap<Class<?>, Class<?>>();

	public synchronized static BuguIndex getInstance() {
		if (instance == null && SpringContextUtil.getApplicationContext() != null) {
			instance = SpringContextUtil.getBeanByType(BuguIndex.class);
		}
		if (instance == null) {
			instance = new BuguIndex();
			instance.afterPropertiesSet();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() {
		Set<Class<?>> indexedClasses = new LinkedHashSet<Class<?>>();
		for (String basePackage : packagesToScan) {
			indexedClasses.addAll(ClassPathScanner.getInstance().findAnnotationedClasses(basePackage, Indexed.class));
			for (Class<?> clazz : ClassPathScanner.getInstance().findInterfaceClasses(basePackage, LuceneDao.class)) {
				luceneDaoClass.put(ClassUtil.getInterfaceGenricType(clazz, LuceneDao.class), (Class<LuceneDao<?>>) clazz);
			}
		}
		for (Class<?> clazz : indexedClasses) {
			indexRebuilders.put(clazz, new IndexRebuilder(clazz));
		}
		if (BuguIndex.instance == null) {
			BuguIndex.instance = this;
		}
		if (this.rebuild) {
			SchedulingTaskExecutor executor = SpringContextUtil.getBean("spring.executor", SchedulingTaskExecutor.class);
			executor.execute(new Runnable() {

				public void run() {
					BuguIndex.this.rebuild();
				}

			}, 1000 * 30);
		}
	}

	public void rebuild() {
		for (IndexRebuilder indexRebuilder : indexRebuilders.values()) {
			indexRebuilder.rebuild();
		}
	}

	public void rebuild(Class<?> clazz) {
		if (!IndexChecker.hasIndexed(clazz)) {
			throw new IgnoreException(clazz + " @Indexed ");
		}
		if (!this.indexRebuilders.containsKey(clazz)) {
			throw new IgnoreException(clazz + " not found indexRebuilder");
		}
		indexRebuilders.get(clazz).rebuild();
	}

	@SuppressWarnings("unchecked")
	public <T> LuceneDao<T> getLuceneDao(Class<T> indexedClass) {
		if (this.luceneDaos.containsKey(indexedClass)){
            return (LuceneDao<T>) this.luceneDaos.get(indexedClass);
        }
		if (this.luceneDaoClass.containsKey(indexedClass)) {
			LuceneDao<T> luceneDao = (LuceneDao<T>) SpringContextUtil.getBeanByType(this.luceneDaoClass.get(indexedClass));
			if (luceneDao == null) {
				luceneDao = (LuceneDao<T>) SpringContextUtil.createBean(this.luceneDaoClass.get(indexedClass), SpringContextUtil.AUTOWIRE_BY_TYPE);
			}
			this.luceneDaos.put(indexedClass, luceneDao);
			return luceneDao;
		}
		return null;
	}

	/**
	 * 初始化方法
	 * 
	 */
	public void open() {
		this.executor = Executors.newFixedThreadPool(this.threadPoolSize);
		this.scheduler = Executors.newSingleThreadScheduledExecutor();
		this.scheduler.scheduleAtFixedRate(new IndexReopenTask(), this.period, this.period, TimeUnit.MILLISECONDS);
		if (this.clusterConfig != null){
            this.clusterConfig.validate();
        }
	}

	/**
	 * 关闭方法
	 * 
	 * @功能描述
	 */
	public void close() {
		if (this.executor != null) {
			this.executor.shutdown();
		}
		if (this.scheduler != null) {
			this.scheduler.shutdown();
		}
		if (this.clusterConfig != null) {
			this.clusterConfig.invalidate();
		}
		Map<String, IndexWriter> map = IndexWriterCache.getInstance().getAll();
		for (IndexWriter writer : map.values()){
            if (writer != null) {
                Directory dir = writer.getDirectory();
                try {
                    writer.commit();
                    writer.close(true);
                } catch (CorruptIndexException ex) {
                    logger.error("Can not commit and close the lucene index", ex);
                } catch (IOException ex) {
                    logger.error("Can not commit and close the lucene index", ex);
                } finally {
                    try {
                        if ((dir != null) && (IndexWriter.isLocked(dir))){
                            IndexWriter.unlock(dir);
                        }
                    } catch (IOException ex) {
                        logger.error("Can not unlock the lucene index", ex);
                    }
                }
            }
        }
	}

	public ExecutorService getExecutor() {
		return this.executor;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public double getBufferSizeMB() {
		return this.bufferSizeMB;
	}

	public void setBufferSizeMB(double bufferSizeMB) {
		this.bufferSizeMB = bufferSizeMB;
	}

	public void setIndexReopenPeriod(long period) {
		this.period = period;
	}

	public Version getVersion() {
		return this.version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Analyzer getAnalyzer() {
		return this.analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public ClusterConfig getClusterConfig() {
		return this.clusterConfig;
	}

	public void setClusterConfig(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
	}

	public void setBasePackage(String basePackage) {
		this.packagesToScan = StringUtils.tokenizeToStringArray(basePackage, ",; \t\n");
	}

	public void setLuceneDaos(HashMap<Class<?>, LuceneDao<?>> luceneDaos) {
		this.luceneDaos = luceneDaos;
	}

	public void setRebuild(boolean rebuild) {
		this.rebuild = rebuild;
	}

	public File getOpenFolder(String remotePath) {
		return FileUtil.createFolder(PathUtil.webinf() + this.directoryPath + remotePath);
	}

}