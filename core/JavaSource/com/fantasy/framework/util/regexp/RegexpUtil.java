package com.fantasy.framework.util.regexp;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-11-30 下午05:01:51
 * @version 1.0
 */
public class RegexpUtil {

	private static final Log logger = LogFactory.getLog(RegexpUtil.class);

	private static ConcurrentHashMap<String, Pattern> patternCache = new ConcurrentHashMap<String, Pattern>();

	public RegexpUtil() {
	}

	/**
	 * 
	 * @param patternString
	 *            验证规则
	 * @return Pattern
	 */
	public static Pattern getPattern(String patternString) {
		if (StringUtil.isBlank(patternString)) {
			throw new IgnoreException("pattern string is space");
		}
		if (!patternCache.containsKey(patternString)) {
			if (logger.isDebugEnabled()) {
				logger.debug("缓存正则表达式:" + patternString);
			}
			patternCache.putIfAbsent(patternString, Pattern.compile(patternString, Pattern.CASE_INSENSITIVE));
		}
		return patternCache.get(patternString);
	}

	public static Group parseFirstGroup(String input, String regEx) {
		Group[] groups = parseGroups(input, getPattern(regEx));
		if (groups.length == 0) {
			return null;
		}
		return groups[0];
	}

	public static Group[] parseGroups(String input, String regEx) {
		return parseGroups(input, getPattern(regEx));
	}

