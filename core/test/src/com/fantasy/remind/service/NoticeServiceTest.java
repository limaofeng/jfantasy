package com.fantasy.remind.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.remind.bean.Model;
import com.fantasy.remind.bean.Notice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class NoticeServiceTest {

    private static final Log logger = LogFactory.getLog(Model.class);

    @Resource
    private NoticeService noticeService;
    @Resource
    private ModelService modelService;
    private String[] models;
    private Long[] notices;

    @Before
    public void setUp() throws Exception {
        this.testModelSave();
        this.testSave();
    }

    @After
    public void tearDown() throws Exception {
        this.testModelDelete();
    }
    @Test
    public void test() throws Exception {
        Model model=new Model();
        model.setCode("shuju");
        model.setName("数据的name");
        model.setModelImageStore("[{\"creator\":\"hebo\",\"createTime\":\"2014-07-25 09:36:28\",\"modifier\":\"hebo\",\"modifyTime\":\"2014-07-25 09:36:28\",\"absolutePath\":\"/static/images/brand/Chrysanthemum.jpg\",\"fileManagerId\":\"haolue-upload\",\"fileName\":\"Chrysanthemum.jpg\",\"ext\":null,\"contentType\":\"image/jpeg\",\"description\":\"\",\"size\":879394,\"md5\":\"076e3caed758a1c18c91a0e9cae3368f\"}]");
        model.setContent("我要提醒content:${content}的key:${key}测试");
        model.setUrl("http://www.baidu.com?id=${id}");
        modelService.save(model);

        Notice notice=new Notice();
        notice.setModel(new Model("shuju"));
        notice.setContent("数据啊");
        notice.setUrl("http://www.baidu.com");
        noticeService.save(notice);

        Notice notice2=new Notice();
        notice2.setModel(new Model("shuju"));
        Map<String,String> map=new HashMap<String, String>();
        map.put("key","我的key");
        map.put("content","mycontent");
        map.put("id","ggg");
        notice2.setReplaceMap(JSON.serialize(map));
        noticeService.save(notice2);

        Notice notice3=new Notice();
        notice3.setModel(new Model("shuju"));
        Map<String,String> map3=new HashMap<String, String>();
        map3.put("key","123");
        map3.put("content","456");
        notice3.setUrl("http://www.douban.com");
        notice3.setReplaceMap(JSON.serialize(map3));
        noticeService.save(notice3);
    }

    @Test
    public void testFindPager() throws Exception {
        List<PropertyFilter> modelFilter=new ArrayList<PropertyFilter>();
        modelFilter.add(new PropertyFilter("EQS_code","test"));
        Pager<Model> modelPager=modelService.findPager(new Pager<Model>(),modelFilter);
        logger.info("modelPager:" + JSON.serialize(modelPager));
        for(Model m:modelPager.getPageItems()){
            Model logModel=modelService.get(m.getCode());
            logger.info("logNotice:" + JSON.serialize(logModel));
        }

        List<Model> list=modelService.findAll();
        for(Model m:list){
            logger.info("logNotice:" + JSON.serialize(m));
        }

        /**
         * notice的查询方法
         */
        List<PropertyFilter> noticeFilter=new ArrayList<PropertyFilter>();
        noticeFilter.add(new PropertyFilter("EQS_model.code","test"));
        Pager<Notice> noticePager=noticeService.findPager(new Pager<Notice>(),noticeFilter);
        logger.info("noticePager:" + JSON.serialize(noticePager));
        for(Notice n:noticePager.getPageItems()){
            Notice logNotice=noticeService.get(n.getId());
            logger.info("logNotice:" + JSON.serialize(logNotice));
        }
    }

    public void testSave() throws Exception {

        Notice notice=new Notice();
        notice.setModel(new Model("test"));
        notice.setContent("无名");
        notice.setUrl("http://www.baidu.com");
        noticeService.save(notice);

        Notice notice2=new Notice();
        notice2.setModel(new Model("test"));
        Map<String,String> map=new HashMap<String, String>();
        map.put("key","1key1");
        map.put("content","1content1");
        map.put("id","123");
        notice2.setReplaceMap(JSON.serialize(map));
        noticeService.save(notice2);

        Notice notice3=new Notice();
        notice3.setModel(new Model("test"));
        Map<String,String> map3=new HashMap<String, String>();
        map3.put("key","1key1");
        map3.put("content","1content1");
        notice3.setUrl("http://www.douban.com");
        notice3.setReplaceMap(JSON.serialize(map3));
        noticeService.save(notice3);
        this.notices=new Long[]{notice.getId(),notice2.getId(),notice3.getId()};
    }

    public void testModelSave() throws Exception {
        this.models=new String[1];
        Model model=new Model();
        model.setCode("test");
        model.setName("测试的name");
        model.setModelImageStore("[{\"creator\":\"hebo\",\"createTime\":\"2014-07-25 09:36:28\",\"modifier\":\"hebo\",\"modifyTime\":\"2014-07-25 09:36:28\",\"absolutePath\":\"/static/images/brand/Chrysanthemum.jpg\",\"fileManagerId\":\"haolue-upload\",\"fileName\":\"Chrysanthemum.jpg\",\"ext\":null,\"contentType\":\"image/jpeg\",\"description\":\"\",\"size\":879394,\"md5\":\"076e3caed758a1c18c91a0e9cae3368f\"}]");
        model.setContent("我要提醒content:${content}的key:${key}测试");
        model.setUrl("http://www.baidu.com?id=${id}");
        modelService.save(model);
        this.models[0]=model.getCode();
    }

    @Test
    public void testModelDelete() throws Exception {
        noticeService.delete(notices);
        modelService.delete(models);
    }
    public void testModelGet() throws Exception {
        Model model=modelService.get("test");
        logger.info("model:"+ JSON.serialize(model));
    }
}