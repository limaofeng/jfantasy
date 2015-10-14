package com.fantasy.website.factory;

import com.fantasy.website.IPage;
import com.fantasy.website.ISwpWebsite;
import com.fantasy.website.ITemplage;
import com.fantasy.website.SwpContext;
import com.fantasy.website.bean.Data;
import com.fantasy.website.bean.DataInferface;
import com.fantasy.website.bean.enums.PageType;
import com.fantasy.website.exception.SwpException;
import com.fantasy.website.service.PageBeanService;
import com.fantasy.website.service.TemplateBeanService;
import com.fantasy.system.bean.Website;

import java.io.OutputStream;
import java.util.List;


public class SwpWebsite implements ISwpWebsite {

    protected SwpWebsite(){}

    private Website website;

    private PageBeanService pageBeanService;

    private TemplateBeanService templateBeanService;

    /**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @throws SwpException
     */
    @Override
    public void addTemplate(String path, String html) throws SwpException {
        this.templateBeanService.saveTemplate(this.website,path,html,PageType.single);
    }

    /**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @throws SwpException
     */
    @Override
    public void addSingleTemplate(String path, String html, List<DataInferface> dataInferface) throws SwpException {
        this.templateBeanService.saveTemplate(this.website,path,html,PageType.single,dataInferface);
    }

    /**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param listKey 多页数据key
     * @throws SwpException
     */
    @Override
    public void addMultiTemplate(String path, String html, List<DataInferface> dataInferface, String listKey) throws SwpException {
        this.templateBeanService.saveTemplate(this.website,path,html,PageType.multi,dataInferface,listKey);
    }

    /**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param pageKey 分页数据key
     * @throws SwpException
     */
    @Override
    public void addPaginationTemplate(String path, String html, List<DataInferface> dataInferface, String pageKey) throws SwpException {
        this.templateBeanService.saveTemplate(this.website,path,html,PageType.pagination,dataInferface,pageKey);
    }

    /**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @return
     * @throws SwpException
     */
    @Override
    public IPage createPage(String url, String templatePath, String name) throws SwpException {
        return this.pageBeanService.savePage(this.website,url,templatePath,name);
    }

    /**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return
     * @throws SwpException
     */
    @Override
    public IPage createSinglePage(String url, String templatePath, String name, List<Data> datas) throws SwpException {
        return this.pageBeanService.savePage(this.website,url, templatePath, name, datas);
    }

    /**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return
     * @throws SwpException
     */
    @Override
    public IPage createMultiPage(String url, String templatePath, String name, List<Data> datas) throws SwpException {
        return this.pageBeanService.savePage(this.website,url,templatePath,name,datas);
    }

    /**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @param size 分页条数
     * @return
     * @throws SwpException
     */
    @Override
    public IPage createPaginationPage(String url, String templatePath, String name, List<Data> datas, int size) throws SwpException {
        return this.pageBeanService.savePage(this.website,url, templatePath, name, datas, size);
    }

    /**
     *
     * @return
     */
    @Override
    public List<IPage> listPage() {
        return this.pageBeanService.listPage();
    }

    @Override
    public List<ITemplage> listTemplate() {
        return this.templateBeanService.listTemplate();
    }

    @Override
    public void removePage(String url) {
        this.pageBeanService.removeFile(url,website);
        this.pageBeanService.remove(url,this.website.getId());
    }

    @Override
    public void removeTemplate(String path) {
        this.templateBeanService.removeFile(path,website);
        this.templateBeanService.remove(path,this.website.getId());
    }

    @Override
    public IPage getPgae(String url) {
        return this.pageBeanService.get(url,this.website.getId());
    }

    @Override
    public ITemplage getTemplate(String path) {
        return this.templateBeanService.get(path,this.website.getId());
    }

    /**
     *
     * @param path 页面path
     * @return
     */
    @Override
    public String priviewPage(String path, OutputStream out) {
        return this.pageBeanService.preview(path, this.website.getId(),out);
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
        SwpContext.getContext().setWebsite(website);
    }

    public PageBeanService getPageBeanService() {
        return pageBeanService;
    }

    public void setPageBeanService(PageBeanService pageBeanService) {
        this.pageBeanService = pageBeanService;
    }

    public TemplateBeanService getTemplateBeanService() {
        return templateBeanService;
    }

    public void setTemplateBeanService(TemplateBeanService templateBeanService) {
        this.templateBeanService = templateBeanService;
    }
}