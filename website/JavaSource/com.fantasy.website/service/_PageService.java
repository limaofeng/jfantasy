package org.jfantasy.website.service;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.filestore.FileManager;
import org.jfantasy.filestore.manager.LocalFileManager;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.website.bean.Data;
import org.jfantasy.website.bean.Page;
import org.jfantasy.website.dao.PageDao;
import org.jfantasy.website.runtime.GeneratePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class _PageService {

    @Autowired
    private PageDao pageDao;

    public Pager<Page> findPager(Pager<Page> pager, List<PropertyFilter> filters) {
        return this.pageDao.findPager(pager, filters);
    }

    public void save(Page page) {
        Page dbpage = this.findUniqueByPath(page.getPath(),page.getWebSite().getId());
        if(dbpage!=null){
            page.setId(dbpage.getId());
        }
        this.pageDao.save(page);

    }

    public Page get(Long id) {
        return this.pageDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.delete(id);
        }
    }

    public void delete(Long id){
        this.pageDao.delete(id);
    }

    public void generation(Long id) {
    }

    public List<Page> find(List<PropertyFilter> filters) {
        return this.pageDao.find(filters);
    }

    public void create(Long[] ids){
        try {
//            AdminUser adminUser = (AdminUser)SpringSecurityUtils.getCurrentUser();
//            String username = adminUser.getUser().getUsername();
            FileManager fileManager = SpringContextUtil.getBeanByType(LocalFileManager.class);
            for(Long id : ids){
                Page page = this.pageDao.get(id);
                GeneratePage generatePage = new GeneratePage(page,fileManager);
                generatePage.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Page findUniqueByPath(String url, Long websiteId) {
        Page page = this.pageDao.findUnique(Restrictions.eq("path", url),Restrictions.eq("webSite.id", websiteId));
        if(page!=null){
            Hibernate.initialize(page.getWebSite().getDefaultFileManager());
            Hibernate.initialize(page.getTemplate().getDataInferfaces());
            Hibernate.initialize(page.getPageItems());
            Hibernate.initialize(page.getDatas());
            for(Data data : page.getDatas()){
                Hibernate.initialize(data.getDataInferface());
            }
        }
        return page;
    }

    public void deleteByPath(String url,Long websiteId) {
        Page page = this.findUniqueByPath(url, websiteId);
        if(page!=null){
            this.delete(page.getId());
        }
    }
}

