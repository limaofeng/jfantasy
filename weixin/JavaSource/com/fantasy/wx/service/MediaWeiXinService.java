package com.fantasy.wx.service;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.bean.Media;
import com.fantasy.wx.dao.MediaDao;
import com.fantasy.wx.framework.exception.WeiXinException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by zzzhong on 2014/12/16.
 */
@Service
@Transactional
public class MediaWeiXinService {
    @Autowired
    private MediaDao mediaDao;
    @Autowired
    private FileUploadService fileUploadService;
//    @Autowired
//    private WeixinConfigInit weixinConfigInit;
    @Autowired
    private FileManagerFactory factory;

    public Pager<Media> findPager(Pager<Media> pager, List<PropertyFilter> filters) {
        return this.mediaDao.findPager(pager, filters);
    }

    public Media getMedia(String mediaId){
        return mediaDao.findUniqueBy("mediaId",mediaId);
    }

    public void save(Media media){
        mediaDao.save(media);
    }

    public void delete(Long... ids){
        for (Long id : ids) {
            this.mediaDao.delete(id);
        }
    }

    public Media mediaUpload(FileDetail fileDetail,String type) throws IOException, WeiXinException {
        Media media=null;
//        InputStream inputStream=factory.getFileManager(fileDetail.getFileManagerId()).readFile(fileDetail.getAbsolutePath());
//        try {
//            WxMediaUploadResult uploadMediaRes = weixinConfigInit.getUtil().mediaUpload(type,fileDetail.getExt(), inputStream);
//            media=BeanUtil.copyProperties(new Media(),uploadMediaRes);
//            this.mediaDao.save(media);
//        } catch (WxErrorException e) {
//            throw WeiXinException.wxExceptionBuilder(e);
//        }
        return media;
    }
    public FileDetail mediaDownload(String mediaId,String dir) throws WeiXinException {
        FileDetail fileDetail=null;
        File file=null;
//        try {
//            file=weixinConfigInit.getUtil().mediaDownload(mediaId);
//            String rename=Long.toString(new Date().getTime())+Integer.toString(new Random().nextInt(900000)+100000)+"."+ WebUtil.getExtension(file.getName());
//            fileDetail=fileUploadService.upload(file, FileUtil.getMimeType(file),rename,dir);
//        } catch (WxErrorException e) {
//            throw WeiXinException.wxExceptionBuilder(e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if(file!=null) file.delete();
//        }
        return fileDetail;
    }
}
