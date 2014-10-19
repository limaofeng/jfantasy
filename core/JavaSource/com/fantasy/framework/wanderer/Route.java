package com.fantasy.framework.wanderer;

import java.util.List;

/**
 * 路线
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-13 下午02:00:31
 * @version 1.0
 */
public class Route {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 协议
	 */
	private String scheme;
	/**
	 * serverName
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 地址信息
	 */
	private String path;
	/**
	 * 访问日志
	 */
	private List<AccessLog> logs;
	/**
	 * 所属网站
	 */
	private WebSite webSite;
	/**
	 * 层级
	 */
	private String level;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<AccessLog> getLogs() {
		return logs;
	}

	public void setLogs(List<AccessLog> logs) {
		this.logs = logs;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WebSite getWebSite() {
		return webSite;
	}

	public void setWebSite(WebSite webSite) {
		this.webSite = webSite;
	}

}
