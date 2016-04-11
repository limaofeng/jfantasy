package org.jfantasy.aliyun.oss;


import org.jfantasy.filestore.FileManagerBuilder;
import org.jfantasy.filestore.bean.enums.FileManagerType;

import java.util.Map;

public class OSSFileManagerBuilder implements FileManagerBuilder<OSSFileManager> {

    @Override
    public FileManagerType getType() {
        return FileManagerType.oss;
    }

    @Override
    public OSSFileManager register(Map<String, String> params) {
        String accessKeyId = params.get("accessKeyId");
        String accessKeySecret = params.get("accessKeySecret");
        String endpoint = params.get("endpoint");
        String bucketName = params.get("bucketName");
        return new OSSFileManager(endpoint, new OSSFileManager.AccessKey(accessKeyId, accessKeySecret), bucketName);
    }

}
