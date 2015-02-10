package com.fantasy.question.ws.dto;

/**
 * 问答
 * Created by zzzhong on 2014/11/25.
 */
public class QuestionDTO {
    /**
     * 问答id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String context;

    /**
     * 回答数量
     */
    private Integer answerCount;
    /**
     * 只需要在查详情的时候设置 追加回答 这时候需要设置筛选    回答级别为默认回答
     */
    private AnswerPagerResult children;
    /**
     * 创建时间
     */
    private Long time;

    /**
     * 创建人[0]=用户名[1]=头像url[2]=id
     */
    private String[] accountDTO=new String[4];

    /**
     * 悬赏金额
     */
    private Double offerMoney;

    /**
     * 最后一条回答数据
     */
    private String askQuestion;

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public AnswerPagerResult getChildren() {
        return children;
    }

    public void setChildren(AnswerPagerResult children) {
        this.children = children;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String[] getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(String[] accountDTO) {
        this.accountDTO = accountDTO;
    }

    public Double getOfferMoney() {
        return offerMoney;
    }

    public void setOfferMoney(Double offerMoney) {
        this.offerMoney = offerMoney;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public String getAskQuestion() {
        return askQuestion;
    }

    public void setAskQuestion(String askQuestion) {
        this.askQuestion = askQuestion;
    }
}
