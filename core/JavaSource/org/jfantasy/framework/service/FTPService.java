package org.jfantasy.framework.service;

import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.*;
import org.springframework.util.Assert;

import java.io.*;
import java.util.StringTokenizer;

/**
 * FTP工具类
 * <p/>
 * 暂时只提供读写及删除操作,使其可以直接返回文件列表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-4 下午04:11:21
 */
public class FTPService {

    private static final String ISO_8859_1 = "ISO-8859-1";
    private static Log LOG = LogFactory.getLog(FTPService.class);

    /**
     * 端口
     */
    private int port = FTPClient.DEFAULT_PORT;
    private int bufferSize = 1024 * 32;
    private int fileTransferMode = FTP.STREAM_TRANSFER_MODE;
    private int fileType = FTP.BINARY_FILE_TYPE;
    /**
     * 超时毫秒数
     */
    private int timeout = 0;
    /**
     * FTP服务地址
     */
    private String hostname;
    /**
     * 登陆名称
     */
    private String username;
    /**
     * 登陆密码
     */
    private String password;
    /**
     * 控制台语言
     */
    private String controlEncoding = "UTF-8";
    /**
     * 服务器的语言
     */
    private String serverLanguageCode = "zh";
    /**
     * 系统标志
     */
    private String systemKey = FTPClientConfig.SYST_NT;
    /**
     * 默认目录 TODO 可以加载FTP内容
     */
    private String defaultDir;

    public void setDefaultDir(String defaultDir) {
        this.defaultDir = defaultDir;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public void setServerLanguageCode(String serverLanguageCode) {
        this.serverLanguageCode = serverLanguageCode;
    }

    public void setFileTransferMode(int fileTransferMode) {
        this.fileTransferMode = fileTransferMode;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setControlEncoding(String encoding) {
        this.controlEncoding = encoding;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 登陆Ftp服务器
     *
     * @return FTPClient
     * @throws IOException
     */
    protected FTPClient login() throws IOException {
        if (LOG.isDebugEnabled()) {
            StringBuffer debug = new StringBuffer("\r\n准备开始连接FTP服务器:" + this.hostname + ",连接信息如下");
            debug.append("\r\nSystemKey:").append(this.systemKey);
            debug.append("\r\nServerLanguageCode:").append(this.serverLanguageCode);
            debug.append("\r\nHostname:").append(this.hostname);
            debug.append("\r\nPort:").append(this.port);
            debug.append("\r\nAuthentication:").append(this.username).append("/").append(this.password);
            debug.append("\r\nFileType:").append(this.fileType);
            debug.append("\r\nFileTransferMode:").append(this.fileTransferMode);
            debug.append("\r\nBufferSize:").append(bufferSize);
            debug.append("\r\nControlEncoding:").append(controlEncoding);
            debug.append("\r\nTimeout:").append(timeout);
            LOG.debug(debug);
        }
        FTPClient ftpClient = new FTPClient();
        FTPClientConfig config = new FTPClientConfig(this.systemKey);
        config.setServerLanguageCode(this.serverLanguageCode);

        if (LOG.isDebugEnabled()) {
            LOG.debug("开始连接到FTP服务器[" + this.hostname + ":" + this.port + "]");
        }
        ftpClient.connect(this.hostname, this.port);

        if (LOG.isDebugEnabled()) {
            LOG.debug("开始登录FTP服务器[" + this.username + "/" + this.password + "]");
        }
        ftpClient.login(this.username, this.password);

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("[" + reply + "]登录ftp服务器失败,请检查[" + this.hostname + ":" + this.port + "][" + this.username + "/" + this.password + "]是否正确!");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("ftp登录成功[" + ftpClient + "]");
        }

        ftpClient.setFileType(this.fileType);
        ftpClient.setFileTransferMode(this.fileTransferMode);

        ftpClient.setBufferSize(this.bufferSize);
        ftpClient.setControlEncoding(this.controlEncoding);

        if (this.timeout > 0) {
            ftpClient.setConnectTimeout(this.timeout);
        }
        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }

    /**
     * 关闭连接
     *
     * @param ftpClient FTPClient
     */
    protected void closeConnection(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("关闭ftp连接[" + ftpClient + "]");
                }
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            LOG.error("关闭FTP连接失败!", e);
        }
    }

