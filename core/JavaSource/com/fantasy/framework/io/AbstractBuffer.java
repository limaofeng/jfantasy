package com.fantasy.framework.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractBuffer implements Buffer {

    private Log logger = LogFactory.getLog(getClass());
    protected static final String __IMMUTABLE = "IMMUTABLE";
    protected static final String __READONLY = "READONLY";
    protected static final String __READWRITE = "READWRITE";
    protected static final String __VOLATILE = "VOLATILE";
    protected int _access;
    protected boolean _volatile;
    protected int _get;
    protected int _put;
    protected int _hash;
    protected int _hashGet;
    protected int _hashPut;
    protected int _mark;
    protected String _string;
    protected View _view;

    public AbstractBuffer(int access, boolean isVolatile) {
        if ((access == 0) && (isVolatile)){
            throw new IllegalArgumentException("IMMUTABLE && VOLATILE");
        }
        setMarkIndex(-1);
        this._access = access;
        this._volatile = isVolatile;
    }

    public byte[] asArray() {
        byte[] bytes = new byte[length()];
        byte[] array = array();
        if (array != null){
            System.arraycopy(array, getIndex(), bytes, 0, bytes.length);
        }else{
            peek(getIndex(), bytes, 0, length());
        }
        return bytes;
    }

    public ByteArrayBuffer duplicate(int access) {
        Buffer b = buffer();
        if ((b instanceof Buffer.CaseInsensitve)) {
            return new ByteArrayBuffer.CaseInsensitive(asArray(), 0, length(), access);
        }
        return new ByteArrayBuffer(asArray(), 0, length(), access);
    }

    public Buffer asNonVolatileBuffer() {
        if (!isVolatile()){
            return this;
        }
        return duplicate(this._access);
    }

    public Buffer asImmutableBuffer() {
        if (isImmutable()){
            return this;
        }
        return duplicate(0);
    }

    public Buffer asReadOnlyBuffer() {
        if (isReadOnly()){
            return this;
        }
        return new View(this, markIndex(), getIndex(), putIndex(), 1);
    }

    public Buffer asMutableBuffer() {
        if (!isImmutable()){
            return this;
        }
        Buffer b = buffer();
        if (b.isReadOnly()) {
            return duplicate(2);
        }
        return new View(b, markIndex(), getIndex(), putIndex(), this._access);
    }

    public Buffer buffer() {
        return this;
    }

    public void clear() {
        setMarkIndex(-1);
        setGetIndex(0);
        setPutIndex(0);
    }

    public void compact() {
        if (isReadOnly()){
            throw new IllegalStateException("READONLY");
        }
        int s = markIndex() >= 0 ? markIndex() : getIndex();
        if (s > 0) {
            byte[] array = array();
            int length = putIndex() - s;
            if (length > 0) {
                if (array != null){
                    System.arraycopy(array(), s, array(), 0, length);
                } else{
                    poke(0, peek(s, length));
                }
            }
            if (markIndex() > 0){
                setMarkIndex(markIndex() - s);
            }
            setGetIndex(getIndex() - s);
            setPutIndex(putIndex() - s);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj == null) || (!(obj instanceof Buffer))){
            return false;
        }
        Buffer b = (Buffer) obj;

        if (((this instanceof Buffer.CaseInsensitve)) || ((b instanceof Buffer.CaseInsensitve))) {
            return equalsIgnoreCase(b);
        }

        if (b.length() != length()){
            return false;
        }

        if ((this._hash != 0) && ((obj instanceof AbstractBuffer))) {
            AbstractBuffer ab = (AbstractBuffer) obj;
            if ((ab._hash != 0) && (this._hash != ab._hash)){
                return false;
            }
        }

        int get = getIndex();
        int bi = b.putIndex();
        for (int i = putIndex(); i-- > get; ) {
            byte b1 = peek(i);
            bi--;
            byte b2 = b.peek(bi);
            if (b1 != b2){
                return false;
            }
        }
        return true;
    }

    public boolean equalsIgnoreCase(Buffer b) {
        if (b == this) {
            return true;
        }

        if (b.length() != length()){
            return false;
        }
        if ((this._hash != 0) && ((b instanceof AbstractBuffer))) {
            AbstractBuffer ab = (AbstractBuffer) b;
            if ((ab._hash != 0) && (this._hash != ab._hash)){
                return false;
            }
        }

        int get = getIndex();
        int bi = b.putIndex();

        byte[] array = array();
        byte[] barray = b.array();
        int i;
        if ((array != null) && (barray != null)) {
            for (i = putIndex(); i-- > get; ) {
                byte b1 = array[i];
                bi--;
                byte b2 = barray[bi];
                if (b1 != b2) {
                    if ((97 <= b1) && (b1 <= 122)){
                        b1 = (byte) (b1 - 97 + 65);
                    }
                    if ((97 <= b2) && (b2 <= 122)){
                        b2 = (byte) (b2 - 97 + 65);
                    }
                    if (b1 != b2){
                        return false;
                    }
                 }
            }
        } else {
            for (i = putIndex(); i-- > get; ) {
                byte b1 = peek(i);
                bi--;
                byte b2 = b.peek(bi);
                if (b1 != b2) {
                    if ((97 <= b1) && (b1 <= 122)){
                        b1 = (byte) (b1 - 97 + 65);
                    }

                    if ((97 <= b2) && (b2 <= 122)){
                        b2 = (byte) (b2 - 97 + 65);
                    }

                    if (b1 != b2){
                        return false;
                    }

                }
            }
        }
        return true;
    }

    public byte get() {
        return peek(this._get++);
    }

    public int get(byte[] b, int offset, int length) {
        int gi = getIndex();
        int l = length();
        if (l == 0) {
            return -1;
        }
        if (length > l) {
            length = l;
        }
        length = peek(gi, b, offset, length);
        if (length > 0){
            setGetIndex(gi + length);
        }
        return length;
    }

    public Buffer get(int length) {
        int gi = getIndex();
        Buffer view = peek(gi, length);
        setGetIndex(gi + length);
        return view;
    }

    public final int getIndex() {
        return this._get;
    }

    public boolean hasContent() {
        return this._put > this._get;
    }

    public int hashCode() {
        if ((this._hash == 0) || (this._hashGet != this._get) || (this._hashPut != this._put)) {
            int get = getIndex();
            byte[] array = array();
            int i;
            if (array == null) {
                for (i = putIndex(); i-- > get; ) {
                    byte b = peek(i);
                    if ((97 <= b) && (b <= 122)){
                        b = (byte) (b - 97 + 65);
                    }
                    this._hash = (31 * this._hash + b);
                }
            } else {
                for (i = putIndex(); i-- > get; ) {
                    byte b = array[i];
                    if ((97 <= b) && (b <= 122)){
                        b = (byte) (b - 97 + 65);
                    }
                    this._hash = (31 * this._hash + b);
                }
            }
            if (this._hash == 0){
                this._hash = -1;
            }
            this._hashGet = this._get;
            this._hashPut = this._put;
        }

        return this._hash;
    }

    public boolean isImmutable() {
        return this._access <= 0;
    }

    public boolean isReadOnly() {
        return this._access <= 1;
    }

    public boolean isVolatile() {
        return this._volatile;
    }

    public int length() {
        return this._put - this._get;
    }

    public void mark() {
        setMarkIndex(this._get - 1);
    }

    public void mark(int offset) {
        setMarkIndex(this._get + offset);
    }

    public int markIndex() {
        return this._mark;
    }

    public byte peek() {
        return peek(this._get);
    }

    public Buffer peek(int index, int length) {
        if (this._view == null) {
            this._view = new View(this, -1, index, index + length, isReadOnly() ? 1 : 2);
        } else {
            this._view.update(buffer());
            this._view.setMarkIndex(-1);
            this._view.setGetIndex(0);
            this._view.setPutIndex(index + length);
            this._view.setGetIndex(index);
        }
        return this._view;
    }

    public int poke(int index, Buffer src) {
        this._hash = 0;

        int length = src.length();
        if (index + length > capacity()) {
            length = capacity() - index;
        }

        byte[] src_array = src.array();
        byte[] dst_array = array();
        if ((src_array != null) && (dst_array != null)) {
            System.arraycopy(src_array, src.getIndex(), dst_array, index, length);
        } else if (src_array != null) {
            int s = src.getIndex();
            for (int i = 0; i < length; i++){
                poke(index++, src_array[(s++)]);
            }
        } else if (dst_array != null) {
            int s = src.getIndex();
            for (int i = 0; i < length; i++){
                dst_array[(index++)] = src.peek(s++);
            }
        } else {
            int s = src.getIndex();
            for (int i = 0; i < length; i++) {
                poke(index++, src.peek(s++));
            }
        }
        return length;
    }

    public int poke(int index, byte[] b, int offset, int length) {
        this._hash = 0;

        if (index + length > capacity()) {
            length = capacity() - index;
        }

        byte[] dst_array = array();
        if (dst_array != null) {
            System.arraycopy(b, offset, dst_array, index, length);
        } else {
            int s = offset;
            for (int i = 0; i < length; i++){
                poke(index++, b[(s++)]);
            }
        }
        return length;
    }

    public int put(Buffer src) {
        int pi = putIndex();
        int l = poke(pi, src);
        setPutIndex(pi + l);
        return l;
    }

    public void put(byte b) {
        int pi = putIndex();
        poke(pi, b);
        setPutIndex(pi + 1);
    }

    public int put(byte[] b, int offset, int length) {
        int pi = putIndex();
        int l = poke(pi, b, offset, length);
        setPutIndex(pi + l);
        return l;
    }

    public int put(byte[] b) {
        int pi = putIndex();
        int l = poke(pi, b, 0, b.length);
        setPutIndex(pi + l);
        return l;
    }

    public final int putIndex() {
        return this._put;
    }

    public void reset() {
        if (markIndex() >= 0){
            setGetIndex(markIndex());
        }
    }

    public void rewind() {
        setGetIndex(0);
        setMarkIndex(-1);
    }

    public void setGetIndex(int getIndex) {
        this._get = getIndex;
        this._hash = 0;
    }

    public void setMarkIndex(int index) {
        this._mark = index;
    }

    public void setPutIndex(int putIndex) {
        this._put = putIndex;
        this._hash = 0;
    }

    public int skip(int n) {
        if (length() < n){
            n = length();
        }
        setGetIndex(getIndex() + n);
        return n;
    }

    public Buffer slice() {
        return peek(getIndex(), length());
    }

    public Buffer sliceFromMark() {
        return sliceFromMark(getIndex() - markIndex() - 1);
    }

    public Buffer sliceFromMark(int length) {
        if (markIndex() < 0){
            return null;
        }
        Buffer view = peek(markIndex(), length);
        setMarkIndex(-1);
        return view;
    }

    public int space() {
        return capacity() - this._put;
    }

    public String toDetailString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        buf.append(super.hashCode());
        buf.append(",");
        buf.append(buffer().hashCode());
        buf.append(",m=");
        buf.append(markIndex());
        buf.append(",g=");
        buf.append(getIndex());
        buf.append(",p=");
        buf.append(putIndex());
        buf.append(",c=");
        buf.append(capacity());
        buf.append("]={");
        if (markIndex() >= 0) {
            for (int i = markIndex(); i < getIndex(); i++) {
                char c = (char) peek(i);
                if (Character.isISOControl(c)) {
                    buf.append(c < '\020' ? "\\0" : "\\");
                    buf.append(Integer.toString(c, 16));
                } else {
                    buf.append(c);
                }
            }
            buf.append("}{");
        }
        int count = 0;
        for (int i = getIndex(); i < putIndex(); i++) {
            char c = (char) peek(i);
            if (Character.isISOControl(c)) {
                buf.append(c < '\020' ? "\\0" : "\\");
                buf.append(Integer.toString(c, 16));
            } else {
                buf.append(c);
            }
            if (count++ != 50){
                continue;
            }
            if (putIndex() - i <= 20){
                continue;
            }
            buf.append(" ... ");
            i = putIndex() - 20;
        }

        buf.append('}');
        return buf.toString();
    }

    public String toString() {
        if (isImmutable()) {
            if (this._string == null){
                this._string = new String(asArray(), 0, length());
            }
            return this._string;
        }
        return new String(asArray(), 0, length());
    }

    public String toString(String charset) {
        try {
            byte[] bytes = array();
            if (bytes != null){
                return new String(bytes, getIndex(), length(), charset);
            }
            return new String(asArray(), 0, length(), charset);
        } catch (Exception e) {
            logger.warn(e);
        }
        return new String(asArray(), 0, length());
    }

    public String toDebugString() {
        return String.valueOf(getClass()) + "@" + super.hashCode();
    }

    public void writeTo(OutputStream out) throws IOException {
        byte[] array = array();

        if (array != null) {
            out.write(array, getIndex(), length());
        } else {
            int len = length();
            byte[] buf = new byte[len > 1024 ? 1024 : len];
            int offset = this._get;
            while (len > 0) {
                int l = peek(offset, buf, 0, len > buf.length ? buf.length : len);
                out.write(buf, 0, l);
                offset += l;
                len -= l;
            }
        }
        clear();
    }

    public int readFrom(InputStream in, int max) throws IOException {
        byte[] array = array();
        int s = space();
        if (s > max) {
            s = max;
        }
        if (array != null) {
            int l = in.read(array, this._put, s);
            if (l > 0){
                this._put += l;
            }
            return l;
        }

        byte[] buf = new byte[s > 1024 ? 1024 : s];
        int total = 0;
        while (s > 0) {
            int l = in.read(buf, 0, buf.length);
            if (l < 0){
                return -1;
            }
            int p = put(buf, 0, l);
            assert (l == p);
            s -= l;
        }
        return total;
    }
}