package com.fantasy.file.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


@Entity
@IdClass(FileDetailKey.class)
@Table(name = "FILE_FILEPART", uniqueConstraints = {@UniqueConstraint(columnNames = {"ENTIRE_FILE_HASH", "PART_FILE_HASH"})})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FilePart extends BaseBusEntity {

    @Id
    private String absolutePath;
    @Id
    private String fileManagerId;
    /**
     * 完整文件的hash值
     */
    @Column(name = "ENTIRE_FILE_HASH")
    private String entireFileHash;
    /**
     * 片段文件的hash值
     */
    @Column(name = "PART_FILE_HASH")
    private String partFileHash;
    /**
     * 总的段数
     */
    @Column(name = "PAER_TOTAL")
    private Integer total;
    /**
     * 当前段数
     */
    @Column(name = "PAER_INDEX")
    private Integer index;


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

    public String getEntireFileHash() {
        return entireFileHash;
    }

    public void setEntireFileHash(String entireFileHash) {
        this.entireFileHash = entireFileHash;
    }

    public String getPartFileHash() {
        return partFileHash;
    }

    public void setPartFileHash(String partFileHash) {
        this.partFileHash = partFileHash;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
