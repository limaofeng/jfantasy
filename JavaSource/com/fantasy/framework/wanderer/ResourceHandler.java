package com.fantasy.framework.wanderer;

import com.fantasy.framework.httpclient.Response;

/**
 * 资源处理程序
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-15 下午07:41:28
 * @version 1.0
 */
public interface ResourceHandler {

	public void onResource(Response response);

}
