package com.fantasy.system.util;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.UploadFileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.htmlcleaner.HtmlCleanerUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.User;
import com.fantasy.security.userdetails.AdminUser;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.bean.Setting;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.DataDictionaryService;
import com.fantasy.system.service.WebsiteService;
import org.htmlcleaner.TagNode;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

public class SettingUtil {

    private static final Map<String, String> constants = new ConcurrentHashMap<String, String>();

    private static ThreadLocal<Website> threadLocal = new ThreadLocal<Website>();

    public static void add(String key, String value) {
        constants.put(key, value);
    }

    public static String remove(String key) {
        return constants.remove(key);
    }

    public static String get(String key) {
        if (!constants.containsKey(key)) {
            return null;
        }
        return constants.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String type, String key, Class<T> clazz) {
        String value = null;
        if ("website".equalsIgnoreCase(type)) {
            value = getValue(key);
        }
        if (StringUtil.isBlank(value)) {
            value = get(type, key);
        }
        if (StringUtil.isNotBlank(value)) {
            return ClassUtil.newInstance(clazz, value);
        }
        return null;
    }

    public static String get(String type, String key) {
        if ("website".equalsIgnoreCase(type)) {
            String value = getValue(key);
            if (StringUtil.isNotBlank(value)) {
                return value;
            }
        }
        DataDictionary config = DataDictionaryService.get(type, key);
        if (config != null) {
            return config.getName();
        }
        return get(key);
    }

    public static String get(String type, String key, String defaultValue) {
        if ("website".equalsIgnoreCase(type)) {
            String value = getValue(key);
            if (StringUtil.isNotBlank(value)) {
                return value;
            }
        }
        DataDictionary config = DataDictionaryService.get(type, key);
        if (config != null) {
            return config.getName();
        }
        return StringUtil.defaultValue(get(key), defaultValue);
    }

    public static void initialize(ServletContext context) {
        constants.put("contextPath", context.getContextPath());
    }

    public static String getText(String message, final String configType) {
        return RegexpUtil.replace(message, "\\{([a-zA-Z]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return get(configType, $(1));
            }
        });
    }

    public static String getValue(String key) {
        Setting setting = ObjectUtil.find(current().getSettings(), "key", key);
        return setting == null ? get(key) : setting.getValue();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(String key, Class<T> clazz) {
        return ClassUtil.newInstance(clazz, getValue(key));
    }

    public static FileManager getDefaultFileManager() {
        return null;
    }

    public static FileManager getDefaultUploadFileManager() {
        String[] fmids = FileManagerFactory.getFileManagerIds();
        for (String fmid : fmids) {
            FileManager fileManager = FileManagerFactory.getInstance().getFileManager(fmid);
            if (!(fileManager instanceof UploadFileManager)) {
                continue;
            }
            return fileManager;
        }
        return null;
    }

    public static Long getRootMenuId() {
        return current().getRootMenu().getId();
    }

    public static String toHtml(String html) {
        return toHtml(html, "website");
    }

    public static String toHtml(String html, String configType) {
        return SettingUtil.getText(html, configType);
    }

    public static String getServerUrl() {
        return getValue("serverUrl");
    }

    public static String cleanFileManagerPath(String html, String configType) {
        if (StringUtil.isBlank(html))
            return html;
        html = SettingUtil.getText(html, configType);
        if (!(html.contains("<a") || html.contains("<img"))) {
            return html;
        }
        TagNode tagNode = HtmlCleanerUtil.htmlCleaner(html);
        for (TagNode node : HtmlCleanerUtil.findTagNodes(tagNode, "//a")) {
            node.addAttribute("href", RegexpUtil.replace(node.getAttributeByName("href"), "(/[a-zA-Z0-9_]+:)", ""));
        }
        for (TagNode node : HtmlCleanerUtil.findTagNodes(tagNode, "//img")) {
            node.addAttribute("src", RegexpUtil.replace(node.getAttributeByName("src"), "(/[a-zA-Z0-9_]+:)", ""));
        }
        html = HtmlCleanerUtil.getAsString(HtmlCleanerUtil.findFristTagNode(tagNode, "//body"));
        html = RegexpUtil.replace(html, "^<[?]xml version=\"1.0\" encoding=\"[a-zA-Z0-9_-]+\"[?]>\\n", "");
        html = RegexpUtil.replace(html, "^<body>", "");
        html = RegexpUtil.replace(html, "</body>$", "");
        return html;
    }

    public static Long getWebsiteId(){
        return current().getId();
    }

    private static Website current() {
        User user = SpringSecurityUtils.getCurrentUser(AdminUser.class).getUser();
        return SpringContextUtil.getBeanByType(WebsiteService.class).findUniqueByKey(user.getWebsite().getKey());
    }

}
