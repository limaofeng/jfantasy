package com.fantasy.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件管理接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-9-28 下午12:09:54
 */
public interface FileManager {

    /**
     * 将一个 File 对象写入地址对应的文件中
     *
     * @param remotePath 路径
     * @param file       文件对象
     * @throws IOException
     */
     void writeFile(String remotePath, File file) throws IOException;

    /**
     * 将 InputStream 写到地址对应的文件中
     *
     * @param remotePath 路径
     * @param in         输出流
     * @throws IOException
     */
    void writeFile(String remotePath, InputStream in) throws IOException;

    /**
     * 返回一个 out 流，用来接收要写入的内容
     *
     * @param remotePath 路径
     * @return {OutputStream}
     * @throws IOException
     */
    OutputStream writeFile(String remotePath) throws IOException;

    /**
     * 通过地址将一个文件写到一个本地地址
     *
     * @param remotePath 路径
     * @param localPath  路径
     * @throws IOException
     */
    void readFile(String remotePath, String localPath) throws IOException;

    /**
     * 通过地址将文件写入到 OutputStream 中
     *
     * @param remotePath 路径
     * @param out        输出流
     * @throws IOException
     */
    void readFile(String remotePath, OutputStream out) throws IOException;

    /**
     * 通过一个地址获取文件对应的 InputStream
     *
     * @param remotePath 路径
     * @return 返回 InputStream 对象
     * @throws IOException
     */
    InputStream readFile(String remotePath) throws IOException;

    /**
     * 获取跟目录的文件列表
     *
     * @return {List<FileItem>}
     */
    List<FileItem> listFiles();

    /**
     * 获取指定路径下的文件列表
     *
     * @param remotePath 路径
     * @return {List<FileItem>}
     */
    List listFiles(String remotePath);

    /**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param selector FileItemSelector
     * @return {List<FileItem>}
     */
    List<FileItem> listFiles(FileItemSelector selector);

    /**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param remotePath 路径
     * @param selector   FileItemSelector
     * @return {List<FileItem>}
     */
    List<FileItem> listFiles(String remotePath, FileItemSelector selector);

    /**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param filter FileItemFilter
     * @return {List<FileItem>}
     */
    List<FileItem> listFiles(FileItemFilter filter);

    /**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param remotePath 路径
     * @param filter     FileItemFilter
     * @return {List<FileItem>}
     */
    List<FileItem> listFiles(String remotePath, FileItemFilter filter);

    /**
     * 获取目录对应的 FileItem 对象
     *
     * @param remotePath 地址
     * @return {FileItem}
     */
    FileItem getFileItem(String remotePath);

    /**
     * 删除地址对应的文件
     *
     * @param remotePath 地址
     */
    void removeFile(String remotePath);

}