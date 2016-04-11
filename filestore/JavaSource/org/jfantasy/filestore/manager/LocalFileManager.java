package org.jfantasy.filestore.manager;

import org.jfantasy.filestore.*;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;

import java.io.*;
import java.util.*;

public class LocalFileManager implements FileManager {

    private String defaultDir;

    public LocalFileManager() {
        super();
    }

    public LocalFileManager(String defaultDir) {
        super();
        this.defaultDir = defaultDir;
    }

    public void readFile(String remotePath, String localPath) throws IOException {
        readFile(remotePath, new FileOutputStream(localPath));
    }

    public void readFile(String remotePath, OutputStream out) throws IOException {
        StreamUtil.copyThenClose(getInputStream(remotePath), out);
    }

    public void writeFile(String remotePath, File file) throws IOException {
        writeFile(remotePath, new FileInputStream(file));
    }

    public void writeFile(String remotePath, InputStream in) throws IOException {
        StreamUtil.copyThenClose(in, getOutputStream(remotePath));
    }

    public InputStream readFile(String remotePath) throws IOException {
        return getInputStream(remotePath);
    }

    public void setDefaultDir(String defaultDir) {
        FileUtil.createFolder(new File(defaultDir));
        this.defaultDir = defaultDir;
    }

    public OutputStream writeFile(String remotePath) throws IOException {
        return getOutputStream(remotePath);
    }

    private OutputStream getOutputStream(String absolutePath) throws IOException {
        return new FileOutputStream(createFile(absolutePath));
    }

    private String filterRemotePath(String remotePath) {
        return StringUtil.defaultValue(remotePath, "").startsWith("/") ? remotePath : ("/" + remotePath);
    }

    private InputStream getInputStream(String remotePath) throws IOException {
        File file = new File(this.defaultDir + filterRemotePath(remotePath));
        if (!file.exists()) {
            throw new FileNotFoundException("文件:" + remotePath + "不存在!");
        }
        return new FileInputStream(file);
    }

    private File createFile(String remotePath) {
        return FileUtil.createFile(this.defaultDir + filterRemotePath(remotePath));
    }

    public FileItem getFileItem(String remotePath) {
        return retrieveFileItem(filterRemotePath(remotePath));
    }

    private FileItem root() {
        return retrieveFileItem(File.separator);
    }

    public FileItem retrieveFileItem(String absolutePath) {
        final File file = new File(this.defaultDir + absolutePath);
        if (!file.getAbsolutePath().startsWith(new File(this.defaultDir).getAbsolutePath()) || !file.exists()) {
            return null;
        }
        return file.isDirectory() ? new LocalFileItem(this, file) : new LocalFileItem(this, file, new FileItem.Metadata(new HashMap<String, Object>() {
            {
                this.put(FileItem.HttpHeaders.CONTENT_TYPE, FileUtil.getMimeType(file));
            }
        }));
    }

    public FileItem retrieveFileItem(final File file) {
        if (!file.getAbsolutePath().startsWith(new File(this.defaultDir).getAbsolutePath()) || !file.exists()) {
            return null;
        }
        return file.isDirectory() ? new LocalFileItem(this, file) : new LocalFileItem(this, file, new FileItem.Metadata(new HashMap<String, Object>() {
            {
                this.put(FileItem.HttpHeaders.CONTENT_TYPE, FileUtil.getMimeType(file));
            }
        }));
    }

    public List<FileItem> listFiles() {
        return root().listFileItems();
    }

    public List<FileItem> listFiles(FileItemFilter filter) {
        return root().listFileItems(filter);
    }

    public List<FileItem> listFiles(FileItemSelector selector) {
        return root().listFileItems(selector);
    }

    @SuppressWarnings("unchecked")
    public List<FileItem> listFiles(String remotePath) {
        FileItem fileItem = retrieveFileItem(remotePath);
        return fileItem == null ? Collections.EMPTY_LIST : fileItem.listFileItems();
    }

    @SuppressWarnings("unchecked")
    public List<FileItem> listFiles(String remotePath, FileItemFilter fileItemFilter) {
        FileItem fileItem = retrieveFileItem(remotePath);
        return fileItem == null ? Collections.EMPTY_LIST : fileItem.listFileItems(fileItemFilter);
    }

    public List<FileItem> listFiles(String remotePath, FileItemSelector selector) {
        return getFileItem(remotePath).listFileItems(selector);
    }

    public void removeFile(String remotePath) {
        FileUtil.delFile(this.defaultDir + remotePath);
    }

    public static class LocalFileItem extends AbstractFileItem {

        private File file;
        private LocalFileManager fileManager;

        public LocalFileItem(LocalFileManager fileManager, File file) {
            super(file.getAbsolutePath().substring(fileManager.defaultDir.length() + (fileManager.defaultDir.endsWith(File.separator) ? -1 : 0)));
            this.file = file;
            this.fileManager = fileManager;
        }

        public LocalFileItem(LocalFileManager fileManager, final File file, FileItem.Metadata metadata) {
            super(file.getAbsolutePath().substring(fileManager.defaultDir.length() + (fileManager.defaultDir.endsWith(File.separator) ? -1 : 0)), file.length(), new Date(file.lastModified()), metadata);
            this.file = file;
            this.fileManager = fileManager;
        }

        public FileItem getParentFileItem() {
            return this.fileManager.retrieveFileItem(file.getParentFile());
        }

        public List<FileItem> listFileItems() {
            List<FileItem> fileItems = new ArrayList<FileItem>();
            if (!this.isDirectory()) {
                return fileItems;
            }
            for (File f : file.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return (pathname.isDirectory() || pathname.isFile()) && !pathname.isHidden();
                }
            })) {
                fileItems.add(this.fileManager.retrieveFileItem(f));
            }
            return fileItems;
        }

        public List<FileItem> listFileItems(FileItemFilter filter) {
            List<FileItem> fileItems = new ArrayList<FileItem>();
            if (!this.isDirectory()) {
                return fileItems;
            }
            for (FileItem item : listFileItems()) {
                if (filter.accept(item)) {
                    fileItems.add(item);
                }
            }
            return fileItems;
        }

        public List<FileItem> listFileItems(FileItemSelector selector) {
            if (!this.isDirectory()) {
                return new ArrayList<FileItem>();
            }
            return FileItem.Util.flat(this.listFileItems(), selector);
        }

        public InputStream getInputStream() throws IOException {
            if (this.isDirectory()) {
                throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
            }
            return this.fileManager.readFile(this.getAbsolutePath());
        }

    }

}