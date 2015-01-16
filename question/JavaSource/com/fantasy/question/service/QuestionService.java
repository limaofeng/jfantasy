package com.fantasy.question.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.question.bean.Category;
import com.fantasy.question.bean.Question;
import com.fantasy.question.dao.CategoryDao;
import com.fantasy.question.dao.QuestionDao;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.util.List;

/**
 * Created by hebo on 2014/9/20.
 */
@Service
@Transactional
public class QuestionService  implements InitializingBean{


    private static final Log logger = LogFactory.getLog(QuestionService.class);

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Category category = getCategoryBySign("root");
            if (category == null) {
                StringBuffer log = new StringBuffer("初始化问题栏目根目录");
                category = new Category();
                category.setSort(0);
                category.setSign("root");
                category.setName("问题根目录");

                save(category);
                logger.debug(log);
            }
            // 菜单初始化
        } finally {
            transactionManager.commit(status);
        }
    }

    /**
     * 保存问题栏目
     *
     * @param category
     * @return
     */
    public Category save(Category category) {
        List<Category> categories;
        boolean root = false;
        Category old = category.getId() != null ? this.categoryDao.get(category.getId()) : null;
        if (category.getParent() == null || StringUtil.isBlank(category.getParent().getId())) {
            category.setLayer(0);
            category.setPath(StringUtil.defaultValue(category.getSign(), old != null ? old.getSign() : "") + Category.PATH_SEPARATOR);
            root = true;
            categories = ObjectUtil.sort(categoryDao.find(Restrictions.isNull("parent")), "sort", "asc");
        } else {
            Category parentCategory = this.categoryDao.get(category.getParent().getId());
            category.setLayer(parentCategory.getLayer() + 1);
            category.setPath(parentCategory.getPath() + StringUtil.defaultValue(category.getSign(), old != null ? old.getSign() : "") + Category.PATH_SEPARATOR);// 设置path
            categories = ObjectUtil.sort(categoryDao.findBy("parent.id", parentCategory.getId()), "sort", "asc");
        }
        if (old != null) {// 更新数据
            if (category.getSort() != null && (ObjectUtil.find(categories, "id", old.getId()) == null || !old.getSort().equals(category.getSort()))) {
                if (ObjectUtil.find(categories, "id", old.getId()) == null) {// 移动了节点的层级
                    int i = 0;
                    for (Category m : ObjectUtil.sort((old.getParent() == null || StringUtil.isBlank(old.getParent().getId())) ? categoryDao.find(Restrictions.isNull("parent")) : categoryDao.findBy("parent.id", old.getParent().getId()), "sort", "asc")) {
                        m.setSort(i++);
                        this.categoryDao.save(m);
                    }
                    categories.add(category.getSort() - 1, category);
                } else {
                    Category t = ObjectUtil.remove(categories, "id", old.getId());
                    if (categories.size() >= category.getSort()) {
                        categories.add(category.getSort() - 1, t);
                    } else {
                        categories.add(t);
                    }
                }
                // 重新排序后更新新的位置
                for (int i = 0; i < categories.size(); i++) {
                    Category m = categories.get(i);
                    if (m.getId().equals(category.getId())) {
                        continue;
                    }
                    m.setSort(i + 1);
                    this.categoryDao.save(m);
                }
            }
        } else {// 新增数据
            category.setSort(categories.size() + 1);
        }
        this.categoryDao.save(category);
        if (root) {
            category.setParent(null);
            this.categoryDao.update(category);
        }
        return category;
    }

    /**
     * 删除分类
     * @param ids
     */
    public void categoryDelete(Long[] ids){
        for(Long id:ids){
            this.categoryDao.delete(id);
        }
    }

    /**
     * 根据分类编码查询分类
     * @param sign
     * @return
     */
    public Category getCategoryBySign(String sign) {
        return this.categoryDao.findUnique(Restrictions.eq("sign",sign));
    }

    /**
     * 根据分类编码查询分类
     * @param id
     * @return
     */
    public Category getCategoryById(Long id){
        return  this.categoryDao.get(id);
    }

    /**
     * 获取分类集合
     * @return
     */
    public List<Category> getCategories() {
        return this.categoryDao.find(new Criterion[0], "layer,sort", "asc,asc");
    }

    /**
     * 获取分类集合
     * @param sign
     * @return
     */
    public List<Category> getCategories(String sign) {
        Category category = this.getCategoryBySign(sign);
        return this.categoryDao.find(new Criterion[]{Restrictions.like("path",category.getPath(), MatchMode.START),Restrictions.ne("sign",sign)},"layer,sort","asc,asc");
    }

    /**
     * 问题查询方法
     *
     * @param pager  翻页对象
     * @param filters 筛选条件
     * @return string
     */
    public Pager<Question> findPager(Pager<Question> pager, List<PropertyFilter> filters) {
        return questionDao.findPager(pager, filters);
    }

    /**
     * 保存问题对象
     *
     * @param question
     * @return question
     */
    public Question save(Question question) {
        this.questionDao.save(question);
        return question;
    }

    /**
     * 获取问题
     * @param id
     * @return
     */
    public Question get(Long id){
        return this.questionDao.get(id);
    }

    /**
     * 删除问题
     * @param ids
     */
    public void delete(Long... ids){
        for(Long id:ids){
            this.questionDao.delete(id);
        }
    }

    /**
     * 关闭问题
     * @param ids
     */
    public void close(Long[] ids){
        for(Long id:ids){
           Question question =this.questionDao.get(id);
           question.setStatus(Question.Status.close);
           this.save(question);
        }
    }

    /**
     * 打开问题
     * @param ids
     */
    public void  run(Long[] ids){
        for(Long id:ids){
            Question question =this.questionDao.get(id);
            question.setStatus(Question.Status.news);
            this.save(question);
        }
    }

    /**
     * 移动问题
     * @param ids
     * @param categoryId
     */
    public void moveQuestion(Long[] ids,Long categoryId){
        Category category = this.categoryDao.get(categoryId);
        for(Long id:ids){
            Question question = this.questionDao.get(id);
            question.setCategory(category);
            this.questionDao.save(question);
        }
    }


    /**
     * 根据传入编码跳转不同的界面
     * @param templateUrl
     * @param object
     * @return
     */
    public static String getTemplatePath(String templateUrl, final Object object) {
        if (object == null || (!(object instanceof Category) && !(object instanceof Question))) {
            return RegexpUtil.replace(templateUrl, "\\{sign\\}", "");
        }
        Configuration configuration = SpringContextUtil.getBean("freemarkerService", Configuration.class);
        Category category = object instanceof Category ? (Category) object : ((Question) object).getCategory();

        do {
            String newTemplateUrl = RegexpUtil.replace(templateUrl, "\\{sign\\}", "_" + category.getSign());
            try {
                configuration.getTemplate(newTemplateUrl);
                return newTemplateUrl;
            } catch (IOException e) {
                if (category.getParent() == null) {
                    break;
                }
                category = category.getParent();
            }
        } while (true);
        return RegexpUtil.replace(templateUrl, "\\{sign\\}", "");
    }

}
