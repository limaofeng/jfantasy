package com.fantasy.framework.httpclient;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Arrays;

/**
 * HttpClient 响应对象
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-30 下午04:46:02
 */
public class Response {

    private static final Log LOGGER = LogFactory.getLog(Response.class);

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
     * @return status
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
     * @return text
     * @throws IOException
     */
    public String getText() throws IOException {
        return getText(getContentEncoding());
    }

    /**
     * 按纯文本格式获取响应信息
     *
     * @param pretty 是否显示换行
     * @return text
     * @throws IOException
     */
    public String getText(boolean pretty) throws IOException {
        return getText(getContentEncoding(), pretty);
    }

    /**
     * 按纯文本格式获取响应信息
     *
     * @param charset 按指定的编码格式解析
     * @return text
     * @throws IOException
     */
    public String getText(String charset) throws IOException {
        return this.getText(charset, false);
    }

    /**
     * 按纯文本格式获取响应信息
     *
     * @param charset 按指定的编码格式解析
     * @param pretty  是否显示换行
     * @return text
     * @throws IOException
     */
    public String getText(String charset, boolean pretty) throws IOException {
        InputStream intemp = new ByteArrayInputStream(cache().toByteArray());
        BufferedReader reader = null;
        StringBuilder html = new StringBuilder();
        try {
            if (StringUtil.isNull(charset)) {
                reader = new BufferedReader(new InputStreamReader(intemp));
            } else {
                reader = new BufferedReader(new InputStreamReader(intemp, charset));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                html.append(line).append(pretty ? System.getProperty("line.separator") : "");
            }
        } finally {
            IOUtils.closeQuietly(intemp);
            IOUtils.closeQuietly(reader);
        }
        return html.toString();
    }

    /**
     * 按json格式反序列化响应信息
     *
     * @param <T>     泛型类型
     * @param clazz   要转化的类型
     * @param charset 编码格式
     * @return T
     * @throws IOException
     */
    public <T> T deserialize(Class<T> clazz, String charset) throws IOException {
        return JSON.deserialize(getText(charset), clazz);
    }

    /**
     * 缓存响应流
     *
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    private ByteArrayOutputStream cache() throws IOException {
        if (this.out == null) {
            LOGGER.debug("将文件写入缓冲区");
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
     * @param headerName headerName
     * @return Header
     */
    public Header getRequestHeader(String headerName) {
        for (Header requestHeader : requestHeaders) {
            if (requestHeader.getName().equalsIgnoreCase(headerName)){
                return requestHeader;
            }
        }
        return null;
    }

    /**
     * 获取响应头信息
     *
     * @param headerName headerName
     * @return Header
     */
    public Header getResponseHeader(String headerName) {
        for (Header responseHeader : responseHeaders) {
            if (responseHeader.getName().equalsIgnoreCase(headerName)){
                return responseHeader;
            }
        }
        return null;
    }

    /**
     * 设置响应输入流
     *
     * @param responseInputStream 响应流
     */
    protected void setInputStream(InputStream responseInputStream) {
        this.in = responseInputStream;
    }

    /**
     * 将相应写入到流中 从缓存区读取，不存在缓存区则建立缓存流
     *
     * @param out   要写入的流对象
     * @param cache 是否缓存
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
     * @param out 输出流
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        InputStream intemp = getInputStream();
        try {
            byte[] buf = new byte[1024];
            int num;
            while ((num = intemp.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, num);
            }
        } finally {
            IOUtils.closeQuietly(intemp);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 将响应信息写入到本地文件
     *
     * @param absolutePath 文件路径
     * @throws IOException
     */
    public void writeFile(String absolutePath) throws IOException {
        writeFile(new File(absolutePath));
    }

    /**
     * 将响应信息写入到本地文件
     *
     * @param file 文件对象
     * @throws IOException
     */
    public void writeFile(File file) throws IOException {
        write(new FileOutputStream(file));
    }

    /**
     * 获取响应信息的类型
     *
     * @return contentType
     */
    public String getContentType() {
        Header header = this.getResponseHeader("Content-Type");
        return StringUtil.isNotNull(header) ? header.getValue() : "text/html;charset=utf-8";
    }

    /**
     * 获取响应信息的大小
     *
     * @return length
     */
    public long getContentLength() {
        return Long.valueOf(this.getResponseHeader("Content-Length").getValue());
    }

    /**
     * 获取响应信息的编码格式
     *
     * @return {encode}
     */
    public String getContentEncoding() {
        String contentType = getContentType();
        return !contentType.contains("charset=") ? null : contentType.substring(contentType.lastIndexOf("charset=") + "charset=".length());// "Unknown"
    }

    /**
     * 获取cookie信息
     *
     * @param name cookieName
     * @return value
     */
    public String getCookie(String name) {
        for (Cookie cookie : this.getCookies()) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 获取响应流
     * 如果缓存过，获取缓存的流
     *
     * @return InputStream
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        return this.out != null ? new ByteArrayInputStream(cache().toByteArray()) : this.in;
    }

    @Override
    public String toString() {
        return "Response{" +
                "url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", cookies=" + Arrays.toString(cookies) +
                ", responseHeaders=" + Arrays.toString(responseHeaders) +
                ", requestHeaders=" + Arrays.toString(requestHeaders) +
                '}';
    }
}