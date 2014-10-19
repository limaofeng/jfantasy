package com.fantasy.framework.lucene.backend;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import com.fantasy.framework.lucene.cache.DaoCache;
import com.fantasy.framework.lucene.cache.IndexWriterCache;
import com.fantasy.framework.lucene.mapper.MapperUtil;

public class IndexRebuildTask implements Runnable {
	private static final Log logger = LogFactory.getLog(IndexRebuildTask.class);
	/**
	 * 每个IndexRebuildTask拥有单独的锁
	 */
	private static final ConcurrentMap<Class<?>, ReentrantLock> rebuildLocks = new ConcurrentHashMap<Class<?>, ReentrantLock>();
	private Lock rebuildLock;
	private Class<?> clazz;
	private IndexWriter writer;
	private int batchSize;

	public IndexRebuildTask(Class<?> clazz, int batchSize) {
		this.clazz = clazz;
		this.batchSize = batchSize;
		String name = MapperUtil.getEntityName(clazz);
		IndexWriterCache cache = IndexWriterCache.getInstance();
		this.writer = cache.get(name);
		if (!rebuildLocks.containsKey(clazz)) {
			rebuildLocks.put(clazz, new ReentrantLock());
		}
		rebuildLock = rebuildLocks.get(clazz);
	}

	public void run() {
		if (!rebuildLock.tryLock()) {
			if (logger.isErrorEnabled()) {
				logger.error("Another rebuilding task is running");
			}
			return;
		}
		try {
			if (logger.isInfoEnabled()) {
				logger.info("Index(" + this.clazz + ") rebuilding start...");
			}
			try {
				this.writer.deleteAll();
			} catch (IOException ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Something is wrong when lucene IndexWriter doing deleteAll()", ex);
				}
			}
			long count = DaoCache.getInstance().get(clazz).count();
			int pages = (int) (count / this.batchSize);
			int remainder = (int) (count % this.batchSize);
			if (pages > 0) {
				for (int i = 1; i <= pages; i++) {
					List<?> list = DaoCache.getInstance().get(clazz).find((i - 1) * this.batchSize, this.batchSize);
					process(list);
				}
			}
			if (remainder > 0) {
				pages++;
				List<?> list = DaoCache.getInstance().get(clazz).find((pages - 1) * this.batchSize, this.batchSize);
				process(list);
			}
			if (logger.isInfoEnabled()) {
				logger.info("Index(" + this.clazz + ") rebuilding finish.");
			}
		} finally {
			rebuildLock.unlock();
		}
	}

	private void process(List<?> list) {
		for (Iterator<?> i$ = list.iterator(); i$.hasNext();) {
			Object o = i$.next();
			process(o);
		}
	}

	private void process(Object entity) {
		IndexFilterChecker checker = new IndexFilterChecker(entity);
		if (checker.needIndex()) {
			Document doc = new Document();
			IndexCreator creator = new IndexCreator(entity, "");
			creator.create(doc);
			try {
				this.writer.addDocument(doc);
			} catch (CorruptIndexException ex) {
				logger.error("IndexWriter can not add a document to the lucene index", ex);
			} catch (IOException ex) {
				logger.error("IndexWriter can not add a document to the lucene index", ex);
			}
		}
	}

}
