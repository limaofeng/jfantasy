package org.jfantasy.framework.hibernate.cache.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

class VicariousThreadLocal<T> extends ThreadLocal<T> {

    private static final ThreadLocal<WeakReference<Thread>> weakThread = new ThreadLocal<>();

    private static WeakReference<Thread> currentThreadRef() {
        WeakReference<Thread> ref = weakThread.get();
        if (ref == null) {
            ref = new WeakReference<>(Thread.currentThread());
            weakThread.set(ref);
        }
        return ref;
    }

    private static final Object UNINITIALISED = new Object();

    private final ThreadLocal<WeakReference<Holder>> local = new ThreadLocal<>();

    private volatile Holder strongRefs;

    private static final AtomicReferenceFieldUpdater<VicariousThreadLocal, Holder> strongRefsUpdater = AtomicReferenceFieldUpdater.newUpdater(VicariousThreadLocal.class, Holder.class, "strongRefs");

    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    VicariousThreadLocal() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        final Holder holder;
        WeakReference<Holder> ref = local.get();
        if (ref != null) {
            holder = ref.get();
            assert holder != null;
            Object value = holder.value;
            if (value != UNINITIALISED) {
                return (T) value;
            }
        } else {
            holder = createHolder();
        }
        T value = initialValue();
        holder.value = value;
        return value;
    }

    @Override
    public void set(T value) {
        WeakReference<Holder> ref = local.get();
        final Holder holder = ref != null ? ref.get() : createHolder();
        assert holder != null;
        holder.value = value;
    }

    private Holder createHolder() {
        poll();
        Holder holder = new Holder(queue);
        WeakReference<Holder> ref = new WeakReference<>(holder);

        Holder old;
        do {
            old = strongRefs;
            holder.next = old;
        } while (!strongRefsUpdater.compareAndSet(this, old, holder));

        local.set(ref);
        return holder;
    }

    @Override
    public void remove() {
        WeakReference<Holder> ref = local.get();
        if (ref != null) {
            Holder holder = ref.get();
            assert holder != null;
            holder.value = UNINITIALISED;
        }
    }

    /**
     * Check if any strong references need should be removed due to thread exit.
     */
    private void poll() {
        synchronized (queue) {
            // Remove queued references.
            // (Is this better inside or out?)
            if (queue.poll() == null) {
                // Nothing to do.
                return;
            }
            while (queue.poll() != null) {
                // Discard.
            }

            // Remove any dead holders.
            Holder first = strongRefs;
            if (first == null) {
                // Unlikely...
                return;
            }
            Holder link = first;
            Holder next = link.next;
            while (next != null) {
                if (next.get() == null) {
                    next = next.next;
                    link.next = next;
                } else {
                    link = next;
                    next = next.next;
                }
            }

            // Remove dead head, possibly.
            if (first.get() == null) {
                if (!strongRefsUpdater.weakCompareAndSet(
                        this, first, first.next
                )) {
                    // Something else has come along.
                    // Just null out - next search will remove it.
                    first.value = null;
                }
            }
        }
    }

    /**
     * Holds strong reference to a thread-local value.
     * The WeakReference is to a thread-local representing the current thread.
     */
    private static class Holder extends WeakReference<Object> {
        /**
         * Construct a new holder for the current thread.
         */
        Holder(ReferenceQueue<Object> queue) {
            super(currentThreadRef(), queue);
        }

        /**
         * Next holder in chain for this thread-local.
         */
        Holder next;
        /**
         * Current thread-local value.
         * {@link #UNINITIALISED} represents an uninitialised value.
         */
        Object value = UNINITIALISED;
    }
}
