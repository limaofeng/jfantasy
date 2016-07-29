package org.jfantasy.archives.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.PropertiesConverter;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 档案信息
 */
@ApiModel(" 档案记录 ")
@Entity
@Table(name = "ARCH_RECORD")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "person"})
public class Record extends BaseBusEntity {

    @Null(message = "创建用户时,请不要传入ID", groups = {RESTful.POST.class})
    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @ApiModelProperty("编号")
    @Column(name = "NUMBER", length = 30)
    private String no;
    /**
     * 对应的人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ARCH_RECORD_PERSON"))
    private Person person;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", notes = "用于区分不同的档案信息")
    @Column(name = "TYPE", length = 10)
    private String type;
    /**
     * 文档记录
     */
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY)
    private List<Document> documents;
    /**
     * 显示时间
     */
    @JsonProperty("display_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "DISPLAY_TIME")
    private Date displayTime;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    @Column(name = "TITLE", length = 100)
    private String title;
    /**
     * 摘要
     */
    @ApiModelProperty("摘要")
    @Column(name = "SUMMARY", length = 200)
    private String summary;
    /**
     * 扩展属性
     */
    @ApiModelProperty(hidden = true)
    @Convert(converter = PropertiesConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "MediumBlob")
    private Properties properties;
    @Transient
    private Long personId;

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

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        this.properties.put(key, value);
    }

    @JsonAnyGetter
    public Properties getProperties() {
        if (ThreadJacksonMixInHolder.getMixInHolder().isIgnoreProperty(Record.class, "properties")) {
            return null;
        }
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
