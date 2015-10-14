package com.fantasy.website.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.website.bean.DataInferface;
import com.fantasy.website.bean.Page;
import com.fantasy.website.bean.Template;
import com.fantasy.website.dao.TemplateDao;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TemplateService {

    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private DataInferfaceService dataInferfaceService;
    @Autowired
    private _PageService pageService;

    public Pager<Template> findPager(Pager<Template> pager, List<PropertyFilter> filters) {
        return this.templateDao.findPager(pager, filters);
    }

    public void save(Template template) {
        // 模版地址是否相同，相同则覆盖更新
        Template dbtemplate = this.findUniqueByPath(template.getPath(),template.getWebSite().getId());
        if(dbtemplate!=null){
            template.setId(dbtemplate.getId());
        }
        try {
            this.templateDao.save(template);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Template get(Long id) {
        return this.templateDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.delete(id);
        }
    }

    public void delete(Long id){
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQL_template.id",id+""));

        List<Page> pages = pageService.find(filters);
        if(pages!=null && pages.size()>0){
            Long[] dids = new Long[pages.size()];
            for(int i=0; i<pages.size(); i++){
//                    //将page中关联的模版置空
//                    page.setTemplate(null);
//                    pageService.save(page);
                dids[i] = pages.get(i).getId();
            }
            pageService.delete(dids);
        }
        // 删除数据定义及数据
        List<DataInferface> dataInferfaces = dataInferfaceService.find(filters);
        if(dataInferfaces!=null && dataInferfaces.size()>0){
            Long[] dids = new Long[dataInferfaces.size()];
            for(int i=0; i<dataInferfaces.size(); i++){
                dids[i] = dataInferfaces.get(i).getId();
            }
            dataInferfaceService.delete(dids);
        }
        this.templateDao.delete(id);
    }

    public List<Template> find(List<PropertyFilter> filters){
        return this.templateDao.find(filters);
    }

    public Template findUniqueByPath(String path,Long websitId){
        return this.templateDao.findUnique(Restrictions.eq("path", path),Restrictions.eq("webSite.id", websitId));
    }

    public void deleteByPath(String path, Long websiteId) {
        Template template = this.findUniqueByPath(path, websiteId);
        if(template!=null){
            this.delete(template.getId());
        }
    }
}

