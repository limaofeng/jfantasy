package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.framework.dao.hibernate.converter.StringsConverter;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.enums.TeamMemberStatus;
import org.jfantasy.security.bean.enums.Sex;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Properties;

/**
 * 团队成员
 */
@Entity
@Table(name = "MEM_TEAM_MEMBER", uniqueConstraints = @UniqueConstraint(name = "UK_TEAM_MEMBER", columnNames = {"MEMBER_ID", "TEAM_ID"}))
@GenericGenerator(name = "team_member_gen", strategy = "enhanced-table",
        parameters = {
                @Parameter(name = "table_name", value = "sys_sequence"),
                @Parameter(name = "value_column_name", value = "gen_value"),
                @Parameter(name = "segment_column_name", value = "gen_name"),
                @Parameter(name = "segment_value", value = "mem_team_member:id"),
                @Parameter(name = "increment_size", value = "10"),
                @Parameter(name = "optimizer", value = "pooled-lo")
        })
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class TeamMember extends BaseBusEntity {

    private static final long serialVersionUID = -7880093458033934231L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "team_member_gen")
    private Long id;
    /**
     * 用户名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 性别
     */
    @Column(name = "SEX")
    private Sex sex;
    /**
     * 邮箱
     */
    @Column(name = "EMAIL")
    private String email;
    /**
     * 电话
     */
    @Column(name = "MOBILE")
    private String mobile;
    /**
     * 身份证
     */
    @Column(name = "ID_CARD")
    private String idCard;
    /**
     * 岗位
     */
    @Column(name = "POSITION")
    private String position;
    /**
     * 部门
     */
    @Column(name = "DEPT")
    private String dept;
    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TeamMemberStatus status;
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_TEAM_MEMBER_MEMBER"))
    private Member member;
    /**
     * 动态属性
     */
    @Convert(converter = MapConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "Text")
    private Properties properties;
    /**
     * 团队ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TEAM_MEMBER_TEAM"))
    private Team team;
    /**
     * 标签
     */
    @Convert(converter = StringsConverter.class)
    @Column(name = "TAGS", length = 2000)
    private String[] tags;

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

    public TeamMemberStatus getStatus() {
        return status;
    }

    public void setStatus(TeamMemberStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    @NotNull(groups = RESTful.POST.class)
    public String getTeamId() {
        return this.team == null ? null : this.team.getKey();
    }

    public void setTeamId(String id) {
        this.team = new Team();
        this.team.setKey(id);
    }

    @Transient
    @NotNull(groups = RESTful.POST.class)
    public Long getMemberId() {
        return this.member == null ? null : this.member.getId();
    }

    public void setMemberId(Long id) {
        this.member = new Member();
        this.member.setId(id);
    }
}
