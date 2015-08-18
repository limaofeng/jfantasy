package com.fantasy.website;

import com.fantasy.website.bean.Data;
import com.fantasy.website.bean.DataInferface;
import com.fantasy.website.exception.SwpException;

import java.io.OutputStream;
import java.util.List;

/**
 *
 */
public interface ISwpWebsite {

    /**
     * 添加单页面模板（无数据定义）
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @throws SwpException
     */
    public void addTemplate (String path, String html) throws SwpException;

    /**
     * 添加单页面模板
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @throws SwpException
     */
    public void addSingleTemplate (String path, String html, List<DataInferface> dataInferface) throws SwpException;

    /**
     * 添加多页面模板
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param listKey 多页数据key
     * @throws SwpException
     */
    public void addMultiTemplate (String path, String html, List<DataInferface> dataInferface, String listKey) throws SwpException;

    /**
     * 添加分页面模板
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param pageKey 分页数据key
     * @throws SwpException
     */
    public void addPaginationTemplate (String path, String html, List<DataInferface> dataInferface, String pageKey) throws SwpException;

    /**
     * 创建页面
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @return IPage
     * @throws SwpException
     */
    public IPage createPage(String url, String templatePath, String name) throws SwpException;

    /**
     * 创建单页面
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return IPage
     * @throws SwpException
     */
    public IPage createSinglePage(String url, String templatePath, String name, List<Data> datas) throws SwpException;

    /**
     * 创建多页面
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return IPage
     * @throws SwpException
     */
    public IPage createMultiPage(String url, String templatePath, String name, List<Data> datas) throws SwpException;

    /**
     * 创建分页
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @param size 分页条数
     * @return IPage
     * @throws SwpException
     */
    public IPage createPaginationPage(String url, String templatePath, String name, List<Data> datas, int size) throws SwpException;

    /**
     * 查询站点中配置的page
     * @return
     */
    public List<IPage> listPage();

    /**
     * 查询站点中配置的模板
     * @return
     */
    public List<ITemplage> listTemplate();

    /**
     * 删除页面
     * @param url 页面地址
     */
    public void removePage(String url);

    /**
     * 删除模板
     * @param path 模板地址
     */
    public void removeTemplate(String path);

    /**
     * 查询页面
     * @param url 页面地址
     */
    public IPage getPgae(String url);

    /**
     * 查询模板
     * @param path 模板地址
     */
    public ITemplage getTemplate(String path);

    /**
     * 预览生成的页面
     * 第一页数据
     * @param path 页面path
     * @return
     */
    public String priviewPage(String path, OutputStream out);
}
