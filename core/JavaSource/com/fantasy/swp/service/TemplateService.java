package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.dao.TemplateDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("swp.page.templateService")
@Transactional
public class TemplateService {

    @Resource(name = "swp.page.template")
    private TemplateDao templateDao;
    @Resource
    private DataInferfaceService dataInferfaceService;

    public Pager<Template> findPager(Pager<Template> pager, List<PropertyFilter> filters) {
        return this.templateDao.findPager(pager, filters);
    }

    public void save(Template template) {
        this.templateDao.save(template);
    }

    public Template get(Long id) {
        return this.templateDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            Template template = this.templateDao.get(id);
            System.out.println("delete...." + id);
            if(template.getDataInferfaces()!=null && template.getDataInferfaces().size()>0){
                Long[] dids = new Long[template.getDataInferfaces().size()];
                for(int i=0; i<template.getDataInferfaces().size(); i++){
                    dids[i] = template.getDataInferfaces().get(i).getId();
                }
                dataInferfaceService.delete(dids);
            }
            this.templateDao.delete(id);
        }
    }

    public List<Template> find(List<PropertyFilter> filters){
        return this.templateDao.find(filters);
    }
}

