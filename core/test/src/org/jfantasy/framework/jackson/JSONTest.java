package org.jfantasy.framework.jackson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.asm.AnnotationDescriptor;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
        department.setUserName("limaofeng");

        String json = JSON.serialize(department);
        LOG.debug(json);

        Assert.assertEquals("{\"name\":\"技术部\",\"projectManager\":\"test\",\"id\":\"1\"}",json);

        department = JSON.deserialize(json,Department.class);

        assert department != null;
        Assert.assertEquals(department.get("id"),"1");

    }



    public static class TestDataBuilder {

        public static <T> T build(Class<T> clazz, String keywords) {
            if(ArticleCategory.class.isAssignableFrom(clazz)){
                ArticleCategory category = new ArticleCategory();
                category.setCode("test");
                category.setName("测试");
                category.setLayer(0);
                category.setArticles(new ArrayList<Article>());
                return (T)category;
            }else if(Article.class.isAssignableFrom(clazz)){
                Article article = new Article();
                article.setCategory(build(ArticleCategory.class,keywords));
                article.setAuthor(keywords);
                article.setTitle(keywords + " 测试");
                article.setSummary(keywords + " Summary UUID = " + UUID.randomUUID());
                article.setContent(new Content(keywords + " Content"));
                article.getCategory().getArticles().add(article);
                return (T)article;
            }
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace(System.err);
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.err);
                return null;
            }
        }

    }

    @JsonIgnoreProperties
    public static class Article {

        private Long id;
        /**
         * 文章标题
         */
        private String title;
        /**
         * 摘要
         */
        private String summary;
        /**
         * 关键词
         */
        private String keywords;
        /**
         * 文章正文
         */
        private Content content;
        /**
         * 作者
         */
        private String author;
        /**
         * 发布日期
         */
        private String releaseDate;
        /**
         * 文章对应的栏目
         */
        private ArticleCategory category;
        /**
         * 发布标志
         */
        private Boolean issue;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public ArticleCategory getCategory() {
            return category;
        }

        public void setCategory(ArticleCategory category) {
            this.category = category;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public Boolean getIssue() {
            return issue;
        }

        public void setIssue(Boolean issue) {
            this.issue = issue;
        }

        public static class ContentSerialize extends JsonSerializer<Content> {

            @Override
            public void serialize(Content content, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(content.toString());
            }

        }

        @Override
        public String toString() {
            return "Article{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", summary='" + summary + '\'' +
                    ", keywords='" + keywords + '\'' +
                    ", author='" + author + '\'' +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", issue=" + issue +
                    '}';
        }
    }


    @JsonIgnoreProperties
    public static class ArticleCategory {

        private String code;
        /**
         * 栏目名称
         */
        private String name;
        /**
         * 层级
         */
        private Integer layer;
        // 树路径
        private String path;
        /**
         * 描述
         */
        private String description;
        /**
         * 排序字段
         */
        private Integer sort;
        /**
         * 上级栏目
         */
        private ArticleCategory parent;
        /**
         * 下级栏目
         */
        private List<ArticleCategory> children;
        /**
         * 文章
         */
        private List<Article> articles;

        public ArticleCategory() {
        }

        public ArticleCategory(String code) {
            this.setCode(code);
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getLayer() {
            return layer;
        }

        public void setLayer(Integer layer) {
            this.layer = layer;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public ArticleCategory getParent() {
            return parent;
        }

        public void setParent(ArticleCategory parent) {
            this.parent = parent;
        }

        public List<ArticleCategory> getChildren() {
            return children;
        }

        public void setChildren(List<ArticleCategory> children) {
            this.children = children;
        }

        public List<Article> getArticles() {
            return articles;
        }

        public void setArticles(List<Article> articles) {
            this.articles = articles;
        }
    }


    public class Department {
        private String name;
        private String pm;
        private String userName;
        private Properties otherProperties = new Properties(); //otherProperties用来存放Department中未定义的json字段
        //指定json反序列化创建Department对象时调用此构造函数
        @JsonCreator
        public Department(@JsonProperty("name") String name){
            this.name = name;
        }

        //将company.json中projectManager字段关联到getPm方法
        @JsonProperty("projectManager")
        public String getPm() {
            return pm;
        }

        public void setPm(String pm) {
            this.pm = pm;
        }

        public String getName() {
            return name;
        }

        public Object get(String key) {
            return otherProperties.get(key);
        }

        //得到所有Department中未定义的json字段的
        @JsonAnyGetter
        public Properties any() {
            return otherProperties;
        }

        @JsonAnySetter
        public void set(String key, Object value) {
            otherProperties.put(key, value);
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public static class Content {

        private static final long serialVersionUID = -7570871629827875364L;

        private Long id;
        /**
         * 正文内容
         */
        private String text;

        public Content() {
        }

        public Content(String text) {
            this.setText(text);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text == null ? "" : this.text;
        }

        public class ContentSerialize extends JsonSerializer<Content> {

            @Override
            public void serialize(Content content, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (content == null) {
                    jgen.writeString("");
                } else {
                    jgen.writeString(content.toString());
                }
            }

        }

    }


}