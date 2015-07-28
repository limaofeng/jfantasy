package com.fantasy.framework.io;

public class BufferUtil {
    static final byte SPACE = 32;
    static final byte MINUS = 45;
    static final byte[] DIGIT = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};

    private static final int[] decDivisors = {1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1};

    private static final int[] hexDivisors = {268435456, 16777216, 1048576, 65536, 4096, 256, 16, 1};

    private static final long[] decDivisorsL = {1000000000000000000L, 100000000000000000L, 10000000000000000L, 1000000000000000L, 100000000000000L, 10000000000000L, 1000000000000L, 100000000000L, 10000000000L, 1000000000L, 100000000L, 10000000L, 1000000L, 100000L, 10000L, 1000L, 100L, 10L, 1L};

    public static int toInt(Buffer buffer) {
        int val = 0;
        boolean started = false;
        boolean minus = false;
        for (int i = buffer.getIndex(); i < buffer.putIndex(); i++) {
            byte b = buffer.peek(i);
            if (b <= SPACE) {
                if (started){
                    break;
                }
            } else if ((b >= 48) && (b <= 57)) {
                val = val * 10 + (b - 48);
                started = true;
            } else {
                if ((b != MINUS) || (started)){
                    break;
                }
                minus = true;
            }

        }

        if (started){
            return minus ? -val : val;
        }
        throw new NumberFormatException(buffer.toString());
    }

    public static long toLong(Buffer buffer) {
        long val = 0L;
        boolean started = false;
        boolean minus = false;
        for (int i = buffer.getIndex(); i < buffer.putIndex(); i++) {
            byte b = buffer.peek(i);
            if (b <= SPACE) {
                if (started){
                    break;
                }
            } else if ((b >= 48) && (b <= 57)) {
                val = val * 10L + (b - 48);
                started = true;
            } else {
                if ((b != MINUS) || (started)){
                    break;
                }
                minus = true;
            }

        }

        if (started){
            return minus ? -val : val;
        }
        throw new NumberFormatException(buffer.toString());
    }

    public static void putHexInt(Buffer buffer, int n) {
        int nt = n;
        if (nt < 0) {
            buffer.put(MINUS);

            if (nt == -2147483648) {
                buffer.put((byte) 56);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);

                return;
            }
            nt = -nt;
        }

        if (nt < 16) {
            buffer.put(DIGIT[nt]);
        } else {
            boolean started = false;
            for (int hex : hexDivisors) {
                if (nt < hex) {
                    if (started){
                        buffer.put((byte) 48);
                    }
                } else {
                    started = true;
                    int d = nt / hex;
                    buffer.put(DIGIT[d]);
                    nt -= d * hex;
                }
            }
        }
    }

    public static void prependHexInt(Buffer buffer, int n) {
        if (n == 0) {
            int gi = buffer.getIndex();
            gi--;
            buffer.poke(gi, (byte) 48);
            buffer.setGetIndex(gi);
        } else {
            boolean minus = false;
            if (n < 0) {
                minus = true;
                n = -n;
            }

            int gi = buffer.getIndex();
            while (n > 0) {
                int d = 0xF & n;
                n >>= 4;
                gi--;
                buffer.poke(gi, DIGIT[d]);
            }

            if (minus) {
                gi--;
                buffer.poke(gi, MINUS);
            }
            buffer.setGetIndex(gi);
        }
    }

    public static void putDecInt(Buffer buffer, int n) {
        if (n < 0) {
            buffer.put(MINUS);

            if (n == -2147483648) {
                buffer.put((byte) 50);
                n = 147483648;
            } else {
                n = -n;
            }
        }
        if (n < 10) {
            buffer.put(DIGIT[n]);
        } else {
            boolean started = false;

            for (int decDivisor : decDivisors) {
                if (n < decDivisor) {
                    if (started){
                        buffer.put((byte) 48);
                    }
                } else {
                    started = true;
                    int d = n / decDivisor;
                    buffer.put(DIGIT[d]);
                    n -= d * decDivisor;
                }
            }
        }
    }

    public static void putDecLong(Buffer buffer, long n) {
        if (n < 0L) {
            buffer.put(MINUS);

            if (n == -9223372036854775808L) {
                buffer.put((byte) 57);
                n = 223372036854775808L;
            } else {
                n = -n;
            }
        }
        if (n < 10L) {
            buffer.put(DIGIT[(int) n]);
        } else {
            boolean started = false;

            for (long aDecDivisorsL : decDivisorsL) {
                if (n < aDecDivisorsL) {
                    if (started){
                        buffer.put((byte) 48);
                    }
                } else {
                    started = true;
                    long d = n / aDecDivisorsL;
                    buffer.put(DIGIT[(int) d]);
                    n -= d * aDecDivisorsL;
                }
            }
        }
    }

    public static Buffer toBuffer(long value) {
        ByteArrayBuffer buf = new ByteArrayBuffer(32);
        putDecLong(buf, value);
        return buf;
    }

    public static void putCRLF(Buffer buffer) {
        buffer.put((byte) 13);
        buffer.put((byte) 10);
    }

    public static boolean isPrefix(Buffer prefix, Buffer buffer) {
        if (prefix.length() > buffer.length()){
            return false;
        }
        int bi = buffer.getIndex();
        for (int i = prefix.getIndex(); i < prefix.putIndex(); i++){
            if (prefix.peek(i) != buffer.peek(bi++)){
                return false;
            }
        }
        return true;
    }

    public static String to8859_1_String(Buffer buffer) {
        if ((buffer instanceof BufferCache.CachedBuffer)){
            return buffer.toString();
        }
        return buffer.toString("ISO-8859-1");
    }
}
