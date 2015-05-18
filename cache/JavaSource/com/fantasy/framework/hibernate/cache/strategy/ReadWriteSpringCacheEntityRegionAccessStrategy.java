package com.fantasy.framework.hibernate.cache.strategy;

import com.fantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;


public class ReadWriteSpringCacheEntityRegionAccessStrategy extends AbstractReadWriteSpringCacheAccessStrategy<SpringCacheEntityRegion> implements EntityRegionAccessStrategy {

    public ReadWriteSpringCacheEntityRegionAccessStrategy(SpringCacheEntityRegion region, Settings settings) {
        super( region, settings );
    }

    @Override
    public EntityRegion getRegion() {
        return region();
    }

    @Override
    public boolean insert(Object key, Object value, Object version) throws CacheException {
        return false;
    }

    @Override
    public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
        region().writeLock( key );
        try {
            final AbstractReadWriteSpringCacheAccessStrategy.Lockable item = (AbstractReadWriteSpringCacheAccessStrategy.Lockable) region().get( key );
            if ( item == null ) {
                region().put( key, new AbstractReadWriteSpringCacheAccessStrategy.Item( value, version, region().nextTimestamp() ) );
                return true;
            }
            else {
                return false;
            }
        }
        finally {
            region().writeUnlock( key );
        }
    }

    @Override
    public boolean update(Object key, Object value, Object currentVersion, Object previousVersion)
            throws CacheException {
        return false;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock)
            throws CacheException {
        //what should we do with previousVersion here?
        region().writeLock( key );
        try {
            final AbstractReadWriteSpringCacheAccessStrategy.Lockable item = (AbstractReadWriteSpringCacheAccessStrategy.Lockable) region().get( key );

            if ( item != null && item.isUnlockable( lock ) ) {
                final AbstractReadWriteSpringCacheAccessStrategy.Lock lockItem = (AbstractReadWriteSpringCacheAccessStrategy.Lock) item;
                if ( lockItem.wasLockedConcurrently() ) {
                    decrementLock( key, lockItem );
                    return false;
                }
                else {
                    region().put( key, new AbstractReadWriteSpringCacheAccessStrategy.Item( value, currentVersion, region().nextTimestamp() ) );
                    return true;
                }
            }
            else {
                handleLockExpiry( key, item );
                return false;
            }
        }
        finally {
            region().writeUnlock( key );
        }
    }

}
