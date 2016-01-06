package org.jfantasy.website;

import org.jfantasy.website.exception.SwpException;

import java.util.List;

/**
 * 页面接口
 */
public interface IPage {

    /**
     * 创建或刷新页面详情
     * @return
     * @throws SwpException
     */
    public List<IPageItem> createPageItems() throws SwpException;

    /**
     * 删除页面详情
     */
    public void removePageItem();

    /**
     * 获得文件名为file的pageItem
     * @param path 文件路径
     * @return
     */
    public IPageItem getPageItem(String path);
}
