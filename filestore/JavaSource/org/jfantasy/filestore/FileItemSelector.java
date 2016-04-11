package org.jfantasy.filestore;

/**
 * 文件选择器
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-8 下午4:50:47
 */
public interface FileItemSelector {

    /**
     * 判断是否这个文件或者目录应该被选择
     *
     * @param fileItem 文件对象
     * @return {boolean}
     */
    boolean includeFile(FileItem fileItem);

    /**
     * 判断这个目录是否应该被遍历
     *
     * @param fileItem 文件对象
     * @return {boolean}
     */
    boolean traverseDescendents(FileItem fileItem);

}
