package org.jfantasy.framework;

import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.member.bean.Member;
import org.jfantasy.system.bean.DataDictionaryType;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import junit.framework.Assert;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * json 测试类
 */
public class JSONTest {

    private final static Log LOG = LogFactory.getLog(JSONTest.class);

    @Test
    public void unknownProperties(){
        String json = "{\"unknownPropertie\":\"xxx\",\"username\":\"limaofeng\",\"enabled\":false,\"password\":\"123456\",\"accountNonLocked\":false,\"credentialsNonExpired\":false}";
        JSON.deserialize(json, Member.class);
    }

    @Test
    public void serializeDynaBean() {
        Member member = new Member();
        member.setUsername("limaofeng");
        member.setPassword("123456");
        LOG.debug(JSON.serialize(member));

        String json = "{\"username\":\"limaofeng\",\"enabled\":false,\"password\":\"123456\",\"accountNonLocked\":false,\"credentialsNonExpired\":false}";
        member = JSON.deserialize(json, Member.class);
        LOG.debug(member);
//        Class clazz = AsmUtil.makeClass(Article.class.getName() + "$json", Article.class.getName(), new Property("test", String.class));
//        Object object = ClassUtil.newInstance(clazz);
//        LOG.debug(JSON.serialize(object));
    }

    @Test
    public void serialize() throws OgnlException {

        DataDictionaryType ddt = new DataDictionaryType();
        ddt.setCode("junit-test");
        ddt.setName("junit-测试");

        System.out.println(JSON.serialize(ddt));

        System.out.println(JSON.deserialize(JSON.serialize(ddt)));

//        Member member= new Member();
//        member.setUsername("limaofeng");
//        member.setPassword("123456");
//        member.setNickName("张三");

        //测试普通json转换
        //Assert.assertEquals(JSON.unicode().serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"\\u5F20\\u4E09\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试text json 转换
        //Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试set json 转换
        //Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试 date
//        member.setLastLoginTime(DateUtil.parse("2014-10-20 10:29:34"));
        //Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":\"2014-10-20 10:29:34\",\"details\":null}");

//        Ognl.getValue("@org.jfantasy.framework.util.jackson.JSON@serialize(pager)",new HashMap(){
//            {
//                this.put("pager",new Object());
//                this.put("array",new String[0]);
//            }
//        });
    }

    @Test
    public void deserialize() {
        Member member = JSON.deserialize("{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":\"2014-10-20 10:29:34\",\"details\":null}", Member.class);
        //中文
        assert member != null;
        Assert.assertEquals(member.getNickName(), "张三");
        //date
        Assert.assertEquals(member.getLastLoginTime(), DateUtil.parse("2014-10-20 10:29:34"));
    }

    @Test
    public void jsonView() throws Exception {
        TestPOJO testPOJO = new TestPOJO();
        testPOJO.setA("1");
        testPOJO.setB("2");
        testPOJO.setC("3");
        testPOJO.setD("4");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        String jsonStr = objectMapper.writerWithView(FilterView.OutputA.class).writeValueAsString(testPOJO);
        Assert.assertEquals("{\"a\":\"1\",\"c\":\"3\"}", jsonStr);
        String jsonStr2 = objectMapper.writerWithView(FilterView.OutputB.class).writeValueAsString(testPOJO);
        Assert.assertEquals("{\"d\":\"4\",\"b\":\"2\"}", jsonStr2);
        String jsonStr3 = objectMapper.writeValueAsString(testPOJO);
        LOG.debug(jsonStr3);
    }

    private static class FilterView {
        static class OutputA {

        }

        static class OutputB {

        }
    }

