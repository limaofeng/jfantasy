package com.fantasy.swp.service;


import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.freemarker.TemplateModelUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.bean.PageItem;
import com.fantasy.swp.bean.enums.PageType;
import com.fantasy.swp.util.GeneratePageUtil;
import freemarker.template.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Service
@Transactional
public class ReGeneratePageService {

    private FileManager fileManager = new LocalFileManager("E:/aa");
    @Resource
    private PageItemService pageItemService;

    public void execute(Long pageItemId) throws IOException {
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
//                    this.savePageItem("");
            }
        }else if(template.getPageType()==PageType.multi){   // 多页面
            List<Object> list = (List<Object>) dm.get(template.getDataKey());
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
            }
        }else{   // 单页面
            String fileName = page.getPath();
            FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(dm), configuration.getTemplate(template.getPath()), fileManager.writeFile(fileName));
//            this.savePageItem(dm,fileName);
        }

        System.out.println("pageItem.createTime="+pageItem.getCreateTime());
    }

}
