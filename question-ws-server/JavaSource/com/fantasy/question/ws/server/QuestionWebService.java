package com.fantasy.question.ws.server;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.member.service.MemberService;
import com.fantasy.question.bean.Answer;
import com.fantasy.question.bean.AnswerAdditional;
import com.fantasy.question.bean.Question;
import com.fantasy.question.service.AnswerAdditionalService;
import com.fantasy.question.service.AnswerService;
import com.fantasy.question.service.QuestionService;
import com.fantasy.question.ws.IQuestionService;
import com.fantasy.question.ws.dto.AnswerDTO;
import com.fantasy.question.ws.dto.AnswerPagerResult;
import com.fantasy.question.ws.dto.QuestionDTO;
import com.fantasy.question.ws.dto.QuestionPagerResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hebo on 2014/12/9.
 * 问题回答webservice实现
 */
@Component
@Transactional
public class QuestionWebService implements IQuestionService {

    @Resource
    private QuestionService questionService;

    @Resource
    private AnswerService answerService;

    @Resource
    private MemberService memberService;

    @Resource
    private AnswerAdditionalService answerAdditionalService;

    @Override
    public QuestionPagerResult findPager(PagerDTO pager, PropertyFilterDTO[] filters) {
        Pager<Question> questionPager = WebServiceUtil.toPager(pager, Question.class);
        List<PropertyFilter> propertyFilters = WebServiceUtil.toFilters(filters, QuestionDTO.class);
        questionPager = this.questionService.findPager(questionPager,propertyFilters);
        QuestionPagerResult questionPagerResult = new QuestionPagerResult();
        questionPagerResult.setTotalCount(questionPager.getTotalCount());
        questionPagerResult.setTotalPage(questionPager.getTotalPage());
        questionPagerResult.setPageSize(questionPager.getPageSize());
        questionPagerResult.setCurrentPage(questionPager.getCurrentPage());
        questionPagerResult.setOrder(questionPager.getOrder().toString());
        List<Question> questionList = questionPager.getPageItems();
        QuestionDTO[] questionDTOs = new QuestionDTO[questionList.size()];
        for(int i=0;i<questionList.size();i++){
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(questionList.get(i).getId());
            questionDTO.setTitle(questionList.get(i).getTitle());
            questionDTO.setContext(questionList.get(i).getContent());
            questionDTO.setAnswerCount(questionList.get(i).getSize());
            questionDTO.setTime(questionList.get(i).getCreateTime().getTime());
            String[] accounts = new String[4];
            accounts[0] = questionList.get(i).getMember().getUsername();
            if(questionList.get(i).getMember().getDetails()!=null && questionList.get(i).getMember().getDetails().getAvatar()!=null){
                    accounts[1] = questionList.get(i).getMember().getDetails().getAvatar().getAbsolutePath();
            }
            accounts[2] = questionList.get(i).getMember().getId().toString();
            accounts[3] = questionList.get(i).getMember().getNickName();
            questionDTO.setAccountDTO(accounts);
            questionDTO.setOfferMoney(questionList.get(i).getOfferMoney());
            questionDTO.setAskQuestion(questionList.get(i).getAskQuestion());
            questionDTOs[i] = questionDTO;
        }
        questionPagerResult.setPageItems(questionDTOs);
        return questionPagerResult;
    }

    @Override
    public QuestionPagerResult findTopPager(PagerDTO pager, PropertyFilterDTO[] filters) {
        Pager<Question> questionPager = WebServiceUtil.toPager(pager, Question.class);
        List<PropertyFilter> propertyFilters = WebServiceUtil.toFilters(filters, QuestionDTO.class);
        propertyFilters.add(new PropertyFilter("EQB_issue","true"));
        questionPager = this.questionService.findPager(questionPager,propertyFilters);
        QuestionPagerResult questionPagerResult = new QuestionPagerResult();
        questionPagerResult.setTotalCount(questionPager.getTotalCount());
        questionPagerResult.setTotalPage(questionPager.getTotalPage());
        questionPagerResult.setPageSize(questionPager.getPageSize());
        questionPagerResult.setCurrentPage(questionPager.getCurrentPage());
        questionPagerResult.setOrder(questionPager.getOrder().toString());
        List<Question> questionList = questionPager.getPageItems();
        QuestionDTO[] questionDTOs = new QuestionDTO[questionList.size()];
        for(int i=0;i<questionList.size();i++){
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(questionList.get(i).getId());
            questionDTO.setTitle(questionList.get(i).getTitle());
            questionDTO.setContext(questionList.get(i).getContent());
            questionDTO.setAnswerCount(questionList.get(i).getSize());
            questionDTO.setTime(questionList.get(i).getCreateTime().getTime());
            String[] accounts = new String[4];
            accounts[0] = questionList.get(i).getMember().getUsername();
            if(questionList.get(i).getMember().getDetails()!=null  && questionList.get(i).getMember().getDetails().getAvatar()!=null){
                    accounts[1] = questionList.get(i).getMember().getDetails().getAvatar().getAbsolutePath();
            }
            accounts[2] = questionList.get(i).getMember().getId().toString();
            accounts[3] = questionList.get(i).getMember().getNickName();
            questionDTO.setAccountDTO(accounts);
            questionDTO.setOfferMoney(questionList.get(i).getOfferMoney());
            questionDTO.setAskQuestion(questionList.get(i).getAskQuestion());
            questionDTOs[i] = questionDTO;
        }
        questionPagerResult.setPageItems(questionDTOs);
        return questionPagerResult;
    }

