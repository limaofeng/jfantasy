package org.jfantasy.filestore.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.filestore.service.FileService;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.SpringContextUtil;

import javax.persistence.*;
import java.util.List;

/**
 * 文件夹表
 *
 * @author 软件
 */
@Entity
@Table(name = "FILE_FOLDER")
@IdClass(FolderKey.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Folder extends BaseBusEntity {

    private static final long serialVersionUID = -1415999483740197039L;

    @Id
    private String absolutePath;

    @Id
    private String fileManagerId;

    /**
     * 同一目录下不允许重名
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人
     */
    @Column(name = "TYPE")
    private String type;
    /**
     * 文件夹类型:当为公司或者个人时，输入公司和个人的ID
     */
    @Column(name = "TYPE_VALUE")
    private String typeValue;
    /**
     * 允许上传的文件扩展名
     */
    @Column(name = "EXTS")
    private String exts;
    /**
     * 可上传文件大小
     */
    @Column(name = "LENGTH")
    private Long size;
    /**
     * 是否为叶子目录,叶子目录不能创建子目录
     */
    @Column(name = "LAST")
    private boolean last;
    /**
     * 父目录ID
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {javax.persistence.CascadeType.REFRESH})
    @JoinColumns(value = {@JoinColumn(name = "PARENT_PATH", referencedColumnName = "ABSOLUTE_PATH"), @JoinColumn(name = "P_MANAGER_CONFIG_ID", referencedColumnName = "FILE_MANAGER_CONFIG_ID")})
    private Folder parentFolder;
    /**
     * 获取子目录列表
     */
    @OneToMany(mappedBy = "parentFolder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("createTime ASC")
    private List<Folder> children;
    /**
     * 附件对应的集合
     */
    @OneToMany(mappedBy = "folder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<FileDetail> fileDetails;

    /**
     * 设置 同一目录下不允许重名
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 同一目录下不允许重名
     *
     * @return java.lang.String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人
     *
     * @return java.lang.String
     */
    public String getType() {
        return this.type;
    }

    /**
     * 设置 文件夹类型:当为公司或者个人时，输入公司和个人的ID
     *
     * @param typeValue 类型值
     */
    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    /**
     * 获取 文件夹类型:当为公司或者个人时，输入公司和个人的ID
     *
     * @return java.lang.String
     */
    public String getTypeValue() {
        return this.typeValue;
    }

    /**
     * 设置 允许上传的文件扩展名
     *
     * @param exts 后缀名
     */
    public void setExts(String exts) {
        this.exts = exts;
    }

    /**
     * 获取 允许上传的文件扩展名
     *
     * @return java.lang.String
     */
    public String getExts() {
        return this.exts;
    }

    /**
     * 设置 可上传文件大小
     *
     * @param size 长度
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 获取 可上传文件大小
     *
     * @return java.lang.Long
     */
    public Long getSize() {
        return this.size;
    }

    /**
     * 设置 是否为叶子目录,叶子目录不能创建子目录
     *
     * @param last 是否为叶节点
     */
    public void setLast(boolean last) {
        this.last = last;
    }

    /**
     * 获取 是否为叶子目录,叶子目录不能创建子目录
     *
     * @return java.lang.Boolean
     */
    public boolean isLast() {
        return this.last;
    }

    public synchronized Folder getParentFolder() {
        if (this.parentFolder == null && !"/".equals(this.absolutePath)) {
            this.parentFolder = SpringContextUtil.getBeanByType(FileService.class).createFolder(this.getAbsolutePath().replaceFirst("[^/]+/$", ""), this.getFileManagerId());
        }
        return parentFolder;
    }

    public synchronized void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<Folder> getChildren() {
        return children;
    }

    public void setChildren(List<Folder> children) {
        this.children = children;
    }

    public List<FileDetail> getFileDetails() {
        return fileDetails;
    }

    public void setFileDetails(List<FileDetail> fileDetails) {
        this.fileDetails = fileDetails;
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

}