package org.jfantasy.framework.util.lock.backup;


public interface LockListener {

    void lockAcquired();

    void lockError();

    long getExpire();
}
