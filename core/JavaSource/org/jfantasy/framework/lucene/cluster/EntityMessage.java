package org.jfantasy.framework.lucene.cluster;


public class EntityMessage implements ClusterMessage {
    private static final long serialVersionUID = 1L;
    private int type;
    private Object entity;

    public Object getEntity() {
        return this.entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
