package com.fantasy.framework.util.common.encode;

public class Encoding {

	// 支持的字符格式
	public static int GB2312 = 0;

	public static int GBK = 1;

	public static int BIG5 = 2;

	public static int UTF8 = 3;

	public static int UNICODE = 4;

	public static int EUC_KR = 5;

	public static int SJIS = 6;

	public static int EUC_JP = 7;

	public static int ASCII = 8;

	public static int UNKNOWN = 9;

	public static int TOTALT = 10;

	public final static int SIMP = 0;

	public final static int TRAD = 1;

	// 解析名称用
	public static String[] javaname;

	// 编码用
	public static String[] nicename;

	// 应用于html中的字符集
	public static String[] htmlname;

	public Encoding() {
		javaname = new String[TOTALT];
		nicename = new String[TOTALT];
		htmlname = new String[TOTALT];
		javaname[GB2312] = "GB2312";
		javaname[GBK] = "GBK";
		javaname[BIG5] = "BIG5";
		javaname[UTF8] = "UTF8";
		javaname[UNICODE] = "Unicode";
		javaname[EUC_KR] = "EUC_KR";
		javaname[SJIS] = "SJIS";
		javaname[EUC_JP] = "EUC_JP";
		javaname[ASCII] = "ASCII";
		javaname[UNKNOWN] = "ISO8859_1";

		// 分配编码名称
		htmlname[GB2312] = "GB2312";
		htmlname[GBK] = "GBK";
		htmlname[BIG5] = "BIG5";
		htmlname[UTF8] = "UTF-8";
		htmlname[UNICODE] = "UTF-16";
		htmlname[EUC_KR] = "EUC-KR";
		htmlname[SJIS] = "Shift_JIS";
		htmlname[EUC_JP] = "EUC-JP";
		htmlname[ASCII] = "ASCII";
		htmlname[UNKNOWN] = "ISO8859-1";

		// 分配可读名称
		nicename[GB2312] = "GB-2312";
		nicename[GBK] = "GBK";
		nicename[BIG5] = "Big5";
		nicename[UTF8] = "UTF-8";
		nicename[UNICODE] = "Unicode";
		nicename[EUC_KR] = "EUC-KR";
		nicename[SJIS] = "Shift-JIS";
		nicename[EUC_JP] = "EUC-JP";
		nicename[ASCII] = "ASCII";
		nicename[UNKNOWN] = "UNKNOWN";

	}

	public String toEncoding(final int type) {
		return (javaname[type] + "," + nicename[type] + "," + htmlname[type]).intern();
	}

}