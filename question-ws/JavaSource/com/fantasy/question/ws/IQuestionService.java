package com.fantasy.question.ws;

import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.question.ws.dto.AnswerDTO;
import com.fantasy.question.ws.dto.AnswerPagerResult;
import com.fantasy.question.ws.dto.QuestionDTO;
import com.fantasy.question.ws.dto.QuestionPagerResult;

/**
 * 问答接口
 * Created by zzzhong on 2014/12/5.
 */
public interface IQuestionService {
    /**
     * 查询问题列表
     * @param pager
     * @param filters  LIKES_titile   EQS_title
     *
     * @return
     */
    public QuestionPagerResult findPager(PagerDTO pager, PropertyFilterDTO[] filters);

    /**
     * 获取热门问题列表
     * @param pager
     * @param filters   LIKES_titile   EQS_title
     * @return
     */
    public QuestionPagerResult findTopPager(PagerDTO pager, PropertyFilterDTO[] filters);

    /**
     * 通过问题id查找回答pager对象
     * @param id parentId
     * @param pager
     * @return
     */
    public AnswerPagerResult findAnswerPager(Long id, PagerDTO pager);

    /**
     * 查看问题 设置回答pager
     * @param id
     * @return
     */
    public QuestionDTO findById(Long id, PagerDTO answerPager);

    /**
     * 保存问题
     * @return
     */
    public QuestionDTO save(QuestionDTO questionDTO);

    /**
     * 保存回答
     * @param answerDTO
     * @return
     */
    public AnswerDTO saveAnswer(AnswerDTO answerDTO);

    /**
     * 设置回答 顶的操作
     * @return 设置成功
     */
    public boolean setAnswerTop(Long answerId);

    /**
     * 设置采纳
     * @param answer
     * @return
     */
    public boolean setAnswerAdoption(Long answer);






}
