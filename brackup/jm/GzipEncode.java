package com.fantasy.framework.util.jm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GzipEncode {
	private static final Log logger = LogFactory.getLog(GzipEncode.class);

	public String encode(String sb) {
		return sb;
	}

	public static void gzip(InputStream in, OutputStream out) {
		try {
			GZIPOutputStream gzout = new GZIPOutputStream(out);
			byte[] buf = new byte[1024];
			int num;
			while ((num = in.read(buf)) != -1) {
				gzout.write(buf, 0, num);
			}
			gzout.close();
			out.close();
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static byte[] gzip(String buffer) {
		try {
			ByteArrayOutputStream o = new ByteArrayOutputStream();

			GZIPOutputStream gzout = new GZIPOutputStream(o);
			gzout.write(buffer.getBytes());
			gzout.finish();
			gzout.close();

			byte[] data_ = o.toByteArray();
			o.close();
			return data_;
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}

	public static byte[] jUnZip(byte[] buffer) {
		try {
			byte[] buf = new byte[8192];

			ByteArrayInputStream i = new ByteArrayInputStream(buffer);

			GZIPInputStream gzin = new GZIPInputStream(i);
			int size = gzin.read(buf);
			i.close();
			gzin.close();
			byte[] b = new byte[size];
			System.arraycopy(buf, 0, b, 0, size);

			return b;
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}

	public static String KL(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 0x74);
		}
		String s = new String(a);
		return s;
	}

	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 0x74);
		}
		String k = new String(a);
		return k;
	}

	public static void main(String[] args) throws Exception {
		String ins = "测试GZIP编码";

		byte[] b = gzip(ins);

		DESPlus desPlus = new DESPlus();

		DESPlus desPlus2 = new DESPlus("wangchongan");
		String e2 = desPlus2.encrypt("13588888888");
		System.out.println(e2);
		String d2 = desPlus2.decrypt(e2);
		System.out.println(d2);

		System.out.println(JM(KL(new String(b))));

		System.out.println(new String(jUnZip(b)));
	}

	public static String StringReader(OutputStream out) {
		System.out.println(out.toString());

		return null;
	}
}
