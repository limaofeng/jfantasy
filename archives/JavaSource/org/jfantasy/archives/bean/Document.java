package org.jfantasy.archives.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 文档,文件 。 没有固定的结构。只做单独的文件存储
 */
@ApiModel(" 人员信息 ")
@Entity
@Table(name = "ARCH_DOCUMENT")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Document extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 编号
     */
    @ApiModelProperty("编号")
    @Column(name = "NUMBER", unique = true, length = 30)
    private String no;
    /**
     * 同一档案中尽量避免重名
     */
    @Column(name = "CODE", length = 10)
    private String code;
    /**
     * 文档标题
     */
    @Column(name = "TITLE", length = 50)
    private String title;
    /**
     * 数据类型
     */
    @ApiModelProperty("数据类型")
    @Column(name = "DATA_TYPE", length = 30)
    private String dataType;
    /**
     * 数据存储
     */
    @Column(name = "DATA_VALUE", columnDefinition = "MediumBlob")
    private String dataValue;
    /**
     * 对应的档案
     */
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ARCH_DOCUMENT_PERSON"))
    private Record record;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataValue() {
        return dataValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
