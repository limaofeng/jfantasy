package com.fantasy.framework.lucene.cluster;

import java.io.Serializable;

public abstract interface ClusterMessage extends Serializable {
	public static final int TYPE_INSERT = 1;
	public static final int TYPE_UPDATE = 2;
	public static final int TYPE_REMOVE = 3;
	public static final int TYPE_REF_BY = 4;

	public abstract void setType(int paramInt);

	public abstract int getType();
}