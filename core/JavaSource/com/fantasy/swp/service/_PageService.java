package com.fantasy.swp.service;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.dao.PageDao;
import com.fantasy.swp.runtime.GeneratePage;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class _PageService {

    @Resource
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

