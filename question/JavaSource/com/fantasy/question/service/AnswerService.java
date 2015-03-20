package com.fantasy.question.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.question.bean.Answer;
import com.fantasy.question.bean.Question;
import com.fantasy.question.dao.AnswerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionService questionService;

    public Answer save(Answer answer){
        answer.setLevel(Answer.Level.comment);
        this.answerDao.save(answer);
        Question question = this.questionService.get(answer.getQuestion().getId());
        question.setSize(question.getSize()+1);
        question.setLastTime(answer.getCreateTime());
        question.setAskQuestion(answer.getContent());
        this.questionService.save(question);
        return answer;
    }

    public Pager<Answer> findPager(Pager<Answer> pager, List<PropertyFilter> filters) {
        return answerDao.findPager(pager, filters);
    }


    public void delete(Long... ids){
        for(Long id:ids){
            this.answerDao.delete(id);
        }
    }


    public List<Answer> queryMemberId(Long questionId){
        List<PropertyFilter> filters =  new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQL_question.id",String.valueOf(questionId)));
        List<Answer> answerList = this.answerDao.find(filters);
        return answerList;
    }


    public Answer get(Long id){
        return this.answerDao.get(id);
    }


    public Answer statusSava(Answer answer){
        this.answerDao.save(answer);
        return answer;
    }

}
