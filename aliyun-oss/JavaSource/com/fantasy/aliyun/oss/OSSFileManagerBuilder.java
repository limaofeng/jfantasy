package com.fantasy.aliyun.oss;


import com.fantasy.file.FileManagerBuilder;
import com.fantasy.file.bean.enums.FileManagerType;

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
