package org.jfantasy.framework.util;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

public class HandlebarsTemplateUtils {

    private static Handlebars handlebars = new Handlebars();

    private final static Log LOG = LogFactory.getLog(HandlebarsTemplateUtils.class);

    static {
        handlebars.registerHelper("format", new Helper<Date>() {
            @Override
            public CharSequence apply(Date context, Options options) throws IOException {
                return DateUtil.format(context, (String) options.params[0]);
            }
        });
        handlebars.registerHelper("URLEncode", new Helper<String>() {
            @Override
            public CharSequence apply(String context, Options options) throws IOException {
                return URLEncoder.encode(context, (String) options.params[0]);
            }
        });
    }

    public static String processTemplateIntoString(String inputTemplate, Object model) {
        if(StringUtil.isBlank(inputTemplate)){
            return null;
        }
        try {
            return handlebars.compileInline(inputTemplate).apply(model);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public static String processTemplateIntoString(Template template, Object model) throws IOException {
        return template.apply(model);
    }

    public static void writer(Object model, String template, ByteArrayOutputStream out) {

    }

    /**
     * 从文件加载模板
     *
     * @param path 文件路径
     * @return Template
     * @throws IOException
     */
    public static Template template(String path) throws IOException {
        return handlebars.compile(path);
    }

}