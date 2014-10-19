package com.fantasy.framework.wanderer;

import java.util.List;

import com.fantasy.framework.httpclient.Response;

/**
 * url解析器
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-15 下午07:36:22
 * @version 1.0
 */
public interface UrlParser {

	public List<Route> parser(Response response);

}
