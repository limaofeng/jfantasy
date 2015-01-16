package com.fantasy.question.service;

import com.fantasy.question.bean.AnswerAdditional;
import com.fantasy.question.dao.AnswerAdditionalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("IziAnswerAdditionalService")
@Transactional
public class AnswerAdditionalService {

    @Autowired
    private AnswerAdditionalDao answerAdditionalDao;


    public AnswerAdditional save(AnswerAdditional answerAdditional){
        this.answerAdditionalDao.save(answerAdditional);
        return answerAdditional;
    }

}
