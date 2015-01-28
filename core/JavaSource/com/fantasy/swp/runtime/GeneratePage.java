package com.fantasy.swp.runtime;

import com.fantasy.file.FileManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.swp.OutPutUrl;
import com.fantasy.swp.PageInstance;
import com.fantasy.swp.Template;
import com.fantasy.swp.TemplateData;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成静态页面
 * Created by wml on 2015/1/26.
 */
public class GeneratePage implements PageInstance {

    private static Log logger = LogFactory.getLog(GeneratePage.class);

    private transient Configuration configuration;

    private Page page;
    private FileManager fileManager;

    public GeneratePage(Page page,FileManager fileManager) {
        this.page = page;
        this.fileManager = fileManager;
        configuration = SpringContextUtil.getBeanByType(Configuration.class);
    }

    @Override
    public void execute(){
        try {
            com.fantasy.swp.bean.Template template = page.getTemplate();
            List<DataInferface> dataInferfaces = template.getDataInferfaces();
            List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataList.add(dataMap);
            for(DataInferface dataInferface : dataInferfaces){
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.id", dataInferface.getId());
                if(dataInferface.getDataType()==DataInferface.DataType.common){
                    for(Map<String,Object> map : dataList){
                        map.put(dataInferface.getKey(), data.getValue());
                    }
                }else if(dataInferface.getDataType()==DataInferface.DataType.list){   // 列表页
                    List<HashMap> datas = JSON.deserialize(data.getValue(),new TypeReference<List<HashMap>>() {});
                    if(datas==null || datas.size()<=0){
                        continue;
                    }
                    int pageSize = page.getPageSize();
                    List<Pager> pagers = this.make(datas,pageSize);
                    for(int i=0; i<pagers.size(); i++){
                        if(i==0){
                            dataList.get(0).put(dataInferface.getKey(), pagers.get(i).getPageItems());
                        }else{
                            Map<String,Object> _map = new HashMap<String, Object>();
                            _map.put(dataInferface.getKey(), pagers.get(i).getPageItems());
                            dataList.add(_map);
                        }
                    }
//                    dataMap.put(dataInferface.getKey(), datas);
                }else if(dataInferface.getDataType()==DataInferface.DataType.object){

                }

            }
            // 生成页面
            for(int i=0; i<dataList.size(); i++){
                Map<String,Object> dm = dataList.get(i);
                String fileName = page.getPath();
                if(dataList.size()>1){
                    String _fileName = fileName.substring(0,fileName.lastIndexOf("."));
                    fileName = _fileName+"_"+i+".html";
                    dm.put("prePager",_fileName+"_"+(i==0?0:i-1)+".html");
                    dm.put("nextPager",_fileName+"_"+(i==dataList.size()-1?dataList.size()-1:i+1)+".html");
                }
                FreeMarkerTemplateUtils.writer(dm, configuration.getTemplate(template.getPath()), fileManager.writeFile(fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException...writeFile exception..."+e.getMessage());
        }

    }

    private List<Pager> make(List<HashMap> datas, int pageSize){
        List<Pager> pagers = new ArrayList<Pager>();
        if(datas==null || datas.size()<=0 || pageSize<=0){
            Pager pager = new Pager();
            pager.setPageSize(pageSize);
            pager.setPageItems(null);
            pager.setTotalCount(datas==null?0:datas.size());
            pager.setTotalPage(0);
            pagers.add(pager);
            return pagers;
        }
        // 总页数
        int totalPager = datas.size()/pageSize;
        if(datas.size()%pageSize>0){
            totalPager += 1;
        }
        for(int i=1; i<=totalPager; i++){
            Pager p = new Pager();
            p.setTotalPage(totalPager);
            p.setTotalCount(datas.size());
            List<HashMap> _dml = new ArrayList<HashMap>();
            for(int j=0,k=pageSize*(i-1); j<pageSize && (k+j)<datas.size(); j++){
                _dml.add(datas.get(k+j));
            }
            p.setPageItems(_dml);
            p.setPageSize(pageSize);
            p.setCurrentPage(i);
            p.setFirst(1);
            pagers.add(p);
        }
        return pagers;
    }
}
