package com.fantasy.question.bean;


import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.member.bean.Member;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "YR_ANSWER")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Answer extends BaseBusEntity {

    public enum Level {
        /**
         * 普通回答
         */
        comment,
        /**
         * 提问者推荐回答
         */
        recommendation,
        /**
         * 提问者采纳回答
         */
        acception;
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;


    /**
     * 回答内容
     */
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    /**
     * 回答级别
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "LEVEL", nullable = false)
    private Level level;

    /**
     * 追问（答）
     */
    @OneToMany(mappedBy = "answer",fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<AnswerAdditional> additionals;


    /**
     * 回答对应的会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",foreignKey = @ForeignKey(name = "FK_ANSWER_MEMBER"))
    private Member member;

    /**
     * 问题
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ANSWER_QUESTION"))
    private Question question;

    /**
     * 好评，赞数量
     */
    @Column(name = "PRAISE",nullable = false,columnDefinition = "int default 0")
    private int praise;
    /**
     * 差评数量
     */
    @Column(name = "UNPRAISE",nullable = false,columnDefinition = "int default 0")
    private int unpraise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<AnswerAdditional> getAdditionals() {
        return additionals;
    }

    public void setAdditionals(List<AnswerAdditional> additionals) {
        this.additionals = additionals;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getUnpraise() {
        return unpraise;
    }

    public void setUnpraise(int unpraise) {
        this.unpraise = unpraise;
    }
}
