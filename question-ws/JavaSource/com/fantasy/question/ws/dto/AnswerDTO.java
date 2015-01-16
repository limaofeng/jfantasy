package com.fantasy.question.ws.dto;

/**
 * 回答对象
 * Created by zzzhong on 2014/12/5.
 */
public class AnswerDTO {

    /**
     * 追问id
     */
    private Long id;
    /**
     * 问题id
     */
    private Long parentId;

    /**
     * 回答等级
     * 应可以按照level排序  提问者采纳
     */
    private String level;

    /**
     * 回答类型 默认是回答，追问
     */
    private String type;

    /**
     * 只在查answer的分页对象的时候与查看问题详情的时候 需要添加追问集合 其
     */
    private AnswerDTO[] children;

    /**
     * 回复内容
     */
    private String context;

    /**
     * 回复时间
     */
    private String time;

    /**
     * [0]=用户名[1]=头像url[2]=id
     */
    private String[] accountDTO=new String[4];

    /**
     * 顶的数量
     */
    private Integer topCount;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(String[] accountDTO) {
        this.accountDTO = accountDTO;
    }

    public Integer getTopCount() {
        return topCount;
    }

    public void setTopCount(Integer topCount) {
        this.topCount = topCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public AnswerDTO[] getChildren() {
        return children;
    }

    public void setChildren(AnswerDTO[] children) {
        this.children = children;
    }
}
