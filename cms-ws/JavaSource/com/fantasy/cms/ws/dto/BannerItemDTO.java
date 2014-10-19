package com.fantasy.cms.ws.dto;

import com.fantasy.file.ws.dto.FileDTO;

public class BannerItemDTO {

	private Long id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 跳转地址
	 */
	private String url;
	/**
	 * 图片存储位置
	 */
	private FileDTO bannerImage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public FileDTO getBannerImage() {
		return bannerImage;
	}

	public void setBannerImage(FileDTO bannerImage) {
		this.bannerImage = bannerImage;
	}

}