    @Test
    public void jsonFilter() throws Exception {
        final TestPOJO testPOJO = new TestPOJO();
        testPOJO.setA("1");
        testPOJO.setB("2");
        testPOJO.setC("3");
        testPOJO.setD("4");

        final TestPOJO testPOJO1 = new TestPOJO();
        testPOJO1.setA("11");
        testPOJO1.setB("22");
        testPOJO1.setC("33");
        testPOJO1.setD("44");

        List<TestPOJO> testPOJOs = new ArrayList<TestPOJO>() {
            {
                this.add(testPOJO);
                this.add(testPOJO1);
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
//        FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.filterOutAllExcept("a"));
//        objectMapper.setFilters(filters);
//        String jsonStr = objectMapper.writeValueAsString(testPOJO);
//        LOG.debug(jsonStr);

//        LOG.debug(objectMapper.writeValueAsString(testPOJOs));

        SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
//        filter.addFilter("myFilter", SimpleBeanPropertyFilter.filterOutAllExcept("a", "b"));

        /*
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

            @Override
            public String[] findPropertiesToIgnore(Annotated ac) {
                JsonIgnoreProperties ignore = ac.getAnnotation(JsonIgnoreProperties.class);
                if (ignore != null) {
                    LOG.debug(Arrays.toString(ignore.value()));
                    return null;
                }
                return (ignore == null) ? null : ignore.value();
            }
        });*/
        //SimpleBeanPropertyFilter.filterOutAllExcept("");//这里需要填需要留下的属性
        //SimpleBeanPropertyFilter.serializeAllExcept("");//这里需要真需要排除的属性

        String[] propertyNames = ObjectUtil.toFieldArray(ClassUtil.getPropertys(TestPOJO.class), "name", new String[0]);


        LOG.debug(Arrays.toString(propertyNames));

        filter.addFilter("testPOJOFilter", SimpleBeanPropertyFilter.serializeAllExcept("b", "c"));
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.writerWithView(FilterView.OutputB.class).writeValueAsString(testPOJO);


        LOG.debug(objectMapper.writer(filter).writeValueAsString(testPOJO));
        LOG.debug(objectMapper.writer(filter).writeValueAsString(testPOJOs));

//        Assert.assertEquals("{\"a\":\"1\"}", jsonStr);
//        objectMapper.setFilters(new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.filterOutAllExcept("b")));
//        String jsonStr1 = objectMapper.writeValueAsString(testPOJO);
//        LOG.debug(jsonStr1);
//        Assert.assertEquals("{\"b\":\"2\"}", jsonStr1);
    }

//    @JsonFilter("testPOJOFilter")
//    @JsonIgnoreProperties("b")
    //http://blog.csdn.net/sdyy321/article/details/40298081
    public static class TestPOJO {
        @JsonView(FilterView.OutputA.class)
        private String a;
        @JsonView(FilterView.OutputA.class)
        private String c;
        @JsonView(FilterView.OutputB.class)
        private String d;
        @JsonUnwrapped
        @JsonView(FilterView.OutputB.class)
        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getD() {
            return d;
        }

        public void setD(String d) {
            this.d = d;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }


    @Test
    public void testJsonUnwrapped(){
        TestPOJOJsonUnwrapped pojo = new TestPOJOJsonUnwrapped();
        pojo.setA("test");
        pojo.setDbean(new JsonUnwrappedPoJo("蔡依林",30));
        LOG.debug(JSON.serialize(pojo));
        pojo.setDbean(new JsonUnwrappedPoJo1("周杰伦",45));
        LOG.debug(JSON.serialize(pojo));

        pojo = JSON.deserialize("{\"name\":\"蔡依林\",\"age\":30,\"a\":\"test\"}", TestPOJOJsonUnwrapped.class);
        Assert.assertNotNull(pojo);
        Assert.assertNotNull(pojo.getPoJo());
        Assert.assertEquals(pojo.getPoJo().getName(), "蔡依林");
    }

    public static class TestPOJOJsonUnwrapped extends AbsTestPOJOJsonUnwrapped{
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

    }

    public abstract static class AbsTestPOJOJsonUnwrapped {
        @JsonUnwrapped
        private JsonUnwrappedPoJo poJo;
        @JsonUnwrapped
        private Object dbean;

        public Object getDbean() {
            return dbean;
        }

        public void setDbean(Object dbean) {
            this.dbean = dbean;
        }

        public JsonUnwrappedPoJo getPoJo() {
            return poJo;
        }

        public void setPoJo(JsonUnwrappedPoJo poJo) {
            this.poJo = poJo;
        }
    }


    public static class JsonUnwrappedPoJo {
        private String name;
        private int age;

        public JsonUnwrappedPoJo() {
        }

        public JsonUnwrappedPoJo(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class JsonUnwrappedPoJo1 {
        private String rname;
        private int rage;
        public JsonUnwrappedPoJo1() {
        }
        public JsonUnwrappedPoJo1(String name, int age) {
            this.rname = name;
            this.rage = age;
        }

        public String getRname() {
            return rname;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public int getRage() {
            return rage;
        }

        public void setRage(int rage) {
            this.rage = rage;
        }
    }
}
