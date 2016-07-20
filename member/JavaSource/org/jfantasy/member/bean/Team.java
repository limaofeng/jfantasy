package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;

import javax.persistence.*;
import java.util.Properties;

/**
 * 团队/小组
 */
@Entity
@Table(name = "MEM_TEAM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team extends BaseBusEntity {

    /**
     * 团队
     */
    @Id
    @Column(name = "CODE", nullable = false)
    private String key;
    /**
     * 名称
     */
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;
    /**
     * 所有者账号
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_TEAM_MEMBER"))
    private Member member;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    /**
     * 扩展属性
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "PROPERTIES", columnDefinition = "MediumBlob")
    private Properties properties;

    public Team(String id) {
        this();
        this.setKey(id);
    }

    public Team() {
        super();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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
        if (ThreadJacksonMixInHolder.getMixInHolder().isIgnoreProperty(Invite.class, "properties")) {
            return null;
        }
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
