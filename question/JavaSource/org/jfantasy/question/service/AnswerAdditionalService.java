package org.jfantasy.question.service;

import org.jfantasy.question.bean.AnswerAdditional;
import org.jfantasy.question.dao.AnswerAdditionalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnswerAdditionalService {

    @Autowired
    private AnswerAdditionalDao answerAdditionalDao;


    public AnswerAdditional save(AnswerAdditional answerAdditional){
        this.answerAdditionalDao.save(answerAdditional);
        return answerAdditional;
    }

}
