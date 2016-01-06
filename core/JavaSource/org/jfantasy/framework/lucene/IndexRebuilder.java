package org.jfantasy.framework.lucene;

import org.jfantasy.framework.lucene.backend.IndexRebuildTask;

public class IndexRebuilder {
    private Class<?> clazz;
    private int batchSize = 100;

    public IndexRebuilder(Class<?> clazz) {
        this.clazz = clazz;
    }

    public IndexRebuilder(Class<?> clazz, int batchSize) {
        this.clazz = clazz;
        this.batchSize = batchSize;
    }

    public void rebuild() {
        IndexRebuildTask task = new IndexRebuildTask(this.clazz, this.batchSize);
        BuguIndex.getInstance().getExecutor().execute(task);
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
