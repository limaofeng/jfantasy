package org.jfantasy.framework.lucene.backend;

import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.lucene.annotations.IndexRefBy;
import org.jfantasy.framework.lucene.cache.PropertysCache;
import org.jfantasy.framework.lucene.cluster.ClassIdMessage;
import org.jfantasy.framework.lucene.cluster.ClusterConfig;
import org.jfantasy.framework.lucene.cluster.EntityMessage;
import org.jfantasy.framework.lucene.cluster.MessageFactory;
import org.jfantasy.framework.util.reflect.Property;

import javax.persistence.Id;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EntityChangedListener {
    private Class<?> clazz;
    private boolean onlyIdRefBy;
    private RefEntityChangedListener refListener;

    private ClusterConfig cluster(){
        return BuguIndex.getInstance().getClusterConfig();
    }

    public EntityChangedListener(Class<?> clazz) {
        this.clazz = clazz;
        Set<Class<?>> refBySet = new HashSet<Class<?>>();
        boolean byId = false;
        boolean byOther = false;
        Property[] properties = PropertysCache.getInstance().get(clazz);
        for (Property p : properties) {
            IndexRefBy irb = p.getAnnotation(IndexRefBy.class);
            if (irb != null) {
                Class<?>[] cls = irb.value();
                refBySet.addAll(Arrays.asList(cls));
                if (p.getAnnotation(Id.class) != null) {
                    byId = true;
                } else {
                    byOther = true;
                }
            }
        }
        if (!refBySet.isEmpty()) {
            this.refListener = new RefEntityChangedListener(refBySet);
            if ((byId) && (!byOther)) {
                this.onlyIdRefBy = true;
            }
        }
    }

    public void entityInsert(Object entity) {
        IndexFilterChecker checker = new IndexFilterChecker(entity);
        if (checker.needIndex()) {
            if ((this.cluster() == null) || (this.cluster().isSelfNode())) {
                IndexInsertTask task = new IndexInsertTask(entity);
                BuguIndex.getInstance().getExecutor().execute(task);
            }
            if (this.cluster() != null) {
                EntityMessage message = MessageFactory.createInsertMessage(entity);
                this.cluster().sendMessage(message);
            }
        }
    }

    public void entityUpdate(Object entity) {
        String id = PropertysCache.getInstance().getIdProperty(clazz).getValue(entity).toString();
        IndexFilterChecker checker = new IndexFilterChecker(entity);
        if (checker.needIndex()) {
            if ((this.cluster() == null) || (this.cluster().isSelfNode())) {
                IndexUpdateTask task = new IndexUpdateTask(entity);
                BuguIndex.getInstance().getExecutor().execute(task);
            }
            if (this.cluster() != null) {
                EntityMessage message = MessageFactory.createUpdateMessage(entity);
                this.cluster().sendMessage(message);
            }
        } else {
            processRemove(id);
        }
        if ((this.refListener != null) && (!this.onlyIdRefBy)) {
            processRefBy(id);
        }
    }

    public void entityRemove(String id) {
        processRemove(id);
        if (this.refListener != null) {
            processRefBy(id);
        }
    }

    private void processRemove(String id) {
        if ((this.cluster() == null) || (this.cluster().isSelfNode())) {
            IndexRemoveTask task = new IndexRemoveTask(this.clazz, id);
            BuguIndex.getInstance().getExecutor().execute(task);
        }
        if (this.cluster() != null) {
            ClassIdMessage message = MessageFactory.createRemoveMessage(this.clazz, id);
            this.cluster().sendMessage(message);
        }
    }

    private void processRefBy(String id) {
        if ((this.cluster() == null) || (this.cluster().isSelfNode())) {
            this.refListener.entityChange(this.clazz, id);
        }
        if (this.cluster() != null) {
            ClassIdMessage message = MessageFactory.createRefByMessage(this.clazz, id);
            this.cluster().sendMessage(message);
        }
    }

    public RefEntityChangedListener getRefListener() {
        return this.refListener;
    }

}