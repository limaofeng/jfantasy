package org.jfantasy.aliyun.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import org.jfantasy.filestore.*;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OSSFileManager implements FileManager {

    protected AccessKey accessKey;
    private String bucketName;
    private String endpoint;
    private OSSClient client;

    public OSSFileManager(String endpoint, AccessKey accessKey, String bucketName) {
        this.accessKey = accessKey;
        this.bucketName = bucketName;
        this.client = new OSSClient(this.endpoint = endpoint, this.accessKey.getId(), this.accessKey.getSecret());
    }

    @Override
    public void writeFile(String remotePath, File file) throws IOException {
        createFolder(remotePath.endsWith("/") ? remotePath : RegexpUtil.replace(remotePath, "[^/]+[/]{0,1}$", ""));
        client.putObject(this.bucketName, RegexpUtil.replace(remotePath, "^/", ""), file);
    }

    @Override
    public void writeFile(String remotePath, InputStream in) throws IOException {
        createFolder(remotePath.endsWith("/") ? remotePath : RegexpUtil.replace(remotePath, "[^/]+[/]{0,1}$", ""));
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(in.available());
        client.putObject(this.bucketName, RegexpUtil.replace(remotePath, "^/", ""), in, meta);
    }

    private void createFolder(String folders) {
        String path = "";
        for (String folder : StringUtil.tokenizeToStringArray(folders, "/")) {
            if (!client.doesObjectExist(bucketName, path += folder + "/")) {
                ObjectMetadata objectMeta = new ObjectMetadata();
                ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
                objectMeta.setContentLength(0);
                try {
                    client.putObject(bucketName, path, in, objectMeta);
                } finally {
                    StreamUtil.closeQuietly(in);
                }
            }
        }
    }

    @Override
    public OutputStream writeFile(String remotePath) throws IOException {
        throw new RuntimeException(" OSS 不支持直接获取输入流对象操作文件,可以通过自定义 OutputStream 实现该方法!");
    }

    @Override
    public void readFile(String remotePath, String localPath) throws IOException {
        readFile(remotePath, new FileOutputStream(localPath));
    }

    @Override
    public void readFile(String remotePath, OutputStream out) throws IOException {
        StreamUtil.copyThenClose(readFile(remotePath), out);
    }

    @Override
    public InputStream readFile(String remotePath) throws IOException {
        FileItem fileItem = this.retrieveFileItem(remotePath);
        if (fileItem == null) {
            throw new FileNotFoundException("文件:" + remotePath + "不存在!");
        }
        return fileItem.getInputStream();
    }

    @Override
    public List<FileItem> listFiles() {
        return this.listFiles("/");
    }

    @Override
    public List<FileItem> listFiles(String remotePath) {
        FileItem fileItem = retrieveFileItem(remotePath);
        return fileItem == null ? Collections.<FileItem>emptyList() : fileItem.listFileItems();
    }

    @Override
    public List<FileItem> listFiles(FileItemSelector selector) {
        return this.listFiles("/", selector);
    }

    @Override
    public List<FileItem> listFiles(String remotePath, FileItemSelector selector) {
        FileItem fileItem = retrieveFileItem(remotePath);
        return fileItem == null ? Collections.<FileItem>emptyList() : fileItem.listFileItems(selector);
    }

    @Override
    public List<FileItem> listFiles(FileItemFilter filter) {
        return this.listFiles("/", filter);
    }

    @Override
    public List<FileItem> listFiles(String remotePath, FileItemFilter filter) {
        FileItem fileItem = retrieveFileItem(remotePath);
        return fileItem == null ? Collections.<FileItem>emptyList() : fileItem.listFileItems(filter);
    }

    @Override
    public FileItem getFileItem(String remotePath) {
        return retrieveFileItem(remotePath);
    }

    private FileItem retrieveFileItem(OSSObjectSummary objectSummary) {
        String absolutePath = "/" + objectSummary.getKey();
        if (absolutePath.endsWith("/")) {
            return new OSSFileItem(this, absolutePath, client.getObjectMetadata(bucketName, objectSummary.getKey()));
        } else {
            return new OSSFileItem(this, absolutePath, objectSummary.getSize(), objectSummary.getLastModified(), client.getObjectMetadata(bucketName, objectSummary.getKey()));
        }
    }

    private FileItem retrieveFileItem(String absolutePath) {
        ObjectMetadata objectMetadata;
        if ("/".equals(absolutePath)) {
            objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0);
            objectMetadata.setContentType("application/ostet-stream");
        } else {
            String ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
            if (!client.doesObjectExist(bucketName, ossAbsolutePath)) {
                return null;
            }
            objectMetadata = client.getObjectMetadata(bucketName, ossAbsolutePath);
        }
        if (absolutePath.endsWith("/")) {
            return new OSSFileItem(this, absolutePath, objectMetadata);
        } else {
            return new OSSFileItem(this, absolutePath, objectMetadata.getContentLength(), objectMetadata.getLastModified(), objectMetadata);
        }
    }

    @Override
    public void removeFile(String remotePath) {
        if (remotePath.endsWith("/")) {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            String prefix = RegexpUtil.replace(remotePath, "^/", "");
            if (StringUtil.isNotBlank(prefix)) {
                listObjectsRequest.setPrefix(prefix);
            }
            ObjectListing listing = client.listObjects(listObjectsRequest);
            for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                client.deleteObject(bucketName, objectSummary.getKey());
            }
            for (String commonPrefix : listing.getCommonPrefixes()) {
                if (client.doesObjectExist(bucketName, commonPrefix)) {
                    client.deleteObject(bucketName, commonPrefix);
                }
            }
        } else {
            client.deleteObject(bucketName, RegexpUtil.replace(remotePath, "^/", ""));
        }
    }

    public class OSSFileItem extends AbstractFileItem {
        private String ossAbsolutePath;
        private OSSFileManager fileManager;

        protected OSSFileItem(OSSFileManager fileManager, String absolutePath, ObjectMetadata objectMetadata) {
            super(absolutePath, new Metadata(objectMetadata.getRawMetadata(), objectMetadata.getUserMetadata()));
            this.ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
            this.fileManager = fileManager;
        }

        protected OSSFileItem(OSSFileManager fileManager, String absolutePath, long size, Date lastModified, ObjectMetadata objectMetadata) {
            super(absolutePath, size, lastModified, new Metadata(objectMetadata.getRawMetadata(), objectMetadata.getUserMetadata()));
            this.ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
            this.fileManager = fileManager;
        }

        @Override
        public FileItem getParentFileItem() {
            return this.fileManager.retrieveFileItem(RegexpUtil.replace(this.getAbsolutePath(), "[^/]+[/]{0,1}$", ""));
        }

        @Override
        public List<FileItem> listFileItems() {
            if (!this.isDirectory()) {
                return Collections.emptyList();
            }
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            listObjectsRequest.setDelimiter("/");
            String prefix = ossAbsolutePath;
            if (StringUtil.isNotBlank(prefix)) {
                listObjectsRequest.setPrefix(prefix);
            }
            ObjectListing listing = client.listObjects(listObjectsRequest);
            List<FileItem> fileItems = new ArrayList<FileItem>();
            for (String commonPrefix : listing.getCommonPrefixes()) {
                if (commonPrefix.equals(ossAbsolutePath)) {
                    continue;
                }
                fileItems.add(this.fileManager.retrieveFileItem("/" + commonPrefix));
            }
            for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                if (objectSummary.getKey().equals(ossAbsolutePath)) {
                    continue;
                }
                fileItems.add(this.fileManager.retrieveFileItem(objectSummary));
            }
            return fileItems;
        }

        @Override
        public List<FileItem> listFileItems(FileItemFilter filter) {
            if (!this.isDirectory()) {
                return Collections.emptyList();
            }
            List<FileItem> fileItems = new ArrayList<FileItem>();
            for (FileItem item : listFileItems()) {
                if (filter.accept(item)) {
                    fileItems.add(item);
                }
            }
            return fileItems;
        }

        @Override
        public List<FileItem> listFileItems(FileItemSelector selector) {
            if (!this.isDirectory()) {
                return Collections.emptyList();
            }
            return FileItem.Util.flat(this.listFileItems(), selector);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (this.isDirectory()) {
                throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
            }
            return client.getObject(bucketName, ossAbsolutePath).getObjectContent();
        }
    }

    public static class AccessKey {
        private String id;
        private String secret;

        public AccessKey(String id, String secret) {
            this.id = id;
            this.secret = secret;
        }

        public String getId() {
            return id;
        }

        public String getSecret() {
            return secret;
        }
    }

}
