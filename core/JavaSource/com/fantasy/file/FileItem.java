package com.fantasy.file;

import com.fantasy.framework.util.common.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文件接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-8 下午4:49:25
 */
public interface FileItem {

    /**
     * 获取文件名
     *
     * @return string
     */
    String getName();

    /**
     * 是否为文件夹
     *
     * @return boolean
     */
    boolean isDirectory();

    /**
     * 文件大小
     *
     * @return long
     */
    long getSize();

    /**
     * 文件类型
     *
     * @return contentType
     */
    String getContentType();

    /**
     * 文文件
     *
     * @return FileItem
     */
    @JsonSerialize(using = ParentFileItemSerialize.class)
    FileItem getParentFileItem();

    /**
     * 子文件
     *
     * @return List<FileItem>
     */
    List<FileItem> listFileItems();

    /**
     * 绝对路径
     *
     * @return string
     */
    String getAbsolutePath();

    /**
     * 最后修改日期
     *
     * @return Date
     */
    @JsonProperty("lastModified")
    Date lastModified();

    /**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param filter 过滤器
     * @return List<FileItem>
     */
    @JsonIgnore
    List<FileItem> listFileItems(FileItemFilter filter);

    /**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param selector 选择器
     * @return List<FileItem>
     */
    @JsonIgnore
    List<FileItem> listFileItems(FileItemSelector selector);

    /**
     * 获取 Metadata 信息
     *
     * @return List<FileItem>
     */
    Metadata getMetadata();

    /**
     * 获取文件输入流
     *
     * @return InputStream
     * @throws IOException
     */
    @JsonIgnore
    InputStream getInputStream() throws IOException;

    class ParentFileItemSerialize extends JsonSerializer<FileItem> {

        @Override
        public void serialize(FileItem fileItem, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(fileItem.getAbsolutePath());
        }

    }

    final class Util {
        private Util(){}
        public static List<FileItem> flat(List<FileItem> items, FileItemSelector selector) {
            List<FileItem> fileItems = new ArrayList<FileItem>();
            for (FileItem item : items) {
                boolean include = selector.includeFile(item);
                if (include) {
                    fileItems.add(item);
                }
                if (include && item.isDirectory() && selector.traverseDescendents(item)) {
                    fileItems.addAll(flat(item.listFileItems(), selector));
                }
            }
            return fileItems;
        }

    }


    interface HttpHeaders {

        String AUTHORIZATION = "Authorization";
        String CACHE_CONTROL = "Cache-Control";
        String CONTENT_DISPOSITION = "Content-Disposition";
        String CONTENT_ENCODING = "Content-Encoding";
        String CONTENT_LENGTH = "Content-Length";
        String CONTENT_MD5 = "Content-MD5";
        String CONTENT_TYPE = "Content-Type";
        String TRANSFER_ENCODING = "Transfer-Encoding";
        String DATE = "Date";
        String ETAG = "ETag";
        String EXPIRES = "Expires";
        String HOST = "Host";
        String LAST_MODIFIED = "Last-Modified";
        String RANGE = "Range";
        String LOCATION = "Location";

    }

    class Metadata {

        // 用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
        private Map<String, String> userMetadata = new HashMap<String, String>();

        // 非用户自定义的元数据。
        private Map<String, Object> metadata = new HashMap<String, Object>();

        public static final long DEFAULT_FILE_SIZE_LIMIT = 5 * 1024 * 1024 * 1024L;

        public Metadata(Map<String, Object> metadata) {
            this.metadata.clear();
            if (metadata != null && !metadata.isEmpty()) {
                this.metadata.putAll(metadata);
            }
        }

        public Metadata(Map<String, Object> metadata, Map<String, String> userMetadata) {
            this(metadata);
            this.setUserMetadata(userMetadata);
        }

        public Metadata() {
        }

        /**
         * <p>
         * 获取用户自定义的元数据。
         * </p>
         * <p>
         * 同时，元数据字典的键名是不区分大小写的，并且在从服务器端返回时会全部以小写形式返回，
         * 即使在设置时给定了大写字母。比如键名为：MyUserMeta，通过getObjectMetadata接口
         * 返回时键名会变为：myusermeta。
         * </p>
         *
         * @return 用户自定义的元数据。
         */
        public Map<String, String> getUserMetadata() {
            return userMetadata;
        }

        /**
         * 设置用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
         *
         * @param userMetadata 用户自定义的元数据。
         */
        public void setUserMetadata(Map<String, String> userMetadata) {
            this.userMetadata.clear();
            if (userMetadata != null && !userMetadata.isEmpty()) {
                this.userMetadata.putAll(userMetadata);
            }
        }

        /**
         * 设置请求头（内部使用）。
         *
         * @param key   请求头的Key。
         * @param value 请求头的Value。
         */
        public void setHeader(String key, Object value) {
            metadata.put(key, value);
        }

