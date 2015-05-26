package com.fantasy.framework;

import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.member.bean.Member;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

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
    public void serialize(){
        Member member= new Member();
        member.setUsername("limaofeng");
        member.setPassword("123456");
        member.setNickName("张三");

        //测试普通json转换
        Assert.assertEquals(JSON.unicode().serialize(member),"{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"\\u5F20\\u4E09\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试text json 转换
        Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试set json 转换
        Assert.assertEquals(JSON.set("text").serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":null,\"details\":null}");
        //测试 date
        member.setLastLoginTime(DateUtil.parse("2014-10-20 10:29:34"));
        Assert.assertEquals(JSON.serialize(member), "{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":\"2014-10-20 10:29:34\",\"details\":null}");

//        Article article = new Article();
//
//        LOG.debug(JSON.serialize(article));
//
//        Class newArticle = AsmUtil.makeClass(Article.class.getName()+"$new", Article.class.getName(),new Property("name", String.class));
//
//        LOG.debug(JSON.serialize(ClassUtil.newInstance(newArticle)));
    }

    @Test
    public void deserialize(){
        Member member = JSON.deserialize("{\"creator\":null,\"createTime\":null,\"modifier\":null,\"modifyTime\":null,\"id\":null,\"username\":\"limaofeng\",\"password\":\"123456\",\"nickName\":\"张三\",\"enabled\":false,\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"lockTime\":null,\"lastLoginTime\":\"2014-10-20 10:29:34\",\"details\":null}",Member.class);
        //中文
        Assert.assertEquals(member.getNickName(),"张三");
        //date
        Assert.assertEquals(member.getLastLoginTime(),DateUtil.parse("2014-10-20 10:29:34"));
    }

}
