package com.fantasy.swp.bean;

import com.fantasy.swp.TemplateData;
import com.fantasy.swp.PageService;

import java.util.List;

public class PageTemplateConfig {
    //添加模板步骤
    //1.模板对应的文件管理器
    //2.选择模板			可以采用简写方式 如:   default:/12312.ftl
    private String template;
    //3.配置数据			如果数据之间有依赖的话。需要配置监听功能。
    private List<TemplateData> datas;
    //4.配置预览数据
    private List<TemplateData> previewDatas;
    //5.文件生成地址生成器
    private PageService pageWriter;
    //6.配置模板生成时对应的文件管理器.		一般指向apache对应的目录
    //7.完成
}
