package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 评论表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:36:07
 */
@ApiModel("评论表")
@Entity
@Table(name = "MEM_COMMENT")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "forComment"})
public class Comment extends BaseBusEntity {

    private static final long serialVersionUID = 8413023474799399082L;

    @Id
    @Column(name = "ID", updatable = false)
    private Long id;
    @ApiModelProperty("用户名")
    @Column(name = "USERNAME")
    private String username;
    @ApiModelProperty("内容")
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;
    @ApiModelProperty("IP")
    @Column(name = "IP", nullable = false, length = 15)
    private String ip;
    @ApiModelProperty("是否显示")
    @Column(name = "IS_SHOW", nullable = false)
    private boolean show;
    @ApiModelProperty(value = "评论目标类型", notes = "评论目标类型,(如商品、医生等)")
    @Column(name = "TARGET_TYPE", updatable = false, nullable = false)
    private String targetType;
    @ApiModelProperty(value = "评论目标ID", notes = "评论目标ID,(如商品、医生等)")
    @Column(name = "TARGET_ID", updatable = false, nullable = false)
    private String targetId;
    @ApiModelProperty(value = "路径", notes = "该字段不需要手动维护")
    @Column(name = "PATH", nullable = false, length = 1000)
    private String path;
    @ApiModelProperty(value = "上个评论")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOR_COMMENT_ID", foreignKey = @ForeignKey(name = "FK_COMMENT_FOR_COMMENT"))
    private Comment forComment;
    @ApiModelProperty(hidden = true, value = "回复")
    @OneToMany(mappedBy = "forComment", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<Comment> replyComments;
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MEMBER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SHIP_ADDRESS_MEMBER"))
    private Member member;

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
