package com.fantasy.question.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.member.service.MemberService;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.userdetails.AdminUser;
import com.fantasy.system.util.SettingUtil;
import com.yr.question.bean.Category;
import com.yr.question.bean.Question;
import com.yr.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hebo on 2014/9/22.
 */
public class QuestionAction extends ActionSupport {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MemberService memberService;

    /**
     * 问题首页
     * @param pager
     * @param filters
     * @return
     */
    public String question(Pager<Question> pager, List<PropertyFilter> filters) {
        List<Category> categories;
        String rootCode = SettingUtil.getValue("root");
        if (StringUtil.isNotBlank(rootCode)) {
            categories = questionService.getCategories(rootCode);
        } else {
            categories = questionService.getCategories();
        }
        // 默认选择根目录的第一个分类
        if (ObjectUtil.find(filters, "filterName", "EQL_category.id") == null) {
            filters.add(new PropertyFilter("EQL_category.id",categories.isEmpty()?rootCode:categories.get(0).getId().toString()));
        }
        // 设置当前根
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "EQL_category.id");
        if (filter != null) {
            this.attrs.put("category", ObjectUtil.find(categories, "id", filter.getPropertyValue(Long.class)));
        }
        // 全部分类
        this.attrs.put("categorys", categories);
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }


    /**
     * 问题列表
     * @param pager
     * @param filters
     * @return
     */
    public String index(Pager<Question> pager, List<PropertyFilter> filters) {
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "LIKES_name");
        if ((filter = ObjectUtil.find(filters, "filterName", "EQL_category.id")) != null) {
            this.attrs.put("category", this.questionService.getCategoryById(filter.getPropertyValue(Long.class)));
        } else if ((filter = ObjectUtil.find(filters, "filterName", "EQS_category.sign")) != null) {
            this.attrs.put("category", this.questionService.getCategoryBySign(filter.getPropertyValue(String.class)));
        }
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 问题查询
     *
     * @param pager   分页条件
     * @param filters 筛选条件
     * @return string
     */
    public String search(Pager<Question> pager, List<PropertyFilter> filters) {
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT, questionService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 问题保存
     * @param question
     * @return
     */
    public String save(Question question) {
        this.attrs.put(ROOT, questionService.save(question));
        return JSONDATA;
    }

    /**
     * 根据ID查询问题对象
     * @param id
     * @return
     */
    public String get(Long id){
        this.attrs.put("question",questionService.get(id));
        return SUCCESS;
    }

    /**
     * 删除问题
     * @param ids
     * @return
     */
    public String delete(Long[] ids){
        this.questionService.delete(ids);
        return JSONDATA;
    }

    /**
     * 问题分类保存
     * @param category
     * @return
     */
    public String categorySave(Category category){
        this.attrs.put(ROOT, questionService.save(category));
        return JSONDATA;
    }

    /**
     * 根据分类ID查询分类对象
     * @param id
     * @return
     */
    public String categoryEdit(Long id){
        this.attrs.put("category", questionService.getCategoryById(id));
        return SUCCESS;
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    public String categoryDelete(Long[]  ids){
        this.questionService.categoryDelete(ids);
        return JSONDATA;
    }

    /**
     * 问题移动分类
     * @param ids
     * @param categoryId
     * @return
     */
    public String moveQuestion(Long[] ids, Long categoryId) {
        this.questionService.moveQuestion(ids, categoryId);
        return JSONDATA;
    }

    /**
     * 关闭问题
     * @param ids
     * @return
     */
    public String close(Long[] ids){
        this.questionService.close(ids);
        return JSONDATA;
    }

    /**
     * 打开问题
     * @param ids
     * @return
     */
    public String run(Long[] ids){
        this.questionService.run(ids);
        return  JSONDATA;
    }


    public String askQuestion(Long id){
       Question question = questionService.get(id);
       Long memberId =question.getMember().getId();
       Long userId = SpringSecurityUtils.getCurrentUser(AdminUser.class).getUser().getId();
       this.attrs.put("memberList",this.memberService.queryMemberByUserAndId(userId,memberId,id));
       this.attrs.put("question",question);
       return SUCCESS;
    }
}
