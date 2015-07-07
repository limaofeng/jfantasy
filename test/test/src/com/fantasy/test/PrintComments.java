package com.fantasy.test;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileItemSelector;
import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.util.common.PathUtil;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import org.junit.Test;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintComments {

    private FileManager fileManager;
    private static final String version = "3.3.5";

    {
        String classesPath = PathUtil.classes();
        fileManager = new LocalFileManager(classesPath.substring(0, classesPath.indexOf("framework/test") + "framework/".length()));
    }

    @Test
    public void comments() throws IOException {
        OutputStream output = fileManager.writeFile("/doc/rest-api-"+version+".js");
        for (FileItem fileItem : fileManager.listFiles(new FileItemSelector() {
            @Override
            public boolean includeFile(FileItem fileItem) {
                return fileItem.isDirectory() || (fileItem.getAbsolutePath().contains("/JavaSource/") && "java".equals(FileUtil.getExtension(fileItem.getAbsolutePath())));
            }

            @Override
            public boolean traverseDescendents(FileItem fileItem) {
                return true;
            }
        })) {
            if (!fileItem.isDirectory()) {
                reader(fileItem, output);
            }
        }
        StreamUtil.closeQuietly(output);
    }

    public static void reader(FileItem fileItem, OutputStream output) throws IOException {
        InputStream input = fileItem.getInputStream();
        Reader reader = new InputStreamReader(input);
        try {
            BufferedReader breader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            try {
                String lineSeparator =  System.getProperty("line.separator");
                String temp;
                /**
                 * 读取文件内容，并将读取的每一行后都不加\n
                 * 其目的是为了在解析双反斜杠（//）注释时做注释中止符
                 */
                while ((temp = breader.readLine()) != null) {
                    sb.append(temp);
                    sb.append(lineSeparator);
                }
                String src = sb.toString();
                /**
                 * 1、做/* 注释的正则匹配
                 *

                 *     通过渐进法做注释的正则匹配，因为/*注释总是成对出现
                 * 当匹配到一个/*时总会在接下来的内容中会首先匹配到"*\\/",
                 * 因此在获取对应的"*\\/"注释时只需要从当前匹配的/*开始即可，
                 * 下一次匹配时只需要从上一次匹配的结尾开始即可
                 * （这样对于大文本可以节省匹配效率）——
                 * 这就是渐进匹配法
                 *

                 * */
                Pattern leftpattern = Pattern.compile("/\\*");
                Matcher leftmatcher = leftpattern.matcher(src);
                Pattern rightpattern = Pattern.compile("\\*/");
                Matcher rightmatcher = rightpattern.matcher(src);
                sb = new StringBuilder();
                /**
                 * begin 变量用来做渐进匹配的游标 {@value}
                 * 初始值为文件开头
                 * **/
                int begin = 0;
                while (leftmatcher.find(begin)) {
                    if (!rightmatcher.find(leftmatcher.start())) {
                        return;
                    }
                    if ((rightmatcher.end() - leftmatcher.start()) <= 4) {
                        return;
                    }
                    sb.append(src.substring(leftmatcher.start(), rightmatcher.end()));
                    /** 为输出时格式的美观 **/
                    sb.append(lineSeparator);
                    begin = rightmatcher.end();
                }
                if (sb.length() != 0) {
                    output.write(("//" + fileItem.getAbsolutePath() + lineSeparator).getBytes());
                    output.write(sb.toString().getBytes());
                }
            } catch (IOException e) {
                System.err.println("文件读取失败");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                breader.close();
                reader.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("文件读取失败");
        }
    }

}