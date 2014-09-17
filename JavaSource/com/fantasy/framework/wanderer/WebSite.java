package com.fantasy.framework.wanderer;

/**
 * 站点
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-13 下午05:30:13
 * @version 1.0
 */
public class WebSite {

	/**
	 * 网站入口
	 */
	private String url;
	/**
	 * 网站名称
	 */
	private String name;
	/**
	 * 单独分派的蜘蛛数量,如果为0，使用公共蜘蛛
	 */
	private int spiderNumber;
	
	//url链接解析器
	
	//文档处理器集合

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSpiderNumber() {
		return spiderNumber;
	}

	public void setSpiderNumber(int spiderNumber) {
		this.spiderNumber = spiderNumber;
	}

}