	public static Group[] parseGroups(String input, Pattern pattern) {
		List<Group> listGroup = new ArrayList<Group>();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String[] g = new String[matcher.groupCount() + 1];
			for (int i = 0; i < g.length; i++) {
				g[i] = matcher.group(i);
			}
			listGroup.add(new Group(g));
		}
		return listGroup.toArray(new Group[listGroup.size()]);
	}

	/**
	 * 是否匹配
	 * 
	 * @param input 验证的string
	 * @param regEx 正则表达式
	 * @return boolean
	 */
	public static boolean isMatch(String input, String regEx) {
		Pattern pattern = RegexpUtil.getPattern(regEx);
		return isMatch(input, pattern);
	}

	public static String[] split(String input, String regEx) {
		Pattern pattern = RegexpUtil.getPattern(regEx);
		return pattern.split(input);
	}

	/**
	 * 是否匹配
	 * 
	 * @param input 验证的string
	 * @param pattern 正则表达式
	 * @return boolean
	 */
	public static boolean isMatch(String input, Pattern pattern) {
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
	}

	/**
	 * 匹配正则表达式(可能有多个) 只要匹配上一个既返回true
	 * 
	 * @param input 验证的string
	 * @param regExs 正则表达式
	 * @return boolean
	 */
	public static boolean find(String input, String... regExs) {
		if (regExs.length <= 1) {
			return regExs.length != 0 && isMatch(input, regExs[0]);
		} else {
			for (String regEx : regExs) {
				if (isMatch(input, regEx)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 获取第一次匹配到的值
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @return string
	 */
	public static String parseFirst(String input, String regEx) {
		Group[] groups = parseGroups(input, regEx);
		if (groups.length > 0 && groups[0].groups.length > 0){
            return groups[0].$(0);
        }
		return "";
	}

	/**
	 * 获取第{0}次匹配到的值
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @param group group 下标
	 * @return string
	 */
	public static String parseGroup(String input, String regEx, int group) {
		Group[] groups = parseGroups(input, regEx);
		if (groups.length > 0 && groups[0].groups.length > group){
            return groups[0].$(group);
        }
		return null;
	}

	public static String parseGroup(String input, Pattern pattern, int group) {
		Group[] groups = parseGroups(input, pattern);
		if (groups.length > 0 && groups[0].groups.length > group){
            return groups[0].$(group);
        }
		return null;
	}

	/**
	 * 替换匹配到的第一个元素
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @param replacement 替换成的字符串
	 * @return string
	 */
	public static String replaceFirst(String input, String regEx, String replacement) {
		Pattern pattern = RegexpUtil.getPattern(regEx);
		return pattern.matcher(input).replaceFirst(replacement);
	}

	/**
	 * 替换匹配到的元素
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @param replacement  替换成的字符串
	 * @return string
	 */
	public static String replace(String input, String regEx, String replacement) {
		Pattern pattern = RegexpUtil.getPattern(regEx);
		return pattern.matcher(input).replaceAll(replacement);
	}

	/**
	 * 将String中的所有regex匹配的字串全部替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param regex
	 *            替换查找的正则表达式
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
	public static String replace(String string, String regex, ReplaceCallBack replacement) {
		return replace(string, getPattern(regex), replacement);
	}

	/**
	 * 将String中的所有pattern匹配的字串替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param pattern
	 *            替换查找的正则表达式对象
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
	public static String replace(String string, Pattern pattern, ReplaceCallBack replacement) {
		if (string == null) {
			return null;
		}
		Matcher m = pattern.matcher(string);
		if (m.find()) {
			StringBuffer sb = new StringBuffer();
			int index = 0;
			while (true) {
				String st = replacement.replace(m.group(0), index++, m);
				m.appendReplacement(sb, st);
				if (!m.find()) {
					break;
				}
			}
			m.appendTail(sb);
			return sb.toString();
		}
		return string;
	}

	/**
	 * 将String中的regex第一次匹配的字串替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param regex
	 *            替换查找的正则表达式
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
	public static String replaceFirst(String string, String regex, ReplaceCallBack replacement) {
		return replaceFirst(string, Pattern.compile(regex), replacement);
	}

	/**
	 * 将String中的pattern第一次匹配的字串替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param pattern
	 *            替换查找的正则表达式对象
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
	public static String replaceFirst(String string, Pattern pattern, ReplaceCallBack replacement) {
		if (string == null) {
			return null;
		}
		Matcher m = pattern.matcher(string);
		StringBuffer sb = new StringBuffer();
		if (m.find()) {
			m.appendReplacement(sb, replacement.replace(m.group(0), 0, m));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static interface ReplaceCallBack {

		String replace(String group, int i, Matcher m);

	}

	/**
	 * 抽象的字符串替换接口 主要是添加了$(group)方法来替代matcher.group(group)
	 */
	public static abstract class AbstractReplaceCallBack implements ReplaceCallBack {
		protected Matcher matcher;

		final public String replace(String text, int index, Matcher matcher) {
			this.matcher = matcher;
			try {
				return doReplace(text, index, matcher);
			} finally {
				this.matcher = null;
			}
		}

		/**
		 * 将text转化为特定的字串返回
		 * 
		 * @param text
		 *            指定的字符串
		 * @param index
		 *            替换的次序
		 * @param matcher
		 *            Matcher对象
		 * @return string
		 */
		public abstract String doReplace(String text, int index, Matcher matcher);

		/**
		 * 获得matcher中的组数据 等同于matcher.group(group)
		 * 
		 * @功能描述 <br/>
		 *       该函数只能在{@link #doReplace(String, int, Matcher)} 中调用
		 * @param group 组下标
		 * @return string
		 */
		protected String $(int group) {
			String data = matcher.group(group);
			return data == null ? "" : data;
		}
	}

	public static class Group {
		private String[] groups;

		protected Group(String[] groups) {
			this.groups = groups;
		}

		public String $(int group) {
			return this.groups[group];
		}

		@Override
		public String toString() {
			return "Group [groups=" + Arrays.toString(groups) + "]";
		}

	}

	public static boolean wildMatch(String pattern, String str) {
		pattern = toJavaPattern(pattern);
		return java.util.regex.Pattern.matches(pattern, str);
	}

	private static String toJavaPattern(String pattern) {
		StringBuilder result = new StringBuilder("^");
		char metachar[] = { '$', '^', '[', ']', '(', ')', '{', '|', /* '*', */'+', '?', '.', '/' };
		for (int i = 0; i < pattern.length(); i++) {
			char ch = pattern.charAt(i);
			boolean isMeta = false;
            for (char aMetachar : metachar) {
                if (ch == aMetachar) {
                    result.append("\\").append(String.valueOf(ch));
                    isMeta = true;
                    break;
                }
            }
			if (!isMeta) {
				if (ch == '*') {
					result.append(".*");
				} else {
					result.append(ch);
				}

			}
		}
		result.append("$");
		return result.toString();
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
