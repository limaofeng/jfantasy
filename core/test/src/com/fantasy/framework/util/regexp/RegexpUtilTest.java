package com.fantasy.framework.util.regexp;

import com.fantasy.security.bean.User;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Array;


public class RegexpUtilTest {

    @Test
    public void testGetPattern() throws Exception {

    }

    @Test
    public void testParseFirstGroup() throws Exception {

    }

    @Test
    public void testParseGroups() throws Exception {

    }

    @Test
    public void testParseGroups1() throws Exception {

    }

    @Test
    public void testIsMatch() throws Exception {

        String className = Array.newInstance(User.class,0).getClass().getName();

        Assert.assertTrue(RegexpUtil.isMatch(className, "^\\[L|;$"));

        Assert.assertTrue(RegexpUtil.isMatch(className, "^\\[L[a-zA-Z._]+;$"));

        Assert.assertTrue(RegexpUtil.isMatch("[Lcom.fantasy.security.bean.User$Test;", "^\\[L[a-zA-Z._$]+;$"));

    }

    @Test
    public void testSplit() throws Exception {

    }

    @Test
    public void testIsMatch1() throws Exception {

    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testParseFirst() throws Exception {

    }

    @Test
    public void testParseGroup() throws Exception {

    }

    @Test
    public void testParseGroup1() throws Exception {

    }

    @Test
    public void testReplaceFirst() throws Exception {

    }

    @Test
    public void testReplace() throws Exception {

    }

    @Test
    public void testReplace1() throws Exception {

    }

    @Test
    public void testReplace2() throws Exception {

    }

    @Test
    public void testReplaceFirst1() throws Exception {

    }

    @Test
    public void testReplaceFirst2() throws Exception {

    }

    @Test
    public void testWildMatch() throws Exception {

    }


    public static void main(String[] args) {
        // System.out.println(replace("15921884771", "(\\d{3})\\d{4}(\\d{4,})", "$1****$2"));
//		test("*.js", "/itsm/static/js/fantasy/String.js");
        // test("toto.java", "tutu.java");
        // test("12345", "1234");
        // test("1234", "12345");
        // test("*f", "");
        // test("***", "toto");
        // test("*.java", "toto.");
        // test("*.java", "toto.jav");
        // test("*.java", "toto.java");
        // test("abc*", "");
        // test("a*c", "abbbbbccccc");
        // test("abc*xyz", "abcxxxyz");
        // test("*xyz", "abcxxxyz");
        // test("abc**xyz", "abcxxxyz");
        // test("abc**x", "abcxxx");
        // test("*a*b*c**x", "aaabcxxx");
        // test("abc*x*yz", "abcxxxyz");
        // test("abc*x*yz*", "abcxxxyz");
        // test("a*b*c*x*yf*z*", "aabbccxxxeeyffz");
        // test("a*b*c*x*yf*zze", "aabbccxxxeeyffz");
        // test("a*b*c*x*yf*ze", "aabbccxxxeeyffz");
        // test("a*b*c*x*yf*ze", "aabbccxxxeeyfze");
        // test("*LogServerInterface*.java", "_LogServerInterfaceImpl.java");
        // test("abc*xyz", "abcxyxyz");
    }

//	private static void test(String pattern, String str) {
//		System.out.println(pattern + " " + str + " =>> " + wildMatch(pattern, str));
//	}


}