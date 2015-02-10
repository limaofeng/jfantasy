package com.fantasy.mall.member.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.mall.goods.bean.Goods;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * 商品评论表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:36:07
 */
@Entity
@Table(name = "MALL_MEM_COMMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Comment extends BaseBusEntity {

    private static final long serialVersionUID = 8413023474799399082L;

    public static final int DEFAULT_COMMENT_LIST_PAGE_SIZE = 12;// 商品评论默认每页显示数

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    private Long id;
    @Column(name = "USERNAME")
    private String username;// 用户名
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;// 内容
    @Column(name = "CONTACT", nullable = false)
    private String contact;// 联系方式
    @Column(name = "IP", nullable = false, length = 15)
    private String ip;// IP
    @Column(name = "IS_SHOW", nullable = false)
    private boolean show;// 是否显示
    @Column(name = "ADMIN_REPLY", nullable = false)
    private boolean adminReply;// 是否为管理员回复
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID", nullable = false, updatable = false,foreignKey =  @ForeignKey(name = "FK_COMMENT_GOODS"))

    private Goods goods;// 商品
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOR_COMMENT_ID",foreignKey =  @ForeignKey(name = "FK_COMMENT_FOR_COMMENT"))

    private Comment forComment;// 评论
    @OneToMany(mappedBy = "forComment", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<Comment> replyComments;// 回复

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public boolean isAdminReply() {
        return adminReply;
    }

    public void setAdminReply(boolean adminReply) {
        this.adminReply = adminReply;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

}
