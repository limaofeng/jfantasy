package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.PropertiesConverter;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.member.bean.enums.InviteStatus;

import javax.persistence.*;
import java.util.Properties;

/**
 * 邀请
 */
@Entity
@Table(name = "MEM_INVITE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Invite extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 用户名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private InviteStatus status;
    /**
     * 用户
     */
    @Column(name = "member")
    private String member;
    /**
     * 动态属性
     */
    @ApiModelProperty(hidden = true)
    @Convert(converter = PropertiesConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "Text")
    private Properties properties;
    /**
     * 团队ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", foreignKey = @ForeignKey(name = "FK_INVITE_TEAM"))
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public void setStatus(InviteStatus status) {
        this.status = status;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonAnyGetter
    public Properties getProperties() {
        if (ThreadJacksonMixInHolder.getMixInHolder().isIgnoreProperty(Invite.class, "properties")) {
            return null;
        }
        return this.properties;
    }

    @JsonAnySetter
    public void set(String key, String value) {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        this.properties.put(key, value);
    }

    @Transient
    public String get(String key) {
        if (this.properties == null) return null;
        return this.properties.getProperty(key);
    }

}
