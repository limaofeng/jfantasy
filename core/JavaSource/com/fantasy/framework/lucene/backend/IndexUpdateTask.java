package com.fantasy.framework.lucene.backend;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.cache.IndexWriterCache;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import com.fantasy.framework.lucene.mapper.MapperUtil;

public class IndexUpdateTask implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(IndexUpdateTask.class);
	private Object entity;

	public IndexUpdateTask(Object entity) {
		this.entity = entity;
	}

	public void run() {
		Class<?> clazz = this.entity.getClass();
		String name = MapperUtil.getEntityName(clazz);
		IndexWriterCache cache = IndexWriterCache.getInstance();
		IndexWriter writer = cache.get(name);
		Document doc = new Document();
		IndexCreator creator = new IndexCreator(this.entity, "");
		creator.create(doc);
		String idName = FieldsCache.getInstance().getIdFieldName(clazz);
		Term term = new Term(idName, FieldUtil.get(this.entity, idName).toString());
		try {
			writer.updateDocument(term, doc);
		} catch (CorruptIndexException ex) {
			LOGGER.error("IndexWriter can not update the document", ex);
		} catch (IOException ex) {
			LOGGER.error("IndexWriter can not update the document", ex);
		}
	}
}