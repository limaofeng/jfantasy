package com.fantasy.file.manager;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileItemFilter;
import com.fantasy.file.FileItemSelector;
import com.fantasy.file.FileManager;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        File file = new File(this.defaultDir + filterRemotePath(remotePath));
        return file.exists() ? retrieveFileItem(file) : null;
    }

    private FileItem root() {
        return retrieveFileItem(new File(this.defaultDir));
    }

    private FileItem retrieveFileItem(final File file) {
        if (!file.getAbsolutePath().startsWith(new File(this.defaultDir).getAbsolutePath()) || !file.exists()) {
            return null;
        }
        return new LocalFileItem(this, file);
    }

    public List<FileItem> listFiles() {
        return root().listFileItems();
    }

    public List<FileItem> listFiles(FileItemFilter filter) {
        List<FileItem> fileItems = new ArrayList<FileItem>();
        for (FileItem item : listFiles()) {
            if (filter.accept(item)) {
                fileItems.add(item);
            }
        }
        return fileItems;
    }

    public List<FileItem> listFiles(FileItemSelector selector) {
        return root().listFileItems(selector);
    }

    @SuppressWarnings("unchecked")
    public List<FileItem> listFiles(String remotePath) {
        FileItem fileItem = retrieveFileItem(new File(this.defaultDir + remotePath));
        return fileItem == null ? Collections.EMPTY_LIST : fileItem.listFileItems();
    }

    @SuppressWarnings("unchecked")
    public List<FileItem> listFiles(String remotePath, FileItemFilter fileItemFilter) {
        FileItem fileItem = retrieveFileItem(new File(this.defaultDir + remotePath));
        return fileItem == null ? Collections.EMPTY_LIST : fileItem.listFileItems(fileItemFilter);
    }

    public List<FileItem> listFiles(String remotePath, FileItemSelector selector) {
        return getFileItem(remotePath).listFileItems(selector);
    }

    public void removeFile(String remotePath) {
        FileUtil.delFile(this.defaultDir + remotePath);
    }

    public static class LocalFileItem implements FileItem {

        private File file;
        private LocalFileManager fileManager;

        public LocalFileItem(File file) {
            this.file = file;
        }

        public LocalFileItem(LocalFileManager fileManager, File file) {
            this.file = file;
            this.fileManager = fileManager;
        }

        public String getAbsolutePath() {
            FileItem fileItem = getParentFileItem();
            return fileItem != null ? ((fileItem.getAbsolutePath().equals("/") ? "/" : (fileItem.getAbsolutePath() + "/")) + getName()) : "/";
        }

        public String getContentType() {
            return file.isDirectory() ? "folder" : FileUtil.getMimeType(file);
        }

        public String getName() {
            return file.getName();
        }

        public FileItem getParentFileItem() {
            if (this.fileManager == null) {
                return new LocalFileItem(file.getParentFile());
            } else {
                return this.fileManager.retrieveFileItem(file.getParentFile());
            }
        }

        public long getSize() {
            return file.length();
        }

        public boolean isDirectory() {
            return file.isDirectory();
        }

        public Date lastModified() {
            return new Date(file.lastModified());
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
                if (this.fileManager == null) {
                    fileItems.add(new LocalFileItem(f));
                } else {
                    fileItems.add(this.fileManager.retrieveFileItem(f));
                }
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

        @Override
        public File getFile() {
            return file;
        }

        public InputStream getInputStream() throws IOException {
            if (this.isDirectory()) {
                throw new RuntimeException("当前对象为一个目录,不能获取 InputStream ");
            }
            if (this.fileManager == null) {
                return new FileInputStream(file);
            } else {
                return this.fileManager.readFile(this.getAbsolutePath());
            }
        }

    }

}