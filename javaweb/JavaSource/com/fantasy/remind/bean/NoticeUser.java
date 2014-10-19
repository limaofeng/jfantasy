package com.fantasy.remind.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.security.bean.User;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 推送公告关联用户表
 */

@Entity
@Table(name="tbl_notice_user")
public class NoticeUser extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;


    /**
     * 对应的公告
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NOTICE_ID")
    @ForeignKey(name="FK_NOTICE_USER")
    private Notice notice;

    /**
     * 推送公告对应的用户
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
