package com.fantasy.file.ws;

import com.fantasy.file.ws.dto.FileDTO;

import java.io.IOException;

public interface IFileUploadService {

	public FileDTO upload(String fileName, String contentType, String dir, String attchmentID) throws IOException;

}
