package com.fantasy.framework.mc;

/**
 * 消息中心接口
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-12-7 上午11:22:33
 * @version 1.0
 */
public interface MessageCenter {

	/**
	 * 将客户端在服务中心中注册
	 * @功能描述 
	 * @param client
	 * @return
	 */
	public Client register(Client client);

}
