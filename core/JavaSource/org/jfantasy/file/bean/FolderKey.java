package org.jfantasy.file.bean;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FolderKey implements Serializable {

	private static final long serialVersionUID = 6090047858630400968L;

	@Column(name = "ABSOLUTE_PATH", nullable = false, insertable = true, updatable = false,length = 250)
	private String absolutePath;

	@Column(name = "FILE_MANAGER_CONFIG_ID", nullable = false, insertable = true, updatable = false,length = 50)
	private String fileManagerId;

	public FolderKey() {
	}

	public FolderKey(String absolutePath, String fileManagerId) {
		super();
		this.absolutePath = absolutePath;
		this.fileManagerId = fileManagerId;
	}

	public String getFileManagerId() {
		return fileManagerId;
	}

	public void setFileManagerId(String fileManagerId) {
		this.fileManagerId = fileManagerId;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getFileManagerId()).append(this.getAbsolutePath()).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FolderKey) {
			FolderKey key = (FolderKey) o;
			return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getFileManagerId(), key.getFileManagerId()).append(this.getAbsolutePath(), key.getAbsolutePath()).isEquals();
		}
		return false;
	}

}
