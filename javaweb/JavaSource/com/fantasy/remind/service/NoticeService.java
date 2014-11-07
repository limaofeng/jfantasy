package com.fantasy.remind.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.remind.bean.Notice;
import com.fantasy.remind.dao.NoticeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提醒 service
 */

@Service
@Transactional
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;

    /**
     * 查看
     * @param pager
     * @param filters
     * @return
     */
    public Pager<Notice> findPager(Pager<Notice> pager,List<PropertyFilter> filters){
        return this.noticeDao.findPager(pager,filters);
    }


    /**
     * 保存
     * @param notice
     */
    public void save(Notice notice){
        this.noticeDao.save(notice);
    }


    /**
     * 查看
     * @param id
     * @return
     */
    public Notice get(Long id){
        return this.noticeDao.get(id);
    }


    /**
     * 删除
     * @param ids
     */
    public void delete(Long[] ids){
        for(Long id:ids){
            this.noticeDao.delete(id);
        }

    }



}