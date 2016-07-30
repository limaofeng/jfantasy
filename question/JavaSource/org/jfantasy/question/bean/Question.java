package org.jfantasy.question.bean;


import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.member.bean.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 问题
 */
@Entity
@Table(name = "YR_QUESTION")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "answers"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question extends BaseBusEntity {


    public enum Status {
        news("新问题"),
        adopted("已采纳"),
        close("已关闭");

        private String value;

        public String getValue() {
            return this.value;
        }

        private Status(String value) {
            this.value = value;
        }
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 标题
     */
    @Column(name = "TITLE", length = 50)
    private String title;
    /**
     * 内容
     */
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;


    /**
     * 问题状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "QUESTION_STATUS", length = 20, nullable = false)
    private Status status;


    /**
     * 问题分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_CATEGORY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_QUESTION_CATEGORY"))
    private Category category;


    /**
     * 问题回答
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime desc")
    private List<Answer> answers;

    /**
     * 问题对应的会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_QUESTION_MEMBER"))
    private Member member;

    /**
     * 回答问题的数量
     */
    @Column(name = "ANSWER_SIZE", nullable = false, columnDefinition = "int default 0")
    private int size;

    /**
     * 最后回答时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTTIME")
    private Date lastTime;

    /**
     * 最后一条回答的答案
     */
    @Column(name = "ASKQUESTION")
    private String askQuestion;

    /**
     * 悬赏金额
     */
    @Column(name = "OFFERMONEY")
    private Double offerMoney;

    /**
     * 是否为热门问题
     */
    @Column(name = "ISSUE")
    private Boolean issue;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getAskQuestion() {
        return askQuestion;
    }

    public void setAskQuestion(String askQuestion) {
        this.askQuestion = askQuestion;
    }

    public Double getOfferMoney() {
        return offerMoney;
    }

    public void setOfferMoney(Double offerMoney) {
        this.offerMoney = offerMoney;
    }

    public Boolean getIssue() {
        return issue;
    }

    public void setIssue(Boolean issue) {
        this.issue = issue;
    }
}

