package com.fantasy.swp;

import com.fantasy.attr.service.ArticleService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.bean.PageBean;
import com.fantasy.swp.exception.SwpException;
import com.fantasy.swp.factory.SwpWebsiteFactory;
import com.fantasy.swp.service.SpelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzhiyong on 2015/3/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SwpNewTest {

    @Resource
    private SwpWebsiteFactory swpWebsiteFactory;

    @Test
    public void run() throws Exception{
        System.out.println("==============run start==============");
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");

        //1.添加模板 - 包括添加数据定义 - 指定模板类型（单页、多页、分页）
        String templatePath = "template/template_test_new_sig.ftl";
        String html = FileUtil.readFile(SwpNewTest.class.getClass().getResource("/").getPath()+templatePath);
        List<DataInferface> dataInferfaces = new ArrayList<DataInferface>();
        DataInferface dataInferface = new DataInferface();
        dataInferface.setKey("articles");
        dataInferface.setName("文章列表");
        dataInferface.setDataType(DataInferface.DataType.list);
        DataInferface dataInferface2 = new DataInferface();
        dataInferface2.setKey("article");
        dataInferface2.setName("文章");
        dataInferface2.setDataType(DataInferface.DataType.object);
        DataInferface dataInferface3 = new DataInferface();
        dataInferface3.setKey("title");
        dataInferface3.setName("标题");
        dataInferface3.setDataType(DataInferface.DataType.common);
        DataInferface dataInferface4 = new DataInferface();
        dataInferface4.setKey("summary");
        dataInferface4.setName("摘要");
        dataInferface4.setDataType(DataInferface.DataType.common);

        dataInferfaces.add(dataInferface);
        dataInferfaces.add(dataInferface2);
        dataInferfaces.add(dataInferface3);
        dataInferfaces.add(dataInferface4);
        swpWebsite.addSingleTemplate(templatePath,html,dataInferfaces);
        //2.添加page - 指定数据定义的数据 - 配置page的触发器
        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        String url = "xxx/article/single.html";
        List<Data> datas = new ArrayList<Data>();
        Data data = new Data();
        data.setDataInferface(dataInferface);
        data.setDataSource(Data.DataSource.func);
        data.setValue("{'func':'#cmsService.find(#filters,#orderby,#order,#size)','params':{filters:[],stat:{'orderby':'id','order':'desc','size':10} }}");
        Data data2 = new Data();
        data2.setDataInferface(dataInferface2);
        data2.setDataSource(Data.DataSource.db);
        data2.setValue("{hql:\"from Article where id=1\",operate:\"object\"}");
        Data data3 = new Data();
        data3.setDataInferface(dataInferface3);
        data3.setDataSource(Data.DataSource.stat);
        data3.setValue("张三");
        Data data4 = new Data();
        data4.setDataInferface(dataInferface4);
        data4.setDataSource(Data.DataSource.stat);
        data4.setValue("摘要XXXXXXX");

        datas.add(data);
        datas.add(data2);
        datas.add(data3);
        datas.add(data4);

        swpWebsite.createSinglePage(url,templatePath,"SWP_JUNIT_TEST_",datas);
        //3.通过page生成页面
        IPage ipage = swpWebsite.getPgae(url);
        List<IPageItem> ipageItems = ipage.createPageItems();
        System.out.println("==============run end==============");
    }
    @Test
    public void createByItem() throws SwpException, IOException {
        System.out.println("===============createByItem start===============");
        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        //4.通过pageItem重新生成页面
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        IPage ipage = swpWebsite.getPgae("xxx/article/single.html");
        IPageItem iPageItem = ipage.getPageItem("xxx/article/single.html");
        iPageItem.refash();
        System.out.println("===============createByItem end===============");
    }

    @Test
    public void deleteTemplate(){
        System.out.println("===============deleteTemplate start===============");
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        String templatePath = "template/template_test_new_sig.ftl";
        swpWebsite.removeTemplate(templatePath);
        System.out.println("===============deleteTemplate end===============");
    }
    @Test
    public void deletePage(){
        System.out.println("===============deletePage start===============");
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        String url = "xxx/article/single.html";
        swpWebsite.removePage(url);
        System.out.println("===============deletePage end===============");
    }


    //1.********************************************************************
    /*
    ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");

        //1.添加模板 - 包括添加数据定义 - 指定模板类型（单页、多页、分页）
        String templatePath = "template/template_test_new.ftl";
        String html = FileUtil.readFile(SwpNewTest.class.getClass().getResource("/").getPath()+templatePath);
        swpWebsite.addTemplate(templatePath,html);
        //2.添加page - 指定数据定义的数据 - 配置page的触发器
        String url = "xxx/article/static.html";
        swpWebsite.createPage(url,templatePath,"SWP_JUNIT_TEST_");
        //3.通过page生成页面
        IPage ipage = swpWebsite.getPgae(url);
        List<IPageItem> ipageItems = ipage.createPageItems();

        //////////////////////////////////////////////
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        IPage ipage = swpWebsite.getPgae("xxx/article/static.html");
        IPageItem iPageItem = ipage.getPageItem("xxx/article/static.html");
        iPageItem.refash();
     */
    //2.单页面********************************************************************
    /*
    ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");

        //1.添加模板 - 包括添加数据定义 - 指定模板类型（单页、多页、分页）
        String templatePath = "template/template_test_new_sig.ftl";
        String html = FileUtil.readFile(SwpNewTest.class.getClass().getResource("/").getPath()+templatePath);
        List<DataInferface> dataInferfaces = new ArrayList<DataInferface>();
        DataInferface dataInferface = new DataInferface();
        dataInferface.setKey("articles");
        dataInferface.setName("文章列表");
        dataInferface.setDataType(DataInferface.DataType.list);
        DataInferface dataInferface2 = new DataInferface();
        dataInferface2.setKey("article");
        dataInferface2.setName("文章");
        dataInferface2.setDataType(DataInferface.DataType.object);
        DataInferface dataInferface3 = new DataInferface();
        dataInferface3.setKey("title");
        dataInferface3.setName("标题");
        dataInferface3.setDataType(DataInferface.DataType.common);
        DataInferface dataInferface4 = new DataInferface();
        dataInferface4.setKey("summary");
        dataInferface4.setName("摘要");
        dataInferface4.setDataType(DataInferface.DataType.common);

        dataInferfaces.add(dataInferface);
        dataInferfaces.add(dataInferface2);
        dataInferfaces.add(dataInferface3);
        dataInferfaces.add(dataInferface4);
        swpWebsite.addSingleTemplate(templatePath,html,dataInferfaces);
        //2.添加page - 指定数据定义的数据 - 配置page的触发器
        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        String url = "xxx/article/single.html";
        List<Data> datas = new ArrayList<Data>();
        Data data = new Data();
        data.setDataInferface(dataInferface);
        data.setDataSource(Data.DataSource.func);
        data.setValue("{'func':'#cmsService.find(#filters,#orderby,#order,#size)','params':{filters:[],stat:{'orderby':'id','order':'desc','size':10} }}");
        Data data2 = new Data();
        data2.setDataInferface(dataInferface2);
        data2.setDataSource(Data.DataSource.db);
        data2.setValue("{hql:\"from Article where id=1\",operate:\"object\"}");
        Data data3 = new Data();
        data3.setDataInferface(dataInferface3);
        data3.setDataSource(Data.DataSource.stat);
        data3.setValue("张三");
        Data data4 = new Data();
        data4.setDataInferface(dataInferface4);
        data4.setDataSource(Data.DataSource.stat);
        data4.setValue("摘要XXXXXXX");

        datas.add(data);
        datas.add(data2);
        datas.add(data3);
        datas.add(data4);

        swpWebsite.createSinglePage(url,templatePath,"SWP_JUNIT_TEST_",datas);
        //3.通过page生成页面
        IPage ipage = swpWebsite.getPgae(url);
        List<IPageItem> ipageItems = ipage.createPageItems();


        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        //4.通过pageItem重新生成页面
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        IPage ipage = swpWebsite.getPgae("xxx/article/single.html");
        IPageItem iPageItem = ipage.getPageItem("xxx/article/single.html");
        iPageItem.refash();
     */

    //3.多页面********************************************************************
    /*
    ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");

        //1.添加模板 - 包括添加数据定义 - 指定模板类型（单页、多页、分页）
        String templatePath = "template/template_test_new_multi.ftl";
        String html = FileUtil.readFile(SwpNewTest.class.getClass().getResource("/").getPath()+templatePath);
        List<DataInferface> dataInferfaces = new ArrayList<DataInferface>();
        DataInferface dataInferface = new DataInferface();
        dataInferface.setKey("article");
        dataInferface.setName("文章");
        dataInferface.setDataType(DataInferface.DataType.list);
        DataInferface dataInferface3 = new DataInferface();
        dataInferface3.setKey("title");
        dataInferface3.setName("标题");
        dataInferface3.setDataType(DataInferface.DataType.common);
        DataInferface dataInferface4 = new DataInferface();
        dataInferface4.setKey("summary");
        dataInferface4.setName("摘要");
        dataInferface4.setDataType(DataInferface.DataType.common);

        dataInferfaces.add(dataInferface);
        dataInferfaces.add(dataInferface3);
        dataInferfaces.add(dataInferface4);

        String dataKey = "article";
        swpWebsite.addMultiTemplate(templatePath,html,dataInferfaces,dataKey);
        //2.添加page - 指定数据定义的数据 - 配置page的触发器
        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        String url = "xxx/article/multi_${"+dataKey+".id}.html";
        List<Data> datas = new ArrayList<Data>();
        Data data = new Data();
        data.setDataInferface(dataInferface);
        data.setDataSource(Data.DataSource.func);
        data.setValue("{'func':'#cmsService.find(#filters,#orderby,#order,#size)','params':{filters:[],stat:{'orderby':'id','order':'desc','size':10} }}");
        Data data3 = new Data();
        data3.setDataInferface(dataInferface3);
        data3.setDataSource(Data.DataSource.stat);
        data3.setValue("张三2");
        Data data4 = new Data();
        data4.setDataInferface(dataInferface4);
        data4.setDataSource(Data.DataSource.stat);
        data4.setValue("摘要XXXXXXX2");

        datas.add(data);
        datas.add(data3);
        datas.add(data4);

        swpWebsite.createMultiPage(url,templatePath,"SWP_JUNIT_TEST_",datas);
        //3.通过page生成页面
        IPage ipage = swpWebsite.getPgae(url);
        List<IPageItem> ipageItems = ipage.createPageItems();

        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        //4.通过pageItem重新生成页面
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        IPage ipage = swpWebsite.getPgae("xxx/article/multi_${article.id}.html");
        IPageItem iPageItem = ipage.getPageItem("xxx/article/multi_110.html");
        iPageItem.refash();


    //4.分页************************************************************************
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");

        //1.添加模板 - 包括添加数据定义 - 指定模板类型（单页、多页、分页）
        String templatePath = "template/template_test_new_pagnation.ftl";
        String html = FileUtil.readFile(SwpNewTest.class.getClass().getResource("/").getPath()+templatePath);
        List<DataInferface> dataInferfaces = new ArrayList<DataInferface>();
        DataInferface dataInferface = new DataInferface();
        String dataKey = "articles";
        dataInferface.setKey(dataKey);
        dataInferface.setName("文章-分页");
        dataInferface.setDataType(DataInferface.DataType.list);
        DataInferface dataInferface3 = new DataInferface();
        dataInferface3.setKey("title");
        dataInferface3.setName("标题");
        dataInferface3.setDataType(DataInferface.DataType.common);
        DataInferface dataInferface4 = new DataInferface();
        dataInferface4.setKey("summary");
        dataInferface4.setName("摘要");
        dataInferface4.setDataType(DataInferface.DataType.common);

        dataInferfaces.add(dataInferface);
        dataInferfaces.add(dataInferface3);
        dataInferfaces.add(dataInferface4);

        swpWebsite.addPaginationTemplate(templatePath,html,dataInferfaces,dataKey);
        //2.添加page - 指定数据定义的数据 - 配置page的触发器
        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        String url = "xxx/article/pagination/${pager.currentPage}.html";
        List<Data> datas = new ArrayList<Data>();
        Data data = new Data();
        data.setDataInferface(dataInferface);
        data.setDataSource(Data.DataSource.func);
        data.setValue("{'func':'#cmsService.find(#filters,#orderby,#order,#size)','params':{filters:[],stat:{'orderby':'id','order':'desc','size':10} }}");
        Data data3 = new Data();
        data3.setDataInferface(dataInferface3);
        data3.setDataSource(Data.DataSource.stat);
        data3.setValue("张三2");
        Data data4 = new Data();
        data4.setDataInferface(dataInferface4);
        data4.setDataSource(Data.DataSource.stat);
        data4.setValue("摘要XXXXXXX2");

        datas.add(data);
        datas.add(data3);
        datas.add(data4);

        int size = 3;
        swpWebsite.createPaginationPage(url,templatePath,"SWP_JUNIT_TEST_",datas,size);
        //3.通过page生成页面
        IPage ipage = swpWebsite.getPgae(url);
        List<IPageItem> ipageItems = ipage.createPageItems();



        SpelService.setServer("cmsService", SpringContextUtil.getBeanByType(ArticleService.class));
        //4.通过pageItem重新生成页面
        ISwpWebsite swpWebsite = swpWebsiteFactory.getInstance("haolue");
        IPage ipage = swpWebsite.getPgae("xxx/article/pagination/${pager.currentPage}.html");
        IPageItem iPageItem = ipage.getPageItem("xxx/article/pagination/1.html");
        iPageItem.refash();
     */
}
