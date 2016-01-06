package org.jfantasy.framework.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Buffer extends Cloneable {
    public static final int IMMUTABLE = 0;
    public static final int READONLY = 1;
    public static final int READWRITE = 2;
    public static final boolean VOLATILE = true;
    public static final boolean NON_VOLATILE = false;

    public abstract byte[] array();

    public abstract byte[] asArray();

    public abstract Buffer buffer();

    public abstract Buffer asNonVolatileBuffer();

    public abstract Buffer asReadOnlyBuffer();

    public abstract Buffer asImmutableBuffer();

    public abstract Buffer asMutableBuffer();

    public abstract int capacity();

    public abstract int space();

    public abstract void clear();

    public abstract void compact();

    public abstract byte get();

    public abstract int get(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    public abstract Buffer get(int paramInt);

    public abstract int getIndex();

    public abstract boolean hasContent();

    public abstract boolean equalsIgnoreCase(Buffer paramBuffer);

    public abstract boolean isImmutable();

    public abstract boolean isReadOnly();

    public abstract boolean isVolatile();

    public abstract int length();

    public abstract void mark();

    public abstract void mark(int paramInt);

    public abstract int markIndex();

    public abstract byte peek();

    public abstract byte peek(int paramInt);

    public abstract Buffer peek(int paramInt1, int paramInt2);

    public abstract int peek(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    public abstract int poke(int paramInt, Buffer paramBuffer);

    public abstract void poke(int paramInt, byte paramByte);

    public abstract int poke(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    public abstract int put(Buffer paramBuffer);

    public abstract void put(byte paramByte);

    public abstract int put(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    public abstract int put(byte[] paramArrayOfByte);

    public abstract int putIndex();

    public abstract void reset();

    public abstract void setGetIndex(int paramInt);

    public abstract void setMarkIndex(int paramInt);

    public abstract void setPutIndex(int paramInt);

    public abstract int skip(int paramInt);

    public abstract Buffer slice();

    public abstract Buffer sliceFromMark();

    public abstract Buffer sliceFromMark(int paramInt);

    public abstract String toDetailString();

    public abstract void writeTo(OutputStream paramOutputStream) throws IOException;

    public abstract int readFrom(InputStream paramInputStream, int paramInt) throws IOException;

    public abstract String toString(String paramString);

    public static abstract interface CaseInsensitve {

    }
}