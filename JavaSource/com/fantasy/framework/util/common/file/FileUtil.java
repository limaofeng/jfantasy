package com.fantasy.framework.util.common.file;

import com.fantasy.framework.dao.mybatis.keygen.GUIDKeyGenerator;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.MimeUtil2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.*;

public class FileUtil {

    private static final Log logger = LogFactory.getLog(FileUtil.class);

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    private static final String ENCODE = "UTF-8";

    public static String readFile(File file, String charset) {
        String line;
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), charset);
            BufferedReader reader = new BufferedReader(read);
            StringBuilder buf = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            reader.close();
            return buf.toString();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("没有找到文件:" + file.getAbsolutePath());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public static String readFile(String file) {
        return readFile(new File(file));
    }

    public static String readFile(File file) {
        return readFile(file, ENCODE);
    }

    public static String readFile(String file, String charset) {
        return readFile(new File(file), charset);
    }

    public static String readFile(InputStream in) throws IOException {
        return readFile(in, ENCODE);
    }

    public static void readFile(InputStream in, ReadLineCallback readLineCallback) throws IOException {
        readFile(in, ENCODE, readLineCallback);
    }

    public static void readFile(InputStream in, String charset, ReadLineCallback readLineCallback) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, charset));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!readLineCallback.readLine(line)) {
                    break;
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(reader);
        }
    }

    public static interface ReadLineCallback {

        public boolean readLine(String line);

    }

    public static String readFile(InputStream in, String charset) throws IOException {
        final StringBuilder html = new StringBuilder();
        readFile(in, charset, new ReadLineCallback() {
            @Override
            public boolean readLine(String line) {
                html.append(line).append(System.getProperty("line.separator"));
                return true;
            }
        });
        return html.toString();
    }

    public static void writeFile(String content, String file) {
        try {
            File f = new File(file).getParentFile();
            if (!f.exists() && !f.mkdirs()) {
                throw new RuntimeException("创建文件" + file + "失败");
            }
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, ENCODE);

            out.flush();
            out.write(content);
            out.close();
        } catch (IOException ex) {
            logger.error(ex);
            throw new RuntimeException(ex);
        }
    }

    public static void writeFile(byte[] content, String file) throws IOException {
        try {
            createFolder(new File(file).getParentFile());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content, 0, content.length);
            fos.flush();
            StreamUtil.closeQuietly(fos);
            logger.debug("Write File:" + file);
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        }
    }

    public static File createFolder(String path) {
        return createFolder(new File(path(path).replaceFirst("[^\\/]+$", "")));
    }

    public static File createFolder(File file) {
        if (!file.isDirectory()) {
            createFolder(file.getParentFile());
        }
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("创建文件" + file + "失败");
        return file;
    }

    public static File createFolder(File file, String folderName) {
        return createFolder(new File(file, folderName));
    }

    private static String path(String pathname) {
        return RegexpUtil.replace(pathname, "[" + ("\\".equals(File.separator) ? "\\" : "") + File.separator + "]", "/");
    }

    /**
     * 创建文件并返回
     *
     * @param pathname 文件目录
     * @return {File}
     */
    public static File createFile(String pathname) {
        pathname = path(pathname);
        String fileName = RegexpUtil.parseGroup(pathname, "[^\\/]+$", 0);
        return fileName == null ? createFolder(pathname) : new File(createFolder(pathname), RegexpUtil.parseGroup(pathname, "[^\\/]+$", 0));
    }

    public static File createFile(File parent, String fileName) {
        return new File(createFolder(parent), fileName);
    }

    public static boolean exists(String folderName) {
        return new File(folderName).exists();
    }

    public static String getMimeType(File file) {
        return MimeUtil2.getMostSpecificMimeType(MimeUtil.getMimeTypes(file)).toString();
    }

    public static String getMimeType(InputStream input) {
        return MimeUtil2.getMostSpecificMimeType(MimeUtil.getMimeTypes(input)).toString();
    }

    public static File[] listFolders(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            throw new RuntimeException("(目录不存在。)folder [" + folder + "]not exist。");
        }
        return folder.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    /**
     * 列出文件夹下面所有扩展名相同的文件名.jpg等
     *
     * @param folderName 文件名
     * @param extNames   扩展名
     * @return {File[]}
     */
    public static File[] listFiles(String folderName, final String... extNames) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            throw new RuntimeException("(目录不存在。)folder [" + folder + "]not exist。");
        }
        return folder.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (extNames.length == 0) {
                    return pathname.isFile();
                }
                return pathname.isFile() && ObjectUtil.indexOf(extNames, getExtension(pathname)) != -1;
            }
        });
    }

    /*
    public static void gbk2utf8(File file, String[] exts) throws IOException {
        if (file.isFile()) {
            for (String ext : exts)
                if (file.getAbsolutePath().toLowerCase().endsWith(ext)) {
                    String gbkContent = readFile(file);
                    byte[] utf8Content = gbk2utf8(gbkContent);
                    writeFile(utf8Content, file.getAbsolutePath());
                    break;
                }
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File _file : files)
                    gbk2utf8(_file, exts);
            }
        }
    }*/

    public static void compressionGZIP(String oldPath, String newPath) {
        try {
            FileInputStream fin = new FileInputStream(oldPath);

            createFolder(RegexpUtil.replace(newPath, "(([a-zA-Z0-9]|([(]|[)]|[ ]))+)[.]([a-zA-Z0-9]+)$", ""));
            System.out.println("创建文件 ：" + RegexpUtil.replace(newPath, "(([a-zA-Z0-9]|([(]|[)]|[ ]))+)[.]([a-zA-Z0-9]+)$", "") + "|" + newPath);
            FileOutputStream fout = new FileOutputStream(newPath);

            GZIPOutputStream gzout = new GZIPOutputStream(fout);
            byte[] buf = new byte[1024];
            int num;
            while ((num = fin.read(buf)) != -1) {
                gzout.write(buf, 0, num);
            }
            gzout.close();
            fout.close();
            fin.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void extractGzip(String fileUrl) {
        try {
            FileInputStream fin = new FileInputStream(fileUrl);
            GZIPInputStream gzin = new GZIPInputStream(fin);
            FileOutputStream fout = new FileOutputStream(RegexpUtil.replace(fileUrl, ".gz$", ""));
            byte[] buf = new byte[1024];
            int num;
            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }
            gzin.close();
            fout.close();
            fin.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static byte[] gbk2utf8(String chenese) throws UnsupportedEncodingException {
        char[] c = chenese.toCharArray();
        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = c[i];
            String word = Integer.toBinaryString(m);

            StringBuilder sb = new StringBuilder();
            int len = 16 - word.length();
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");

            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);

            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[(i * 3)] = bf[0];
            bf[1] = b1;
            fullByte[(i * 3 + 1)] = bf[1];
            bf[2] = b2;
            fullByte[(i * 3 + 2)] = bf[2];
        }

        return fullByte;
    }

    public static boolean moveFile(File sourceFile, File targetFile) {
        if (sourceFile.isFile()) {
            return moveOnlyFile(sourceFile, targetFile);
        }
        File[] files = sourceFile.listFiles();
        if (files != null) {
            for (File file : files) {
                String newName = targetFile.getAbsolutePath() + "/" + file.getName();
                moveFile(file, new File(newName));
            }
        }
        if (!sourceFile.delete())
            throw new RuntimeException("删除文件" + sourceFile.getAbsolutePath() + "失败");
        return true;
    }

    private static boolean moveOnlyFile(File sourceFile, File targetFile) {
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new RuntimeException("创建文件" + parentFile.getAbsolutePath() + "失败");
        if (targetFile.exists() && !targetFile.delete())
            throw new RuntimeException("删除文件" + targetFile.getAbsolutePath() + "失败");
        boolean flag = sourceFile.renameTo(targetFile);
        if (!flag) {
            try {
                copyFile(sourceFile, targetFile);
                if (sourceFile.exists())
                    System.out.println("delete file:" + sourceFile + ":" + sourceFile.delete());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        if (sourceFile.isFile()) {
            copyOnlyFile(sourceFile, targetFile);
        } else {
            File[] files = sourceFile.listFiles();
            if (files != null)
                for (File file : files) {
                    String newName = targetFile.getAbsolutePath() + "/" + file.getName();
                    copyFile(file, new File(newName));
                }
        }
    }

    private static void copyOnlyFile(File sourceFile, File targetFile) throws IOException {
        System.out.println("copy from:" + sourceFile);
        System.out.println("copy to:" + targetFile);
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new RuntimeException("创建文件" + parentFile.getAbsolutePath() + "失败");
        FileInputStream fis = new FileInputStream(sourceFile);
        FileOutputStream fos = new FileOutputStream(targetFile);
        StreamUtil.copyThenClose(fis, fos);
    }

    public static URL generate(URL url, File file) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buf.append("\n").append(line);
        }
        StreamUtil.closeQuietly(in);
        FileWriter fw = new FileWriter(file);
        fw.write(buf.toString());
        fw.flush();
        StreamUtil.closeQuietly(fw);
        return url;
    }

    public static void delFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                delOnlyFile(file);
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File _file : files) {
                        delFile(_file);
                    }
                }
                if (!file.delete()) {
                    logger.error("删除文件" + file.getAbsolutePath() + "失败");
                }
            }
        }
    }

    private static void delOnlyFile(File file) {
        if (file.exists() && !file.delete())
            logger.error("删除文件" + file.getAbsolutePath() + "失败");
    }

    public static void delFile(String filePath) {
        delFile(new File(filePath));
    }

    public static File tmp() throws IOException {
        return createFile(System.getProperty("java.io.tmpdir") + File.separator + GUIDKeyGenerator.getInstance().getGUID());
    }

    public static File tmp(InputStream in) throws IOException {
        File file = tmp();
        FileOutputStream out = new FileOutputStream(file);
        StreamUtil.copyThenClose(in, out);
        return file;
    }

    @Deprecated
    public static String loadResponseByUrl(String urlName) throws IOException {
        URL url = new URL(urlName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705)");
        connection.addRequestProperty("Cache-Control", "no-cache");
        connection.addRequestProperty("Accept", "*/*");
        connection.addRequestProperty("Connection", "Keep-Alive");
        connection.connect();
        connection.disconnect();
        return connection.getURL().toString();
    }

    public static void replaceInFolder(File file, String oldStr, String newStr) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) replaceInFolder(file1, oldStr, newStr);
        } else {
            String content = readFile(file);
            if ((file.getName().endsWith(".html")) && (content.contains(oldStr))) {
                writeFile(content, file.getAbsolutePath());
            }
        }
    }

    public static Date lastModified(String filePath) {
        File file = new File(filePath);
        return new Date(file.lastModified());
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    public static String getExtension(String fileName) {
        String[] ress = fileName.split("\\.");
        return ress.length < 2 ? "" : ress[(ress.length - 1)];
    }

    public static File writeFile(InputStream in, String filePath) throws IOException {
        File file = createFile(filePath);
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1)
            out.write(buffer);
        out.close();
        in.close();
        return file;
    }

    public void writeFile(OutputStream out, InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1)
            out.write(buffer);
        out.close();
        in.close();
    }

    @Deprecated
    public static byte[] getBytes(File attach) {
        return null;
    }

    public static ZipOutputStream compress(String _unZipFile, OutputStream zipOut) throws FileNotFoundException, IOException {
        File srcFile = new File(_unZipFile);
        DataInputStream dis = new DataInputStream(new FileInputStream(srcFile));
        ZipOutputStream zos = new ZipOutputStream(zipOut);
        zos.setMethod(ZipOutputStream.DEFLATED);
        ZipEntry ze = new ZipEntry(srcFile.getName());
        zos.putNextEntry(ze);
        DataOutputStream dos = new DataOutputStream(zos);

        byte[] buf = new byte[2048];
        int len;
        while ((len = dis.read(buf)) != -1) {
            dos.write(buf, 0, len);
        }
        dos.close();
        dis.close();
        return zos;
    }

    @SuppressWarnings("unchecked")
    public static void decompress(File file, UnZipCallBack callBack) {
        Hashtable<String, Integer> htSizes = new Hashtable<String, Integer>();
        try {
            ZipFile zf = new ZipFile(file);
            Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = e.nextElement();
                htSizes.put(ze.getName(), (int) ze.getSize());
            }
            zf.close();
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            ZipInputStream zis = new ZipInputStream(bis);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    continue;
                }
                int size = (int) ze.getSize();
                if (size == -1) {
                    size = htSizes.get(ze.getName());
                }
                byte[] b = new byte[size];
                int rb = 0;
                int chunk;
                while ((size - rb) > 0) {
                    chunk = zis.read(b, rb, size - rb);
                    if (chunk == -1) {
                        break;
                    }
                    rb += chunk;
                }
                callBack.execute(ze.getName(), new ByteArrayInputStream(b));
            }
            zis.closeEntry();
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static interface UnZipCallBack {

        public void execute(String fileName, InputStream stream);

    }

    public static String fileSize(long length) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int i = 0;
        while (length >= 1024 && i < 4) {
            length /= 1024;
            i++;
        }
        return Math.round(length) + units[i];
    }

}