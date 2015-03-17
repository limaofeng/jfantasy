package com.fantasy.remind.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.remind.bean.Model;
import com.fantasy.remind.dao.ModelDao;
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

    @Autowired
    private ModelDao modelDao;

    /**
     * 查看
     * @param pager
     * @param filters
     * @return
     */
    public Pager<Model> findPager(Pager<Model> pager,List<PropertyFilter> filters){
       return this.modelDao.findPager(pager,filters);
    }

    public List<Model> findAll(){
        return this.modelDao.find();
    }

    /**
     * 保存
     * @param notice
     */
    public void save(Model notice){
        this.modelDao.save(notice);
    }


    /**
     * 查看
     * @param id
     * @return
     */
    public Model get(String id){
         return this.modelDao.get(id);
    }


    /**
     * 删除
     * @param ids
     */
    public void delete(String... ids){
        for(String id:ids){
            this.modelDao.delete(id);
        }

    }

}
