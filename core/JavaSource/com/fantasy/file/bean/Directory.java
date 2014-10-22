package com.fantasy.file.bean;

import com.fantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 文件上传时，为其指定的上传目录
 * 
 * @功能描述 通过Key获取上传的目录及文件管理器
 * @author 李茂峰
 * @since 2013-7-23 上午10:30:06
 * @version 1.0
 */
@Entity
@Table(name = "FILE_DIRECTORY")
public class Directory extends BaseBusEntity {

	private static final long serialVersionUID = 8150927437017643578L;

	@Id
	@Column(name = "DIR_KEY", nullable = false, insertable = true, updatable = false, length = 50, unique = true)
	private String key;
	/**
	 * 对应的文件管理器
	 */
	@JoinColumn(name = "FILE_MANAGER_CONFIG_ID", insertable = true, updatable = false,foreignKey =@ForeignKey(name="FK_FILE_DIRECTORY_MANAGER") )
	@ManyToOne(fetch = FetchType.LAZY)

	private FileManagerConfig fileManager;
	/**
	 * 对应的默认目录
	 */
	@Column(name = "DIR_PATH", length = 250)
	private String dirPath;
	/**
	 * 目录名称
	 */
	@Column(name = "DIR_NAME", length = 250)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public FileManagerConfig getFileManager() {
		return fileManager;
	}

	public void setFileManager(FileManagerConfig fileManager) {
		this.fileManager = fileManager;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

}