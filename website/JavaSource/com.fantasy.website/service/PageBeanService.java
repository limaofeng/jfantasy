package org.jfantasy.website.service;

import org.jfantasy.filestore.FileManager;
import org.jfantasy.filestore.service.FileManagerFactory;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.system.bean.Website;
import org.jfantasy.website.IPage;
import org.jfantasy.website.IPageItem;
import org.jfantasy.website.bean.*;
import org.jfantasy.website.runtime.GenerateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class PageBeanService {
    @Autowired
    private _PageService pageService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private GenerateImpl generate;
    @Autowired
    private PageItemService pageItemService;
    @Autowired
    private DataService dataService;
    @Autowired
    private FileManagerFactory fileManagerFactory;

    /**
     * 页面列表
     * @return
     */
    public List<IPage> listPage(){
        List<Page> pages = pageService.find(new ArrayList<PropertyFilter>());
        List<IPage> ipages = new ArrayList<IPage>();
        for(Page page : pages){
            PageBean pageBean = new PageBean();
            pageBean.setPage(page);
            ipages.add(pageBean);
        }
        return ipages;
    }

    /**
     * 保存
     * @param website 站点
     * @param url 页面存储相对路径
     * @param templatePath 模板
     * @param name 页面名称
     * @return
     */
    public IPage savePage(Website website,String url, String templatePath, String name){
        return this.savePage(website, url, templatePath, name,null,0);
    }

    /**
     * 保存
     * @param website 站点
     * @param url 页面存储相对路径
     * @param templatePath 模板
     * @param name 页面名称
     * @param datas 构成页面数据
     * @return
     */
    public IPage savePage(Website website,String url, String templatePath, String name,List<Data> datas){
        return this.savePage(website, url, templatePath, name,datas,0);
    }

    /**
     * 保存
     * @param website 站点
     * @param url 页面存储相对路径
     * @param templatePath 模板
     * @param name 页面名称
     * @param datas 构成页面数据
     * @param pageSize 分页时pageSize
     * @return
     */
    public IPage savePage(Website website,String url, String templatePath, String name,List<Data> datas,int pageSize){
        if(datas!=null && datas.size()>0){
            for(Data data : datas){
                this.dataService.save(data);
            }
        }
        PageBean pageBean = new PageBean();
        Page page = new Page();
        page.setWebSite(website);
        page.setPath(url);
        page.setName(name);
        page.setTemplate(this.templateService.findUniqueByPath(templatePath,website.getId()));
        page.setDatas(datas);
        page.setPageSize(pageSize);
        // 保存数据库
        this.pageService.save(page);

        pageBean.setPage(page);

        return pageBean;
    }

    /**
     * 获得某个页面
     * @param url 页面相对路径
     * @param websiteId 站点id
     * @return
     */
    public IPage get(String url, Long websiteId) {
        PageBean pageBean = new PageBean();
        Page page = this.pageService.findUniqueByPath(url,websiteId);
        pageBean.setPage(page);
        return pageBean;
    }

    /**
     * 删除
     * @param url
     * @param websiteId
     */
    public void remove(String url, Long websiteId) {
        this.pageService.deleteByPath(url,websiteId);
    }

    /**
     * 创建页面
     * @param page
     * @return
     */
    public List<IPageItem> execute(Page page) {
        Page newpage = this.generate.create(page);
        List<IPageItem> pageItems = new ArrayList<IPageItem>();
        for(PageItem pageItem : newpage.getPageItems()){
            PageItemBean pageItemBean = new PageItemBean();
            pageItemBean.setPageItem(pageItem);
            pageItems.add(pageItemBean);
        }
        return pageItems;
    }

    /**
     * 获取页面详情
     * @param path
     * @param pageId
     * @return
     */
    public IPageItem getPageItem(String path, Long pageId) {
        PageItem pageItem = pageItemService.findUniqueByPath(path, pageId);
        if(pageItem==null){
            return null;
        }
        PageItemBean iPageItem = new PageItemBean();
        iPageItem.setPageItem(pageItem);
        return iPageItem;
    }

    /**
     * 删除静态文件
     * @param url 文件相对路径
     * @param website 站点
     */
    public void removeFile(String url, Website website) {
        FileManager fileManager = fileManagerFactory.getFileManager(website.getDefaultFileManager().getId());
        Page page = this.pageService.findUniqueByPath(url,website.getId());
        if(page==null){
            return;
        }
        List<PageItem> pageItems = page.getPageItems();
        for(PageItem pageItem : pageItems){
            fileManager.removeFile(pageItem.getFile());
        }
    }

    /**
     * 获得数据
     * @param url 页面相对路径
     * @param websiteId 站点
     */
    public Map<String, Object> getPageDatas(String url, Long websiteId){
        Page page = this.pageService.findUniqueByPath(url,websiteId);
        return this.generate.getPageDataMap(page);
    }

    /**
     * 预览
     * @param url 页面相对路径
     * @param websiteId 站点
     */
    public String preview(String url, Long websiteId, OutputStream out){
        Page page = this.pageService.findUniqueByPath(url,websiteId);
        return this.generate.preview(page,out);
    }
}
