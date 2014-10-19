package com.fantasy.remind.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 公告
 */

@Entity
@Table(name="tbl_notice")
public class Notice extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 公告标题
     */
    @Column(name="TITLE",length = 500)
    private String title;


    /**
     * 公告内容
     */
    @Column(name="CONTENT")
    private String content;

    /**
     * 是否失效 (true：(默认未失效) ,flase)
     */
    @Column(name="ISSUE")
    private boolean issue;

    /**
     * 公告开始时间
     */
    @Column(name="START_DATE")
    private Date startDate;

    /**
     * 公告失效时间
     */
    @Column(name="END_DATE")
    private Date endDate;

    /**
     * 公告推送用户关联表
     */
    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<NoticeUser> noticeUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<NoticeUser> getNoticeUsers() {
        return noticeUsers;
    }

    public void setNoticeUsers(List<NoticeUser> noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public boolean isIssue() {
        return issue;
    }

    public void setIssue(boolean issue) {
        this.issue = issue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
