package com.fantasy.framework.websocket;

public interface WebSocketOutbound {

    /**
     * 发送文本消息
     *
     * @param paramString 消息
     */
    public void sendMessage(String paramString);

    /**
     * 发送消息
     *
     * @param paramByte   操作
     * @param paramString 消息
     */
    public void sendMessage(byte paramByte, String paramString);

    /**
     * 发送消息
     *
     * @param paramByte        操作
     * @param paramArrayOfByte 字节数组
     * @param paramInt1        开始位置
     * @param paramInt2        长度
     */
    public void sendMessage(byte paramByte, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    /**
     * 发送消息片段
     *
     * @param paramBoolean     mask
     * @param paramByte        操作
     * @param paramArrayOfByte 字节数组
     * @param paramInt1        开始位置
     * @param paramInt2        长度
     */
    public void sendFragment(boolean paramBoolean, byte paramByte, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    /**
     * 断开连接
     */
    public void disconnect();

    /**
     * 判断连接是否打开
     *
     * @return boolean
     */
    public boolean isOpen();

}