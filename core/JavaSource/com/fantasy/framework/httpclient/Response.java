package com.fantasy.framework.httpclient;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * HttpClient 响应对象
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-11-30 下午04:46:02
 * @version 1.0
 */
public class Response {

	private static final Log logger = LogFactory.getLog(Response.class);

	private String url;
	private int statusCode;
	private Cookie[] cookies;
	private Header[] responseHeaders;
	private Header[] requestHeaders;
	private InputStream in;
	private ByteArrayOutputStream out;

	public Response(String url, int statusCode) {
		this.url = url;
		this.statusCode = statusCode;
	}

	/**
	 * 状态码
	 * 
	 * @功能描述
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}

	public String getUrl() {
		return url;
	}

	public Cookie[] getCookies() {
		return cookies;
	}

	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}

	public Header[] getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Header[] responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public Header[] getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Header[] requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	/**
	 * 按纯文本格式获取响应信息
	 * 
	 * @功能描述
	 * @return
	 * @throws IOException
	 */
	public String getText() throws IOException {
		return getText(getContentEncoding());
	}

	/**
	 * 按纯文本格式获取响应信息
	 * 
	 * @功能描述
	 * @param pretty
	 *            是否显示换行
	 * @return
	 * @throws IOException
	 */
	public String getText(boolean pretty) throws IOException {
		return getText(getContentEncoding(), pretty);
	}

	/**
	 * 按纯文本格式获取响应信息
	 * 
	 * @功能描述
	 * @param charset
	 *            按指定的编码格式解析
	 * @return
	 * @throws IOException
	 */
	public String getText(String charset) throws IOException {
		return this.getText(charset, false);
	}

	/**
	 * 按纯文本格式获取响应信息
	 * 
	 * @功能描述
	 * @param charset
	 *            按指定的编码格式解析
	 * @param pretty
	 *            是否显示换行
	 * @return
	 * @throws IOException
	 */
	public String getText(String charset, boolean pretty) throws IOException {
		InputStream in = new ByteArrayInputStream(cache().toByteArray());
		BufferedReader reader = null;
		StringBuilder html = new StringBuilder();
		try {
			if (StringUtil.isNull(charset)) {
				reader = new BufferedReader(new InputStreamReader(in));
			} else {
				reader = new BufferedReader(new InputStreamReader(in, charset));
			}
			String line;
			while ((line = reader.readLine()) != null) {
				html.append(line).append(pretty ? System.getProperty("line.separator") : "");
			}
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}
		return html.toString();
	}

	/**
	 * 按json格式反序列化响应信息
	 * 
	 * @功能描述
	 * @param <T>
	 * @param clazz
	 *            要转化的类型
	 * @param charset
	 *            编码格式
	 * @return
	 * @throws IOException
	 */
	public <T> T deserialize(Class<T> clazz, String charset) throws IOException {
		return (T) JSON.deserialize(getText(charset), clazz);
	}

	/**
	 * 缓存响应流
	 * 
	 * @功能描述
	 * @return
	 * @throws IOException
	 */
	private ByteArrayOutputStream cache() throws IOException {
		if (this.out == null) {
			logger.debug("将文件写入缓冲区");
			this.out = new ByteArrayOutputStream();
			try {
				byte[] buf = new byte[1024];
				int num;
				while ((num = in.read(buf, 0, buf.length)) != -1) {
					out.write(buf, 0, num);
				}
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
				in = null;
			}
		}
		return this.out;
	}

	/**
	 * 获取请求头信息
	 * 
	 * @功能描述
	 * @param headerName
	 * @return
	 */
	public Header getRequestHeader(String headerName) {
		for (int i = 0; i < requestHeaders.length; i++) {
			if (requestHeaders[i].getName().equalsIgnoreCase(headerName))
				return requestHeaders[i];
		}
		return null;
	}

	/**
	 * 获取响应头信息
	 * 
	 * @功能描述
	 * @param headerName
	 * @return
	 */
	public Header getResponseHeader(String headerName) {
		for (int i = 0; i < responseHeaders.length; i++) {
			if (responseHeaders[i].getName().equalsIgnoreCase(headerName))
				return responseHeaders[i];
		}
		return null;
	}

	/**
	 * 设置响应输入流
	 * 
	 * @功能描述
	 * @param responseInputStream
	 */
	protected void setInputStream(InputStream responseInputStream) {
		this.in = responseInputStream;
	}

	/**
	 * 将相应写入到流中 从缓存区读取，不存在缓存区则建立缓存流
	 * 
	 * @param out
	 *            要写入的流对象
	 * @param cache
	 *            是否缓存
	 * @throws IOException
	 */
	public void write(OutputStream out, boolean cache) throws IOException {
		if (cache) {
			cache();
		}
		this.write(out);
	}

	/**
	 * 将相应写入到流中 如果缓存区存在，读取缓存区
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void write(OutputStream out) throws IOException {
		InputStream in = getInputStream();
		try {
			byte[] buf = new byte[1024];
			int num;
			while ((num = in.read(buf, 0, buf.length)) != -1) {
				out.write(buf, 0, num);
			}
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 将响应信息写入到本地文件
	 * 
	 * @功能描述
	 * @param absolutePath
	 * @throws IOException
	 */
	public void writeFile(String absolutePath) throws IOException {
		writeFile(new File(absolutePath));
	}

	/**
	 * 将响应信息写入到本地文件
	 * 
	 * @功能描述
	 * @param file
	 * @throws IOException
	 */
	public void writeFile(File file) throws IOException {
		write(new FileOutputStream(file));
	}

	/**
	 * 获取响应信息的类型
	 * 
	 * @功能描述
	 * @return
	 */
	public String getContentType() {
		Header header = this.getResponseHeader("Content-Type");
		return StringUtil.isNotNull(header) ? header.getValue() : "text/html;charset=utf-8";
	}

	/**
	 * 获取响应信息的大小
	 * 
	 * @功能描述
	 * @return
	 */
	public long getContentLength() {
		return Long.valueOf(this.getResponseHeader("Content-Length").getValue()).longValue();
	}

	/**
	 * 获取响应信息的编码格式
	 * 
	 * @return {encode}
	 */
	public String getContentEncoding() {
		String contentType = getContentType();
		return contentType.contains("charset=") ? null : contentType.substring(contentType.lastIndexOf("charset=") + "charset=".length());// "Unknown"
	}

	/**
	 * 获取cookie信息
	 * 
	 * @功能描述
	 * @param name
	 * @return
	 */
	public String getCookie(String name) {
		for (Cookie cookie : this.getCookies()) {
			if (cookie.getName().toUpperCase().equals(name.toUpperCase())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * 获取响应流
	 * 
	 * @功能描述 如果缓存过，获取缓存的流
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		return this.out != null ? new ByteArrayInputStream(cache().toByteArray()) : this.in;
	}
}