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

import java.util.List;


public class ArticleAction extends ActionSupport {

    @Autowired
    private CmsService cmsService;

    public String search(Pager<Article> pager,List<PropertyFilter> filters){
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
        this.attrs.put("pager",this.cmsService.findPager(pager,filters));
        return SUCCESS;
    }

    public String view(Long id) {
        this.attrs.put("article", cmsService.get(id));
        return SUCCESS;
    }

    public String create(Article article){
        this.attrs.put(ROOT, cmsService.save(article));
        return SUCCESS;
    }

    public String delete(Long... id){
        this.cmsService.delete(id);
        return SUCCESS;
    }

    public String update(Article article){
        this.attrs.put("article", cmsService.save(article));
        return SUCCESS;
    }



}
