package com.fantasy.wx.media.service;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.media.bean.WxMedia;

import java.io.IOException;
import java.util.List;

/**
 * Created by zzzhong on 2014/12/16.
 */
public interface IMediaService {

    /**
     * 列表查询
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<WxMedia> findPager(Pager<WxMedia> pager, List<PropertyFilter> filters);

    /**
     * 获取媒体对象
     * @param mediaId
     * @return
     */
    public WxMedia getMedia(String mediaId);

    /**
     * 保存媒体对象
     * @param media
     */
    public void save(WxMedia media);

    /**
     * 删除媒体对象
     * @param ids
     */
    public void delete(Long... ids);

    /**
     * 上传文件到微信服务器
     * @param fileDetail 系统文件对象
     * @param type 微信上传类型
     * @throws IOException io异常
     * @throws WxException 微信异常
     */
    public WxMedia mediaUpload(FileDetail fileDetail,String type) throws IOException, WxException;

    /**
     * 下载文件到系统
     * @param mediaId 微信文件id
     * @param dir 系统fileManagerId
     * @return 系统文件对象
     * @throws WxException
     */
    public FileDetail mediaDownload(String mediaId,String dir) throws WxException;
}
