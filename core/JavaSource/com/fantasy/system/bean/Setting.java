package com.fantasy.system.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 参数设置
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2014-2-17 上午11:16:22
 */
@Entity
@Table(name = "SYS_SETTING", uniqueConstraints = {@UniqueConstraint(columnNames = {"WEBSITE_ID", "CODE"})})
public class Setting extends BaseBusEntity {

    private static final long serialVersionUID = 4751489388437713481L;

    public Setting() {
    }

    private Setting(String key,String name) {
        this.key = key;
        this.name= name;
    }

    private Setting(Website website, String name, String key, String value, String description) {
        this.website = website;
        this.key = key;
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public static Setting newInstance(String key,String name) {
        return new Setting(key, name);
    }

    public static Setting newInstance(Website website, String name, String key, String value, String description) {
        return new Setting(website, name, key, value, description);
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 对应的站点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WEBSITE_ID")
    private Website website;
    /**
     * 键
     */
    @Column(name = "CODE", length = 10)
    private String key;
    /**
     * 值
     */
    @Column(name = "VALUE", length = 200)
    private String value;
    /**
     * 名称
     */
    @Column(name = "name", length = 50)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
