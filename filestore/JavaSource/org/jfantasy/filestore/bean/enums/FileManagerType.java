package org.jfantasy.filestore.bean.enums;

public enum FileManagerType {
    local("本地文件系统"), oss("阿里开放存储服务"), ftp("FTP文件系统"), jdbc("数据库存储"), virtual("上传文件存储");

    private String value;

    FileManagerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