    public FTPFile[] listFiles() throws IOException {
        if (StringUtil.isNotBlank(this.defaultDir)) {
            return listFiles(this.defaultDir);
        } else {
            FTPClient client = login();
            try {
                return client.listFiles();
            } finally {
                closeConnection(client);
            }
        }
    }

    public FTPFile[] listFiles(String pathname) throws IOException {
        FTPClient ftpClient = login();
        try {
            return ftpClient.listFiles(encode(pathname));
        } finally {
            closeConnection(ftpClient);
        }
    }

    /**
     * 判断文件或者文件夹是否存在
     *
     * @param pathname 文件路径
     * @return boolean
     * @throws IOException
     */
    public boolean exist(String pathname) throws IOException {
        FTPClient ftpClient = login();
        try {
            return isDirExist(encode(pathname), ftpClient) || ftpClient.listFiles(encode(pathname)).length > 0;
        } finally {
            closeConnection(ftpClient);
        }
    }

    public boolean isDir(String pathname) throws IOException {
        FTPClient ftpClient = login();
        try {
            return ftpClient.changeWorkingDirectory(encode(pathname));
        } finally {
            closeConnection(ftpClient);
        }
    }

    /**
     * 上传文件到服务器
     *
     * @param local        本地路径
     * @param remoteFolder 远程路径
     * @throws IOException
     */
    public void uploadFile(String local, String remoteFolder) throws IOException {
        uploadFile(new File(local), remoteFolder);
    }

    /**
     * 上传文件到服务器
     *
     * @param localFile    本地文件
     * @param remoteFolder 远程目录
     * @throws IOException
     */
    public void uploadFile(File localFile, String remoteFolder) throws IOException {
        if (!localFile.exists()) {
            throw new FileNotFoundException("此文件[" + localFile.getName() + "]有误或不存在");
        }
        if (remoteFolder.endsWith("/")) {
            remoteFolder += RegexpUtil.parseGroup(localFile.getPath().replace("\\", "/"), "[^\\/]+$", 0);
        }
        if (localFile.isDirectory()) {
            throw new IOException(localFile.getPath() + "不是一个文件");
        }
        uploadFile(new FileInputStream(localFile), remoteFolder);
    }

    /**
     * 上传单个文件
     *
     * @param in         输入流
     * @param remoteFile 远程路径
     * @param ftpClient  ftpclien对象
     * @throws IOException
     */
    protected void uploadFile(InputStream in, String remoteFile, FTPClient ftpClient) throws IOException {
        if (remoteFile.indexOf(".") < 1) {
            throw new FileNotFoundException("必须指定上传文件的目录及文件名");
        }
        OutputStream out = getOutputStream(remoteFile, ftpClient);
        try {
            StreamUtil.copy(in, out, this.bufferSize);
        } finally {
            StreamUtil.closeQuietly(in);
            StreamUtil.closeQuietly(out);
        }
    }

    /**
     * 上传单个文件
     *
     * @param in         输入流
     * @param remoteFile 远程文件路径
     * @throws IOException
     */
    public void uploadFile(InputStream in, String remoteFile) throws IOException {
        OutputStream out = getOutputStream(remoteFile);
        try {
            StreamUtil.copy(in, out, this.bufferSize);
        } finally {
            StreamUtil.closeQuietly(in);
            StreamUtil.closeQuietly(out);
        }
    }

    /**
     * 将文件夹上传到FTP服务器
     *
     * @param localFolder  本地目录
     * @param remoteFolder 远程目录
     * @throws IOException
     */
    public void uploadFolder(String localFolder, String remoteFolder) throws IOException {
        uploadFolder(new File(localFolder), remoteFolder);
    }

