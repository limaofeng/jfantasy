package org.jfantasy.framework.hibernate.cache.util;

final class TimeProviderLoader {

    private static SlewClock.TimeProvider timeProvider = new SlewClock.TimeProvider() {
        public final long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    };

    private TimeProviderLoader() {
    }

    static synchronized SlewClock.TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public static synchronized void setTimeProvider(final SlewClock.TimeProvider timeProvider) {
        TimeProviderLoader.timeProvider = timeProvider;
    }
}