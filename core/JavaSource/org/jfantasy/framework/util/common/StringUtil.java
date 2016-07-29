package org.jfantasy.framework.util.common;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public abstract class StringUtil {
    private StringUtil() {

    }

    private static final Log LOG = LogFactory.getLog(StringUtil.class);

    /**
     * 字符串超出指定长度时截取到指定长度，并在末尾追加指定的字符串。
     *
     * @param value 源字符串
     * @param len   要求的长度
     * @param word  超出长度，追加末尾的字符串
     * @return {string}
     */
    public static String ellipsis(String value, int len, String word) {
        if (length(value) <= len) {
            return value;
        }
        len -= length(word);
        if (value.length() > len) {
            value = value.substring(0, len);
        }
        while (length(value) > len) {
            value = value.substring(0, value.length() - 1);
        }
        return value + word;
    }

    /**
     * 判断字符串长度,非单字节字符串算两个长度
     *
     * @param str 计算长度的字符串
     * @return {int}
     */
    public static int length(String str) {
        return str.replaceAll("[^\\x00-\\xff]", "rr").length();
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param input 要判断的字符串
     * @return {boolean}
     */
    public static boolean isChinese(String input) {
        return RegexpUtil.isMatch(input, "[^\\x00-\\xff]");
    }

    /**
     * 剔除字符串中的空白字符
     *
     * @param s 字符串
     * @return {string}
     */
    public static String trim(String s) {
        if (s == null) {
            return "";
        }
        return s.trim();
    }

    /**
     * 以{delim}格式截取字符串返回list,不返回为空的字符串
     *
     * @param s     源字符串
     * @param delim 分割字符
     * @return {List<String>}
     */
    public static List<String> split(String s, String delim) {
        List<String> list = new ArrayList<String>();
        s = trim(s);
        String[] rs = s.split(delim);
        for (String str : rs) {
            if (str.trim().length() > 0) {
                list.add(str);
            }
        }

        return list;
    }

    /**
     * 判断字符串是否为空或者空字符串
     *
     * @param s 要判断的字符串
     * @return {boolean}
     */
    public static boolean isNull(Object s) {
        return isBlank(nullValue(s));
    }

    /**
     * 判断字符串是否为非空字符串 {@link #isNull(Object)} 方法的取反
     *
     * @param s 要判断的字符串
     * @return {boolean}
     */
    public static boolean isNotNull(Object s) {
        return !isNull(s);
    }

    /**
     * 判断字符串是否为空，如果为空返回空字符，如果不为空，trim该字符串后返回
     *
     * @param s 要判断的字符串
     * @return {String}
     */
    public static String nullValue(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * 判断对象是否为空，如果为空返回空字符，如果不为空，返回该字符串的toString字符串
     *
     * @param s 要判断的对象
     * @return {String}
     */
    public static String nullValue(Object s) {
        return s == null ? "" : s.toString();
    }

    public static String nullValue(long s) {
        return s < 0L ? "" : String.valueOf(s);
    }

    public static String nullValue(int s) {
        return s < 0 ? "" : "" + s;
    }

    /**
     * 判断字符串是否为空，如果为空返回 {defaultValue}，如果不为空，直接返回该字符串
     *
     * @param s            要转换的对象
     * @param defaultValue 默认字符串
     * @return {String}
     */
    public static String defaultValue(Object s, String defaultValue) {
        return s == null ? defaultValue : s.toString();
    }

    /**
     * 判断字符串是否为空，如果为空返回 {defaultValue}，如果不为空，直接返回该字符串
     * {@link #defaultValue(Object, String)} 的重载
     *
     * @param s            要转换的字符串
     * @param defaultValue 默认字符串
     * @return {String}
     */
    public static String defaultValue(String s, String defaultValue) {
        return isBlank(s) ? defaultValue : s;
    }

    /**
     * 将Long转为String格式
     * 如果{s}为NULL 或者 小于 0 返回空字符。否则返回{s}的toString字符串
     *
     * @param s Long
     * @return {String}
     */
    public static String longValue(Long s) {
        return (s == null) || (s.intValue() <= 0) ? "" : s.toString();
    }

    /**
     * 将Long转为String格式
     * 如果{s}为NULL 或者 小于 0 返回"0"。否则返回{s}的toString字符串
     *
     * @param s Long
     * @return {String}
     */
    public static String longValueZero(Long s) {
        return (s == null) || (s.intValue() <= 0) ? "0" : s.toString();
    }

    public static String noNull(String s) {
        return s == null ? "" : s;
    }

    public static boolean isBlank(Object s) {
        return (s == null) || (nullValue(s).trim().length() == 0);
    }

    public static boolean isNotBlank(Object s) {
        return !isBlank(s);
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    /**
     * 首字母大写
     *
     * @param s 字符串
     * @return {String}
     */
    public static String upperCaseFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 字符串
     * @return {String}
     */
    public static String lowerCaseFirst(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * 将字符串{ori}补齐到{length}长度
     * {length} 为正数右边补{fillChar}，为负数左边补{fillChar}<br/>
     * {fillChar}有多个的话取随机
     *
     * @param ori      源字符串
     * @param length   要补齐的长度
     * @param fillChar 追加的字符串
     * @return {String}
     */
    public static String append(String ori, int length, String... fillChar) {
        int absLength = Math.abs(length);
        if (ori == null) {
            return "";
        }
        StringBuilder result = new StringBuilder(ori);
        if (result.length() < absLength) {
            for (int i = result.length(); i < absLength; i++) {
                if (length < 0) {
                    result.insert(0, fillChar[NumberUtil.randomInt(fillChar.length)]);
                } else {
                    result.append(fillChar[NumberUtil.randomInt(fillChar.length)]);
                }
                if (result.length() == absLength) {
                    break;
                }
            }
        }
        return result.toString();
    }

    /**
     * 左边补零以满足长度要求
     * <p/>
     * resultLength 最终长度
     */
    public static String addZeroLeft(String ori, int resultLength) {
        return append(ori, resultLength < 0 ? resultLength : 0 - resultLength, "0");
    }

    public static String addLeft(String ori, int resultLength, String... fillChar) {
        return append(ori, resultLength < 0 ? resultLength : 0 - resultLength, fillChar);
    }

    /**
     * 判断字符串是否为数值类型<br/>
     * <p/>
     * 判断规则: 是否为[0-9]之间的字符组成，不能包含其他字符，否则返回false
     *
     * @param curPage 判断字符串是否为数字
     * @return {boolean}
     */
    public static boolean isNumber(String curPage) {
        return RegexpUtil.isMatch(curPage, "^[0-9]+$");
    }

    /**
     * 编码字符串 {s} 格式{enc}
     * 调用{@link #'URLEncoder.encode(String,String)'}
     *
     * @param s   要编码的字符串
     * @param enc 格式
     * @return {String}
     */
    public static String encodeURI(String s, String enc) {
        try {
            return isBlank(s) ? s : URLEncoder.encode(s, enc).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return s;
        }
    }

    public static boolean contains(String s, CharSequence... ses) {
        for (CharSequence sequence : ses) {
            if (s.contains(sequence)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解码字符串 {s} 格式{enc}
     * 调用{@link #'URLEncoder.decode(String,String)'}
     *
     * @param s   要解码的字符串
     * @param enc 格式
     * @return {String}
     */
    public static String decodeURI(String s, String enc) {
        try {
            if (s.contains(" ") && s.contains("+")) {
                LOG.error("同时存在 ' ' 与 '+' decodeURI 后 可能会出现问题,所以原串返回");
                return s;
            }
            if (contains(s, /*"+",*/ " ", "/", "?",/*"%",*/"#", "&", "=")) {
                LOG.warn("存在 特殊字符 decodeURI 后 字符串可能已经 decodeURI 过.");
                return s;
            }
            return isBlank(s) ? s : URLDecoder.decode(s, enc);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return s;
        }
    }

    /**
     * 转义特殊字符
     *
     * @param condition 字符串
     * @return {String}
     */
    public static String escapeSpecialSign(String condition) {
        String bb = org.apache.commons.lang.StringUtils.replace(condition, "/", "//");
        bb = org.apache.commons.lang.StringUtils.replace(bb, "%", "/%");
        bb = org.apache.commons.lang.StringUtils.replace(bb, "_", "/_");
        return bb;
    }


    private static char[] lowercases = {'\000', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', ''};

    public static String asciiToLowerCase(String s) {
        char[] c = null;
        int i = s.length();

        while (i-- > 0) {
            char c1 = s.charAt(i);
            if (c1 <= '') {
                char c2 = lowercases[c1];
                if (c1 != c2) {
                    c = s.toCharArray();
                    c[i] = c2;
                    break;
                }
            }
        }

        while (i-- > 0) {
            if (c != null && c.length > i && c[i] <= '') {
                c[i] = lowercases[c[i]];
            }
        }
        return c == null ? s : new String(c);
    }

    public static boolean startsWithIgnoreCase(String s, String w) {
        if (w == null) {
            return true;
        }
        if ((s == null) || (s.length() < w.length())) {
            return false;
        }
        for (int i = 0; i < w.length(); i++) {
            char c1 = s.charAt(i);
            char c2 = w.charAt(i);
            if (c1 == c2) {
                continue;
            }
            if (c1 <= '') {
                c1 = lowercases[c1];
            }
            if (c2 <= '') {
                c2 = lowercases[c2];
            }
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    public static boolean endsWithIgnoreCase(String s, String w) {
        if (w == null) {
            return true;
        }
        if (s == null) {
            return false;
        }
        int sl = s.length();
        int wl = w.length();

        if (sl < wl) {
            return false;
        }
        for (int i = wl; i-- > 0; ) {
            sl--;
            char c1 = s.charAt(sl);
            char c2 = w.charAt(i);
            if (c1 != c2) {
                if (c1 <= '') {
                    c1 = lowercases[c1];
                }
                if (c2 <= '') {
                    c2 = lowercases[c2];
                }
                if (c1 != c2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int indexFrom(String s, String chars) {
        for (int i = 0; i < s.length(); i++) {
            if (chars.indexOf(s.charAt(i)) >= 0) {
                return i;
            }
        }
        return -1;
    }

    public static String replace(String s, String sub, String with) {
        int c = 0;
        int i = s.indexOf(sub, c);
        if (i == -1) {
            return s;
        }
        StringBuilder buf = new StringBuilder(s.length() + with.length());
        do {
            buf.append(s.substring(c, i));
            buf.append(with);
            c = i + sub.length();
        } while ((i = s.indexOf(sub, c)) != -1);

        if (c < s.length()) {
            buf.append(s.substring(c, s.length()));
        }
        return buf.toString();
    }

    public synchronized static void append(StringBuilder buf, String s, int offset, int length) {
        int end = offset + length;
        for (int i = offset; i < end; i++) {
            if (i >= s.length()) {
                break;
            }
            buf.append(s.charAt(i));
        }
    }

    public static void append(StringBuilder buf, byte b, int base) {
        int bi = 0xFF & b;
        int c = 48 + bi / base % base;
        if (c > 57) {
            c = 97 + (c - 48 - 10);
        }
        buf.append((char) c);
        c = 48 + bi % base;
        if (c > 57) {
            c = 97 + (c - 48 - 10);
        }
        buf.append((char) c);
    }

    public static void append2digits(StringBuffer buf, int i) {
        if (i < 100) {
            buf.append((char) (i / 10 + 48));
            buf.append((char) (i % 10 + 48));
        }
    }

    public static void append2digits(StringBuilder buf, int i) {
        if (i < 100) {
            buf.append((char) (i / 10 + 48));
            buf.append((char) (i % 10 + 48));
        }
    }

    public static String join(String[] array) {
        return join(array, "");
    }

    public static String join(String[] array, String str) {
        StringBuilder buf = new StringBuilder();
        for (String s : array) {
            buf.append(s).append(str);
        }
        return buf.toString().replaceAll(str + "$", "");
    }

    public static String[] add(String[] array, String... strs) {
        Set<String> list = new HashSet<String>(Arrays.asList(array));
        Collections.addAll(list, strs);
        return list.toArray(new String[list.size()]);
    }

    public static String nonNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    public static String toUTF8String(byte[] b, int offset, int length) {
        try {
            return new String(b, offset, length, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public static String toString(byte[] b, String charset) {
        try {
            return new String(b, 0, b.length, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toString(byte[] b, int offset, int length, String charset) {
        try {
            return new String(b, offset, length, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String printable(String name) {
        if (name == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isISOControl(c)) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static byte[] getBytes(String s) {
        try {
            return s.getBytes("ISO-8859-1");
        } catch (Exception e) {
            LOG.warn(e);
        }
        return s.getBytes();
    }

    public static byte[] getBytes(String s, String charset) {
        try {
            return s.getBytes(charset);
        } catch (Exception e) {
            LOG.warn(e);
        }
        return s.getBytes();
    }

    public static String sidBytesToString(byte[] sidBytes) {
        StringBuilder sidString = new StringBuilder();

        sidString.append("S-");

        sidString.append(Byte.toString(sidBytes[0])).append('-');

        StringBuilder tmpBuilder = new StringBuilder();

        for (int i = 2; i <= 7; i++) {
            tmpBuilder.append(Integer.toHexString(sidBytes[i] & 0xFF));
        }

        sidString.append(Long.parseLong(tmpBuilder.toString(), 16));

        int subAuthorityCount = sidBytes[1];

        for (int i = 0; i < subAuthorityCount; i++) {
            int offset = i * 4;
            tmpBuilder.setLength(0);

            tmpBuilder.append(String.format("%02X%02X%02X%02X", new Object[]{sidBytes[11 + offset] & 0xFF, sidBytes[10 + offset] & 0xFF, sidBytes[9 + offset] & 0xFF, sidBytes[8 + offset] & 0xFF}));

            sidString.append('-').append(Long.parseLong(tmpBuilder.toString(), 16));
        }

        return sidString.toString();
    }

    public static byte[] sidStringToBytes(String sidString) {
        String[] sidTokens = sidString.split("-");

        int subAuthorityCount = sidTokens.length - 3;

        int byteCount = 0;
        byte[] sidBytes = new byte[8 + 4 * subAuthorityCount];

        sidBytes[byteCount++] = (byte) Integer.parseInt(sidTokens[1]);

        sidBytes[byteCount++] = (byte) subAuthorityCount;

        String hexStr = Long.toHexString(Long.parseLong(sidTokens[2]));

        while (hexStr.length() < 12) {
            hexStr = "0" + hexStr;
        }

        for (int i = 0; i < hexStr.length(); i += 2) {
            sidBytes[byteCount++] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
        }

        for (int i = 3; i < sidTokens.length; i++) {
            hexStr = Long.toHexString(Long.parseLong(sidTokens[i]));

            while (hexStr.length() < 8) {
                hexStr = "0" + hexStr;
            }

            for (int j = hexStr.length(); j > 0; j -= 2) {
                sidBytes[byteCount++] = (byte) Integer.parseInt(hexStr.substring(j - 2, j), 16);
            }
        }

        return sidBytes;
    }

    public static boolean isEmpty(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    public static String escapeHtml(String result) {
        return StringEscapeUtils.escapeHtml4(result);
    }

    public static String escapeJavaScript(String result) {
        return StringEscapeUtils.escapeEcmaScript(result);
    }

    public static String escapeXml(String result) {
        return StringEscapeUtils.escapeXml(result);
    }

    public static String escapeCsv(String result) {
        return StringEscapeUtils.escapeCsv(result);
    }

    public static String[] tokenizeToStringArray(String[] tokenizes) {
        List<String> strs = new ArrayList<>(tokenizes.length);
        for (String tokenize : tokenizes) {
            strs.addAll(Arrays.asList(tokenizeToStringArray(tokenize)));
        }
        return strs.toArray(new String[strs.size()]);
    }

    public static String[] tokenizeToStringArray(String tokenize) {
        return tokenizeToStringArray(tokenize, ",; \t\n");
    }

    private final static String[] emptyArray = new String[0];

    public static String[] tokenizeToStringArray(String tokenize, String delimiters) {
        return ObjectUtil.defaultValue(StringUtils.tokenizeToStringArray(tokenize, delimiters), emptyArray);
    }

    public static Date getBirthday(String idCard) {
        return DateUtil.parse(idCard.substring(6, 6 + 8), "yyyyMMdd");
    }

    public static final char[] charMap;

    static {
        charMap = new char[64];
        for (int i = 0; i < 10; i++) {
            charMap[i] = (char) ('0' + i);
        }
        for (int i = 10; i < 36; i++) {
            charMap[i] = (char) ('a' + i - 10);
        }
        for (int i = 36; i < 62; i++) {
            charMap[i] = (char) ('A' + i - 36);
        }
        charMap[62] = '_';
        charMap[63] = '-';
    }

    public static String hexTo64(String hex) {
        StringBuilder r = new StringBuilder();
        int index;
        int[] buff = new int[3];
        int l = hex.length();
        for (int i = 0; i < l; i++) {
            index = i % 3;
            buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
            if (index == 2) {
                r.append(charMap[buff[0] << 2 | buff[1] >>> 2]);
                r.append(charMap[(buff[1] & 3) << 4 | buff[2]]);
            }
        }
        return r.toString();
    }

    public static String[] shortUrl(String s, String key) {
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"
        };
        // 对传入网址进行 MD5 加密
        String hex = DigestUtils.md5DigestAsHex((key + s).getBytes());
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用 long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

    /**
     * @param s
     * @return
     */
    public static String[] shortUrl(String s) {
        return shortUrl(s, "org.jfantasy");
    }

}