    @Override
    public AnswerPagerResult findAnswerPager(Long id, PagerDTO pager) {
        Pager<Answer> answerPager = WebServiceUtil.toPager(pager, Answer.class);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQL_question.id",id.toString()));
        answerPager = this.answerService.findPager(answerPager,filters);
        AnswerPagerResult answerPagerResult = new AnswerPagerResult();
        answerPagerResult.setTotalCount(answerPager.getTotalCount());
        answerPagerResult.setTotalPage(answerPager.getTotalPage());
        answerPagerResult.setPageSize(answerPager.getPageSize());
        answerPagerResult.setCurrentPage(answerPager.getCurrentPage());
        answerPagerResult.setOrder(answerPager.getOrder().toString());
        List<Answer> answerList = answerPager.getPageItems();
        AnswerDTO[] answerDTOs = answerListToArr(answerList);
        answerPagerResult.setPageItems(answerDTOs);
        return answerPagerResult;
    }

    @Override
    public QuestionDTO findById(Long id,PagerDTO answerPager) {
        Question question = this.questionService.get(id);
        Pager<Answer> pager = WebServiceUtil.toPager(answerPager, Answer.class);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQL_question.id",id.toString()));
        pager = this.answerService.findPager(pager,filters);
        AnswerPagerResult answerPagerResult = new AnswerPagerResult();
        answerPagerResult.setTotalCount(pager.getTotalCount());
        answerPagerResult.setTotalPage(pager.getTotalPage());
        answerPagerResult.setPageSize(pager.getPageSize());
        answerPagerResult.setCurrentPage(pager.getCurrentPage());
        answerPagerResult.setOrder(pager.getOrder().toString());
        List<Answer> answerList = pager.getPageItems();
        AnswerDTO[] answerDTOs = answerListToArr(answerList);
        answerPagerResult.setPageItems(answerDTOs);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setTitle(question.getTitle());
        questionDTO.setContext(question.getContent());
        questionDTO.setAnswerCount(question.getSize());
        questionDTO.setTime(question.getCreateTime().getTime());
        String[] accounts = new String[4];
        accounts[0] = question.getMember().getUsername();
        if(question.getMember().getDetails()!=null && question.getMember().getDetails().getAvatar()!=null){
                accounts[1] = question.getMember().getDetails().getAvatar().getAbsolutePath();
        }
        accounts[2] = question.getMember().getId().toString();
        accounts[3] = question.getMember().getNickName();
        questionDTO.setAccountDTO(accounts);
        questionDTO.setOfferMoney(question.getOfferMoney());
        questionDTO.setAskQuestion(question.getAskQuestion());
        questionDTO.setChildren(answerPagerResult);
        return questionDTO;
    }

    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContext());
        question.setStatus(Question.Status.news);
        question.setCategory(this.questionService.getCategoryBySign("root"));
        if(questionDTO.getAccountDTO()!=null){
            question.setMember(this.memberService.findUniqueByUsername(questionDTO.getAccountDTO()[0]));
        }
        question.setOfferMoney(questionDTO.getOfferMoney());
        question.setIssue(false);
        this.questionService.save(question);
        questionDTO.setId(question.getId());
        questionDTO.setTitle(question.getTitle());
        questionDTO.setContext(question.getContent());
        questionDTO.setAnswerCount(question.getSize());
        questionDTO.setTime(question.getCreateTime().getTime());
        String[] accounts = new String[4];
        accounts[0] = question.getMember().getUsername();
        if(question.getMember().getDetails()!=null && question.getMember().getDetails().getAvatar()!=null){
                accounts[1] = question.getMember().getDetails().getAvatar().getAbsolutePath();
        }
        accounts[2] = question.getMember().getId().toString();
        accounts[3] = question.getMember().getNickName();
        questionDTO.setAccountDTO(accounts);
        questionDTO.setOfferMoney(question.getOfferMoney());
        return questionDTO;
    }

    @Override
    public AnswerDTO saveAnswer(AnswerDTO answerDTO) {
        if(answerDTO.getId()==null){
            Answer answer = new Answer();
            answer.setContent(answerDTO.getContext());
            answer.setLevel(Answer.Level.comment);
            if(answerDTO.getAccountDTO()!=null){
                answer.setMember(this.memberService.findUniqueByUsername(answerDTO.getAccountDTO()[0]));
            }
            answer.setQuestion(this.questionService.get(answerDTO.getParentId()));
            this.answerService.save(answer);
            answerDTO.setId(answer.getId());
            answerDTO.setParentId(answer.getQuestion().getId());
            answerDTO.setLevel(answer.getLevel().toString());
            if(answer.getMember().getId().equals(answer.getQuestion().getMember().getId())){
                answerDTO.setType("追问");
            }else{
                answerDTO.setType("回答");
            }
            answerDTO.setContext(answer.getContent());
            answerDTO.setTime(answer.getCreateTime().toString());
            String[] accounts = new String[4];
            accounts[0] = answer.getMember().getUsername();
            if(answer.getMember().getDetails()!=null && answer.getMember().getDetails().getAvatar()!=null){
                    accounts[1] = answer.getMember().getDetails().getAvatar().getAbsolutePath();
            }
            accounts[2] = answer.getMember().getId().toString();
            accounts[3] = answer.getMember().getNickName();
            answerDTO.setAccountDTO(accounts);
            answerDTO.setTopCount(answer.getPraise());
        }else {
            AnswerAdditional answerAdditional = new AnswerAdditional();
            answerAdditional.setAnswer(this.answerService.get(answerDTO.getId()));
            answerAdditional.setContent(answerDTO.getContext());
            if(answerDTO.getAccountDTO()!=null){
                answerAdditional.setMember(this.memberService.findUniqueByUsername(answerDTO.getAccountDTO()[0]));
            }
            this.answerAdditionalService.save(answerAdditional);
            answerDTO.setParentId(answerDTO.getId());
            answerDTO.setId(answerAdditional.getId());
            answerDTO.setType("追问");
            answerDTO.setContext(answerAdditional.getContent());
            answerDTO.setTime(answerAdditional.getCreateTime().toString());
            String[] accounts = new String[4];
            accounts[0] = answerAdditional.getMember().getUsername();
            if(answerAdditional.getMember().getDetails()!=null && answerAdditional.getMember().getDetails().getAvatar()!=null){
                accounts[1] = answerAdditional.getMember().getDetails().getAvatar().getAbsolutePath();
            }
            accounts[2] = answerAdditional.getMember().getId().toString();
            accounts[3] = answerAdditional.getMember().getNickName();
            answerDTO.setAccountDTO(accounts);
        }

        return answerDTO;
    }

    @Override
    public boolean setAnswerTop(Long answerId) {
        Answer answer = this.answerService.get(answerId);
        Integer praise = answer.getPraise();
        answer.setPraise(praise+1);
        this.answerService.statusSava(answer);
        return praise.equals(answer.getPraise()) ? false : true;
    }

    @Override
    public boolean setAnswerAdoption(Long answer) {
        Answer answers = this.answerService.get(answer);
        answers.setLevel(Answer.Level.acception);
        this.answerService.statusSava(answers);
        return "acception".equals(answers.getLevel().toString()) ? true : false;

    }


    private AnswerDTO[] answerChildrensToDTO(Answer answer){
        AnswerDTO[] answerDTOs = new AnswerDTO[answer.getAdditionals().size()];
        for(int j= 0;j<answer.getAdditionals().size();j++){
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setParentId(answer.getId());
            answerDTO.setContext(answer.getAdditionals().get(j).getContent());
            answerDTO.setType("追问");
            String[] member = new String[4];
            member[0] = answer.getAdditionals().get(j).getMember().getUsername();
            if(answer.getAdditionals().get(j).getMember().getDetails()!=null && answer.getAdditionals().get(j).getMember().getDetails().getAvatar()!=null){
                    member[1] = answer.getAdditionals().get(j).getMember().getDetails().getAvatar().getAbsolutePath();
            }
            member[2] = answer.getAdditionals().get(j).getMember().getId().toString();
            member[3] = answer.getAdditionals().get(j).getMember().getNickName();
            answerDTO.setAccountDTO(member);
            answerDTOs[j] =  answerDTO;
        }
        return answerDTOs;
    }

    private AnswerDTO answerToDto(Answer answer){
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setParentId(answer.getId());
        answerDTO.setLevel(answer.getLevel().toString());
        if(answer.getMember().getId().equals(answer.getQuestion().getMember().getId())){
            answerDTO.setType("追问");
        }else{
            answerDTO.setType("回答");
        }
        answerDTO.setContext(answer.getContent());
        answerDTO.setTime(answer.getCreateTime().toString());
        String[] accounts = new String[4];
        accounts[0] = answer.getMember().getUsername();
        if(answer.getMember().getDetails()!=null && answer.getMember().getDetails().getAvatar()!=null){
                accounts[1] = answer.getMember().getDetails().getAvatar().getAbsolutePath();
        }
        accounts[2] = answer.getMember().getId().toString();
        accounts[3] = answer.getMember().getNickName();
        answerDTO.setAccountDTO(accounts);
        answerDTO.setTopCount(answer.getPraise());
        AnswerDTO[] childrens = answerChildrensToDTO(answer);
        answerDTO.setChildren(childrens);
        return answerDTO;
    }

    private AnswerDTO[] answerListToArr(List<Answer> answerList){
        AnswerDTO[] answerDTOs = new AnswerDTO[answerList.size()];
        for(int i= 0;i<answerList.size();i++){
            answerDTOs[i] = answerToDto(answerList.get(i));
        }
        return answerDTOs;
    }



}
