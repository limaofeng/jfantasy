package com.fantasy.framework.lucene.backend;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.cache.IndexWriterCache;
import com.fantasy.framework.lucene.mapper.MapperUtil;

/**
 * 删除索引
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-26 下午09:09:59
 * @version 1.0
 */
public class IndexRemoveTask implements Runnable {
	private static final Logger logger = Logger.getLogger(IndexRemoveTask.class);
	private Class<?> clazz;
	private String id;

	public IndexRemoveTask(Class<?> clazz, String id) {
		this.clazz = clazz;
		this.id = id;
	}

	public void run() {
		String name = MapperUtil.getEntityName(this.clazz);
		IndexWriterCache cache = IndexWriterCache.getInstance();
		IndexWriter writer = cache.get(name);
		Term term = new Term(FieldsCache.getInstance().getIdFieldName(this.clazz), this.id);
		try {
			writer.deleteDocuments(term);
		} catch (CorruptIndexException ex) {
			logger.error("IndexWriter can not delete a document from the lucene index", ex);
		} catch (IOException ex) {
			logger.error("IndexWriter can not delete a document from the lucene index", ex);
		}
	}
}