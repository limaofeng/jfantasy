package com.fantasy.framework.io;

import com.fantasy.framework.util.StringMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BufferCache {
    private final HashMap<Buffer, CachedBuffer> _bufferMap;
    private final StringMap<CachedBuffer> _stringMap;
    private final ArrayList<CachedBuffer> _index;

    public BufferCache() {
        this._bufferMap = new HashMap<Buffer, CachedBuffer>();
        this._stringMap = new StringMap<CachedBuffer>(true);
        this._index = new ArrayList<CachedBuffer>();
    }

    public CachedBuffer add(String value, int ordinal) {
        CachedBuffer buffer = new CachedBuffer(value, ordinal);
        this._bufferMap.put(buffer, buffer);
        this._stringMap.put(value, buffer);
        while (ordinal - this._index.size() >= 0)
            this._index.add(null);
        if (this._index.get(ordinal) == null)
            this._index.add(ordinal, buffer);
        return buffer;
    }

    public CachedBuffer get(int ordinal) {
        if ((ordinal < 0) || (ordinal >= this._index.size()))
            return null;
        return this._index.get(ordinal);
    }

    public CachedBuffer get(Buffer buffer) {
        return this._bufferMap.get(buffer);
    }

    public CachedBuffer get(String value) {
        return this._stringMap.get(value);
    }

    public Buffer lookup(Buffer buffer) {
        Buffer b = get(buffer);
        if (b == null) {
            if ((buffer instanceof Buffer.CaseInsensitve))
                return buffer;
            return new View.CaseInsensitive(buffer);
        }

        return b;
    }

    public CachedBuffer getBest(byte[] value, int offset, int maxLength) {
        Map.Entry<String, CachedBuffer> entry = this._stringMap.getBestEntry(value, offset, maxLength);
        if (entry != null)
            return entry.getValue();
        return null;
    }

    public Buffer lookup(String value) {
        Buffer b = get(value);
        if (b == null) {
            return new CachedBuffer(value, -1);
        }
        return b;
    }

    public String toString(Buffer buffer) {
        return lookup(buffer).toString();
    }

    public int getOrdinal(Buffer buffer) {
        if ((buffer instanceof CachedBuffer))
            return ((CachedBuffer) buffer).getOrdinal();
        buffer = lookup(buffer);
        if ((buffer != null) && ((buffer instanceof CachedBuffer)))
            return ((CachedBuffer) buffer).getOrdinal();
        return -1;
    }

    public String toString() {
        return "CACHE[bufferMap=" + this._bufferMap + ",stringMap=" + this._stringMap + ",index=" + this._index + "]";
    }

    public static class CachedBuffer extends ByteArrayBuffer.CaseInsensitive {
        private final int _ordinal;
        private HashMap<Object, CachedBuffer> _associateMap = null;

        public CachedBuffer(String value, int ordinal) {
            super(value);
            this._ordinal = ordinal;
        }

        public int getOrdinal() {
            return this._ordinal;
        }

        public CachedBuffer getAssociate(Object key) {
            if (this._associateMap == null)
                return null;
            return this._associateMap.get(key);
        }

        public void setAssociate(Object key, CachedBuffer associate) {
            if (this._associateMap == null)
                this._associateMap = new HashMap<Object, CachedBuffer>();
            this._associateMap.put(key, associate);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}