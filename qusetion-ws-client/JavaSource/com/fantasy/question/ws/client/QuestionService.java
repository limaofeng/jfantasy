package com.fantasy.question.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.question.ws.IQuestionService;
import com.fantasy.question.ws.dto.AnswerDTO;
import com.fantasy.question.ws.dto.AnswerPagerResult;
import com.fantasy.question.ws.dto.QuestionDTO;
import com.fantasy.question.ws.dto.QuestionPagerResult;

public class QuestionService extends WebServiceClient implements IQuestionService {

    public QuestionService(){
        super("QuestionService");
    }


    @Override
    public QuestionPagerResult findPager(PagerDTO pager, PropertyFilterDTO[] filters) {
        return super.invokeOption("findPager",new Object[] { pager,filters },QuestionPagerResult.class);
    }

    @Override
    public QuestionPagerResult findTopPager(PagerDTO pager, PropertyFilterDTO[] filters) {
        return super.invokeOption("findTopPager",new Object[] { pager,filters },QuestionPagerResult.class);
    }

    @Override
    public AnswerPagerResult findAnswerPager(Long id, PagerDTO pager) {
        return super.invokeOption("findAnswerPager",new Object[] { id,pager },AnswerPagerResult.class);
    }

    @Override
    public QuestionDTO findById(Long id, PagerDTO answerPager) {
        return super.invokeOption("findById",new Object[] { id,answerPager },QuestionDTO.class);
    }

    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        return super.invokeOption("save",new Object[] {questionDTO },QuestionDTO.class);
    }

    @Override
    public AnswerDTO saveAnswer(AnswerDTO answerDTO) {
        return super.invokeOption("saveAnswer",new Object[] {answerDTO },AnswerDTO.class);
    }

    @Override
    public boolean setAnswerTop(Long answerId) {
        return super.invokeOption("setAnswerTop",new Object[] {answerId },Boolean.class);
    }

    @Override
    public boolean setAnswerAdoption(Long answer) {
        return super.invokeOption("setAnswerAdoption",new Object[] {answer },Boolean.class);
    }

}
