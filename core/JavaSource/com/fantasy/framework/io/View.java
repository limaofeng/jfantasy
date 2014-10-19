
package com.fantasy.framework.io;

public class View extends AbstractBuffer {
    Buffer _buffer;

    public View(Buffer buffer, int mark, int get, int put, int access) {
        super(2, !buffer.isImmutable());
        this._buffer = buffer.buffer();
        setPutIndex(put);
        setGetIndex(get);
        setMarkIndex(mark);
        this._access = access;
    }

    public View(Buffer buffer) {
        super(2, !buffer.isImmutable());
        this._buffer = buffer.buffer();
        setPutIndex(buffer.putIndex());
        setGetIndex(buffer.getIndex());
        setMarkIndex(buffer.markIndex());
        this._access = (buffer.isReadOnly() ? 1 : 2);
    }

    public View() {
        super(2, true);
    }

    public void update(Buffer buffer) {
        this._access = 2;
        this._buffer = buffer.buffer();
        setGetIndex(0);
        setPutIndex(buffer.putIndex());
        setGetIndex(buffer.getIndex());
        setMarkIndex(buffer.markIndex());
        this._access = (buffer.isReadOnly() ? 1 : 2);
    }

    public void update(int get, int put) {
        int a = this._access;
        this._access = 2;
        setGetIndex(0);
        setPutIndex(put);
        setGetIndex(get);
        setMarkIndex(-1);
        this._access = a;
    }

    public byte[] array() {
        return this._buffer.array();
    }

    public Buffer buffer() {
        return this._buffer.buffer();
    }

    public int capacity() {
        return this._buffer.capacity();
    }

    public void clear() {
        setMarkIndex(-1);
        setGetIndex(0);
        setPutIndex(this._buffer.getIndex());
        setGetIndex(this._buffer.getIndex());
    }

    public void compact() {
    }

    public boolean equals(Object obj) {
        return (this == obj) || (((obj instanceof Buffer)) && (obj.equals(this))) || (super.equals(obj));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isReadOnly() {
        return this._buffer.isReadOnly();
    }

    public boolean isVolatile() {
        return true;
    }

    public byte peek(int index) {
        return this._buffer.peek(index);
    }

    public int peek(int index, byte[] b, int offset, int length) {
        return this._buffer.peek(index, b, offset, length);
    }

    public Buffer peek(int index, int length) {
        return this._buffer.peek(index, length);
    }

    public int poke(int index, Buffer src) {
        return this._buffer.poke(index, src);
    }

    public void poke(int index, byte b) {
        this._buffer.poke(index, b);
    }

    public int poke(int index, byte[] b, int offset, int length) {
        return this._buffer.poke(index, b, offset, length);
    }

    public String toString() {
        if (this._buffer == null)
            return "INVALID";
        return super.toString();
    }

    public static class CaseInsensitive extends View implements Buffer.CaseInsensitve {
        public CaseInsensitive() {
        }

        public CaseInsensitive(Buffer buffer, int mark, int get, int put, int access) {
            super(buffer, mark, get, put, access);
        }

        public CaseInsensitive(Buffer buffer) {
            super(buffer);
        }

        public boolean equals(Object obj) {
            return (this == obj) || (((obj instanceof Buffer)) && (((Buffer) obj).equalsIgnoreCase(this))) || (super.equals(obj));
        }
    }
}
