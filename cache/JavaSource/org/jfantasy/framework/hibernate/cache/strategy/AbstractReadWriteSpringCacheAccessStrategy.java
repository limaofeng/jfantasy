package org.jfantasy.framework.hibernate.cache.strategy;


import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionImplementor;
import org.jboss.logging.Logger;
import org.jfantasy.framework.hibernate.cache.log.SpringCacheMessageLogger;
import org.jfantasy.framework.hibernate.cache.regions.SpringCacheTransactionalDataRegion;

import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

abstract class AbstractReadWriteSpringCacheAccessStrategy<T extends SpringCacheTransactionalDataRegion> extends AbstractSpringCacheAccessStrategy<T> {

    private static final SpringCacheMessageLogger LOG = Logger.getMessageLogger(SpringCacheMessageLogger.class, AbstractReadWriteSpringCacheAccessStrategy.class.getName());

    private final UUID uuid = UUID.randomUUID();
    private final AtomicLong nextLockId = new AtomicLong();

    private final Comparator versionComparator;

    public AbstractReadWriteSpringCacheAccessStrategy(T region, SessionFactoryOptions settings) {
        super(region, settings);
        this.versionComparator = region.getCacheDataDescription().getVersionComparator();
    }

    public final Object get(SessionImplementor session, Object key, long txTimestamp) throws CacheException {
        readLockIfNeeded(key);
        try {
            final Lockable item = (Lockable) region().get(key);
            final boolean readable = item != null && item.isReadable(txTimestamp);
            if (readable) {
                return item.getValue();
            } else {
                return null;
            }
        } finally {
            readUnlockIfNeeded(key);
        }
    }

    @Override
    public final boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        region().writeLock(key);
        try {
            final Lockable item = (Lockable) region().get(key);
            final boolean writeable = item == null || item.isWriteable(txTimestamp, version, versionComparator);
            if (writeable) {
                region().put(key, new Item(value, version, region().nextTimestamp()));
                return true;
            } else {
                return false;
            }
        } finally {
            region().writeUnlock(key);
        }
    }

    public final SoftLock lockItem(SessionImplementor session, Object key, Object version) throws CacheException {
        region().writeLock(key);
        try {
            final Lockable item = (Lockable) region().get(key);
            final long timeout = region().nextTimestamp() + region().getTimeout();
            final Lock lock = (item == null) ? new Lock(timeout, uuid, nextLockId(), version) : item.lock(timeout, uuid, nextLockId());
            region().put(key, lock);
            return lock;
        } finally {
            region().writeUnlock(key);
        }
    }

    public final void unlockItem(SessionImplementor session, Object key, SoftLock lock) throws CacheException {
        region().writeLock(key);
        try {
            final Lockable item = (Lockable) region().get(key);
            if ((item != null) && item.isUnlockable(lock)) {
                decrementLock(key, (Lock) item);
            } else {
                handleLockExpiry(key, item);
            }
        } finally {
            region().writeUnlock(key);
        }
    }

    private long nextLockId() {
        return nextLockId.getAndIncrement();
    }

    protected void decrementLock(Object key, Lock lock) {
        lock.unlock(region().nextTimestamp());
        region().put(key, lock);
    }

    protected void handleLockExpiry(Object key, Lockable lock) {
        LOG.softLockedCacheExpired(region().getName(), key, lock == null ? "(null)" : lock.toString());
        final long ts = region().nextTimestamp() + region().getTimeout();
        final Lock newLock = new Lock(ts, uuid, nextLockId.getAndIncrement(), null);
        newLock.unlock(ts);
        region().put(key, newLock);
    }

    private void readLockIfNeeded(Object key) {
        if (region().locksAreIndependentOfCache()) {
            region().readLock(key);
        }
    }

    private void readUnlockIfNeeded(Object key) {
        if (region().locksAreIndependentOfCache()) {
            region().readUnlock(key);
        }
    }

    protected interface Lockable {

        boolean isReadable(long txTimestamp);

        boolean isWriteable(long txTimestamp, Object version, Comparator versionComparator);

        Object getValue();

        boolean isUnlockable(SoftLock lock);

        Lock lock(long timeout, UUID uuid, long lockId);
    }

    protected static final class Item implements Serializable, Lockable {
        private static final long serialVersionUID = 1L;
        private final Object value;
        private final Object version;
        private final long timestamp;

        Item(Object value, Object version, long timestamp) {
            this.value = value;
            this.version = version;
            this.timestamp = timestamp;
        }

        @Override
        public boolean isReadable(long txTimestamp) {
            return txTimestamp > timestamp;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean isWriteable(long txTimestamp, Object newVersion, Comparator versionComparator) {
            return version != null && versionComparator.compare(version, newVersion) < 0;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public boolean isUnlockable(SoftLock lock) {
            return false;
        }

        @Override
        public Lock lock(long timeout, UUID uuid, long lockId) {
            return new Lock(timeout, uuid, lockId, version);
        }
    }

    protected static final class Lock implements Serializable, Lockable, SoftLock {
        private static final long serialVersionUID = 2L;

        private final UUID sourceUuid;
        private final long lockId;
        private final Object version;

        private long timeout;
        private boolean concurrent;
        private int multiplicity = 1;
        private long unlockTimestamp;

        Lock(long timeout, UUID sourceUuid, long lockId, Object version) {
            this.timeout = timeout;
            this.lockId = lockId;
            this.version = version;
            this.sourceUuid = sourceUuid;
        }

        @Override
        public boolean isReadable(long txTimestamp) {
            return false;
        }

        @Override
        @SuppressWarnings({"SimplifiableIfStatement", "unchecked"})
        public boolean isWriteable(long txTimestamp, Object newVersion, Comparator versionComparator) {
            if (txTimestamp > timeout) {
                return true;
            }
            if (multiplicity > 0) {
                return false;
            }
            return version == null
                    ? txTimestamp > unlockTimestamp
                    : versionComparator.compare(version, newVersion) < 0;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean isUnlockable(SoftLock lock) {
            return equals(lock);
        }

        @Override
        @SuppressWarnings("SimplifiableIfStatement")
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Lock) {
                return (lockId == ((Lock) o).lockId) && sourceUuid.equals(((Lock) o).sourceUuid);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            final int hash = (sourceUuid != null ? sourceUuid.hashCode() : 0);
            int temp = (int) lockId;
            for (int i = 1; i < Long.SIZE / Integer.SIZE; i++) {
                temp ^= (lockId >>> (i * Integer.SIZE));
            }
            return hash + temp;
        }

        /**
         * Returns true if this Lock has been concurrently locked by more than one transaction.
         */
        public boolean wasLockedConcurrently() {
            return concurrent;
        }

        @Override
        public Lock lock(long timeout, UUID uuid, long lockId) {
            concurrent = true;
            multiplicity++;
            this.timeout = timeout;
            return this;
        }

        public void unlock(long timestamp) {
            if (--multiplicity == 0) {
                unlockTimestamp = timestamp;
            }
        }

        @Override
        public String toString() {
            return "Lock Source-UUID:" + sourceUuid + " Lock-ID:" + lockId;
        }
    }

}
