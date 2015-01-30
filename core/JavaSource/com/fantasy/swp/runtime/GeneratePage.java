package com.fantasy.swp.runtime;

import com.fantasy.file.FileManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.swp.PageInstance;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.service.HqlService;
import com.fantasy.swp.service.SpelService;
import com.fasterxml.jackson.core.type.TypeReference;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

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
            // 模版
            com.fantasy.swp.bean.Template template = page.getTemplate();
            // 数据定义
            List<DataInferface> dataInferfaces = template.getDataInferfaces();
            // 将生成的页面，一条数据即为一个页面
            List<Map<String,Object>> pagerList = new ArrayList<Map<String, Object>>();
            // 一个页面的数据
            Map<String,Object> dataMap = new HashMap<String, Object>();
            // 默认生成一个页面
            pagerList.add(dataMap);

            for(DataInferface dataInferface : dataInferfaces){
                // 当前页面数据
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.id", dataInferface.getId());
                if(dataInferface.getDataSource()==DataInferface.DataSource.stat){   // 数据源为静态
                    // 处理静态数据源，将数据集存入到pagerList
                    this.dealStatSource(dataInferface,data,pagerList);
                }else if(dataInferface.getDataSource()==DataInferface.DataSource.func){// 数据源为方法
                    this.dealFuncSource(dataInferface,data,pagerList);
                }else if(dataInferface.getDataSource()==DataInferface.DataSource.db){   // 数据库查询
                    this.dealDbSource(dataInferface,data,pagerList);
                }
            }
            // 生成页面
            for(int i=0; i<pagerList.size(); i++){
                Map<String,Object> dm = pagerList.get(i);
                String fileName = page.getPath();
                if(pagerList.size()>1){
                    String _fileName = fileName.substring(0,fileName.lastIndexOf("."));
                    fileName = _fileName+"_"+i+".html";
//                    // 翻页按钮
//                    dm.put("prePager",_fileName+"_"+(i==0?0:i-1)+".html");
//                    dm.put("nextPager",_fileName+"_"+(i==pagerList.size()-1?pagerList.size()-1:i+1)+".html");
                }
                FreeMarkerTemplateUtils.writer(dm, configuration.getTemplate(template.getPath()), fileManager.writeFile(fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException...writeFile exception..."+e.getMessage());
        }

    }

    private List<Pager> make(List datas, int pageSize){
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
            List _dml = new ArrayList();
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

    private void dealStatSource(DataInferface dataInferface, Data data,List<Map<String,Object>> pagerList){
        if(dataInferface.getDataType()==DataInferface.DataType.common){       // 普通
            for(Map<String,Object> map : pagerList){
                map.put(dataInferface.getKey(), data.getValue());
            }
        }else if(dataInferface.getDataType()==DataInferface.DataType.list){   // 列表页
            List<HashMap> datas = JSON.deserialize(data.getValue(),new TypeReference<List<HashMap>>() {});
            if(datas==null || datas.size()<=0){
                return;
            }
            int pageSize = page.getPageSize();
            List<Pager> pagers = this.make(datas,pageSize);
            for(int i=0; i<pagers.size(); i++){
                if(i==0){
                    pagerList.get(0).put(dataInferface.getKey(), pagers.get(i).getPageItems());
                }else{
                    Map<String,Object> _map = new HashMap<String, Object>();
                    _map.put(dataInferface.getKey(), pagers.get(i).getPageItems());
                    pagerList.add(_map);
                }
            }
        }else if(dataInferface.getDataType()==DataInferface.DataType.object){
            List<HashMap> datas = JSON.deserialize(data.getValue(),new TypeReference<List<HashMap>>() {});
            if(datas==null || datas.size()<=0){
                return;
            }
            for(int i=0; i<datas.size(); i++){
                if(i==0){
                    pagerList.get(0).put(dataInferface.getKey(), datas.get(i));
                }else{
                    Map<String,Object> _map = new HashMap<String, Object>();
                    _map.put(dataInferface.getKey(), datas.get(i));
                    pagerList.add(_map);
                }
            }
        }
    }

    public void dealFuncSource(DataInferface dataInferface, Data data,List<Map<String,Object>> pagerList){
        SpelService spelService = SpringContextUtil.getBeanByType(SpelService.class);
        Map<String,Object> params = JSON.deserialize(data.getValue(),new TypeReference<HashMap<String,Object>>() {});
        String func = params.get("func").toString();
        Map<String,Object> paramsMap = (Map<String,Object>)params.get("params");
        Object obj = spelService.executeMethod(func,paramsMap);
        if(obj==null){
            return;
        }
        if(dataInferface.getDataType()==DataInferface.DataType.common){       // 普通
            for(Map<String,Object> map : pagerList){
                map.put(dataInferface.getKey(), obj);
            }
        }else if(dataInferface.getDataType()==DataInferface.DataType.list){   // 列表页
            List datas;
            if(obj instanceof List){
                datas = (List)obj;
            }else if(obj instanceof Array){
                datas = Arrays.asList(((Array)obj));
            }else {
                datas = new ArrayList();
                datas.add(obj);
            }
            int pageSize = page.getPageSize();
            List<Pager> pagers = this.make(datas,pageSize);
            for(int i=0; i<pagers.size(); i++){
                if(i==0){
                    pagerList.get(0).put(dataInferface.getKey(), pagers.get(i).getPageItems());
                }else{
                    Map<String,Object> _map = new HashMap<String, Object>();
                    _map.put(dataInferface.getKey(), pagers.get(i).getPageItems());
                    pagerList.add(_map);
                }
            }
        }else if(dataInferface.getDataType()==DataInferface.DataType.object){
            List datas;
            if(obj instanceof List){
                datas = (List)obj;
            }else if(obj instanceof Array){
                datas = Arrays.asList(((Array)obj));
            }else {
                datas = new ArrayList();
                datas.add(obj);
            }
            for(int i=0; i<datas.size(); i++){
                if(i==0){
                    pagerList.get(0).put(dataInferface.getKey(), datas.get(i));
                }else{
                    Map<String,Object> _map = new HashMap<String, Object>();
                    _map.put(dataInferface.getKey(), datas.get(i));
                    pagerList.add(_map);
                }
            }
        }
    }

    private void dealDbSource(DataInferface dataInferface, Data data, List<Map<String, Object>> pagerList) {
        HqlService hqlService = SpringContextUtil.getBeanByType(HqlService.class);
        Map<String,Object> params = JSON.deserialize(data.getValue(),new TypeReference<HashMap<String,Object>>() {});
        String hql = params.get("hql").toString();
        String operate = params.get("operate").toString();
        Object obj = hqlService.execute(hql,operate);

        if(dataInferface.getDataType()==DataInferface.DataType.common){       // 普通
            for(Map<String,Object> map : pagerList){
                map.put(dataInferface.getKey(), obj);
            }
        }else if(dataInferface.getDataType()==DataInferface.DataType.list){   // 列表页
            List datas;
            if(obj instanceof List){
                datas = (List)obj;
            }else if(obj instanceof Array){
                datas = Arrays.asList(((Array)obj));
            }else {
                datas = new ArrayList();
                datas.add(obj);
            }
            int pageSize = page.getPageSize();
            List<Pager> pagers = this.make(datas,pageSize);
            for(int i=0; i<pagers.size(); i++){
                if(i==0){
                    pagerList.get(0).put(dataInferface.getKey(), pagers.get(i).getPageItems());
                }else{
                    Map<String,Object> _map = new HashMap<String, Object>();
                    _map.put(dataInferface.getKey(), pagers.get(i).getPageItems());
                    pagerList.add(_map);
                }
            }
        }else if(dataInferface.getDataType()==DataInferface.DataType.object){
            List datas;
            if(obj instanceof List){
                datas = (List)obj;
            }else if(obj instanceof Array){
                datas = Arrays.asList(((Array)obj));
            }else {
                datas = new ArrayList();
                datas.add(obj);
            }
            for(int i=0; i<datas.size(); i++){
                if(i==0){
                    pagerList.get(0).put(dataInferface.getKey(), datas.get(i));
                }else{
                    Map<String,Object> _map = new HashMap<String, Object>();
                    _map.put(dataInferface.getKey(), datas.get(i));
                    pagerList.add(_map);
                }
            }
        }
    }
}
