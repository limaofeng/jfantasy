package com.fantasy.framework.mc;

import java.io.IOException;

/**
 * 消息中心客户端
 * 
 * @功能描述 <br>
 *       自动维护两个数据队列 <br>
 *       1.服务中心下发数据 <br>
 *       2.将要上传至服务中心的数据<br>
 *       守护线程一直向服务中心推送数据
 * 
 * <pre></pre>
 * @author 李茂峰
 * @since 2012-12-7 上午11:25:24
 * @version 1.0
 */
public interface Client {

	public void setInfo(Info info);

	public Info getInfo();

	/**
	 * 向服务中心请求获取数据
	 * 
	 * @return Message
	 */
	public Message pull();

	/**
	 * 将消息发送到服务中心
	 * 
	 * @param message Message
	 */
	public void push(Message message);
	
	public void service() throws IOException;
	
	
//	  public abstract void onConnect(/*Outbound paramOutbound*/);

//	  public abstract void onMessage(byte paramByte, String paramString);

//	  public abstract void onFragment(boolean paramBoolean, byte paramByte, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

//	  public abstract void onMessage(byte paramByte, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

//	  public void onDisconnect();
	
}
