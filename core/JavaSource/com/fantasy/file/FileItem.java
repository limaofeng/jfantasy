package com.fantasy.file;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件接口
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-8 下午4:49:25
 */
public interface FileItem {
    /**
     * 获取文件名
     *
     * @return string
     */
    public String getName();

    /**
     * 是否为文件夹
     *
     * @return boolean
     */
    public boolean isDirectory();

    /**
     * 文件大小
     *
     * @return long
     */
    public long getSize();

    /**
     * 文件类型
     *
     * @return contentType
     */
    public String getContentType();

    /**
     * 文文件
     *
     * @return FileItem
     */
    @JsonSerialize(using = ParentFileItemSerialize.class)
    public FileItem getParentFileItem();

    /**
     * 子文件
     *
     * @return List<FileItem>
     */
    public List<FileItem> listFileItems();

    /**
     * 绝对路径
     *
     * @return string
     */
    public String getAbsolutePath();

    /**
     * 最后修改日期
     *
     * @return Date
     */
    @JsonProperty("lastModified")
    public Date lastModified();

    /**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param filter 过滤器
     * @return List<FileItem>
     */
    @JsonIgnore
    public List<FileItem> listFileItems(FileItemFilter filter);

    /**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param selector 选择器
     * @return List<FileItem>
     */
    @JsonIgnore
    public List<FileItem> listFileItems(FileItemSelector selector);

    @JsonIgnore
    public File getFile();

    /**
     * 获取文件输入流
     *
     * @return InputStream
     * @throws IOException
     */
    @JsonIgnore
    public InputStream getInputStream() throws IOException;

    public static class ParentFileItemSerialize extends JsonSerializer<FileItem> {

        @Override
        public void serialize(FileItem fileItem, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(fileItem.getAbsolutePath());
        }

    }

    public final static class Util {

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

}
