package com.fantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScriptHelp {

    private final static Log LOG = LogFactory.getLog(ScriptHelp.class);

    private static final String ENCODE = "UTF-8";
    public static final List<String> FILE_LIST = new ArrayList<String>();

    public static void batchCompressJS(String baseScriptPath) {
        List<String> fileList = getListFiles(baseScriptPath, "js", true);

        for (String fromFile : fileList) {
            String toFile = replaceLast(fromFile, ".js", ".min").concat(".js");
            compressSingleJS(fromFile, toFile);
        }
    }

    static int lastIndexOf(String source, String str) {
        int i = 0;
        int indexof = 0;
        while (indexof >= 0) {
            i = indexof + 1;
            indexof = source.indexOf(str, i);
        }
        return i;
    }

    public static String replaceLast(String source, String regex, String prefix) {
        int index = lastIndexOf(source, regex);
        if (index <= 0) {
            return source;
        }
        return source.substring(0, index - 1) + prefix + source.substring(index - 1 + regex.length());
    }

    public static void compressSingleJS(String fromFile, String toFile) {
        String content = readFile(fromFile);
        writeFile(compressJS(content), toFile);
    }

    public static void mergeJS(List<String> fileList, String toFile, Boolean isCompress) {
        StringBuilder content = new StringBuilder();
        for (String fromFile : fileList) {
            content.append(readFile(fromFile));
        }
        if (isCompress){
            writeFile(compressJS(content.toString()), toFile);
        } else{
            writeFile(content.toString(), toFile);
        }
    }

    public static String compressJS(String content) {
        content = content.replaceAll("([^\"])\\/\\*([^\\*^\\/]*|[\\*^\\/*]*[^\\**\\/]*)*\\*\\/", "$1").replaceAll("\\/\\/[^\\n]*", "");

        if (content.indexOf("/*") == 0){
            content = content.substring(content.indexOf("*/") + 2, content.length());
        }
        content = content.replaceAll("\\s{2,}", " ").replaceAll("\r\n", "").replaceAll("\n", "");
        return content;
    }

    public static void writeFile(String content, String comspec) {
        try {
            FileOutputStream fos = new FileOutputStream(comspec);
            Writer out = new OutputStreamWriter(fos, ENCODE);
            out.write(content);
            LOG.debug("成功输出文件：" + comspec);
            out.close();
            fos.close();
        } catch (IOException e) {
            LOG.error("写文件操作出错:" + e.getMessage(), e);
        }
    }

    public static String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(filePath);
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODE);
            BufferedReader reader = new BufferedReader(read);
            String s = reader.readLine();
            while (s != null) {
                sb.append(s);
                sb.append("\r\n");
                s = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    public static List<String> getListFiles(String path, String suffix, boolean isdepth) {
        File file = new File(path);
        return listFile(path, file, suffix, isdepth);
    }

    public static List<String> listFile(String path, File f, String suffix, boolean isdepth) {
        String temp = path.replaceAll("/", "\\\\");
        if (((f.isDirectory()) && (isdepth)) || (temp.equals(f.getAbsolutePath()))) {
            File[] ts = f.listFiles();
            for (File aT : ts != null ? ts : new File[0]){
                listFile(path, aT, suffix, isdepth);
            }
        } else {
            addFilePath(f, suffix);
        }
        return FILE_LIST;
    }

    public static List<String> addFilePath(File f, String suffix) {
        String filePath = f.getAbsolutePath().replaceAll("\\\\", "/");
        if (suffix != null) {
            int begIndex = filePath.lastIndexOf(".");
            String tempsuffix = "";
            if (begIndex != -1) {
                tempsuffix = filePath.substring(begIndex + 1, filePath.length());
            }
            if (tempsuffix.equals(suffix)){
                FILE_LIST.add(filePath);
            }
        } else {
            FILE_LIST.add(filePath);
        }
        return FILE_LIST;
    }

}