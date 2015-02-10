package com.fantasy.swp.service;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.freemarker.loader.FreemarkerTemplateLoader;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.dao.PageDao;
import com.fantasy.swp.runtime.GeneratePage;
import freemarker.template.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class _PageService {

    @Resource
    private PageDao pageDao;

    public Pager<Page> findPager(Pager<Page> pager, List<PropertyFilter> filters) {
        return this.pageDao.findPager(pager, filters);
    }

    public void save(Page page) {
        this.pageDao.save(page);
    }

    public Page get(Long id) {
        return this.pageDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.pageDao.delete(id);
        }
    }

    public void generation(Long id) {
    }

    public List<Page> find(List<PropertyFilter> filters) {
        return this.pageDao.find(filters);
    }

    public void create(Long[] ids){
        try {
            FileManager fileManager = new LocalFileManager("E:/aa");
            for(Long id : ids){
                Page page = this.pageDao.get(id);
                GeneratePage generatePage = new GeneratePage(page,fileManager);
                generatePage.execute();
//                Map<String,Object> dataMap = new HashMap<String, Object>();
//                Template template = page.getTemplate();
//                List<DataInferface> dataInferfaces = template.getDataInferfaces();
//                for(DataInferface dataInferface : dataInferfaces){
//                    Data data = ObjectUtil.find(page.getDatas(),"dataInferface.id",dataInferface.getId());
//                    dataMap.put(dataInferface.getKey(),data.getValue());
//                }
//                FreeMarkerTemplateUtils.writer(dataMap, configuration.getTemplate(template.getPath()), fileManager.writeFile(page.getPath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

