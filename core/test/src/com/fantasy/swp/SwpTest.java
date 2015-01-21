package com.fantasy.swp;


import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.swp.service.DefaultPageService;
import com.fantasy.swp.template.FreemarkerTemplate;
import com.fantasy.swp.url.SimpleUrl;
import freemarker.template.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SwpTest {

    @Resource
    private transient Configuration configuration;

    @Test
    public void run() throws IOException {

        FileManager fileManager = new LocalFileManager("/Users/lmf/Downloads/test.jfantasy.org");

        //定义模板
        FreemarkerTemplate template = new FreemarkerTemplate(configuration.getTemplate("template/test.ftl"));
        template.add("testData", "我的测试页面");

        //定义 pageService
        DefaultPageService pageService = new DefaultPageService();
        pageService.setFileManager(fileManager);

        SimpleUrl url = new SimpleUrl("/test.txt");
        /**
         * 创建页面实例
         */
        PageInstance pageInstance = pageService.createPageInstance(url,template);

        /**
         * 生成页面
         */
        pageInstance.execute();

        //触发器
        //1.模板修改后，全部重新生成页面
        //2.如果页面引用的bean修改或者删除（TemplateData为hibernateBean时），重构生成页面
        //3.新增bean时，需要指定判断条件（1.什么类型的bean，2.字段判断）
        //4.删除bean时,需要指定判断条件（1.什么类型的bean，2.字段判断）

        //TODO 如果是列表页面或者详细页面如果设置
        //实例生成器
        //1.单例生成
        //2.Bean关联
        //3.列表管理

        //生成页面

        //HistoryService historyService = null;


        /*
        //添加模板
        Template template = new Template();
        template.setName("测试页面");
        template.setType(Template.Type.freeMarker);
        template.setFilePath("template/test.ftl");
        template.setDescription("测试模板");
        //添加数据接口
        List<DataInferface> dataInferfaces = new ArrayList<DataInferface>();
        DataInferface dataInferface = new DataInferface();
        dataInferface.setKey("testData");
        dataInferface.setName("测试数据");
        dataInferface.setDefaultValue("xxxxx");
        dataInferface.setJavaType(String.class.getName());
        dataInferfaces.add(dataInferface);
        template.setDataInferfaces(dataInferfaces);
        //添加页面
        Page page = new Page();
        page.setTemplate(template);
        page.setName("测试页");
        //添加数据定义
        List<Data> datas = new ArrayList<Data>();
        Data data = new Data();
        data.setDataInferface(dataInferface);
        data.setScope(Data.Scope.page);
        data.setCacheInterval(10000l);
        data.setValue("测试基本的数据集");
        datas.add(data);
        page.setDatas(datas);

        //添加数据解析器
        com.fantasy.swp.bean.DataAnalyzer dataAnalyzer = new com.fantasy.swp.bean.DataAnalyzer();
        dataAnalyzer.setName("数据解析器");
        dataAnalyzer.setClassName(BasicTypeAnalyzer.class.getName());
        data.setDataAnalyzer(dataAnalyzer);

        //添加页面解析器
        com.fantasy.swp.bean.PageAnalyzer pageAnalyzer = new PageAnalyzer();
        pageAnalyzer.setName("页面解析器");
        pageAnalyzer.setClassName("xxxxx");
        page.setPageAnalyzer(pageAnalyzer);

        System.out.println("模板文件路径:" + page.getTemplate().getFilePath());

        freemarker.template.Template template1 = configuration.getTemplate(page.getTemplate().getFilePath());


        Map<String,Object> dataMap = new HashMap<String,Object>();
        for (final Data _data : page.getDatas()) {
            dataMap.put(_data.getDataInferface().getKey(), new TemplateMethodModel() {
                @Override
                public Object exec(List arguments) throws TemplateModelException {
                    com.fantasy.swp.DataAnalyzer analyzer = (com.fantasy.swp.DataAnalyzer) ClassUtil.newInstance(_data.getDataAnalyzer().getClassName());
                    return analyzer.exec(_data.getValue(), ClassUtil.forName(_data.getDataInferface().getJavaType()),_data.getDataInferface().isList(),arguments);
                }
            });
        }
        StringWriter writer = new StringWriter();
        FreeMarkerTemplateUtils.writer(dataMap, template1, writer);

        System.out.println(writer);

        System.out.println("-------------------------------------");
        */
    }

}
