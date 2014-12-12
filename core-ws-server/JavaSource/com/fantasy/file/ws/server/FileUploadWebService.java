package com.fantasy.file.ws.server;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.file.ws.IFileUploadService;
import com.fantasy.file.ws.dto.FileDTO;
import com.fantasy.framework.util.common.file.FileUtil;
import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.context.MessageContext;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUploadWebService implements IFileUploadService{

	@Resource
	private transient FileUploadService fileUploadService;
	
	@Override
	public FileDTO upload(String fileName,String contentType, String dir,String attchmentID) throws IOException {
		MessageContext msgCtx = MessageContext.getCurrentMessageContext();
        Attachments attachment = msgCtx.getAttachmentMap();
		DataHandler dataHandler = attachment.getDataHandler(attchmentID);

		File tmp = FileUtil.tmp();
		dataHandler.writeTo(new FileOutputStream(tmp));

        FileDetail fileDetail = fileUploadService.upload(tmp,contentType,fileName,dir);
		FileUtil.delFile(tmp);
		//TODO 查询上传文件对应的记录
		return null;//WebServiceUtil.toBean(fileDetail,FileDTO.class);
	}

}
