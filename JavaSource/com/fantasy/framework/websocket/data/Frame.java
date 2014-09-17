package com.fantasy.framework.websocket.data;

import com.fantasy.framework.io.ByteArrayBuffer;
import com.fantasy.framework.net.util.CoderUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 消息帧
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-11 上午11:33:35
 */
public class Frame {
    private ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(1024);
    private long length;

    public final static byte FIN = (byte) 0x80; // 1000 0000
    public final static byte RSV1 = 0x70; // 0111 0000
    public final static byte RSV2 = 0x30; // 0011 0000
    public final static byte RSV3 = 0x10; // 0001 0000
    public final static byte OPCODE = 0x0F;// 0000 1111
    public final static byte MASK = (byte) 0x80;// 1000 0000
    public final static byte PAYLOADLEN = 0x7F;// 0111 1111

    public final static byte HAS_EXTEND_DATA = 126;
    public final static byte HAS_EXTEND_DATA_CONTINUE = 127;

    /**
     * 文本消息分片
     */
    public final static byte TXT = 0x01;// 0000 0001
    /**
     * 二进制消息分片
     */
    public final static byte BINARY = 0x02;// 0000 0001
    // 0x3-7 为将来的非控制消息片断保留的操作码
    /**
     * 连接关闭
     */
    public final static byte CLOSE = 0x08;// 0000 1000
    /**
     * 表示心跳检查的ping
     */
    public final static byte PING = 0x09;
    /**
     * 表示心跳检查的pong
     */
    public final static byte PONG = 0x0A;
    // xB-F 为将来的控制消息片断的保留操作码

    private Handler handler;
    private byte[] body;
    private String message;

    public Frame() {
    }

    private Frame(byte opcode, String msg, boolean mask) {
        this.message = msg;
        this.body = CoderUtil.toByte(msg);
        this.handler = new Handler(opcode, body.length, mask);
    }

    public static class Handler {
        private byte fin;
        private byte rsv1 = 0;
        private byte rsv2 = 0;
        private byte rsv3 = 0;
        private byte opcode = 1;
        private boolean mask;
        private byte payloadLen = 0;
        private long dataLength;
        private byte[] maskingkeys = new byte[4];
        private byte[] header;

        public Handler() {
        }

        public Handler(byte opcode, int length, boolean mask) {
            this.opcode = opcode;
            this.dataLength = length;
            if (dataLength < HAS_EXTEND_DATA) {
                this.payloadLen = (byte) dataLength;
            } else if (dataLength < 1 * Short.MAX_VALUE * 2) {// UNSIGNED
                this.payloadLen = HAS_EXTEND_DATA;
            } else {
                this.payloadLen = HAS_EXTEND_DATA_CONTINUE;
            }
            this.mask = mask;
            if (mask) {
                setMaskingkeys(new byte[]{-116, -67, 86, -24});
            }
            header = new byte[2];
            header[0] = FIN;// 需要调整
            header[0] |= this.getRsv1() | this.getRsv2() | this.getRsv3() | opcode;// Frame.TXT;
            header[1] = 0;
            header[1] |= (this.isMask() ? MASK : 0x00) | this.getPayloadLen();

            if (this.getPayloadLen() == HAS_EXTEND_DATA) {// 处理数据长度为126位的情况
                header = CoderUtil.join(header, CoderUtil.shortToByte((short) this.getDataLength()));
            } else if (this.getPayloadLen() == HAS_EXTEND_DATA_CONTINUE) {// 处理数据长度为127位的情况
                header = CoderUtil.join(header, CoderUtil.longToByte(this.getDataLength()));
            }
            if (this.isMask()) {// 做了掩码处理的，需要传递掩码的key
                header = CoderUtil.join(header, this.getMaskingkeys());
            }
        }

        private byte[] mask(byte[] body) {
            if (!this.mask)
                return body;
            for (int i = 0; i < body.length; i++) {// 进行掩码处理
                body[i] ^= this.getMaskingkeys()[i % 4];
            }
            return body;
        }

        public byte getFin() {
            return fin;
        }

        public void setFin(byte fin) {
            this.fin = fin;
        }

        public byte getRsv1() {
            return rsv1;
        }

        public void setRsv1(byte rsv1) {
            this.rsv1 = rsv1;
        }

        public byte getRsv2() {
            return rsv2;
        }

        public void setRsv2(byte rsv2) {
            this.rsv2 = rsv2;
        }

        public byte getRsv3() {
            return rsv3;
        }

        public void setRsv3(byte rsv3) {
            this.rsv3 = rsv3;
        }

        public byte getOpcode() {
            return opcode;
        }

