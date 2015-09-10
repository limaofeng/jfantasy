package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.cache.IndexWriterCache;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.lucene.mapper.MapperUtil;
import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;

/**
 * 删除索引
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-26 下午09:09:59
 */
public class IndexRemoveTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(IndexRemoveTask.class);
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
        Term term = new Term(PropertysCache.getInstance().getIdPropertyName(this.clazz), this.id);
        try {
            writer.deleteDocuments(term);
        } catch (CorruptIndexException ex) {
            LOGGER.error("IndexWriter can not delete a document from the lucene index", ex);
        } catch (IOException ex) {
            LOGGER.error("IndexWriter can not delete a document from the lucene index", ex);
        }
    }
}