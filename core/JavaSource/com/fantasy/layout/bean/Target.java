package com.fantasy.layout.bean;

import java.util.Date;


/**
 * 推荐内容表
 * @功能描述 
 * @author 李茂峰
 * @since 2013-3-17 上午10:38:00
 * @version 1.0
 */
public class Target {

	private Long id;
	/**
	 * 推荐位置
	 */
	private Position position;
	/**
	 * 推荐标题
	 */
	private String title;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 跳转URL
	 */
	private String url;
	/**
	 * 显示时间
	 */
	private Date displayDate;
	/**
	 * 关联Id
	 */
	private String originId;
	/**
	 * 关联对象
	 */
	private String originTyple;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getOriginTyple() {
		return originTyple;
	}

	public void setOriginTyple(String originTyple) {
		this.originTyple = originTyple;
	}

}
