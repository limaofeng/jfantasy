package com.fantasy.framework.wanderer;

import java.util.Date;

/**
 * 访问日志
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-13 下午04:18:41
 * @version 1.0
 */
public class AccessLog {

	/**
	 * 响应状态
	 */
	private int status;

	/**
	 * 文档大小
	 */
	private long contentLength;
	/**
	 * 编码格式
	 */
	private String contentEncoding;
	/**
	 * 类型
	 */
	private String contentType;
	/**
	 * 最后修改时间
	 */
	private Date lastModified;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
