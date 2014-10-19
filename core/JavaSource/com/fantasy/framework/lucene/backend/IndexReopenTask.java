package com.fantasy.framework.lucene.backend;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

import com.fantasy.framework.lucene.cache.IndexSearcherCache;

public class IndexReopenTask implements Runnable {

	private static final Logger logger = Logger.getLogger(IndexReopenTask.class);
	private static final Lock reopenLock = new ReentrantLock();

	public void run() {
		if (!reopenLock.tryLock()) {
			return;
		}
		try {
			IndexSearcherCache searcherCache = IndexSearcherCache.getInstance();
			for (Map.Entry<String, IndexSearcher> entry : searcherCache.getAll().entrySet()) {
				IndexSearcher searcher = entry.getValue();
				IndexReader reader = searcher.getIndexReader();
				IndexReader newReader = null;
				try {
					newReader = IndexReader.openIfChanged(reader);
				} catch (IOException ex) {
					logger.error("Something is wrong when reopen the Lucene IndexReader", ex);
				}
				if ((newReader != null) && (newReader != reader)) {
					try {
						reader.decRef();
					} catch (IOException ex) {
						logger.error("Something is wrong when decrease the reference of the lucene IndexReader", ex);
					}
					IndexSearcher newSearcher = new IndexSearcher(newReader);
					searcherCache.put((String) entry.getKey(), newSearcher);
				}
			}
		} finally {
			reopenLock.unlock();
		}
	}
}