package com.fantasy.swp;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.freemarker.TemplateModelUtils;
import com.fantasy.framework.util.common.PathUtil;
import com.fantasy.framework.util.jackson.JSON;
import freemarker.template.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class Freemarker {

    @Resource
    private transient Configuration configuration;

    @Test
    public void writer() throws IOException, ServletException {
        FileManager fileManager = new LocalFileManager(PathUtil.classes() + "/test.jfantasy.org");

        Map<String,Object> data = new HashMap<String, Object>();
        data.put("art", JSON.deserialize("{title:\"测试title\",text:\"text测试\"}"));
        data.put("title","测试标题");

        FreeMarkerTemplateUtils.writer(TemplateModelUtils.createScopesHashModel(data), configuration.getTemplate("template/test.ftl"), fileManager.writeFile("/test.txt"));
    }

}
