package com.fantasy.framework.util.jm;

import java.io.IOException;
import java.io.PrintStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Test {
	public static void main(String[] args) throws IOException {
		String src = "BASE64编码测试";

		BASE64Encoder en = new BASE64Encoder();

		String encodeStr = en.encode(src.getBytes("gbk"));

		System.out.println(encodeStr);

		BASE64Decoder dec = new BASE64Decoder();

		byte[] data = dec.decodeBuffer(encodeStr);

		System.out.println(new String(data, "gbk"));
	}
}