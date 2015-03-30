package com.fantasy.cms.web;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.util.SettingUtil;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章
 */
public class CmsAction extends ActionSupport {

    private static final long serialVersionUID = 3799834983783507214L;

    @Autowired
    private transient CmsService cmsService;

    /**
     * 文章主页面
     *
     * @return string
     */
    public String article() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<ArticleCategory> categories;
        String rootCode = SettingUtil.getValue("cms");
        if(StringUtil.isNotBlank(rootCode)){
            categories = cmsService.getCategorys(rootCode);
        }else{
            categories = cmsService.getCategorys();
        }
        filters.add(new PropertyFilter("EQS_category.code",categories.isEmpty()?rootCode:categories.get(0).getCode()));
        // 设置当前根
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "EQS_category.code");
        if (filter != null) {
            this.attrs.put("category", ObjectUtil.find(categories, "code", filter.getPropertyValue(String.class)));
        }
        this.attrs.put("categorys", categories);
        this.search(new Pager<Article>(), filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 文章列表页
     *
     * @return string
     */
    public String index(Pager<Article> pager, List<PropertyFilter> filters) {
        // 设置当前根
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "EQS_category.code");
        if (filter != null) {
            this.attrs.put("category", this.cmsService.get(filter.getPropertyValue(String.class)));
        }
        // 全部分类
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 文章搜索
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return string
     */
    public String search(Pager<Article> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, cmsService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 文章保存
     *
     * @param article 文章对象
     * @return string
     */
    public String articleSave(Article article) {
        this.attrs.put(ROOT, cmsService.save(article));
        return JSONDATA;
    }

    /**
     * 文章修改
     *
     * @param id 文章id
     * @return string
     */
    public String articleEdit(Long id) {
        this.attrs.put("art", this.cmsService.get(id));
        return SUCCESS;
    }

    /**
     * 文章显示
     *
     * @param id 文章id
     * @return string
     */
    public String articleView(Long id) {
        this.attrs.put("art", this.cmsService.get(id));
        return SUCCESS;
    }

    /**
     * 发布文章
     *
     * @param ids 文章id
     * @return string
     */
    public String articleIssue(Long[] ids) {
        this.cmsService.issue(ids);
        return JSONDATA;
    }

    /**
     * 关闭文章
     *
     * @param ids 文章id
     * @return string
     */
    public String colseissue(Long[] ids) {
        this.cmsService.colseissue(ids);
        return JSONDATA;
    }

    /**
     * 文章批量删除
     *
     * @param ids 文章id
     * @return string
     */
    public String articleDelete(Long[] ids) {
        this.cmsService.delete(ids);
        return JSONDATA;
    }

    /**
     * 文章栏目保存
     *
     * @param category 文章分类
     * @return string
     */
    public String categorySave(ArticleCategory category) {
        this.attrs.put(ROOT, this.cmsService.save(category));
        return JSONDATA;
    }

    /**
     * 文章栏目修改
     *
     * @param id 分类code
     * @return string
     */
    public String categoryEdit(String id) {
        this.attrs.put("category", this.cmsService.get(id));
        return SUCCESS;
    }


    /**
     * 删除文章栏目
     *
     * @param ids 分类code
     * @return string
     */
    public String categoryDelete(String[] ids) {
        for (String code : ids) {
            this.cmsService.delete(code);
        }
        return JSONDATA;
    }


    /**
     * 文章栏目添加
     *
     * @param categoryCode 分类code
     * @return string
     */
    public String categoryAdd(String categoryCode) {
        this.attrs.put("category", this.cmsService.get(categoryCode));
        return SUCCESS;
    }

    /**
     * 文章添加
     *
     * @param categoryCode 分类code
     * @return string
     */
    public String articleAdd(String categoryCode) {
        this.attrs.put("category", cmsService.get(categoryCode));
        return SUCCESS;
    }

    /**
     * 移动文章
     *
     * @param ids          文章ids
     * @param categoryCode 分类code
     * @return string
     */
    public String moveArticle(Long[] ids, String categoryCode) {
        this.cmsService.moveArticle(ids, categoryCode);
        return JSONDATA;
    }


}
