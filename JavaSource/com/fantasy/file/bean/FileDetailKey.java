package com.fantasy.file.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import java.io.Serializable;

public class FileDetailKey implements Serializable {

    private static final long serialVersionUID = -483649311695334839L;

    public static FileDetailKey newInstance(String absolutePath, String fileManagerId) {
        return new FileDetailKey(absolutePath, fileManagerId);
    }

    /**
     * 虚拟文件路径
     */
    @Column(name = "ABSOLUTE_PATH", nullable = false, insertable = true, updatable = true, length = 250)
    private String absolutePath;

    @Column(name = "FILE_MANAGER_CONFIG_ID", nullable = false, insertable = true, updatable = false, length = 50)
    private String fileManagerId;

    public FileDetailKey() {
    }

    public FileDetailKey(String absolutePath, String fileManagerId) {
        super();
        this.absolutePath = absolutePath;
        this.fileManagerId = fileManagerId;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }


    public String getFileManagerId() {
        return fileManagerId;
    }

    public void setFileManagerId(String fileManagerId) {
        this.fileManagerId = fileManagerId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getFileManagerId()).append(this.getAbsolutePath()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileDetailKey) {
            FileDetailKey key = (FileDetailKey) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getFileManagerId(), key.getFileManagerId()).append(this.getAbsolutePath(), key.getAbsolutePath()).isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.fileManagerId + ":" + this.absolutePath;
    }

}
