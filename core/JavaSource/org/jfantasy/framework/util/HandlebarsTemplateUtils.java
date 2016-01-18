package org.jfantasy.framework.util;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;

public class HandlebarsTemplateUtils {

    private static Handlebars handlebars = new Handlebars();

    public static String processTemplateIntoString(String template, Object model) throws IOException {
        return null;
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
