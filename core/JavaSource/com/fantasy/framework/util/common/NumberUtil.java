package com.fantasy.framework.util.common;

import com.fantasy.framework.util.regexp.RegexpUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * 数字处理集合类 主要功能：四舍五入，随机数，数字类型转换等方法
 * 
 * 
 */
public class NumberUtil {

	static String[] numberSimplifiedChinese = new String[] { "", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
	static String[] unitSimplifiedChinese = new String[] { "", "十", "百", "千", "万", "亿", "", "", "" };
	static String[] numberTraditionalChinese = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	static String[] unitTraditionalChinese = new String[] { "元", "拾", "佰", "仟", "万", "亿", "角", "分", "整" };
	private final static byte[] hex = "0123456789ABCDEF".getBytes();
	public static final int INTEGER_MAX = 99999;
	private static Random random = new Random();

	public static int randomInt() {
		return random.nextInt();
	}

	public static int randomInt(int i) {
		return random.nextInt(i);
	}

	public static float randomFloat() {
		return random.nextFloat();
	}

	public static long randomLong() {
		return random.nextLong();
	}

	public static Integer toInteger(String num) {
		if (!isNumber(num)) {
			return null;
		}
		return Integer.valueOf(num);
	}

	/**
	 * 判断num是否为数字
	 * 
	 * @param num 字符串
	 * @return {boolean}
	 */
	public static boolean isNumber(String num) {
		return RegexpUtil.find(num, "^\\d+$");
	}

	/**
	 * 四舍五入方法
	 * 
	 * @param n
	 *            值
	 * @param w
	 *            位数
	 * @return {double}
	 */
	public static double round(double n, int w) {
		BigDecimal d = new BigDecimal(n);
		d = d.setScale(w, BigDecimal.ROUND_HALF_UP);
		return d.doubleValue();
	}

	/**
	 * int 转 string
	 * 
	 * @param num
	 *            数值
	 * @return {String}
	 */
	public static String nullValue(Integer num) {
		if (num == null) {
			return "";
		}
		return num.toString();
	}

	/**
	 * long 转 string
	 * 
	 * @param num
	 *            数值
	 * @return {String}
	 */
	public static String nullValue(Long num) {
		if (num == null) {
			return "";
		}
		return num.toString();
	}

	public static String percent(double p1, double p2) {
		String str;
		double p3 = p1 / p2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		str = nf.format(p3);
		return str;
	}

	/**
	 * 整数到字节数组转换
	 * 
	 * @param n
	 *            整数
	 * @return {byte[]}
	 */
	public static byte[] int2bytes(int n) {
		byte[] ab = new byte[4];
		ab[0] = (byte) (0xff & n);
		ab[1] = (byte) ((0xff00 & n) >> 8);
		ab[2] = (byte) ((0xff0000 & n) >> 16);
		ab[3] = (byte) ((0xff000000 & n) >> 24);
		return ab;
	}

	/**
	 * 字节数组到整数的转换
	 * 
	 * @param b
	 *            字节数组
	 * @return {int}
	 */
	public static int bytes2int(byte b[]) {
		int s;

		s = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);
		return s;
	}

	/**
	 * 字节转换到字符
	 * 
	 * @param b
	 *            字节
	 * @return {char}
	 */
	public static char byte2char(byte b) {
		return (char) b;
	}

	/**
	 * 解析字符
	 * 
	 * @param c
	 *            字符
	 * @return {int}
	 */
	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	/**
	 * 从字节数组到十六进制字符串转换
	 * 
	 * @param b
	 *            字节数组
	 * @return {String}
	 */
	@Deprecated
	public static String bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 从十六进制字符串到字节数组转换
	 * 
	 * @param hexstr
	 *            字符串
	 * @return {byte[]}
	 */
	@Deprecated
	public static byte[] hexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	/**
	 * 验证整数n是否在整数数组中
	 * 
	 * @param n
	 *            整数
	 * @param ins
	 *            整数数组
	 * @return {boolean}
	 */
	public static boolean in(int n, int[] ins) {
		for (int i : ins) {
			if (i == n) {
				return true;
			}
		}
		return false;
	}

	public static boolean in(long n, long[] ins) {
        for (long i : ins) {
            if (i == n) {
                return true;
            }
        }
		return false;
	}

	public static String format(double num) {
		NumberFormat nf = new DecimalFormat("0.00");
		return nf.format(num);
	}

    public static String toChinese(int number) {
        return toChinese(number+"");
    }

	public static String toChinese(String number) {
		return toChinese(number, 0, numberSimplifiedChinese, unitSimplifiedChinese);
	}

	public static String toRMB(String number) {
		return toChinese(number, 0, numberTraditionalChinese, unitTraditionalChinese);
	}

	private static String toChinese(String number, int unit, String[] numberChinese, String[] unitChinese) {
		String chinese = number.split("\\.")[0];
		number = number.split("\\.").length > 1 ? number.split("\\.")[1] : null;
		int index = unit == 0 ? 0 : unit % 4 == 0 ? unit % 8 == 0 ? 5 : 4 : unit % 4;
		chinese = chinese.length() > 1 ? toChinese(chinese.substring(0, chinese.length() - 1), unit + 1, numberChinese, unitChinese) + (numberChinese[Integer.valueOf(chinese.substring(chinese.length() - 1))] + unitChinese[index]) : (numberChinese[Integer.valueOf(chinese)] + unitChinese[index]);

		if (number != null) {
			chinese = chinese.replaceAll(numberChinese[0] + "{1,}[" + unitChinese[1] + "|" + unitChinese[2] + "|" + unitChinese[3] + "]", numberChinese[0]);// 零拾|零佰|零仟改写为零
			chinese = chinese.replaceAll("^" + numberChinese[1] + unitChinese[1] + numberChinese[0], unitChinese[1]);// 壹拾零改写为拾
			chinese = chinese.replaceAll(unitChinese[5] + numberChinese[0] + "{1,}" + unitChinese[4], unitChinese[5] + numberChinese[0]);// 亿零{0,}万改写为亿零
			chinese = chinese.replaceAll(numberChinese[0] + "{1,}" + unitChinese[4], unitChinese[4] + numberChinese[0]);// 零万改写为万零
			chinese = chinese.replaceAll(numberChinese[0] + "{1,}" + unitChinese[5], unitChinese[5] + numberChinese[0]);// 零亿改写为亿零
			chinese = chinese.replaceAll(numberChinese[0] + unitChinese[4] + numberChinese[0], numberChinese[0]);// 零万零改写为零
			chinese = chinese.replaceAll(numberChinese[0] + "{1,}", numberChinese[0]);// 多个零改写为零
			chinese = chinese.replaceAll(numberChinese[0] + unitChinese[0], unitChinese[0]);// 零元改写为元
			chinese = chinese.replaceAll("^" + unitChinese[0], "");// 以元打头的去掉
			chinese += "00".equals(number) ? unitChinese[8] : ((numberChinese[Integer.valueOf(number.substring(0, 1))] + (number.substring(0, 1).equals("0") ? "" : unitChinese[6])) + (number.substring(1).equals("0") ? "" : (numberChinese[Integer.valueOf(number.substring(1))] + unitChinese[7])));
		}
		return chinese.replaceAll("^" + numberChinese[0], "");// 以零打头的去掉
	}

	public static void main(String[] args) {

		System.out.println(percent(23d,123d));

		System.out.println(toChinese("11"));
		System.out.println(toRMB("11"));

		System.out.println(toRMB("11010001000.11"));
	}
}