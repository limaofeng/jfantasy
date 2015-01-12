package com.fantasy.framework.lucene.cache;

import com.fantasy.framework.lucene.BuguIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IndexWriterCache {
	private static final Log logger = LogFactory.getLog(IndexWriterCache.class);

	private static IndexWriterCache instance = new IndexWriterCache();
	private Map<String, IndexWriter> cache;

	private IndexWriterCache() {
		this.cache = new ConcurrentHashMap<String, IndexWriter>();
	}

	public static IndexWriterCache getInstance() {
		return instance;
	}

	public IndexWriter get(String name) {
		IndexWriter writer = null;
		if (this.cache.containsKey(name)){
            writer = (IndexWriter) this.cache.get(name);
        }else {
            synchronized (this) {
                if (this.cache.containsKey(name)) {
                    writer = (IndexWriter) this.cache.get(name);
                } else {
                    BuguIndex index = BuguIndex.getInstance();
                    IndexWriterConfig cfg = new IndexWriterConfig(index.getVersion(), index.getAnalyzer());
                    cfg.setRAMBufferSizeMB(index.getBufferSizeMB());
                    try {
                        Directory dir = FSDirectory.open(BuguIndex.getInstance().getOpenFolder("/" + name));// new File(path + "/" + name);
                        if (IndexWriter.isLocked(dir)) {
                            IndexWriter.unlock(dir);
                        }
                        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                        writer = new IndexWriter(dir, cfg);
                    } catch (IOException ex) {
                        logger.error("Something is wrong when create IndexWriter for " + name, ex);
                    }
                    this.cache.put(name, writer);
                }
            }
        }
		return writer;
	}

	public Map<String, IndexWriter> getAll() {
		return this.cache;
	}

}