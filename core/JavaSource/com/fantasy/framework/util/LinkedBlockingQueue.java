package com.fantasy.framework.util;

import com.fantasy.framework.error.IgnoreException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全的队列实现
 *
 * @param <E>
 * @author 李茂峰
 * @version 1.0
 * @since 2012-12-4 下午02:36:30
 */
public class LinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6642633435807467698L;
    /**
     * 容量
     */
    private final int capacity;
    /**
     * 计数器
     */
    private final AtomicInteger count = new AtomicInteger(0);
    /**
     * 链表头元素
     */
    private transient Node<E> head;
    /**
     * 链表尾元素
     */
    private transient Node<E> last;
    /**
     * 输出锁
     */
    private final ReentrantLock takeLock = new ReentrantLock();
    private final Condition notEmpty = this.takeLock.newCondition();
    /**
     * 输入锁
     */
    private final ReentrantLock putLock = new ReentrantLock();
    private final Condition notFull = this.putLock.newCondition();

    /**
     * 提供List接口的访问方式
     */
    private List<E> list = new List<E>() {
        public boolean add(E o) {
            return LinkedBlockingQueue.this.add(o);
        }

        public void add(int index, E element) {
            LinkedBlockingQueue.this.add(index, element);
        }

        public boolean addAll(Collection<? extends E> c) {
            return LinkedBlockingQueue.this.addAll(c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            throw new IgnoreException("null method");
        }

        public void clear() {
            LinkedBlockingQueue.this.clear();
        }

        public boolean contains(Object o) {
            return LinkedBlockingQueue.this.contains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return LinkedBlockingQueue.this.containsAll(c);
        }

        public E get(int index) {
            return LinkedBlockingQueue.this.get(index);
        }

        public boolean isEmpty() {
            return LinkedBlockingQueue.this.isEmpty();
        }

        public Iterator<E> iterator() {
            return LinkedBlockingQueue.this.iterator();
        }

        public int lastIndexOf(Object o) {
            throw new IgnoreException("null method");
        }

        public ListIterator<E> listIterator() {
            throw new IgnoreException("null method");
        }

        public ListIterator<E> listIterator(int index) {
            throw new IgnoreException("null method");
        }

        public boolean remove(Object o) {
            return LinkedBlockingQueue.this.remove(o);
        }

        public E remove(int index) {
            return LinkedBlockingQueue.this.remove(index);
        }

        public boolean removeAll(Collection<?> c) {
            return LinkedBlockingQueue.this.removeAll(c);
        }

        @SuppressWarnings("unchecked")
        public int indexOf(Object o) {
            return LinkedBlockingQueue.this.indexOf((E) o);
        }

        public boolean retainAll(Collection<?> c) {
            return LinkedBlockingQueue.this.retainAll(c);
        }

        public E set(int index, E element) {
            return LinkedBlockingQueue.this.set(index, element);
        }

        public int size() {
            return LinkedBlockingQueue.this.size();
        }

        public List<E> subList(int fromIndex, int toIndex) {
            throw new IgnoreException("null method");
        }

        public Object[] toArray() {
            return LinkedBlockingQueue.this.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return LinkedBlockingQueue.this.toArray(a);
        }
    };

    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingQueue(int capacity) {
        if (capacity <= 0){
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.last = (this.head = new Node<E>());// 初始化链表
    }

    public LinkedBlockingQueue(Collection<? extends E> c) {
        this();
        for (E e : c){
            add(e);
        }

    }

    /**
     * 唤醒等待的线程(输出)
     *
     * 输出线程可以开始取值了.
     */
    private void signalNotEmpty() {
        ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            this.notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * 唤醒等待的线程(输入)
     *
     * @功能描述 <br/>
     * 输入线程可以开始存值了.
     */
    private void signalNotFull() {
        ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            this.notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * 向链表的末尾追加元素
     *
     * @param x E
     */
    private void insert(E x) {
        this.last = (this.last.next = new Node<E>(x, null, this.last));
        this.head.previous = this.last;// 让链表首尾相连 --> 链表头会链接链表尾 但链表尾并没有链接到链表头
    }

    /**
     * 从链表的头取出元素
     *
     * @return E
     */
    private E extract() {
        Node<E> first = this.head.next;
        this.head = first;
        this.head.previous = this.last;
        E x = first.item;
        first.item = null;
        return x;
    }

    /**
     * 同时获取输入及输出锁
     *
     * @功能描述
     */
    public void fullyLock() {
        this.putLock.lock();
        this.takeLock.lock();
    }

    /**
     * 同时释放输入及输出锁
     *
     * @功能描述
     */
    public void fullyUnlock() {
        this.takeLock.unlock();
        this.putLock.unlock();
    }

    /**
     * 获取链表长度
     */
    public int size() {
        return this.count.get();
    }

    /**
     * 获取链表的可用长度
     */
    public int remainingCapacity() {
        return this.capacity - this.count.get();
    }

    /**
     * 向链表添加元素
     * <p/>
     * 如果链表容量已满，会持续等待
     *
     * @param o E
     * @throws InterruptedException
     */
    public void put(E o) throws InterruptedException {
        if (o == null){
            throw new NullPointerException();
        }
        int c = -1;
        ReentrantLock putLock = this.putLock;
        AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            try {
                // 如果链表已满,等待释放
                while (count.get() == this.capacity){
                    this.notFull.await();
                }
            } catch (InterruptedException ie) {
                this.notFull.signal();
                throw ie;
            }
            insert(o);// 添加
            c = count.getAndIncrement();// 获取当前链表数量
            if (c + 1 < this.capacity){
                this.notFull.signal();
            // 判断是否可以继续输入
            }

        } finally {
            putLock.unlock();
            if (c == 0){
                signalNotEmpty();// 通知输出锁，如果有元素正在等待输出。立即激活该线程
            }

        }
    }

    /**
     * 添加元素
     *
     * @param o       要添加的元素
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return boolean
     * @throws InterruptedException
     * @功能描述 <br/>
     * 可设置操作的超时时间
     */
    public boolean offer(E o, long timeout, TimeUnit unit) throws InterruptedException {
        if (o == null){
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);// 将超时时间转为 毫微秒
        int c = -1;
        ReentrantLock putLock = this.putLock;
        AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            try {
                while (count.get() == this.capacity) {// 如果容量已满。
                    if (nanos <= 0){
                        return false;  // 等待时间耗尽，返回false
                    }
                    nanos = this.notFull.awaitNanos(nanos);// 挂起线程
                }
            } catch (InterruptedException ie) {
                this.notFull.signal();
                throw ie;
            }
            insert(o);
            c = count.getAndIncrement();
            if (c + 1 < this.capacity){
                this.notFull.signal();
            }
            return c >= 0;
        } finally {
            putLock.unlock();
            if (c == 0){
                signalNotEmpty();
            }
        }
    }

    /**
     * 添加元素
     *
     * @param o E
     * @return bookean
     * @功能描述 <br/>
     * 如果容量超出，则直接返回false
     */
    public boolean offer(E o) {
        if (o == null){
            throw new NullPointerException();
        }
        AtomicInteger count = this.count;
        if (count.get() == this.capacity){
            return false;
        }
        int c = -1;
        ReentrantLock putLock = this.putLock;
        putLock.lock();// TODO lock 与 lockInterruptibly 的区别
        try {
            if (count.get() < this.capacity) {
                insert(o);
                c = count.getAndIncrement();
                if (c + 1 < this.capacity){
                    this.notFull.signal();
                }
            }
        } finally {
            putLock.unlock();
            if (c > 0){
                signalNotEmpty();
            }
        }
        return c >= 0;
    }

    /**
     * 获取队列的元素
     *
     * @return E
     * @throws InterruptedException
     */
    public E take() throws InterruptedException {
        int c = -1;
        AtomicInteger count = this.count;
        ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        E x;
        try {
            try {
                // 如果容量为0,持续等待获取元素
                while (count.get() == 0){
                    this.notEmpty.await();
                }
            } catch (InterruptedException ie) {
                this.notEmpty.signal();
                throw ie;
            }
            x = extract();// 获取元素
            c = count.getAndDecrement();// 计数器减1
            if (c > 1){
                this.notEmpty.signal();// 判断是否可以继续输出
            }
        } finally {
            takeLock.unlock();
            if (c == this.capacity){
                signalNotFull(); // 如果有线程在等待输入的话，激活线程。因为容量满额时，可能有线程正在等待输入
            }
        }
        return x;
    }

    /**
     * 获取队列元素，取出
     *
     * @param timeout 操作超时时间
     * @param unit    时间单位
     * @return 队列为空返回 null
     * @throws InterruptedException
     */
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        int c = -1;
        long nanos = unit.toNanos(timeout);
        AtomicInteger count = this.count;
        ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        E x = null;
        try {
            try {
                while (count.get() == 0) {
                    if (nanos <= 0){
                        return null;
                    }
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } catch (InterruptedException ie) {
                this.notEmpty.signal();
                throw ie;
            }
            x = extract();// 获取元素
            c = count.getAndDecrement();// 计数器减1
            if (c > 1){
                this.notEmpty.signal();     // 判断是否可以继续输出
            }
        } finally {
            takeLock.unlock();
            if (c == this.capacity){
                signalNotFull();
            }
        }
        return x;
    }

    /**
     * 获取队列元素，取出
     *
     * @return 队列为空返回 null
     */
    public E poll() {
        AtomicInteger count = this.count;
        if (count.get() == 0){
            return null;
        }
        E x = null;
        int c = -1;
        ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            if (count.get() > 0) {
                x = extract();
                c = count.getAndDecrement();
                if (c <= 1){
                    this.notEmpty.signal();
                }
            }
        } finally {
            takeLock.unlock();
        }
        if (c == this.capacity){
            signalNotFull();
        }
        return x;
    }

    /**
     * 获取队列元素，不取出。
     * <p/>
     * 队列为空返回 null
     *
     * @return E
     */
    public E peek() {
        if (this.count.get() == 0){
            return null;
        }
        ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Node<E> first = this.head.next;
            if (first == null) {
                return null;
            }
            return first.item;
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * 转换为数组
     *
     * @return Object[]
     */
    public Object[] toArray() {
        fullyLock();
        try {
            int size = this.count.get();
            Object[] a = new Object[size];
            int k = 0;
            for (Node<E> p = this.head.next; p != null; p = p.next){
                a[(k++)] = p.item;
            }
            return a;
        } finally {
            fullyUnlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        fullyLock();
        try {
            int size = this.count.get();
            if (a.length < size) {
                a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            }
            int k = 0;
            for (Node<E> p = this.head.next; p != null; p = p.next){
                a[(k++)] = (T) p.item;
            }
            return a;
        } finally {
            fullyUnlock();
        }
    }

    @Override
    public String toString() {
        fullyLock();
        try {
            return super.toString();
        } finally {
            fullyUnlock();
        }
    }

    /**
     * 清空队列
     *
     * @功能描述
     */
    public void clear() {
        fullyLock();
        try {
            this.head.next = null;
            assert (this.head.item == null);
            this.last = this.head;
            if (this.count.getAndSet(0) == this.capacity){
                this.notFull.signalAll();
            }
        } finally {
            fullyUnlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        if (c == null){
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        fullyLock();
        Node<E> first;
        try {
            first = this.head.next;
            this.head.next = null;
            assert (this.head.item == null);
            this.last = this.head;
            if (this.count.getAndSet(0) == this.capacity){
                this.notFull.signalAll();
            }

        } finally {
            fullyUnlock();
        }
        int n = 0;
        for (Node<E> p = first; p != null; p = p.next) {
            c.add(p.item);
            p.item = null;
            n++;
        }
        return n;
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null){
            throw new NullPointerException();
        }
        if (c == this){
            throw new IllegalArgumentException();
        }
        fullyLock();
        try {
            int n = 0;
            Node<E> p = this.head.next;
            while ((p != null) && (n < maxElements)) {
                c.add(p.item);
                p.item = null;
                p = p.next;
                n++;
            }
            if (n != 0) {
                this.head.next = p;
                assert (this.head.item == null);
                if (p == null){
                    this.last = this.head;
                }
                if (this.count.getAndAdd(-n) == this.capacity){
                    this.notFull.signalAll();
                }
            }
            return n;
        } finally {
            fullyUnlock();
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        fullyLock();
        try {
            s.defaultWriteObject();
            for (Node<E> p = this.head.next; p != null; p = p.next){
                s.writeObject(p.item);
            }
            s.writeObject(null);
        } finally {
            fullyUnlock();
        }
    }

    /**
     * jdk 中 为什么会有这个方法
     *
     * @param s ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.count.set(0);
        this.last = (this.head = new Node<E>());
        while (true) {
            E item = (E) s.readObject();
            if (item == null){
                break;
            }
            add(item);
        }
    }

    /**
     * 为List接口提供的方法
     * <p/>
     * {@link #list 的 get(int) 方法}
     *
     * @param index 下标
     * @return E
     */
    protected E get(int index) {
        return entry(index).item;
    }

    /**
     * 获取{index}对应的元素
     * <p/>
     * 不移除元素
     *
     * @param index 下标
     * @return Node<E>
     */
    private Node<E> entry(int index) {
        int size = this.count.get();
        if ((index < 0) || (index >= size)){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> e = this.head;
        if (index < size >> 1){
            for (int i = 0; i <= index; i++){
                e = e.next;
            }
        } else {
            for (int i = size; i > index; i--){
                e = e.previous;
            }
        }
        return e;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * 添加元素
     *
     * @param index 下标
     * @param o     E
     * @return boolean
     */
    public boolean add(int index, E o) {
        if (o == null){
            throw new NullPointerException();
        }
        AtomicInteger count = this.count;
        if (count.get() == this.capacity){
            return false;
        }
        int c = -1;
        ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            if (count.get() < this.capacity) {
                if (index >= size()) {
                    insert(o);
                } else {
                    Node<E> p = entry(index);
                    p.previous.next = (p.previous = new Node<E>(o, p, p.previous));
                }
                c = count.getAndIncrement();
                if (c + 1 < this.capacity){
                    this.notFull.signal();
                }
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0){
            signalNotEmpty();
        }
        return c >= 0;
    }

    protected int indexOf(E o) {
        fullyLock();
        try {
            int index = 0;
            for (Node<E> e = this.head.next; e != this.head; e = e.next) {
                if (e.item.equals(o)){
                    return index;
                }
                index++;
            }
            return -1;
        } finally {
            fullyUnlock();
        }
    }

    protected E set(int index, E element) {
        fullyLock();
        try {
            Node<E> e = entry(index);
            E oldVal = e.item;
            e.item = element;
            return oldVal;
        } finally {
            fullyUnlock();
        }
    }

    /**
     * 删除元素
     *
     * @param index 下标
     * @return E
     */
    protected E remove(int index) {
        fullyLock();
        try {
            Node<E> e = entry(index);
            Node<E> p = e.previous;
            E item = e.item;
            e.item = null;
            p.next = e.next;// 上个节点的下一个指向当前节点的下一个节点
            e.next.previous = p;// 当前节点的下一节点的上节点指向当前节点的上一个节点
            if (this.count.getAndDecrement() != this.capacity){
                this.notFull.signalAll();
            }
            return item;
        } finally {
            fullyUnlock();
        }
    }

    /**
     * 移除元素
     *
     * @param o 对象
     * @return boolean
     */
    @Override
    public boolean remove(Object o) {
        if (o == null){
            return false;
        }
        boolean removed = false;
        fullyLock();
        try {
            Node<E> trail = this.head;
            Node<E> p = this.head.next;
            while (p != null) {
                if (o.equals(p.item)) {
                    removed = true;
                    break;
                }
                trail = p;
                p = p.next;
            }
            if (removed) {
                p.item = null;
                trail.next = p.next;
                p.next.previous = trail;
                if (this.count.getAndDecrement() != this.capacity){
                    this.notFull.signalAll();
                }
            }
        } finally {
            fullyUnlock();
        }
        return removed;
    }

    public List<E> list() {
        return this.list;
    }

    private class Itr implements Iterator<E> {
        private LinkedBlockingQueue.Node<E> current;
        private LinkedBlockingQueue.Node<E> lastRet;
        private E currentElement;

        Itr() {
            ReentrantLock putLock = LinkedBlockingQueue.this.putLock;
            ReentrantLock takeLock = LinkedBlockingQueue.this.takeLock;
            putLock.lock();
            takeLock.lock();
            try {
                this.current = LinkedBlockingQueue.this.head.next;
                if (this.current != null){
                    this.currentElement = this.current.item;
                }
            } finally {
                takeLock.unlock();
                putLock.unlock();
            }
        }

        public boolean hasNext() {
            return this.current != null;
        }

        public E next() {
            ReentrantLock putLock = LinkedBlockingQueue.this.putLock;
            ReentrantLock takeLock = LinkedBlockingQueue.this.takeLock;
            putLock.lock();
            takeLock.lock();
            try {
                if (this.current == null){
                    throw new NoSuchElementException();
                }
                E x = this.currentElement;
                this.lastRet = this.current;
                this.current = this.current.next;
                if (this.current != null){
                    this.currentElement = this.current.item;
                }
                return x;
            } finally {
                takeLock.unlock();
                putLock.unlock();
            }
        }

        public void remove() {
            if (this.lastRet == null){
                throw new IllegalStateException();
            }
            ReentrantLock putLock = LinkedBlockingQueue.this.putLock;
            ReentrantLock takeLock = LinkedBlockingQueue.this.takeLock;
            putLock.lock();
            takeLock.lock();
            try {
                LinkedBlockingQueue.Node<E> node = this.lastRet;
                this.lastRet = null;
                LinkedBlockingQueue.Node<E> trail = LinkedBlockingQueue.this.head;
                LinkedBlockingQueue.Node<E> p = LinkedBlockingQueue.this.head.next;
                while ((p != null) && (p != node)) {
                    trail = p;
                    p = p.next;
                }
                if (p == node) {
                    assert p != null;
                    p.item = null;
                    trail.next = p.next;
                    int c = LinkedBlockingQueue.this.count.getAndDecrement();
                    if (c == LinkedBlockingQueue.this.capacity){
                        LinkedBlockingQueue.this.notFull.signalAll();
                    }
                }
            } finally {
                takeLock.unlock();
                putLock.unlock();
            }
        }
    }

    /**
     * 链表节点类
     *
     * @param <E>
     * @author 李茂峰
     * @version 1.0
     * @since 2012-12-4 下午02:14:43
     */
    static class Node<E> {
        /**
         * 当前节点对于的元素
         */
        volatile E item;
        /**
         * 下一个节点
         */
        Node<E> next;
        /**
         * 上一个节点
         */
        Node<E> previous;

        Node() {
        }

        Node(E item, Node<E> next, Node<E> previous) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }

    }
}