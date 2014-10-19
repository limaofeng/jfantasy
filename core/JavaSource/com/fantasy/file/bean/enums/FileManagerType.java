package com.fantasy.file.bean.enums;

public enum FileManagerType {
	ftp("FTP文件系统"), local("本地文件系统"), jdbc("数据库存储"), virtual("上传文件存储");

	private String value;

	private FileManagerType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
