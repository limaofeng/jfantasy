package com.fantasy.question.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.member.bean.Member;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 追问（答）
 */
@Entity
@Table(name="YR_ANSWERADDITIONAL")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnswerAdditional extends BaseBusEntity {

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;


    /**
     * 所属回答
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANSWER_ID", nullable = false,foreignKey = @ForeignKey(name = "FK_AA_ANSWER"))
    private Answer answer;

    /**
     * 回答内容
     */
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    /**
     * 追答对应的会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",foreignKey = @ForeignKey(name = "FK_ANSWERADDITIONAL_MEMBER"))
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
