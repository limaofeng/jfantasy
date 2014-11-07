package com.fantasy.remind.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.remind.bean.Model;
import com.fantasy.remind.bean.Notice;
import com.fantasy.remind.dao.ModelDao;
import com.fantasy.remind.dao.NoticeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息模版 service
 */

@Service
@Transactional
public class ModelService {

    @Resource
    private ModelDao noticeDao;

    /**
     * 查看
     * @param pager
     * @param filters
     * @return
     */
    public Pager<Model> findPager(Pager<Model> pager,List<PropertyFilter> filters){
       return this.noticeDao.findPager(pager,filters);
    }


    /**
     * 保存
     * @param notice
     */
    public void save(Model notice){
        this.noticeDao.save(notice);
    }


    /**
     * 查看
     * @param id
     * @return
     */
    public Model get(Long id){
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
