package com.fantasy.file.ws.client;

import com.fantasy.file.ws.IFileUploadService;
import com.fantasy.file.ws.dto.FileDTO;
import com.fantasy.framework.ws.axis2.WebServiceClient;

import javax.activation.DataHandler;

/**
 * 上传文件服务
 */
public class FileUploadService extends WebServiceClient implements IFileUploadService {

    public FileUploadService() {
        super("FileUploadService");
    }

    @Override
    public FileDTO uploadFile(String fileName, String contentType, String dir, String attchmentID) {
        return null;
    }

    @Override
    public String upload(DataHandler handler, String fileName, String dir) {
        return this.invokeOption("upload", new Object[]{handler, fileName,dir}, String.class);
    }

}
