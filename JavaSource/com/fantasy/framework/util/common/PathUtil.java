package com.fantasy.framework.util.common;

import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PathUtil {

    private static final Log logger = LogFactory.getLog(PathUtil.class);

    private static String WEBCLASSES_PATH = null;
    private static String WEBINF_PATH = null;
    private static String WEBROOT_PATH = null;
    private static String WEBLIB_PATH;

    static {
        try {
            // TODO 此处与DateUtil获取资源文件的地方需要修改
            String  webAppRootKey = PropertiesHelper.load("props/application.properties").getProperty("webAppRootKey", "webapp.root");
            PathUtil.WEBROOT_PATH = System.getProperty(webAppRootKey);
            if (StringUtil.isNull(PathUtil.WEBROOT_PATH))
                throw new Exception("根据webAppRootKey[" + webAppRootKey + "]获取WebRoot路径失败");
            PathUtil.WEBINF_PATH = PathUtil.WEBROOT_PATH + "WEB-INF" + File.separator;
            PathUtil.WEBCLASSES_PATH = PathUtil.WEBINF_PATH + "classes" + File.separator;
            PathUtil.WEBLIB_PATH = PathUtil.webinf() + "lib" + File.separator;
        } catch (Exception e) {// TODO 待完善
            logger.error(e.getMessage());
            logger.debug("改用 Thread.currentThread().getContextClassLoader().getResource(\"/\") 方式获取WebRoot路径");
            URL url = Thread.currentThread().getContextClassLoader().getResource("/");
            if (url == null) {
                url = PathUtil.class.getResource("/");
            }
            PathUtil.WEBCLASSES_PATH = RegexpUtil.replace(url.getPath(), "[\\/]$", "");
            if (WEBCLASSES_PATH.indexOf("WEB-INF") > 0) {
                File classFile = new File(PathUtil.WEBCLASSES_PATH);
                PathUtil.WEBINF_PATH = RegexpUtil.replace(classFile.getParentFile().getPath(), "[\\/]$", "");
                PathUtil.WEBROOT_PATH = RegexpUtil.replace(classFile.getParentFile().getParentFile().getPath(), "[\\/]$", "");
            }
            if (StringUtil.isBlank(WEBROOT_PATH) || StringUtil.isBlank(WEBINF_PATH) || "/".equals(WEBCLASSES_PATH)) {
                logger.debug("改用  PathUtil.getClassLocationURL(PathUtil.class) 方式获取WebRoot路径");
                url = PathUtil.getClassLocationURL(PathUtil.class);
                if ("jar".equalsIgnoreCase(url.getProtocol())) {
                    String path = PathUtil.getPathFromClass(PathUtil.class);
                    PathUtil.WEBROOT_PATH = RegexpUtil.replace(path.substring(0, path.lastIndexOf("WEB-INF")), "[\\/]$", "");
                    PathUtil.WEBINF_PATH = PathUtil.WEBROOT_PATH + "/WEB-INF";
                    PathUtil.WEBCLASSES_PATH = PathUtil.WEBINF_PATH + "/classes";
                }
            }
            PathUtil.WEBROOT_PATH = StringUtil.defaultValue(PathUtil.WEBROOT_PATH,"").replaceAll("\\" + File.separator, "/");
            PathUtil.WEBINF_PATH = StringUtil.defaultValue(PathUtil.WEBINF_PATH,"").replaceAll("\\" + File.separator, "/");
            PathUtil.WEBCLASSES_PATH = PathUtil.WEBCLASSES_PATH.replaceAll("\\" + File.separator, "/");
        }
        if (logger.isDebugEnabled()) {
            StringBuffer debug = new StringBuffer("\r\nPathUtil获取的路径信息如下:");
            debug.append("\r\nwebclasses:").append(WEBCLASSES_PATH);
            debug.append("\r\nwebinf:").append(WEBINF_PATH);
            debug.append("\r\nwebroot:").append(WEBROOT_PATH);
            logger.debug(debug);
        }
    }

    public static String getFileName(String path) {
        String[] ress = path.split("/");
        return ress[(ress.length - 1)];
    }

    public static String getFilePath(String path, String fileName) {
        return path.replaceAll("/" + fileName, "").replaceFirst("/", "");
    }

    public static String getExtension(String uri) {
        String[] ress = uri.split("\\.");
        return ress.length < 2 ? "" : ress[(ress.length - 1)];
    }

    public static String classes() {
        return WEBCLASSES_PATH;
    }

    public static String webinf() {
        return WEBINF_PATH;
    }

    public static String root() {
        return WEBROOT_PATH;
    }

    public static String lib(){
        return WEBLIB_PATH;
    }

    public static URL getURL(String path) {
        try {
            return new URL("file://" + PathUtil.classes() + path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getPathFromClass(Class<?> cls) {
        String path = null;
        if (cls == null) {
            throw new NullPointerException();
        }
        URL url = getClassLocationURL(cls);
        if (url != null) {
            path = url.getPath();
            if ("jar".equalsIgnoreCase(url.getProtocol())) {
                try {
                    path = new URL(path).getPath();
                } catch (MalformedURLException ignored) {
                }
                int location = path.indexOf("!/");
                if (location != -1) {
                    path = path.substring(0, location);
                }
            }
            File file = new File(path);
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 获取 Class 相对文件路径
     *
     * @param relatedPath 文件路径
     * @param cls         相对class
     * @return 文件路径
     * @throws IOException
     */
    @Deprecated
    public static String getFullPathRelateClass(String relatedPath, Class<?> cls) throws IOException {
        if (relatedPath == null) {
            throw new NullPointerException();
        }
        String path;
        String clsPath = getPathFromClass(cls);
        File clsFile = new File(clsPath);
        String tempPath = clsFile.getParent() + File.separator + relatedPath;
        File file = new File(tempPath);
        path = file.getCanonicalPath();
        return path;
    }

    public static URL getClassLocationURL(Class<?> cls) {
        if (cls == null)
            throw new IllegalArgumentException("null input: cls");
        URL result = null;
        String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        ProtectionDomain pd = cls.getProtectionDomain();
        if (pd != null) {
            CodeSource cs = pd.getCodeSource();
            if (cs != null)
                result = cs.getLocation();
            if ((result != null) && ("file".equals(result.getProtocol())))
                try {
                    if ((result.toExternalForm().endsWith(".jar")) || (result.toExternalForm().endsWith(".zip")))
                        result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
                    else if (new File(result.getFile()).isDirectory())
                        result = new URL(result, clsAsResource);
                } catch (MalformedURLException ignored) {
                }
        }
        if (result == null) {
            ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader.getSystemResource(clsAsResource);
        }
        return result;
    }

    public static URI getClassLocationURI(Class<?> cls) {
        try {
            URL url = getClassLocationURL(cls);
            return new URI(url.toString().replaceAll(" ", "%20"));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Deprecated
    public static String getFilePath(String path) {
        StringBuilder result = new StringBuilder();
        if (path.endsWith(".xml"))
            result.append(classes()).append("/").append(path.replace(".xml", "").replace('.', '/').concat(".xml"));
        else if (path.endsWith(".class"))
            result.append(classes()).append("/").append(path.replace(".class", "").replace('.', '/').concat(".class"));
        else
            result.append(root()).append("/").append(path);
        return result.toString();
    }

    @Deprecated
    public static String getClassFilePath(Class<?> classes) {
        return classes.getName().replaceAll("\\.", "/").concat(".class");
    }

    public static String[] getPackagePath(String packages) {
        if (packages == null)
            return null;
        List<String> pathPrefixes = new ArrayList<String>();
        String pathPrefix;
        for (StringTokenizer st = new StringTokenizer(packages, ", \n\t"); st.hasMoreTokens(); pathPrefixes.add(pathPrefix)) {
            pathPrefix = st.nextToken().replace('.', '/');
            if (!pathPrefix.endsWith("/"))
                pathPrefix = pathPrefix + "/";
        }
        return pathPrefixes.toArray(new String[pathPrefixes.size()]);
    }

    public static URL getTmpdir() {
        try {
            return new URL("file:///" + System.getProperty("java.io.tmpdir"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static URL getContextClassLoaderResource(String url) {
        return Thread.currentThread().getContextClassLoader().getResource(url);
    }

}