        /**
         * 添加一个用户自定义的元数据。
         *
         * @param key   请求头的Key。
         *              这个Key不需要包含OSS要求的前缀，即不需要加入“x-oss-meta-”。
         * @param value 请求头的Value。
         */
        public void addUserMetadata(String key, String value) {
            this.userMetadata.put(key, value);
        }

        /**
         * 获取Last-Modified请求头的值，表示Object最后一次修改的时间。
         *
         * @return Object最后一次修改的时间。
         */
        public Date getLastModified() {
            return (Date) metadata.get(HttpHeaders.LAST_MODIFIED);
        }

        /**
         * 设置Last-Modified请求头的值，表示Object最后一次修改的时间（内部使用）。
         *
         * @param lastModified Object最后一次修改的时间。
         */
        public void setLastModified(Date lastModified) {
            metadata.put(HttpHeaders.LAST_MODIFIED, lastModified);
        }

        /**
         * 获取Expires请求头。
         * 如果Object没有定义过期时间，则返回null。
         *
         * @return Expires请求头。
         */
        public Date getExpirationTime() {
            return (Date) metadata.get(HttpHeaders.EXPIRES);
        }

        /**
         * 设置Expires请求头。
         *
         * @param expirationTime 过期时间。
         */
        public void setExpirationTime(Date expirationTime) {
            metadata.put(HttpHeaders.EXPIRES, DateUtil.formatRfc822Date(expirationTime));
        }

        /**
         * 获取Content-Length请求头，表示Object内容的大小。
         *
         * @return Object内容的大小。
         */
        public long getContentLength() {
            Long contentLength = (Long) metadata.get(HttpHeaders.CONTENT_LENGTH);
            return contentLength == null ? 0 : contentLength;
        }

        /**
         * 设置Content-Length请求头，表示Object内容的大小。
         * 当上传Object到OSS时，请总是指定正确的content length。
         *
         * @param contentLength Object内容的大小。
         * @throws IllegalArgumentException Object内容的长度大小大于最大限定值：5G字节。
         */
        public void setContentLength(long contentLength) {
            if (contentLength > DEFAULT_FILE_SIZE_LIMIT) {
                throw new IllegalArgumentException("内容长度不能超过5G字节。");
            }
            metadata.put(HttpHeaders.CONTENT_LENGTH, contentLength);
        }

        /**
         * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
         *
         * @return Object内容的类型，为标准的MIME类型。
         */
        public String getContentType() {
            return (String) metadata.get(HttpHeaders.CONTENT_TYPE);
        }

        /**
         * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
         *
         * @param contentType Object内容的类型，为标准的MIME类型。
         */
        public void setContentType(String contentType) {
            metadata.put(HttpHeaders.CONTENT_TYPE, contentType);
        }

        public String getContentMD5() {
            return (String) metadata.get(HttpHeaders.CONTENT_MD5);
        }

        public void setContentMD5(String contentMD5) {
            metadata.put(HttpHeaders.CONTENT_MD5, contentMD5);
        }

        /**
         * 获取Content-Encoding请求头，表示Object内容的编码方式。
         *
         * @return Object内容的编码方式。
         */
        public String getContentEncoding() {
            return (String) metadata.get(HttpHeaders.CONTENT_ENCODING);
        }

        /**
         * 设置Content-Encoding请求头，表示Object内容的编码方式。
         *
         * @param encoding 表示Object内容的编码方式。
         */
        public void setContentEncoding(String encoding) {
            metadata.put(HttpHeaders.CONTENT_ENCODING, encoding);
        }

        /**
         * 获取Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
         *
         * @return Cache-Control请求头。
         */
        public String getCacheControl() {
            return (String) metadata.get(HttpHeaders.CACHE_CONTROL);
        }

        /**
         * 设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
         *
         * @param cacheControl Cache-Control请求头。
         */
        public void setCacheControl(String cacheControl) {
            metadata.put(HttpHeaders.CACHE_CONTROL, cacheControl);
        }

        /**
         * 获取Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
         *
         * @return Content-Disposition请求头
         */
        public String getContentDisposition() {
            return (String) metadata.get(HttpHeaders.CONTENT_DISPOSITION);
        }

        /**
         * 设置Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
         *
         * @param disposition Content-Disposition请求头
         */
        public void setContentDisposition(String disposition) {
            metadata.put(HttpHeaders.CONTENT_DISPOSITION, disposition);
        }

        /**
         * 获取一个值表示与Object相关的hex编码的128位MD5摘要。
         *
         * @return 与Object相关的hex编码的128位MD5摘要。
         */
        public String getETag() {
            return (String) metadata.get(HttpHeaders.ETAG);
        }

        /**
         * 返回内部保存的请求头的元数据（内部使用）。
         *
         * @return 内部保存的请求头的元数据（内部使用）。
         */
        public Map<String, Object> getRawMetadata() {
            return Collections.unmodifiableMap(metadata);
        }

    }

}
