package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.dao.TemplateDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("swp.page.templateService")
@Transactional
public class TemplateService {

    @Resource(name = "swp.page.template")
    private TemplateDao templateDao;
    @Resource
    private DataInferfaceService dataInferfaceService;
    @Resource
    private _PageService pageService;

    public Pager<Template> findPager(Pager<Template> pager, List<PropertyFilter> filters) {
        return this.templateDao.findPager(pager, filters);
    }

    public void save(Template template) {
        // 模版地址是否相同，相同则覆盖更新
        Template dbtemplate = this.templateDao.findUniqueBy("path", template.getPath());
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
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            filters.add(new PropertyFilter("EQL_template.id",id+""));

            // 将page中关联的模版置空
            List<Page> pages = pageService.find(filters);
            if(pages!=null && pages.size()>0){
                for(Page page : pages){
                    page.setTemplate(null);
                    pageService.save(page);
                }
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
    }

    public List<Template> find(List<PropertyFilter> filters){
        return this.templateDao.find(filters);
    }

    public Template findUniqueByPath(String path){
        return this.templateDao.findUniqueBy("path", path);
    }
}

