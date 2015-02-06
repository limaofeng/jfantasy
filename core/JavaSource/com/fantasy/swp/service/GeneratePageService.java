package com.fantasy.swp.service;


import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.freemarker.TemplateModelUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.swp.bean.*;
import com.fantasy.swp.bean.enums.PageType;
import com.fantasy.swp.util.GeneratePageUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import freemarker.template.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Service
@Transactional
public class GeneratePageService {

    private FileManager fileManager = new LocalFileManager("E:/aa");
    @Resource
    private PageItemService pageItemService;

    public void reGenerate(Long pageItemId) throws IOException {
        Configuration configuration = SpringContextUtil.getBeanByType(Configuration.class);
        Map<String,Object> dm = new HashMap<String, Object>();

        PageItem pageItem = pageItemService.get(pageItemId);
        Page page = pageItem.getPage();
        // 模版
        com.fantasy.swp.bean.Template template = page.getTemplate();
        // 数据定义
        List<DataInferface> dataInferfaces = template.getDataInferfaces();
        for(DataInferface dataInferface : dataInferfaces){
            Data data = ObjectUtil.find(page.getDatas(), "dataInferface.id", dataInferface.getId());
            if(dataInferface.getKey().equals(template.getDataKey())){
                continue;
            }
            dm.put(dataInferface.getKey(), GeneratePageUtil.getValue(data));
        }

        if(template.getPageType()== PageType.pagination){    // 分页
            List<Object> list = (List<Object>) dm.get(template.getDataKey());
            final Pager pager = new Pager(page.getPageSize());
            pager.setTotalCount(list.size());
            pager.setCurrentPage(1);
            for (int i = 1; i <= pager.getTotalPage(); pager.setCurrentPage(pager.getCurrentPage() + 1),i++) {
                int start = pager.getPageSize() * (pager.getCurrentPage()-1);
                int end = Math.min(pager.getPageSize() * pager.getCurrentPage(), pager.getTotalCount());
                pager.setPageItems(list.subList(start, end));
                final Map<String, Object> pageDataMap = new HashMap<String, Object>();
                pageDataMap.put(template.getDataKey(),pager);
                pageDataMap.putAll(dm);
                // /aaaa/xxxx/bbbb/p_${art.id}.html
                String path = RegexpUtil.replace(page.getPath(), "\\$\\{([a-zA-Z.]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {
                    @Override
                    public String doReplace(String text, int index, Matcher matcher) {
                        return StringUtil.nullValue(OgnlUtil.getInstance().getValue($(1), pageDataMap));
                    }
                });
                FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(pageDataMap), configuration.getTemplate(template.getPath()), fileManager.writeFile(path));
                // 页面生成成功，记录pageItem
                this.updatePageItem(pageDataMap,path,page,pageItemId);
            }
        }else if(template.getPageType()==PageType.multi){   // 多页面
            Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
            List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
            Object obj = ObjectUtil.find(list, "id", Long.valueOf(pageItem.getCode()));
            if(obj==null){
                return;
            }
            final Map<String, Object> pageDataMap = new HashMap<String, Object>();
            pageDataMap.put(template.getDataKey(), obj);
            pageDataMap.putAll(dm);
            String path = RegexpUtil.replace(page.getPath(), "\\$\\{([a-zA-Z.]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {
                @Override
                public String doReplace(String text, int index, Matcher matcher) {
                    return StringUtil.nullValue(OgnlUtil.getInstance().getValue($(1),pageDataMap));
                }
            });
            FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(pageDataMap), configuration.getTemplate(template.getPath()), fileManager.writeFile(path));
            this.updatePageItem(pageDataMap,path,page,pageItemId);
        }else{   // 单页面
            String fileName = page.getPath();
            FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm), configuration.getTemplate(template.getPath()), fileManager.writeFile(fileName));
            this.updatePageItem(dm,fileName,page,pageItemId);
        }

        System.out.println("pageItem.createTime="+pageItem.getCreateTime());
    }

    /**
     *
     * @param dm
     * @param fileName
     * @param page
     * @param pageItemId
     */
    private void updatePageItem(Map<String,Object> dm,String fileName,Page page, Long pageItemId){
        // 删除
        if(pageItemId!=null && pageItemId>0){
            Long[] ids = {pageItemId};
            this.pageItemService.delete(ids);
        }
        // 新增
        PageItemService pageItemService = SpringContextUtil.getBeanByType(PageItemService.class);
        PageItemDataService pageItemDataService = SpringContextUtil.getBeanByType(PageItemDataService.class);
        PageItem pageItem = new PageItem();
        pageItem.setPage(page);
        pageItem.setContent("");
        pageItem.setFile(fileName);
        List<PageItemData> pageItemDatas = new ArrayList<PageItemData>();
        pageItem.setPageItemDatas(pageItemDatas);

        pageItemService.save(pageItem);

        for (Map.Entry<String, Object> entry : dm.entrySet()) {
            Object hibernateEntityObject = null;
            if(entry.getValue() instanceof List){
                if(entry.getValue()==null || ((List) entry.getValue()).size()==0){
                    continue;
                }
                hibernateEntityObject = ((List) entry.getValue()).get(0);
            }else if(entry.getValue() instanceof Array){
                if(entry.getValue()==null || ((Object[]) entry.getValue()).length==0){
                    continue;
                }
                hibernateEntityObject = ((Object[]) entry.getValue())[0];
            }else{
                hibernateEntityObject = entry.getValue();
            }
            Class clazz = ClassUtil.getRealClass(hibernateEntityObject.getClass());
            Entity entity = (Entity) clazz.getAnnotation(Entity.class);
            if(entity!=null){
                Field[] field = ClassUtil.getDeclaredFields(clazz,Id.class);
                Object id = null;
                if(field.length == 1){
                    id = ClassUtil.getValue(hibernateEntityObject,field[0].getName());
                }
                PageItemData pageItemData = new PageItemData();
                pageItemData.setPageItem(pageItem);
                pageItemData.setClassName(clazz.toString());
                pageItemData.setBeanId(id.toString());
                pageItemDataService.save(pageItemData);
                pageItemDatas.add(pageItemData);
            }
        }

//        pageItemService.save(pageItem);
    }

    /**
     * 获取数据
     * @param data
     * @return
     */
    public Object getValue(Data data){
        DataInferface dataInferface = data.getDataInferface();
        if(data.getDataSource()==Data.DataSource.stat){   // 数据源为静态
            if(dataInferface.getDataType()==DataInferface.DataType.list){
                return JSON.deserialize(data.getValue(), new TypeReference<List<HashMap>>() {
                });
            }
            return JSON.deserialize(data.getValue());
        }else if(data.getDataSource()==Data.DataSource.func){// 数据源为方法
            SpelService spelService = SpringContextUtil.getBeanByType(SpelService.class);
            Map<String,Object> params = JSON.deserialize(data.getValue(),new TypeReference<HashMap<String,Object>>() {});
            String func = params.get("func").toString();
            Map<String,Object> paramsMap = (Map<String,Object>)params.get("params");
            return spelService.executeMethod(func,paramsMap);
        }else if(data.getDataSource()==Data.DataSource.db){   // 数据库查询
            HqlService hqlService = SpringContextUtil.getBeanByType(HqlService.class);
            Map<String,Object> params = JSON.deserialize(data.getValue(),new TypeReference<HashMap<String,Object>>() {});
            String hql = params.get("hql").toString();
            String operate = params.get("operate").toString();
            return hqlService.execute(hql,operate);
        }else {
            throw new IgnoreException("不支持的转换类型");
        }
    }
}
