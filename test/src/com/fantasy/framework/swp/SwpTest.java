package com.fantasy.framework.swp;


import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.swp.analysis.data.BasicTypeAnalyzer;
import com.fantasy.swp.bean.*;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SwpTest {

    @Resource
    private transient Configuration configuration;

    @Test
    public void run() throws IOException {

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
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        dataAnalyzer.setName("数据解析器");
        dataAnalyzer.setClassName(BasicTypeAnalyzer.class.getName());
        data.setDataAnalyzer(dataAnalyzer);

        //添加页面解析器
        PageAnalyzer pageAnalyzer = new PageAnalyzer();
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
    }

}
