package com.fantasy.framework;

import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.member.bean.Member;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import junit.framework.Assert;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.HashMap;

/**
 * json 测试类
 */
public class JSONTest {

    private final static Log LOG = LogFactory.getLog(JSONTest.class);

    @Test
    public void serializeDynaBean(){
//        Class clazz = AsmUtil.makeClass(Article.class.getName() + "$json", Article.class.getName(), new Property("test", String.class));
//        Object object = ClassUtil.newInstance(clazz);
//        LOG.debug(JSON.serialize(object));
    }

    @Test
    public void serialize() throws OgnlException {
        Member member= new Member();
        member.setUsername("limaofeng");
        member.setPassword("123456");
        member.setNickName("张三");

        //测试普通json转换
        Assert.assertEquals(JSON.unicode().serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"\\u5F20\\u4E09\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试text json 转换
        Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试set json 转换
        Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试 date
        member.setLastLoginTime(DateUtil.parse("2014-10-20 10:29:34"));
        Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":\"2014-10-20 10:29:34\",\"details\":null}");

        Ognl.getValue("@com.fantasy.framework.util.jackson.JSON@serialize(pager)",new HashMap(){
            {
                this.put("pager",new Object());
                this.put("array",new String[0]);
            }
        });
    }

    @Test
    public void deserialize(){
        Member member = JSON.deserialize("{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":\"2014-10-20 10:29:34\",\"details\":null}",Member.class);
        //中文
        assert member != null;
        Assert.assertEquals(member.getNickName(),"张三");
        //date
        Assert.assertEquals(member.getLastLoginTime(),DateUtil.parse("2014-10-20 10:29:34"));
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
        Assert.assertEquals("{\"a\":\"1\",\"c\":\"3\"}",jsonStr);
        String jsonStr2 = objectMapper.writerWithView(FilterView.OutputB.class).writeValueAsString(testPOJO);
        Assert.assertEquals("{\"d\":\"4\",\"b\":\"2\"}",jsonStr2);
        String jsonStr3 = objectMapper.writeValueAsString(testPOJO);
        LOG.debug(jsonStr3);
    }

    private static class FilterView {
        static class OutputA {

        }
        static class OutputB {

        }
    }

    public void jsonFilter() throws Exception {
        TestPOJO testPOJO = new TestPOJO();
        testPOJO.setA("1");
        testPOJO.setB("2");
        testPOJO.setC("3");
        testPOJO.setD("4");
        ObjectMapper objectMapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.filterOutAllExcept("a"));
        objectMapper.setFilters(filters);
        String jsonStr = objectMapper.writeValueAsString(testPOJO);
        LOG.debug(jsonStr);
        Assert.assertEquals("{\"a\":\"1\"}", jsonStr);
        objectMapper.setFilters(new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.filterOutAllExcept("b")));
        String jsonStr1 = objectMapper.writeValueAsString(testPOJO);
        LOG.debug(jsonStr1);
        Assert.assertEquals("{\"b\":\"2\"}", jsonStr1);
    }

    public static class TestPOJO{
        @JsonView(FilterView.OutputA.class)
        private String a;
        @JsonView(FilterView.OutputA.class)
        private String c;
        @JsonView(FilterView.OutputB.class)
        private String d;
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

}
