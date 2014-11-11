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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class NoticeServiceTest {

    private static final Log logger = LogFactory.getLog(Model.class);

    @Resource
    private NoticeService noticeService;
    @Resource
    private ModelService modelService;

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
    public void testFindPager() throws Exception {
        List<PropertyFilter> modelFilter=new ArrayList<PropertyFilter>();
        modelFilter.add(new PropertyFilter("code","test"));
        Pager<Model> modelPager=modelService.findPager(new Pager<Model>(),modelFilter);
        logger.info("modelPager:" + JSON.serialize(modelPager));
        for(Model m:modelPager.getPageItems()){
            Model logModel=modelService.get(m.getCode());
            logger.info("logNotice:" + JSON.serialize(logModel));
        }
        /**
         * notice的查询方法
         */
        List<PropertyFilter> noticeFilter=new ArrayList<PropertyFilter>();
        noticeFilter.add(new PropertyFilter("model.code","test"));
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
        notice.setReplaceMap(JSON.serialize(map));
        notice2.setUrl("http://www.baidu.com");
        noticeService.save(notice);
    }

    public void testModelSave() throws Exception {
        Model model=new Model();
        model.setCode("test");
        model.setModelImageStore("haolue-upload:/static/images/brand/Chrysanthemum.jpg");
        model.setContent("我要提醒content:${content}的key:${key}测试");
        modelService.save(model);
    }

    @Test
    public void testModelDelete() throws Exception {
        Model model=modelService.get("test");
        List<Notice> notices=model.getNotices();
        Long[] ids=new Long[notices.size()];
        for(int i=0,count=notices.size();i<count;i++){
            ids[i]=notices.get(i).getId();
        }
        noticeService.delete(ids);
        modelService.delete(new String[]{"title"});
    }
    public void testModelGet() throws Exception {
        Model model=modelService.get("test");
        logger.info("model:"+ JSON.serialize(model));
    }
}