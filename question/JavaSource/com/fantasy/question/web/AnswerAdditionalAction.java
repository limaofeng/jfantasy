package com.fantasy.question.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.yr.question.bean.Answer;
import com.yr.question.bean.AnswerAdditional;
import com.yr.question.bean.Question;
import com.yr.question.service.AnswerAdditionalService;
import com.yr.question.service.AnswerService;
import com.yr.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hebo on 2014/10/17.
 */
public class AnswerAdditionalAction extends ActionSupport {

    @Autowired
    private AnswerAdditionalService answerAdditionalService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private MemberService memberService;


    @Autowired
    private QuestionService questionService;

    public String save(Long id,String content){
        Answer answer = this.answerService.get(id);
        Question question = this.questionService.get(answer.getQuestion().getId());
        Member member = this.memberService.get(question.getMember().getId());
        AnswerAdditional answerAdditional = new AnswerAdditional();
        answerAdditional.setMember(member);
        answerAdditional.setContent(content);
        answerAdditional.setAnswer(answer);
        this.attrs.put(ROOT,this.answerAdditionalService.save(answerAdditional));
        return JSONDATA;
    }


    public String zhuida(Long id,String content){
        Answer answer = this.answerService.get(id);
        Member member = this.memberService.get(answer.getMember().getId());
        AnswerAdditional answerAdditional = new AnswerAdditional();
        answerAdditional.setMember(member);
        answerAdditional.setContent(content);
        answerAdditional.setAnswer(answer);
        this.attrs.put(ROOT,this.answerAdditionalService.save(answerAdditional));
        return JSONDATA;
    }



}
