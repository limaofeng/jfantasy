package org.jfantasy.framework.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.build.TestDataBuilder;
import org.jfantasy.framework.util.asm.AnnotationDescriptor;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.test.bean.Article;
import org.jfantasy.test.bean.ArticleCategory;
import org.jfantasy.test.bean.Department;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JSONTest {

    private final static Log LOG = LogFactory.getLog(JSONTest.class);

    private Article article;
    private ArticleCategory category;

    @Before
    public void setUp() throws Exception {
        article = TestDataBuilder.build(Article.class,"JSONTest");
        assert article != null;
        category = article.getCategory();

        ObjectMapper objectMapper = JSON.getObjectMapper();
        ThreadJacksonMixInHolder.scan(Article.class,ArticleCategory.class);
        objectMapper.setMixIns(ThreadJacksonMixInHolder.getSourceMixins());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void serialize() throws Exception {
        ThreadJacksonMixInHolder holder = ThreadJacksonMixInHolder.getMixInHolder();
        holder.addIgnorePropertyNames(Article.class,"category");
        LOG.debug(JSON.serialize(category));

        holder = ThreadJacksonMixInHolder.getMixInHolder();
        holder.addIgnorePropertyNames(ArticleCategory.class,"articles");
        LOG.debug(JSON.serialize(article));
    }

    @Test
    public void filter() throws IOException {
        ObjectMapper objectMapper = JSON.getObjectMapper();
        objectMapper.addMixIn(Article.class, ArticleFilterMixIn.class);

        LOG.debug("mixInCount = " + objectMapper.mixInCount());

        LOG.debug("findMixInClassFor = " + objectMapper.findMixInClassFor(Article.class));

        SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
        filter.addFilter("article", SimpleBeanPropertyFilter.serializeAllExcept("category"));

        ObjectWriter objectWriter = objectMapper.writer(filter);

        String json = objectWriter.writeValueAsString(category);

        LOG.debug(json);

        if(objectMapper.findMixInClassFor(ArticleCategory.class) == null) {
            objectMapper = objectMapper.copy().addMixIn(ArticleCategory.class, CategoryFilterMixIn.class);
        }

        filter = new SimpleFilterProvider().setFailOnUnknownId(false);
        filter.addFilter("category", SimpleBeanPropertyFilter.serializeAllExcept("articles"));

        objectWriter = objectMapper.writer(filter);

        json = objectWriter.writeValueAsString(article);

        LOG.debug(json);
    }

    @Test
    public void addDynamicMixIn() throws IOException {
        // 动态生成 MixIn 接口
        Class newInterface = AsmUtil.makeInterface("org.jfantasy.framework.jackson.mixin.TestMixIn", AnnotationDescriptor.builder(JsonFilter.class).setValue("value", "x").build(), FilterMixIn.class);
        Assert.assertTrue(newInterface.isInterface());

        LOG.debug(AsmUtil.trace(newInterface));

        Assert.assertNotNull(newInterface.getAnnotation(JsonFilter.class));

        // 测试接口
        ObjectMapper objectMapper = JSON.getObjectMapper();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(output, JsonEncoding.UTF8);

        objectMapper.addMixIn(Article.class, newInterface);

        LOG.debug("mixInCount = " + objectMapper.mixInCount());

        LOG.debug("findMixInClassFor = " + objectMapper.findMixInClassFor(Article.class));

        SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
        filter.addFilter("x", SimpleBeanPropertyFilter.filterOutAllExcept("title"));

        ObjectWriter objectWriter = objectMapper.writer(filter);

        objectWriter.writeValue(generator, article);
        generator.flush();

        String json = output.toString("utf-8");

        LOG.debug(json);

    }

    @Test
    public void jsonAny(){
        Department department = new Department("技术部");
        department.setPm("test");
        department.set("id","1");

        String json = JSON.serialize(department);
        LOG.debug(json);

        Assert.assertEquals("{\"name\":\"技术部\",\"projectManager\":\"test\",\"id\":\"1\"}",json);

        department = JSON.deserialize(json,Department.class);

        assert department != null;
        Assert.assertEquals(department.get("id"),"1");

    }


}