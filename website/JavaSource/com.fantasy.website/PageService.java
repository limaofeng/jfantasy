package com.fantasy.website;

/**
 * 页面生成接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-28 下午01:31:54
 */
public interface PageService {

    /**
     * 创建页面实例<br/>
     * 一个页面实例，可以理解为一个具体的html页面
     *
     * @param outPutUrl 输出页面的url地址
     * @param template  模板
     * @param datas     如果有外置数据的话
     * @return PageInstance
     */
    public PageInstance createPageInstance(OutPutUrl outPutUrl, Template template, TemplateData... datas);

}
