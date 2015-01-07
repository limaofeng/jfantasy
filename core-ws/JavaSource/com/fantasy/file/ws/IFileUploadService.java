package com.fantasy.file.ws;

import com.fantasy.file.ws.dto.FileDTO;

import javax.activation.DataHandler;

public interface IFileUploadService {

	public FileDTO uploadFile(String fileName, String contentType, String dir, String attchmentID);

	public String upload(DataHandler handler, String fileName, String dir);

}
