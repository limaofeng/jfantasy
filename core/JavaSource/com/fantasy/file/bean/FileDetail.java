package com.fantasy.file.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * 文件信息表
 *
 * @author 软件
 */
@Entity
@IdClass(FileDetailKey.class)
@Table(name = "FILE_FILEDETAIL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "folder", "realPath"})
public class FileDetail extends BaseBusEntity {

    private static final long serialVersionUID = -3377507500960127984L;

    /**
     * 虚拟文件路径
     */
    @Id
    private String absolutePath;
    @Id
    private String fileManagerId;
    /**
     * 文件名称
     */
    @Column(name = "FILE_NAME", length = 150)
    private String fileName;
    /**
     * 文件后缀名
     */
    @Column(name = "EXT", length = 20)
    private String ext;
    /**
     * 文件类型
     */
    @Column(name = "CONTENT_TYPE", length = 50)
    private String contentType;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
    /**
     * 文件长度
     */
    @Column(name = "LENGTH")
    private Long size;
    /**
     * 文件MD5码
     */
    @Column(name = "MD5", length = 50)
    private String md5;
    /**
     * 文件真实路径
     */
    @Column(name = "REAL_PATH", length = 250)
    private String realPath;
    /**
     * 文件夹
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "FOLDER_PATH", referencedColumnName = "ABSOLUTE_PATH"), @JoinColumn(name = "FOLDER_MANAGER_CONFIG_ID", referencedColumnName = "FILE_MANAGER_CONFIG_ID")})
    private Folder folder;

    public FileDetail() {
    }

    /**
     * 设置 文件路径(文件系统中的路径，非虚拟路径)
     *
     * @param absolutePath 文件路径
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
     * @param fileName 文件名
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
     * @param contentType 文件类型
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
     * @param description 文件描述
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
     * @param size 文件大小
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

    /**
     * 设置 文件夹ID
     *
     * @param folder 文件夹
     */
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    /**
     * 获取 文件夹ID
     *
     * @return Folder
     */
    public Folder getFolder() {
        return this.folder;
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

    public String getExt() {
        return this.ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public FileDetail clone(){
        return ObjectUtil.clone(this);
    }

    @Override
    public String toString() {
        return "FileDetail{" +
                "absolutePath='" + absolutePath + '\'' +
                ", fileManagerId='" + fileManagerId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", ext='" + ext + '\'' +
                ", contentType='" + contentType + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", md5='" + md5 + '\'' +
                ", realPath='" + realPath + '\'' +
                ", folder=" + folder +
                '}';
    }
}