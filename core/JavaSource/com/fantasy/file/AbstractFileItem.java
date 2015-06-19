package com.fantasy.file;

import com.fantasy.framework.util.regexp.RegexpUtil;

import java.io.File;
import java.util.Date;

public abstract class AbstractFileItem implements FileItem {
    private String absolutePath;
    private String name;
    private boolean directory;
    private Date lastModified;
    private Metadata metadata;
    private long size;

    public AbstractFileItem(String absolutePath) {
        this.absolutePath = absolutePath.replace(File.separator, "/");
        this.name = RegexpUtil.parseGroup(absolutePath, "([^/]+)/$", 1);
        this.directory = true;
        this.lastModified = null;
        this.metadata = new Metadata();
        if (!this.absolutePath.startsWith("/")) {
            this.absolutePath = "/" + this.absolutePath;
        }
        if (!this.absolutePath.endsWith("/")) {
            this.absolutePath = this.absolutePath + "/";
        }
    }

    public AbstractFileItem(String absolutePath, Metadata metadata) {
        this.absolutePath = absolutePath.replace(File.separator, "/");
        this.name = RegexpUtil.parseGroup(absolutePath, "([^/]+)/$", 1);
        this.directory = true;
        this.lastModified = null;
        this.metadata = metadata;
    }

    public AbstractFileItem(String absolutePath, long size, Date lastModified, Metadata metadata) {
        this.absolutePath = absolutePath;
        this.name = RegexpUtil.parseGroup(absolutePath, "([^/]+)/$", 1);
        this.size = size;
        this.directory = false;
        this.lastModified = lastModified;
        this.metadata = metadata;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDirectory() {
        return this.directory;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public String getContentType() {
        return this.metadata.getContentType();
    }

    @Override
    public String getAbsolutePath() {
        return this.absolutePath;
    }

    @Override
    public Date lastModified() {
        return this.lastModified;
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

}
