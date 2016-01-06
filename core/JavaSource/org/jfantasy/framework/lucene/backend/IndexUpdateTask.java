package org.jfantasy.framework.lucene.backend;

import org.jfantasy.framework.lucene.cache.IndexWriterCache;
import org.jfantasy.framework.lucene.cache.PropertysCache;
import org.jfantasy.framework.lucene.mapper.MapperUtil;
import org.jfantasy.framework.util.reflect.Property;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;

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
        Property property = PropertysCache.getInstance().getIdProperty(clazz);
        Term term = new Term(property.getName(),property.getValue(this.entity).toString());
        try {
            writer.updateDocument(term, doc);
        } catch (CorruptIndexException ex) {
            LOGGER.error("IndexWriter can not update the document", ex);
        } catch (IOException ex) {
            LOGGER.error("IndexWriter can not update the document", ex);
        }
    }
}