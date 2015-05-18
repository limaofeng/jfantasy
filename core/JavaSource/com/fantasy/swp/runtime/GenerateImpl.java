package com.fantasy.swp.runtime;

import com.fantasy.file.FileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.freemarker.TemplateModelUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.PropertiesHelper;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.swp.IGenerate;
import com.fantasy.swp.bean.*;
import com.fantasy.swp.bean.enums.PageType;
import com.fantasy.swp.exception.SwpException;
import com.fantasy.swp.service.PageItemDataService;
import com.fantasy.swp.service.PageItemService;
import com.fantasy.swp.service._PageService;
import com.fantasy.swp.util.GeneratePageUtil;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


@Component
@Transactional
public class GenerateImpl implements IGenerate {

    private static Log logger = LogFactory.getLog(GeneratePage.class);

    @Autowired
    private FileManagerFactory fileManagerFactory;
    @Autowired
    private Configuration configuration;
    @Autowired
    private PageItemService pageItemService;
    @Autowired
    private _PageService pageService;
    @Override
    public Page create(Page page){
        List<PageItem> pageItems = new ArrayList<PageItem>();
        try {
            FileManager fileManager = fileManagerFactory.getFileManager(page.getWebSite().getDefaultFileManager().getId());
            // 模版
            final com.fantasy.swp.bean.Template template = page.getTemplate();
            // 数据定义
            List<DataInferface> dataInferfaces = template.getDataInferfaces();
            // 所有数据
            final Map<String, Object> dm = new HashMap<String, Object>();
            dm.putAll(this.sysProperty());

            if(dataInferfaces!=null && dataInferfaces.size()>0){
                for(DataInferface dataInferface : dataInferfaces){
                    Data data = ObjectUtil.find(page.getDatas(), "dataInferface.id", dataInferface.getId());
                    if(dataInferface.getKey().equals(template.getDataKey())){
                        continue;
                    }
                    dm.put(dataInferface.getKey(), GeneratePageUtil.getValue(data));
                }
            }
            if(template.getPageType()== PageType.pagination){    // 分页
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
                List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
                final Pager pager = new Pager(page.getPageSize());
                pager.setTotalCount(list.size());
                pager.setCurrentPage(1);
                for (int i = 1; i <= pager.getTotalPage(); pager.setCurrentPage(pager.getCurrentPage() + 1),i++) {
                    int start = pager.getPageSize() * (pager.getCurrentPage()-1);
                    int end = Math.min(pager.getPageSize() * pager.getCurrentPage(), pager.getTotalCount());
                    pager.setPageItems(list.subList(start, end));
                    final Map<String, Object> pageDataMap = new HashMap<String, Object>();
                    pageDataMap.put(template.getDataKey(),pager.getPageItems());
                    pageDataMap.putAll(dm);
                    pageDataMap.put("pager",pager);
                    // /aaaa/xxxx/bbbb/p_${art.id}.html
                    String path = RegexpUtil.replace(page.getPath(), "\\$\\{([a-zA-Z.]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {
                        @Override
                        public String doReplace(String text, int index, Matcher matcher) {
                            return StringUtil.nullValue(OgnlUtil.getInstance().getValue($(1), pageDataMap));
                        }
                    });
                    FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(pageDataMap), configuration.getTemplate(template.getPath()), fileManager.writeFile(path));
                    pageDataMap.remove("pager");
                    // 页面生成成功，记录pageItem
                    PageItem pageItem = this.savePageItem(pageDataMap,path,page,pager.getCurrentPage());
                    pageItems.add(pageItem);
                }
            }else if(template.getPageType()==PageType.multi){   // 多页面
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
                List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
                for(Object o : list){
                    final Map<String, Object> pageDataMap = new HashMap<String, Object>();
                    pageDataMap.put(template.getDataKey(), o);
                    pageDataMap.putAll(dm);
                    String path = RegexpUtil.replace(page.getPath(), "\\$\\{([a-zA-Z.]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {
                        @Override
                        public String doReplace(String text, int index, Matcher matcher) {
                            return StringUtil.nullValue(OgnlUtil.getInstance().getValue($(1),pageDataMap));
                        }
                    });
                    FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(pageDataMap), configuration.getTemplate(template.getPath()), fileManager.writeFile(path));
                    PageItem pageItem = this.savePageItem(pageDataMap,path,page);
                    pageItems.add(pageItem);
                }
            }else{   // 单页面
                String fileName = page.getPath();
                FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm), configuration.getTemplate(template.getPath()), fileManager.writeFile(fileName));
                PageItem pageItem = this.savePageItem(dm,fileName,page);
                pageItems.add(pageItem);
            }
            page.setPageItems(pageItems);
        } catch (IOException e) {
            logger.error("IOException...writeFile exception..."+e.getMessage());
        }
        return page;
    }

    @Override
    public Page reCreate(Long pageItemId) throws SwpException, IOException {
        return this.reCreate(pageItemId,new ArrayList<Data>());
    }

    @Override
    public Page reCreate(Long pageItemId, List<Data> dataList) throws SwpException, IOException {
        Configuration configuration = SpringContextUtil.getBeanByType(Configuration.class);
        Map<String,Object> dm = new HashMap<String, Object>();
        dm.putAll(this.sysProperty());

        PageItem pageItem = this.pageItemService.get(pageItemId);
        Page page = pageItem.getPage();
        FileManager fileManager = fileManagerFactory.getFileManager(page.getWebSite().getDefaultFileManager().getId());
        // 模版
        com.fantasy.swp.bean.Template template = page.getTemplate();
        // 数据定义
        List<DataInferface> dataInferfaces = template.getDataInferfaces();
        // 数据
        List<Data> datas = page.getDatas();
        if(dataList.size()>0){
            datas = dataList;
        }
        for(DataInferface dataInferface : dataInferfaces){
            Data data = ObjectUtil.find(datas, "dataInferface.id", dataInferface.getId());
            if(dataInferface.getKey().equals(template.getDataKey())){
                continue;
            }
            dm.put(dataInferface.getKey(), GeneratePageUtil.getValue(data));
        }

        PageItem backPageItem = null;
        if(template.getPageType()== PageType.pagination){    // 分页
            Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
            List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
            final Pager pager = new Pager(page.getPageSize());
            pager.setTotalCount(list.size());
            pager.setCurrentPage(Integer.parseInt(pageItem.getCode()));

            int start = pager.getPageSize() * (pager.getCurrentPage()-1);
            int end = Math.min(pager.getPageSize() * pager.getCurrentPage(), pager.getTotalCount());
            pager.setPageItems(list.subList(start, end));
            final Map<String, Object> pageDataMap = new HashMap<String, Object>();
            pageDataMap.put(template.getDataKey(),pager.getPageItems());
            pageDataMap.putAll(dm);
            pageDataMap.put("pager",pager);
            // /aaaa/xxxx/bbbb/p_${art.id}.html
            String path = RegexpUtil.replace(page.getPath(), "\\$\\{([a-zA-Z.]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {
                @Override
                public String doReplace(String text, int index, Matcher matcher) {
                    return StringUtil.nullValue(OgnlUtil.getInstance().getValue($(1), pageDataMap));
                }
            });
            FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(pageDataMap), configuration.getTemplate(template.getPath()), fileManager.writeFile(path));
            pageDataMap.remove("pager");
            // 页面生成成功，记录pageItem
            backPageItem = this.savePageItem(pageDataMap,path,page,pager.getCurrentPage(),pageItemId);
        }else if(template.getPageType()==PageType.multi){   // 多页面
            Data data = ObjectUtil.find(datas, "dataInferface.key", template.getDataKey());
            List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
            Object obj = ObjectUtil.find(list, "id", Long.valueOf(pageItem.getCode()));
            if(obj==null){
                return null;
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
            backPageItem = this.savePageItem(pageDataMap,path,page,null,pageItemId);
        }else{   // 单页面
            String fileName = page.getPath();
            FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm), configuration.getTemplate(template.getPath()), fileManager.writeFile(fileName));
            backPageItem = this.savePageItem(dm,fileName,page,null,pageItemId);
        }
        Page backPage = pageService.findUniqueByPath(page.getPath(),page.getWebSite().getId());
        return backPage;
    }

    /**
     * 预览
     * 第一页或第一条数据
     * @param page
     * @return
     * @throws IOException
     */
    public String preview(Page page, OutputStream out){
        try {
            // 所有数据
            final Map<String, Object> dm = new HashMap<String, Object>();
            dm.putAll(this.sysProperty());
            // 模版
            final com.fantasy.swp.bean.Template template = page.getTemplate();
            // 数据定义
            List<DataInferface> dataInferfaces = template.getDataInferfaces();
            if(dataInferfaces!=null && dataInferfaces.size()>0){
                for(DataInferface dataInferface : dataInferfaces){
                    Data data = ObjectUtil.find(page.getDatas(), "dataInferface.id", dataInferface.getId());
                    if(dataInferface.getKey().equals(template.getDataKey())){
                        continue;
                    }
                    dm.put(dataInferface.getKey(), GeneratePageUtil.getValue(data));
                }
            }
            String view = "";
            if(template.getPageType()== PageType.pagination){    // 分页
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
                List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
                final Pager pager = new Pager(page.getPageSize());
                pager.setTotalCount(list.size());
                pager.setCurrentPage(1);

                int start = pager.getPageSize() * (pager.getCurrentPage()-1);
                int end = Math.min(pager.getPageSize() * pager.getCurrentPage(), pager.getTotalCount());
                pager.setPageItems(list.subList(start, end));
                dm.put(template.getDataKey(),pager.getPageItems());
                dm.put("pager",pager);
                FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm),configuration.getTemplate(template.getPath()),out);
            }else if(template.getPageType()==PageType.multi){   // 多页面
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
                List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
                for(Object o : list){
                    dm.put(template.getDataKey(), o);
                    FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm),configuration.getTemplate(template.getPath()),out);
                    break;
                }
            }else{   // 单页面
                FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm),configuration.getTemplate(template.getPath()),out);
            }
            return view;
        } catch (Exception e){
            e.printStackTrace();
            return "Exception:"+e.getMessage();
        }

    }

    public Map<String, Object> getPageDataMap(Page page){
        // 所有数据
        final Map<String, Object> dm = new HashMap<String, Object>();
        // 模版
        final com.fantasy.swp.bean.Template template = page.getTemplate();
        // 数据定义
        List<DataInferface> dataInferfaces = template.getDataInferfaces();
        if(dataInferfaces!=null && dataInferfaces.size()>0){
            for(DataInferface dataInferface : dataInferfaces){
                Data data = ObjectUtil.find(page.getDatas(), "dataInferface.id", dataInferface.getId());
                if(dataInferface.getKey().equals(template.getDataKey())){
                    continue;
                }
                dm.put(dataInferface.getKey(), GeneratePageUtil.getValue(data));
            }
        }
        if(template.getPageType()== PageType.pagination){    // 分页
            Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
            List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
            final Pager pager = new Pager(page.getPageSize());
            pager.setTotalCount(list.size());
            pager.setCurrentPage(1);

            int start = pager.getPageSize() * (pager.getCurrentPage()-1);
            int end = Math.min(pager.getPageSize() * pager.getCurrentPage(), pager.getTotalCount());
            pager.setPageItems(list.subList(start, end));
            dm.put(template.getDataKey(),pager.getPageItems());
            dm.put("pager",pager);
        }else if(template.getPageType()==PageType.multi){   // 多页面
            Data data = ObjectUtil.find(page.getDatas(), "dataInferface.key", template.getDataKey());
            List<Object> list = (List<Object>) GeneratePageUtil.getValue(data);
            for(Object o : list){
                dm.put(template.getDataKey(), o);
                break;
            }
        }else{   // 单页面
        }
        return dm;
    }
