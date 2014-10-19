package com.fantasy.framework.cache;

import java.util.concurrent.locks.ReadWriteLock;

public abstract interface Cache {

	public abstract String getId();

	public abstract int getSize();

	public abstract void putObject(Object key, Object value);

	public abstract Object getObject(Object key);

	public abstract Object removeObject(Object key);

	public abstract void clear();

	public abstract ReadWriteLock getReadWriteLock();

}