    /**
     * 将文件夹上传到FTP服务器
     *
     * @param localFile    本地文件夹对象
     * @param remoteFolder 远程目录
     * @throws IOException
     */
    public void uploadFolder(File localFile, String remoteFolder) throws IOException {
        if (!localFile.exists()) {
            throw new FileNotFoundException("此文件夹[" + localFile.getName() + "]有误或不存在");
        }
        if (!localFile.isDirectory()) {
            throw new IOException(localFile.getPath() + "不是一个文件夹");
        }
        FTPClient ftpClient = login();
        try {
            // 在服务器上创建目录
            String remote = RegexpUtil.parseGroup(remoteFolder, "^[\\/][^\\/]+[\\/]", 0);
            if (!isDirExist(encode(remote), ftpClient)) {
                createDir(encode(remote), ftpClient);
            }
            // 遍历本地目录上传文件
            File[] sourceFiles = ObjectUtil.defaultValue(localFile.listFiles(), new File[0]);
            for (File file : sourceFiles) {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        uploadFolder(file, remote + file.getName() + "/", ftpClient);
                    } else {
                        uploadFile(new FileInputStream(file), remote.concat(file.getName()), ftpClient);
                    }
                }
            }
        } finally {
            closeConnection(ftpClient);
        }
    }

    /**
     * @param localFile    本地目录
     * @param remoteFolder 远程目录
     * @param ftpClient    ftpClient
     * @throws IOException
     */
    protected void uploadFolder(File localFile, String remoteFolder, FTPClient ftpClient) throws IOException {
        // 在服务器上创建目录
        String remote = remoteFolder.substring(0, remoteFolder.lastIndexOf("/") + 1);
        if (!isDirExist(encode(remote), ftpClient)) {
            createDir(encode(remote), ftpClient);
        }
        // 遍历本地目录上传文件
        File[] sourceFile = ObjectUtil.defaultValue(localFile.listFiles(), new File[0]);
        for (File file : sourceFile) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    uploadFolder(file, remote + file.getName() + "/", ftpClient);
                } else {
                    uploadFile(new FileInputStream(file), remote.concat(file.getName()), ftpClient);
                }
            }
        }
    }

    private String encode(String str) {
        try {
            return new String(str.getBytes(this.controlEncoding), ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            throw new IgnoreException(e.getMessage(),e);
        }
    }

    private String decode(String str) {
        try {
            return str == null ? "" : new String(str.getBytes(ISO_8859_1), this.controlEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new IgnoreException(e.getMessage(),e);
        }
    }

    /**
     * 删除文件
     *
     * @param remoteFile 远程目录
     * @throws IOException
     */
    public void deleteRemoteFile(String remoteFile) throws IOException {
        FTPClient ftpClient = login();
        try {
            deleteRemoteFile(remoteFile, ftpClient);
        } finally {
            closeConnection(ftpClient);
        }
    }

    /**
     * 删除文件夹
     *
     * @param remoteFolder 远程目录
     * @throws IOException
     */
    public void deleteRemoteFolder(String remoteFolder) throws IOException {
        FTPClient ftpClient = login();
        try {
            deleteRemoteFolder(remoteFolder, ftpClient);
        } finally {
            closeConnection(ftpClient);
        }
    }

    /**
     * 删除FTP服务器上的文件
     *
     * @param remoteFile 远程目录
     * @param ftpClient  FTPClient
     * @throws IOException
     */
    protected void deleteRemoteFile(String remoteFile, FTPClient ftpClient) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("从FTP[" + this.hostname + "]上删除文件:" + remoteFile);
        }
        int replyCode = ftpClient.dele(encode(remoteFile));
        if (replyCode != 250) {
            throw new FileNotFoundException("此文件[" + remoteFile + "]有误或不存在!");
        }
    }

    /**
     * 删除FTP服务器上的文件夹
     *
     * @param remoteFolder 远程目录
     * @param ftpClient    FTPClient
     * @throws IOException
     */
    protected void deleteRemoteFolder(String remoteFolder, FTPClient ftpClient) throws IOException {
        if (!isDirExist(encode(remoteFolder), ftpClient)) {
            throw new FileNotFoundException("此文件夹[" + remoteFolder + "]不存在!");
        }
        if (ftpClient.changeWorkingDirectory(encode(remoteFolder))) {
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                String remote = (remoteFolder.endsWith("/") ? remoteFolder : new StringBuilder(String.valueOf(remoteFolder)).append("/").toString()) + file.getName();
                if (!file.getName().endsWith(".")) {
                    if (file.isDirectory()) {
                        deleteRemoteFolder(remote + "/", ftpClient);
                    } else {
                        deleteRemoteFile(remote, ftpClient);
                    }
                }
            }
        }
        boolean success = ftpClient.rmd(encode(remoteFolder)) != 550;
        if (LOG.isDebugEnabled()) {
            LOG.debug("从FTP[" + this.hostname + "]上删除文件夹:" + remoteFolder + "\t处理结果:" + success);
        }
    }

    protected boolean isDirExist(String dir, FTPClient ftpClient) {
        int code;
        try {
            code = ftpClient.cwd(dir);
            ftpClient.cdup();
            return code != FTPReply.FILE_UNAVAILABLE;
        } catch (IOException e) {
            LOG.error("检查远程虚拟目录是否存在时出现异常!", e);
        }
        return false;
    }

    protected void createDir(String dir, FTPClient ftpClient) {
        LOG.debug("在FTP[" + this.hostname + "]上创建目录[" + decode(dir) + "]");
        StringTokenizer s = new StringTokenizer(dir, "/");
        s.countTokens();
        String pathName = "";
        while (s.hasMoreElements()) {
            pathName = pathName + "/" + (String) s.nextElement();
            try {
                boolean success = ftpClient.makeDirectory(pathName);
                if ((!success) && (dir.equals(pathName + "/"))) {
                    throw new IOException();
                }
            } catch (IOException e) {
                LOG.error("创建远程目录[" + decode(dir) + "]时出现异常 ", e);
            }
        }
    }

    /**
     * 下载整个目录到本地
     *
     * @param remoteFolder 远程目录
     * @param localFolder  本地目录
     * @throws IOException
     */
    public void downloadFolder(String remoteFolder, String localFolder) throws IOException {
        File folder = FileUtil.createFile(localFolder);
        if (folder.isDirectory() && !folder.exists()) {
            throw new FileNotFoundException("创建本地目录:" + localFolder + "失败");
        }
        if (!folder.isDirectory()) {
            throw new FileNotFoundException(localFolder + "不是文件夹");
        }
        FTPClient ftpClient = login();
        try {
            downloadFolder(remoteFolder, folder, ftpClient);
        } finally {
            closeConnection(ftpClient);
        }
    }

    protected void downloadFolder(String remoteFolder, File localFolder, FTPClient ftpClient) throws IOException {
        if (!isDirExist(encode(remoteFolder), ftpClient)) {
            throw new FileNotFoundException("在[" + this.hostname + "]上不存在[" + remoteFolder + "]目录");
        }
        FTPFile[] files = ftpClient.listFiles(encode(remoteFolder));
        for (FTPFile file : files) {
            if (file.getName().endsWith(".")) {
                continue;
            }
            if (file.isDirectory()) {
                File folder = FileUtil.createFolder(localFolder, file.getName());
                if (!folder.exists()) {
                    throw new IOException("创建本地目录:" + localFolder + "失败");
                }
                downloadFolder(remoteFolder + file.getName() + "/", folder, ftpClient);
            } else {
                File localFile = FileUtil.createFile(localFolder, file.getName());
                String remote = remoteFolder + file.getName();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("创建文件[" + localFile.getAbsolutePath() + "]");
                }
                download(remote, new FileOutputStream(localFile), ftpClient);
            }
        }
    }

    protected void download(String remote, OutputStream out, FTPClient ftpClient) throws IOException {
        InputStream in = this.getInputStream(remote, ftpClient);
        try {
            StreamUtil.copy(in, out, this.bufferSize);
        } finally {
            StreamUtil.closeQuietly(in);
            StreamUtil.closeQuietly(out);
        }
    }

    /**
     * 将远程文件写到本地路径下
     *
     * @param remote 远程目录
     * @param local  本地目录
     * @throws IOException
     */
    public void download(String remote, String local) throws IOException {
        download(remote, FileUtil.createFile(local));
    }

    /**
     * 将远程文件下载到文件中
     *
     * @param remote    远程目录
     * @param localFile 本地文件对象
     * @throws IOException
     */
    public void download(String remote, File localFile) throws IOException {
        if ((localFile.isDirectory() && !localFile.exists()) || !localFile.getParentFile().exists()) {
            throw new FileNotFoundException("创建本地目录:" + localFile.getAbsolutePath() + "失败");
        }
        if (remote.endsWith("/")) {
            throw new FileNotFoundException("必须指定下载文件的文件名");
        }
        if (localFile.isDirectory()) {
            localFile = FileUtil.createFile(localFile, RegexpUtil.parseGroup(remote, "[^\\/]+$", 0));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("开始下载文件:" + remote + " > " + localFile.getAbsolutePath());
        }
        download(remote, new FileOutputStream(localFile));
    }

    /**
     * 将远程文件写入到OutputStream中
     *
     * @param remote 远程目录
     * @param out    输入流
     * @throws IOException
     */
    public void download(String remote, OutputStream out) throws IOException {
        InputStream in = this.getInputStream(remote);
        try {
            StreamUtil.copy(in, out, this.bufferSize);
        } finally {
            StreamUtil.closeQuietly(in);
            StreamUtil.closeQuietly(out);
        }
    }

    public InputStream getInputStream(String remote) throws IOException {
        FTPClient ftpClient = login();
        try {
            FTPFile[] files = ftpClient.listFiles(encode(remote));
            if (files.length < 1) {
                throw new FileNotFoundException(remote + "在  FTP[" + this.hostname + "]上文件未找到!");
            }
            return new RemoteFtpInputStream(ftpClient, ftpClient.retrieveFileStream(encode(remote)), true);
        } catch (IOException ex) {
            closeConnection(ftpClient);
            throw ex;
        }
    }

    public String getLastModified(String remote) throws IOException {
        FTPClient ftpClient = login();
        try {
            FTPFile[] files = ftpClient.listFiles(encode(remote));
            if (files.length < 1) {
                throw new FileNotFoundException(remote + "在  FTP[" + this.hostname + "]上文件未找到!");
            }
            return ftpClient.getModificationTime(encode(remote));
        } catch (IOException ex) {
            closeConnection(ftpClient);
            throw ex;
        } finally {
            closeConnection(ftpClient);
        }
    }

    protected InputStream getInputStream(String remote, FTPClient ftpClient) throws IOException {
        FTPFile[] files = ftpClient.listFiles(encode(remote));
        if (files.length < 1) {
            throw new FileNotFoundException(remote + "在  FTP[" + this.hostname + "]上文件未找到!");
        }
        return new RemoteFtpInputStream(ftpClient, ftpClient.retrieveFileStream(encode(remote)));
    }

    /**
     * 获取FTP服务器的输出流
     *
     * @param remoteFile 远程目录
     * @param ftpClient  FtpClient对象
     * @return OutputStream
     * @throws IOException
     */
    protected OutputStream getOutputStream(String remoteFile, FTPClient ftpClient) throws IOException {
        String fileName = remoteFile.substring(remoteFile.lastIndexOf("/") + 1);
        String remoteFolder = "/";
        if (remoteFile.indexOf("/") != remoteFile.lastIndexOf("/")) {
            remoteFolder = remoteFile.substring(0, remoteFile.lastIndexOf("/"));
            if (!isDirExist(encode(remoteFolder), ftpClient)) {
                createDir(encode(remoteFolder), ftpClient);
            }
        }
        if (ftpClient.changeWorkingDirectory(encode(remoteFolder))) {
            return new RemoteFtpOutputStream(ftpClient, ftpClient.storeFileStream(encode(fileName)));
        }
        throw new IOException("访问目录" + remoteFile + "时出错");
    }

    /**
     * 获取FTP服务器的输出流
     *
     * @param remoteFile 远程目录
     * @return OutputStream
     * @throws IOException
     */
    public OutputStream getOutputStream(String remoteFile) throws IOException {
        if (remoteFile.endsWith("/")) {
            throw new FileNotFoundException("必须指定上传文件的目录及文件名");
        }
        FTPClient ftpClient = login();
        try {
            String fileName = RegexpUtil.parseGroup(remoteFile, "[^\\/]+$", 0);
            String remoteFolder = "/";
            if (remoteFile.indexOf("/") != remoteFile.lastIndexOf("/")) {
                remoteFolder = remoteFile.substring(0, remoteFile.lastIndexOf("/"));
                if (!isDirExist(encode(remoteFolder), ftpClient)) {
                    createDir(encode(remoteFolder), ftpClient);
                }
            }
            if (ftpClient.changeWorkingDirectory(encode(remoteFolder))) {
                return new RemoteFtpOutputStream(ftpClient, ftpClient.storeFileStream(encode(fileName)), true);
            }
            throw new IOException("访问目录" + remoteFile + "时出错");
        } catch (IOException e) {
            closeConnection(ftpClient);
            throw e;
        }
    }

    protected class RemoteFtpOutputStream extends OutputStream {

        private FTPClient ftpClient;
        private OutputStream outputStream;
        private boolean isCloseConnection;

        public RemoteFtpOutputStream(FTPClient ftpClient, OutputStream outputStream, boolean isCloseConnection) {
            this(ftpClient, outputStream);
            this.isCloseConnection = isCloseConnection;
        }

        public RemoteFtpOutputStream(FTPClient ftpClient, OutputStream outputStream) {
            this.ftpClient = ftpClient;
            this.outputStream = outputStream;

            Assert.notNull(this.ftpClient, "ftpClient is null");
            Assert.notNull(this.outputStream, "outputStream is null");
        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void close() throws IOException {
            try {
                this.outputStream.close();
                ftpClient.completePendingCommand();
            } finally {
                if (isCloseConnection) {
                    closeConnection(ftpClient);
                }
            }

        }

    }

    protected class RemoteFtpInputStream extends InputStream {
        private FTPClient ftpClient;
        private InputStream inputStream;
        private boolean isCloseConnection;

        public RemoteFtpInputStream(FTPClient ftpClient, InputStream inputStream, boolean isCloseConnection) {
            this(ftpClient, inputStream);
            this.isCloseConnection = isCloseConnection;
        }

        public RemoteFtpInputStream(FTPClient ftpClient, InputStream inputStream) {
            this.ftpClient = ftpClient;
            this.inputStream = inputStream;

            Assert.notNull(this.ftpClient, "ftpClient is null");
            Assert.notNull(this.inputStream, "inputStream is null");
        }

        public int read() throws IOException {
            return this.inputStream.read();
        }

        public int available() throws IOException {
            return this.inputStream.available();
        }

        public void close() throws IOException {
            try {
                this.inputStream.close();
                this.ftpClient.completePendingCommand();
            } finally {
                if (isCloseConnection) {
                    closeConnection(ftpClient);
                }
            }
        }

        public synchronized void mark(int readlimit) {
            this.inputStream.mark(readlimit);
        }

        public boolean markSupported() {
            return this.inputStream.markSupported();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            return this.inputStream.read(b, off, len);
        }

        public int read(byte[] bs) throws IOException {
            return this.inputStream.read(bs);
        }

        public synchronized void reset() throws IOException {
            this.inputStream.reset();
        }

        public long skip(long n) throws IOException {
            return this.inputStream.skip(n);
        }

    }

}
