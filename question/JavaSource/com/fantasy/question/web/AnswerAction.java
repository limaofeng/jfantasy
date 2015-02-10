package com.fantasy.question.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.question.bean.Answer;
import com.fantasy.question.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hebo on 2014/10/16.
 */
public class AnswerAction extends ActionSupport {

    @Autowired
    private AnswerService answerService;


    public String save(Answer answer){
        this.attrs.put(ROOT,this.answerService.save(answer));
        return JSONDATA;
    }

}
