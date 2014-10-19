package com.fantasy.framework.util.common;

import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ScriptHelp {
	private static final String ENCODE = "UTF-8";
	public static List<String> fileList = new ArrayList<String>();

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
		if (isCompress)
			writeFile(compressJS(content.toString()), toFile);
		else
			writeFile(content.toString(), toFile);
	}

	public static String compressJS(String content) {
		content = content.replaceAll("([^\"])\\/\\*([^\\*^\\/]*|[\\*^\\/*]*[^\\**\\/]*)*\\*\\/", "$1").replaceAll("\\/\\/[^\\n]*", "");

		if (content.indexOf("/*") == 0)
			content = content.substring(content.indexOf("*/") + 2, content.length());
		content = content.replaceAll("\\s{2,}", " ").replaceAll("\r\n", "").replaceAll("\n", "");

		return content;
	}

	public static void writeFile(String content, String comspec) {
		try {
			FileOutputStream fos = new FileOutputStream(comspec);
			Writer out = new OutputStreamWriter(fos, ENCODE);
			out.write(content);
			System.out.println("成功输出文件：" + comspec);
			out.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("写文件操作出错！");
			e.printStackTrace();
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
			e.printStackTrace();
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
            for (File aT : ts != null ? ts : new File[0]) listFile(path, aT, suffix, isdepth);
		} else {
			addFilePath(f, suffix);
		}
		return fileList;
	}

	public static List<String> addFilePath(File f, String suffix) {
		String filePath = f.getAbsolutePath().replaceAll("\\\\", "/");
		if (suffix != null) {
			int begIndex = filePath.lastIndexOf(".");
			String tempsuffix = "";

			if (begIndex != -1) {
				tempsuffix = filePath.substring(begIndex + 1, filePath.length());
			}
			if (tempsuffix.equals(suffix))
				fileList.add(filePath);
		} else {
			fileList.add(filePath);
		}
		return fileList;
	}

	public static void main(String[] args) {
		final String dir = "C:/workspace/fantasy_projects/itsm/WebContent/";
		String commonJs = FileUtil.readFile(dir + "static/js/common.js");
		// System.out.println( commonJs);
		// 去掉注释掉的js
		commonJs = RegexpUtil.replace(commonJs, "//jQuery\\.include\\([^\\n]*\\);", new RegexpUtil.AbstractReplaceCallBack() {

			@Override
			public String doReplace(String text, int index, Matcher matcher) {
				return "";
			}

		});
		commonJs = RegexpUtil.replace(commonJs, "jQuery\\.include\\(\\[([^\\n]*)\\]\\);", new RegexpUtil.AbstractReplaceCallBack() {

			@Override
			public String doReplace(String text, int index, Matcher matcher) {
				StringBuilder newJs = new StringBuilder();
				System.out.println("=>" + index);
				for (String js : $(1).split(",")) {
					js = js.replaceAll("^'|'$", "");
					if (js.endsWith(".css")) {
						continue;
					}
					newJs.append(FileUtil.readFile(dir + js)).append("\n");
				}
				return newJs.toString().replaceAll("\\\\", "\\u005C").replaceAll("\\$", "\\u0024");
			}

		});
		commonJs = RegexpUtil.replace(commonJs, "jQuery\\.include\\(([^\\n]*)\\);", new RegexpUtil.AbstractReplaceCallBack() {
			
			@Override
			public String doReplace(String text, int index, Matcher matcher) {
				String js = $(1).replaceAll("^'|'$", "");
				if(!js.endsWith(".css")){
					if(StringUtil.isBlank(js)){
						System.out.println(js  + "|" + text);
					}
					return FileUtil.readFile(dir + js).replaceAll("\\$","\\u0024").replaceAll("\\\\", "\\u005C") + "\n";
				}
				return text;
			}
			
		});
		FileUtil.writeFile(commonJs.replaceAll("u0024", "\\$").replaceAll("u005C", "\\\\"), dir + "static/js/common.min.js");

	}
}