//    /**
//     *
//     * @param dm
//     * @param fileName
//     * @param page
//     * @param pageItemId
//     */
//    private void updatePageItem(Map<String,Object> dm,String fileName,Page page, Long pageItemId){
//        // 删除
//        if(pageItemId!=null && pageItemId>0){
//            Long[] ids = {pageItemId};
//            this.pageItemService.delete(ids);
//        }
//        // 新增
//        PageItemService pageItemService = SpringContextUtil.getBeanByType(PageItemService.class);
//        PageItemDataService pageItemDataService = SpringContextUtil.getBeanByType(PageItemDataService.class);
//        PageItem pageItem = new PageItem();
//        pageItem.setPage(page);
//        pageItem.setContent("");
//        pageItem.setFile(fileName);
//        List<PageItemData> pageItemDatas = new ArrayList<PageItemData>();
//        pageItem.setPageItemDatas(pageItemDatas);
//
////        for (Map.Entry<String, Object> entry : dm.entrySet()) {
////            Object hibernateEntityObject = null;
////            if(entry.getValue() instanceof List){
////                if(entry.getValue()==null || ((List) entry.getValue()).size()==0){
////                    continue;
////                }
////                hibernateEntityObject = ((List) entry.getValue()).get(0);
////            }else if(entry.getValue() instanceof Array){
////                if(entry.getValue()==null || ((Object[]) entry.getValue()).length==0){
////                    continue;
////                }
////                hibernateEntityObject = ((Object[]) entry.getValue())[0];
////            }else{
////                hibernateEntityObject = entry.getValue();
////            }
////            Class clazz = ClassUtil.getRealClass(hibernateEntityObject.getClass());
////            Entity entity = (Entity) clazz.getAnnotation(Entity.class);
////            if(entity!=null){
////                Field[] field = ClassUtil.getDeclaredFields(clazz,Id.class);
////                Object id = null;
////                if(field.length == 1){
////                    id = ClassUtil.getValue(hibernateEntityObject,field[0].getName());
////                }
////                PageItemData pageItemData = new PageItemData();
////                pageItemData.setPageItem(pageItem);
////                pageItemData.setClassName(clazz.getName());
////                pageItemData.setBeanId(id.toString());
////                pageItemDataService.save(pageItemData);
////                pageItemDatas.add(pageItemData);
////            }
////        }
//        for (Map.Entry<String, Object> entry : dm.entrySet()) {
//            if(entry.getValue() instanceof List){
//                if(entry.getValue()==null || ((List) entry.getValue()).size()==0){
//                    continue;
//                }
//                List<Object> objs= ((List) entry.getValue());
//                for(Object obj : objs){
//                    PageItemData pageItemData = this.getPageItemData(obj,pageItem,page.getTemplate().getPageType(),page.getPageSize());
//                    if(pageItemData!=null){
//                        pageItemDatas.add(pageItemData);
//                    }
//                }
//            }else if(entry.getValue() instanceof Array){
//                if(entry.getValue()==null || ((Object[]) entry.getValue()).length==0){
//                    continue;
//                }
//                Object []objs = ((Object[]) entry.getValue());
//                for(Object obj : objs){
//                    PageItemData pageItemData = this.getPageItemData(obj,pageItem,page.getTemplate().getPageType(),page.getPageSize());
//                    if(pageItemData!=null){
//                        pageItemDatas.add(pageItemData);
//                    }
//                }
//            }else{
////                hibernateEntityObject = entry.getValue();
//                PageItemData pageItemData = this.getPageItemData(entry.getValue(),pageItem);
//                if(pageItemData!=null){
//                    pageItemDatas.add(pageItemData);
//                }
//            }
//        }
//        // 保存pageItem
//        pageItemService.save(pageItem);
//        // 保存pageItemData
//        for(PageItemData pageItemData : pageItemDatas){
//            pageItemDataService.save(pageItemData);
//        }
//    }

    private PageItem savePageItem(Map<String,Object> dm,String fileName,Page page){
        return this.savePageItem(dm,fileName,page,null,null);
    }
    private PageItem savePageItem(Map<String,Object> dm,String fileName,Page page,Integer currentPage){
        return this.savePageItem(dm,fileName,page,currentPage,null);
    }
    private PageItem savePageItem(Map<String,Object> dm,String fileName,Page page,Integer currentPage, Long pageItemId){
        PageItemService pageItemService = SpringContextUtil.getBeanByType(PageItemService.class);

        if(pageItemId!=null){
            pageItemService.delete(pageItemId);
        }else{
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            filters.add(new PropertyFilter("EQL_page.id",page.getId()+""));
            filters.add(new PropertyFilter("EQS_file",fileName));
            List<PageItem> items = pageItemService.find(filters);
            if(items!=null && items.size()>0){
                for(PageItem item : items){
                    pageItemService.delete(item.getId());
                }
            }
        }

        PageItemDataService pageItemDataService = SpringContextUtil.getBeanByType(PageItemDataService.class);
        PageItem pageItem = new PageItem();
        pageItem.setPage(page);
        pageItem.setContent(page.getTemplate().getContent());
        pageItem.setFile(fileName);
        List<PageItemData> pageItemDatas = new ArrayList<PageItemData>();
        pageItem.setPageItemDatas(pageItemDatas);

        for (Map.Entry<String, Object> entry : dm.entrySet()) {
            if(entry.getValue() instanceof List){
                if(entry.getValue()==null || ((List) entry.getValue()).size()==0){
                    continue;
                }
                List<Object> objs= ((List) entry.getValue());
                for(Object obj : objs){
                    PageItemData pageItemData = this.getPageItemData(obj);
                    if(pageItemData==null){
                        continue;
                    }
                    pageItemData.setPageItem(pageItem);
                    pageItemDatas.add(pageItemData);
                    if(page.getTemplate().getPageType()==PageType.multi && entry.getKey().equals(page.getTemplate().getDataKey())){
                        pageItem.setCode(pageItemData.getBeanId() + "");
                    }
                }
            }else if(entry.getValue() instanceof Array){
                if(entry.getValue()==null || ((Object[]) entry.getValue()).length==0){
                    continue;
                }
                Object []objs = ((Object[]) entry.getValue());
                for(Object obj : objs){
                    PageItemData pageItemData = this.getPageItemData(obj);
                    if(pageItemData==null){
                        continue;
                    }
                    pageItemData.setPageItem(pageItem);
                    pageItemDatas.add(pageItemData);
                    if(page.getTemplate().getPageType()==PageType.multi && entry.getKey().equals(page.getTemplate().getDataKey())){
                        pageItem.setCode(pageItemData.getBeanId()+"");
                    }
                }
            }else{
//                hibernateEntityObject = entry.getValue();
                if(entry.getValue()!=null){
                    PageItemData pageItemData = this.getPageItemData(entry.getValue());
                    if(pageItemData!=null){
                        pageItemData.setPageItem(pageItem);
                        pageItemDatas.add(pageItemData);
                        if(page.getTemplate().getPageType()==PageType.multi && entry.getKey().equals(page.getTemplate().getDataKey())){
                            pageItem.setCode(pageItemData.getBeanId()+"");
                        }
                    }
                }
            }
        }
        if(page.getTemplate().getPageType()==PageType.pagination){  // 分页页面
            pageItem.setCode(currentPage+"");
        }
        // 保存pageItem
        pageItemService.save(pageItem);
        // 保存pageItemData
        for(PageItemData pageItemData : pageItemDatas){
            pageItemDataService.save(pageItemData);
        }
        return pageItem;
    }

    private PageItemData getPageItemData(Object hibernateEntityObject){
        Class clazz = ClassUtil.getRealClass(hibernateEntityObject.getClass());
        Entity entity = (Entity) clazz.getAnnotation(Entity.class);
        if(entity!=null){
            Field[] fields = ClassUtil.getDeclaredFields(clazz,Id.class);
            Object id = null;
            if(fields.length == 1){
                id = ClassUtil.getValue(hibernateEntityObject,fields[0].getName());
            }
            PageItemData pageItemData = new PageItemData();
            pageItemData.setClassName(clazz.getName());
            pageItemData.setBeanId(id.toString());
//            pageItemDatas.add(pageItemData);

            return pageItemData;
        }
        return null;
    }
    private Map<String,Object> sysProperty(){
        String webSource = PropertiesHelper.load("props/application.properties").getProperty("webSource");
        String tripartiteSource = PropertiesHelper.load("props/application.properties").getProperty("tripartiteSource");
        String serverSource =  PropertiesHelper.load("props/application.properties").getProperty("serverSource");
        Map<String, Object> dm = new HashMap<String, Object>();
        if(webSource!=null){
            dm.put("webSource",webSource);
        }
        if(tripartiteSource!=null){
            dm.put("tripartiteSource",tripartiteSource);
        }
        if(serverSource!=null){
            dm.put("serverSource",serverSource);
        }
        return dm;
    }
}