        public void setOpcode(byte opcode) {
            this.opcode = opcode;
        }

        public boolean isMask() {
            return mask;
        }

        public void setMask(boolean mask) {
            this.mask = mask;
        }

        public byte getPayloadLen() {
            return payloadLen;
        }

        public void setPayloadLen(byte payloadLen) {
            this.payloadLen = payloadLen;
        }

        public long getDataLength() {
            return dataLength;
        }

        public void setDataLength(long dataLength) {
            this.dataLength = dataLength;
        }

        public byte[] getMaskingkeys() {
            return maskingkeys;
        }

        public void setMaskingkeys(byte[] maskingkeys) {
            this.maskingkeys = maskingkeys;
        }

        @Override
        public String toString() {
            return "Handler [fin=" + fin + ", rsv1=" + rsv1 + ", rsv2=" + rsv2 + ", rsv3=" + rsv3 + ", opcode=" + opcode + ", payloadLen=" + payloadLen + ", dataLength=" + dataLength + ", mask=" + mask + ", maskingkeys=" + Arrays.toString(maskingkeys) + "]";
        }

        private byte[] toBytes() {
            return this.header;
        }

    }

    public Handler getHandler() {
        return handler;
    }

    public String getMessage() {
        if (message == null && Frame.TXT == this.handler.getOpcode() || Frame.CLOSE == this.handler.getOpcode() || Frame.PONG == this.handler.getOpcode()) {
            for (int i = 0; i < body.length; i++) {
                if (this.handler.isMask()) {
                    body[i] ^= handler.getMaskingkeys()[i % 4];
                }
            }
            this.message = CoderUtil.decode(body);
        }
        return message;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Frame newFrame(byte opcode, String message, boolean ismask) {
        return new Frame(opcode, message, ismask);
    }

    public static byte[] toBytes(Frame frame) {
        return CoderUtil.join(frame.getHandler().toBytes(), frame.getHandler().mask(frame.body));
    }

    public static void appendBytes(ByteBuffer buffer, byte... datas) {
        if (buffer.remaining() >= datas.length) {// 缓冲区的空间大于数据的长度
            buffer.put(datas);
        } else {// 缓冲区的空间小于数据的长度
            int offset = 0;// 初始相对位移为0
            int length = buffer.remaining();// 初始传递的数据长度为缓冲区的剩余长度
            do {
                buffer.put(datas, offset, length);
                offset += length;
                if (offset < datas.length) {
                    length = (buffer.remaining() > (datas.length - offset) ? (datas.length - offset) : buffer.remaining());
                } else {
                    break;
                }
            } while (true);
        }
    }

    public void put(byte intByte) {
        byteArrayBuffer.put(intByte);
        switch (byteArrayBuffer.length()) {
            case 1:
                this.handler = new Handler();
                handler.setFin((byte) (intByte & FIN));
                handler.setRsv1((byte) (intByte & RSV1));
                handler.setRsv2((byte) (intByte & RSV2));
                handler.setRsv3((byte) (intByte & RSV3));
                handler.setOpcode((byte) (intByte & OPCODE));
                break;
            case 2:
                handler.setMask((0 == ((intByte & MASK) ^ MASK)));
                handler.setPayloadLen((byte) (intByte & PAYLOADLEN));
                if (handler.getPayloadLen() < HAS_EXTEND_DATA) {
                    handler.dataLength = handler.getPayloadLen();
                    length = byteArrayBuffer.length() + (handler.isMask() ? 4 : 0) + handler.getDataLength();
                }
                break;
            case 4:
                if (handler.getPayloadLen() == HAS_EXTEND_DATA) {
                    handler.dataLength = CoderUtil.toLong(byteArrayBuffer.peek(2, 2).asArray());
                    length = byteArrayBuffer.length() + (handler.isMask() ? 4 : 0) + handler.getDataLength();
                }
                break;
            case 10:
                if (handler.getPayloadLen() == HAS_EXTEND_DATA_CONTINUE) {
                    handler.dataLength = CoderUtil.toLong(byteArrayBuffer.peek(2, 8).asArray());
                    length = byteArrayBuffer.length() + (handler.isMask() ? 4 : 0) + handler.getDataLength();
                }
                break;
        }
        if (finish()) {
            if (this.handler.isMask())
                handler.setMaskingkeys(byteArrayBuffer.peek(byteArrayBuffer.length() - (int) handler.getDataLength() - 4, 4).asArray());
            this.body = this.byteArrayBuffer.peek(this.byteArrayBuffer.length() - (int) this.handler.dataLength, (int) this.handler.dataLength).asArray();
        }
    }

    public boolean finish() {
        return length == byteArrayBuffer.length();
    }

}
