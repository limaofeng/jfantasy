package com.fantasy.file.manager;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileItemFilter;
import com.fantasy.file.FileItemSelector;
import com.fantasy.file.FileManager;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.service.FTPService;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPFile;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FTPFileManager implements FileManager {

	private final static Log logger = LogFactory.getLog(FTPFileManager.class);

	private FTPService ftpService;

	public FTPFileManager() {
	}

	public FTPFileManager(FTPService ftpService) {
		super();
		this.ftpService = ftpService;
	}

	public void setFtpService(FTPService ftpService) {
		this.ftpService = ftpService;
	}

	public void readFile(String remotePath, String localPath) throws IOException {
		this.ftpService.download(remotePath, localPath);
	}

	public void readFile(String remotePath, OutputStream out) throws IOException {
		this.ftpService.download(remotePath, out);
	}

	public void writeFile(String absolutePath, File file) throws IOException {
		writeFile(absolutePath, new FileInputStream(file));
	}

	public void writeFile(String absolutePath, InputStream in) throws IOException {
		this.ftpService.uploadFile(in, absolutePath);
	}

	public InputStream readFile(String remote) throws IOException {
		return this.ftpService.getInputStream(remote);
	}

	public OutputStream writeFile(String remotePath) throws IOException {
		return this.ftpService.getOutputStream(remotePath);
	}

	private FileItem retrieveFileItem(final FTPFile ftpFile, final String parentPath) throws IOException {
		return new FTPFileItem(ftpFile, parentPath, this);
	}

	public static class FTPFileItem implements FileItem {
		private FTPFileManager fileManager;
		private FTPFile ftpFile;
		private String parentPath;
		private String absolutePath;

		public FTPFileItem(final FTPFile ftpFile, String parentPath, FTPFileManager fileManager) {
			this.ftpFile = ftpFile;
			this.parentPath = parentPath;
			this.fileManager = fileManager;
			this.absolutePath = (parentPath.endsWith("/") ? parentPath : (parentPath + "/")) + (".".equals(ftpFile.getName()) ? "" : ftpFile.getName());
		}

		public FTPFileItem(String absolutePath, FTPFileManager fileManager) {
			this.absolutePath = absolutePath;
			this.fileManager = fileManager;
			this.parentPath = RegexpUtil.replace(absolutePath, "[^/]+[/][^/]*$", "");
		}

		public List<FileItem> listFileItems(FileItemSelector selector) {
			if (!this.isDirectory()) {
				return new ArrayList<FileItem>();
			}
			return FileItem.Util.flat(this.listFileItems(), selector);
		}

        @Override
        public File getFile() {
            return null;
        }

        public List<FileItem> listFileItems(FileItemFilter filter) {
			List<FileItem> fileItems = new ArrayList<FileItem>();
			if (!this.isDirectory()) {
				return fileItems;
			}
			for (FileItem item : listFileItems()) {
				if (filter.accept(item)) {
					fileItems.add(item);
				}
			}
			return fileItems;
		}

		public List<FileItem> listFileItems() {
			try {
				List<FileItem> fileItems = new ArrayList<FileItem>();
				if (!this.isDirectory()) {
					return fileItems;
				}
				for (FTPFile ftpFile : fileManager.ftpService.listFiles(this.getAbsolutePath() + "/")) {
					if (RegexpUtil.find(ftpFile.getName(), "^[.]{1,}$")) {
						continue;
					}
					fileItems.add(fileManager.retrieveFileItem(ftpFile, this.getAbsolutePath()));
				}
				return fileItems;
			} catch (IOException e) {
				throw new IgnoreException(e.getMessage());
			}
		}

		public Date lastModified() {
			return getFtpFile().getTimestamp().getTime();
		}

		public boolean isDirectory() {
			return getFtpFile().isDirectory();
		}

		public long getSize() {
			return getFtpFile().getSize();
		}

		public FileItem getParentFileItem() {
			return "/".equals(this.getAbsolutePath()) ? null : new FTPFileItem(this.parentPath, fileManager);
		}

		public String getName() {
			return getFtpFile().getName();
		}

		public String getContentType() {
			try {
				return FileUtil.getMimeType(getInputStream());
			} catch (IOException e) {
				logger.error(" getContentType Error : ", e);
				return getFtpFile().getType() + "";
			}
		}

		public String getAbsolutePath() {
			return this.absolutePath;
		}

		public InputStream getInputStream() throws IOException {
			if (this.isDirectory()) {
				throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
			}
			return fileManager.ftpService.getInputStream(getAbsolutePath());
		}

		@JsonIgnore
		public FTPFile getFtpFile() {
			if (ftpFile == null) {
				try {
					ftpFile = fileManager.ftpService.listFiles(RegexpUtil.replace(this.absolutePath, "/$", ""))[0];
				} catch (IOException e) {
					throw new IgnoreException(e.getMessage());
				}
			}
			return ftpFile;
		}

	}

	public FileItem getFileItem(String remotePath) {
		try {
			if (!this.ftpService.exist(remotePath)) {
				return null;
			}
			boolean dir = this.ftpService.isDir(remotePath);
			remotePath = dir ? (remotePath.endsWith("/") ? remotePath : (remotePath + "/")) : remotePath;
			String parentPath = RegexpUtil.replace(remotePath, "[^/]+[/][^/]*$", "");
			if (dir) {
				return retrieveFileItem(this.ftpService.listFiles(RegexpUtil.replace(remotePath, "/$", ""))[0], parentPath);
			} else {
				return retrieveFileItem(this.ftpService.listFiles(remotePath)[0], parentPath);
			}
		} catch (IOException e) {
			throw new IgnoreException(e.getMessage());
		}
	}

	public List<FileItem> listFiles() {
		return null;
	}

	public List<FileItem> listFiles(String remotePath) {
		return getFileItem(remotePath).listFileItems();
	}

	public List<FileItem> listFiles(FileItemSelector selector) {
		return null;
	}

	public List<FileItem> listFiles(String remotePath, FileItemSelector selector) {
		return null;
	}

	public List<FileItem> listFiles(FileItemFilter filter) {
		return null;
	}

	public List<FileItem> listFiles(String remotePath, FileItemFilter filter) {
		return null;
	}

	public void removeFile(String remotePath) {
		try {
			this.ftpService.deleteRemoteFile(remotePath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) throws IOException {
		FTPFileManager fileManager = new FTPFileManager();

		FTPService ftpService = new FTPService();
		ftpService.setHostname("127.0.0.1");
		ftpService.setUsername("lmf");
		ftpService.setPassword("123456");
		ftpService.setControlEncoding("gbk");

		fileManager.setFtpService(ftpService);

		FileItem fileItem = fileManager.getFileItem("/urcbweb");
		System.out.println(fileItem.getName() + "\t" + fileItem.getAbsolutePath());
		for (FileItem f : fileItem.listFileItems()) {
			System.out.println(f.getName() + "\t" + f.getAbsolutePath());
		}

	}

}