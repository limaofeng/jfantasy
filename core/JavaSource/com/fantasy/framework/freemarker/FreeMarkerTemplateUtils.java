package com.fantasy.framework.freemarker;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StreamUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

public class FreeMarkerTemplateUtils {

    private final static Logger logger = LoggerFactory.getLogger(FreeMarkerTemplateUtils.class);

    /**
     * @param data 初始数据
     * @param t    模板对象
     * @param out  输出流
     */
    public static void writer(Map data, Template t, Writer out) {
        try {
            t.process(data, out);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            StreamUtil.closeQuietly(out);
        }
    }

    /**
     * @param data 初始数据
     * @param t    模板对象
     * @param out  输出流
     */
    public static void writer(Object data, Template t, Writer out) {
        try {
            Map<String, Object> rootMap = ObjectUtil.toMap(data);
            t.process(rootMap, out);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            StreamUtil.closeQuietly(out);
        }
    }

    public static void writer(TemplateModel model, Template t, Writer out) {
        try {
            t.process(model, out);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            StreamUtil.closeQuietly(out);
        }
    }

    public static void writer(TemplateModel model, Template t, OutputStream out) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(out, t.getEncoding()));
            t.process(model, writer);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                StreamUtil.closeQuietly(writer);
            }
        }
    }

    public static void writer(Object data, Template t, OutputStream out) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(out, t.getEncoding()));
            Map<String, Object> rootMap = ObjectUtil.toMap(data);
            t.process(rootMap, writer);
        } catch (TemplateException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                StreamUtil.closeQuietly(writer);
            }
        }
    }

    public static String processTemplateIntoString(Template t, Object model) {
        StringWriter out = new StringWriter();
        writer(model, t, out);
        return out.toString();
    }

}
