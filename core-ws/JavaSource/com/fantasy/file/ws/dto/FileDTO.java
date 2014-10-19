package com.fantasy.file.ws.dto;


/**
 * 文件信息表
 * 
 * @author 软件
 */
public class FileDTO  {

	/**
	 * 虚拟文件路径
	 */
	private String absolutePath;
	
	private String fileManagerId;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件类型
	 */
	private String contentType;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 文件长度
	 */
	private Long size;

	/**
	 * 文件MD5码
	 */
	private String md5;

	/**
	 * 文件真实路径
	 */
	private String realPath;


	public FileDTO() {
	}

	/**
	 * 设置 文件路径(文件系统中的路径，非虚拟路径)
	 * 
	 * @param absolutePath
	 */
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	/**
	 * 获取 文件路径(文件系统中的路径，非虚拟路径)
	 * 
	 * @return java.lang.String
	 */
	public String getAbsolutePath() {
		return this.absolutePath;
	}

	/**
	 * 设置 文件名称
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取 文件名称
	 * 
	 * @return java.lang.String
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * 设置 文件类型
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 获取 文件类型
	 * 
	 * @return java.lang.String
	 */
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * 设置 描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取 描述
	 * 
	 * @return java.lang.String
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 设置 文件长度
	 * 
	 * @param size
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * 获取 文件长度
	 * 
	 * @return java.lang.Long
	 */
	public Long getSize() {
		return this.size;
	}


	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getFileManagerId() {
		return fileManagerId;
	}

	public void setFileManagerId(String fileManagerId) {
		this.fileManagerId = fileManagerId;
	}

}