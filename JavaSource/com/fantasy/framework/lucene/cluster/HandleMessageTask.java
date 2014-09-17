package com.fantasy.framework.lucene.cluster;

import com.fantasy.framework.lucene.BuguIndex;
import com.fantasy.framework.lucene.backend.EntityChangedListener;
import com.fantasy.framework.lucene.backend.IndexInsertTask;
import com.fantasy.framework.lucene.backend.IndexRemoveTask;
import com.fantasy.framework.lucene.backend.IndexUpdateTask;
import com.fantasy.framework.lucene.backend.RefEntityChangedListener;

public class HandleMessageTask implements Runnable {
	private ClusterMessage message;

	public HandleMessageTask(ClusterMessage message) {
		this.message = message;
	}

	public void run() {
		switch (this.message.getType()) {
		case 1:
			handleInsert();
			break;
		case 2:
			handleUpdate();
			break;
		case 3:
			handleRemove();
			break;
		case 4:
			handleRefBy();
			break;
		}
	}

	private void handleInsert() {
		EntityMessage msg = (EntityMessage) this.message;
		Object entity = msg.getEntity();
		if (entity != null) {
			IndexInsertTask task = new IndexInsertTask(entity);
			BuguIndex.getInstance().getExecutor().execute(task);
		}
	}

	private void handleUpdate() {
		EntityMessage msg = (EntityMessage) this.message;
		Object entity = msg.getEntity();
		if (entity != null) {
			IndexUpdateTask task = new IndexUpdateTask(entity);
			BuguIndex.getInstance().getExecutor().execute(task);
		}
	}

	private void handleRemove() {
		ClassIdMessage msg = (ClassIdMessage) this.message;
		Class<?> clazz = msg.getClazz();
		String id = msg.getId();
		if ((clazz != null) && (id != null)) {
			IndexRemoveTask task = new IndexRemoveTask(clazz, id);
			BuguIndex.getInstance().getExecutor().execute(task);
		}
	}

	private void handleRefBy() {
		ClassIdMessage msg = (ClassIdMessage) this.message;
		Class<?> clazz = msg.getClazz();
		String id = msg.getId();
		if ((clazz != null) && (id != null)) {
			//TODO 更新 InternalDao dao = DaoCache.getInstance().get(clazz);
			EntityChangedListener luceneListener = null;//dao.getLuceneListener();
			if (luceneListener != null) {
				RefEntityChangedListener refListener = luceneListener.getRefListener();
				if (refListener != null)
					refListener.entityChange(clazz, id);
			}
		}
	}
}