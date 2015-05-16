package com.fantasy.framework.dao.hibernate.cache.redis.util;

final class TimeProviderLoader {

    private static SlewClock.TimeProvider timeProvider = new SlewClock.TimeProvider() {
        public final long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    };

    private TimeProviderLoader() {
    }

    public static synchronized SlewClock.TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public static synchronized void setTimeProvider(final SlewClock.TimeProvider timeProvider) {
        TimeProviderLoader.timeProvider = timeProvider;
    }
}