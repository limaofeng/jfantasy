package com.fantasy.file.ws.server;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.file.ws.IFileUploadService;
import com.fantasy.file.ws.dto.FileDTO;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.context.MessageContext;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceException;
import java.io.*;

@Component
public class FileUploadWebService implements IFileUploadService {

    @Resource
    private transient FileUploadService fileUploadService;

    public FileDTO uploadFile(String fileName, String contentType, String dir, String attchmentID) {
        MessageContext msgCtx = MessageContext.getCurrentMessageContext();
        Attachments attachment = msgCtx.getAttachmentMap();
        System.out.println(attachment.getAllContentIDs());
        DataHandler dataHandler = attachment.getDataHandler(attchmentID);
        try {
            File tmp = FileUtil.tmp();
            dataHandler.writeTo(new FileOutputStream(tmp));
            FileDetail fileDetail = fileUploadService.upload(tmp, contentType, fileName, dir);
            FileUtil.delFile(tmp);
            return BeanUtil.copyProperties(new FileDTO(), fileDetail);
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage(), e);
        }
    }

    public String upload(DataHandler dataHandler,String fileName,String dir) {
        try {
            File tmp = FileUtil.tmp();
            FileOutputStream out = new FileOutputStream(tmp);
            dataHandler.writeTo(out);
            StreamUtil.closeQuietly(out);
            fileUploadService.upload(tmp, dataHandler.getContentType(), fileName, dir);
            FileDetail fileDetail = fileUploadService.upload(tmp, dataHandler.getContentType(), fileName, dir);
            FileUtil.delFile(tmp);
            return fileDetail.getAbsolutePath();
        }catch (IOException e){
            throw new WebServiceException(e.getMessage(), e);
        }
    }

}
