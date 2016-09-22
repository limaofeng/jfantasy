package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:36:07
 */
@Entity
@Table(name = "MEM_COMMENT")
@TableGenerator(name = "comment_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "mem_comment:id", valueColumnName = "gen_value")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "for_comment"})
public class Comment extends BaseBusEntity {

    private static final long serialVersionUID = 8413023474799399082L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "comment_gen")
    @Column(name = "ID", updatable = false)
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @NotNull(groups = RESTful.POST.class)
    @Lob
    @Column(name = "CONTENT", updatable = false, nullable = false)
    private String content;
    @Column(name = "IP", updatable = false, nullable = false, length = 15)
    private String ip;
    @Column(name = "IS_SHOW", nullable = false)
    private boolean show;
    @NotNull(groups = RESTful.POST.class)
    @Column(name = "TARGET_TYPE", updatable = false, nullable = false)
    private String targetType;
    @NotNull(groups = RESTful.POST.class)
    @Column(name = "TARGET_ID", updatable = false, nullable = false)
    private String targetId;
    @Column(name = "PATH", updatable = false, nullable = false, length = 1000)
    private String path;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOR_COMMENT_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_COMMENT_FOR_COMMENT"))
    private Comment forComment;
    @OneToMany(mappedBy = "forComment", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<Comment> replyComments;
    @NotNull(groups = RESTful.POST.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MEMBER_ID", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "FK_SHIP_ADDRESS_MEMBER"))
    private Member member;
    /**
     * 扩展属性
     */
    @Convert(converter = MapConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "Text")
    private Map<String, Object> properties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Comment getForComment() {
        return forComment;
    }

    public void setForComment(Comment forComment) {
        this.forComment = forComment;
    }

    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    @Transient
    public Long getMemberId() {
        return this.member != null ? this.getMember().getId() : null;
    }

    @Transient
    public void setMemberId(Long memberId) {
        this.member = new Member(memberId);
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if (value == null) {
            return;
        }
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        if (ThreadJacksonMixInHolder.getMixInHolder().isIgnoreProperty(Comment.class, "properties")) {
            return null;
        }
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
