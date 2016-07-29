package org.jfantasy.archives.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.archives.bean.enums.Sex;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.DateUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 人员信息
 */
@ApiModel(" 人员信息 ")
@Entity
@Table(name = "ARCH_PERSON")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Person extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @ApiModelProperty("编号")
    @Column(name = "NUMBER", unique = true, nullable = false, updatable = false, length = 30)
    private String no;
    @ApiModelProperty(value = "类型", notes = "用于分类不同的人员信息。")
    @Column(name = "TYPE", length = 10)
    private String type;
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Record> records;
    /* 基本信息 */
    @ApiModelProperty("名称")
    @Column(name = "NAME", length = 30)
    private String name;
    @ApiModelProperty("生日")
    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
    @ApiModelProperty("性别")
    @Enumerated(EnumType.STRING)
    @Column(name = "SEX", length = 10)
    private Sex sex;
    /**
     * 用户分支属性
     */
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Feature> features;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @ApiModelProperty("年龄")
    @Transient
    public Integer getAge() {
        if (this.birthday == null) {
            return null;
        }
        return DateUtil.age(this.birthday);
    }

}
