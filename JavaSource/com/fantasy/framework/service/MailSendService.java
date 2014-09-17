package com.fantasy.framework.service;

import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.regexp.RegexpCst;
import com.fantasy.framework.util.regexp.RegexpUtil;
import freemarker.template.Configuration;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Email 工具类
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-6-4 下午04:07:27
 */
public class MailSendService implements InitializingBean {

    /**
     * 用于将:李茂峰<limaofeng@msn.com>格式的字符串解开
     */
    private static final Pattern parseEmail = RegexpUtil.getPattern("^([\\S\\s]+)?\\<([\\S\\s]+)\\>");

    private Configuration configuration;
    private String hostname;
    private String from;
    private String displayName;
    private String username;
    private String password;
    private String charset = "utf-8";

    private final static Log logger = LogFactory.getLog(MailSendService.class);

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.hostname, "Property 'hostname' is required");
        Assert.notNull(this.username, "Property 'username' is required");
        Assert.notNull(this.password, "Property 'password' is required");
        Assert.notNull(this.from, "Property 'from' is required");
        Assert.notNull(this.displayName, "Property 'displayName' is required");
    }

    protected Email createEmail(EmailType type, String... tos) throws EmailException {
        Email email = EmailType.createEmail(type);
        if (email == null) {
            throw new EmailException("创建" + Email.class.getName() + "失败！[" + type + "]");
        }
        // 设置邮箱服务器地址
        email.setHostName(hostname);
        // 设置字符集
        email.setCharset(charset);
        // 发送人的邮箱
        email.setFrom(this.from, this.displayName);
        // 设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
        email.setAuthentication(this.username, this.password);
        // 解析字符串将中间有[,; \t\n]按多个发件人处理
        List<String> toEmails = new ArrayList<String>();
        for (String to : tos) {
            ObjectUtil.join(toEmails, Arrays.asList(StringUtils.tokenizeToStringArray(to, ",; \t\n")));
        }
        // 收件人的邮箱
        for (String to : toEmails) {
            if (RegexpUtil.isMatch(to, parseEmail)) {
                String toDisplayName = RegexpUtil.parseGroup(to, parseEmail, 1);
                String toEmail = RegexpUtil.parseGroup(to, parseEmail, 2);
                if (validateEmail(toEmail)) {
                    email.addTo(toEmail, toDisplayName);
                }
            } else {
                if (validateEmail(to))
                    email.addTo(to);
            }
        }
        if (email.getToAddresses().isEmpty()) {
            throw new EmailException("收件人列表为空:" + toEmails);
        }
        if (logger.isDebugEnabled()) {
            StringBuffer debug = new StringBuffer("\r\n邮件发送信息如下:");
            debug.append("\r\nEmailType:").append(type.toString());
            debug.append("\r\nCharset:").append(this.charset);
            debug.append("\r\nHost:").append(this.hostname);
            debug.append("\r\nAuthentication:").append(this.username).append("/").append(this.password);
            debug.append("\r\nFrom:").append(this.from);
            debug.append("\r\nDisplayName:").append(this.displayName);
            debug.append("\r\nTos:").append(toEmails);
            logger.debug(debug);
        }
        return email;
    }

    private boolean validateEmail(String email) {
        return RegexpUtil.isMatch(email, RegexpCst.VALIDATOR_EMAIL);
    }

    /**
     * 以普通文本的格式发送Email
     *
     * @param to      发送给谁，对应的email
     * @param title   邮件主题
     * @param message 邮件内容 支持普通文本
     */
    public void sendSimpleEmail(String title, String message, String... to) {
        try {
            Email email = this.createEmail(EmailType.simple, to);
            email.setSubject(title);
            email.setMsg(message);
            send(email);
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 发送 邮件 给某人
     *
     * @param title    邮件标题
     * @param template 模板名称
     * @param model    数据
     * @param to       发送到的人
     */
    public void sendSimpleEmail(String title, String template, Object model, String... to) {
        try {
            Email email = this.createEmail(EmailType.simple, to);
            email.setSubject(title);
            email.setMsg(FreeMarkerTemplateUtils.processTemplateIntoString(this.configuration.getTemplate(template, this.charset), model));
            send(email);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 支持HTML脚本的格式发送Email
     *
     * @param to      发送给谁，对应的email
     * @param title   邮件标题
     * @param message 邮件内容 支持HTML脚本
     */
    public void sendHtmlEmail(String title, String message, String... to) {
        try {
            HtmlEmail email = (HtmlEmail) this.createEmail(EmailType.html, to);
            email.setSubject(title);
            email.setContent(message,EmailConstants.TEXT_HTML);
            send(email);
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param title    标题
     * @param template 模板路径
     * @param model    数据
     * @param to       收件人
     */
    public void sendHtmlEmail(String title, String template, Object model, String... to) {
        try {
            HtmlEmail email = (HtmlEmail) this.createEmail(EmailType.html, to);
            email.setSubject(title);
            email.setContent(FreeMarkerTemplateUtils.processTemplateIntoString(this.configuration.getTemplate(template, this.charset), model),EmailConstants.TEXT_HTML);
            send(email);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 带附件的邮件
     *
     * @param title    标题
     * @param template 模板路径
     * @param model    数据
     * @param attachs  附件信息
     * @param to       收件人
     */
    public void sendHtmlEmail(String title, String template, Object model, EmailAttachment[] attachs, String... to) {
        try {
            HtmlEmail email = (HtmlEmail) this.createEmail(EmailType.html, to);
            email.setSubject(title);
            email.setContent(FreeMarkerTemplateUtils.processTemplateIntoString(this.configuration.getTemplate(template, this.charset), model),EmailConstants.TEXT_HTML);
            // 添加附件
            if (attachs != null && attachs.length > 0) {
                for (EmailAttachment attachment : attachs) {
                    email.attach(attachment);
                }
            }
            send(email);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 带附件的邮件
     *
     * @param title    标题
     * @param template 模板路径
     * @param model    数据
     * @param attachs  附件信息
     * @param to       收件人
     */
    public void sendHtmlEmail(String title, String template, Object model, Attachment[] attachs, String... to) {
        try {
            HtmlEmail email = (HtmlEmail) this.createEmail(EmailType.html, to);
            email.setSubject(title);
            email.setContent(FreeMarkerTemplateUtils.processTemplateIntoString(this.configuration.getTemplate(template, this.charset), model),EmailConstants.TEXT_HTML);
            if (attachs != null && attachs.length > 0) {
                for (Attachment attachment : attachs) {// 添加流形式的附件
                    if (attachment.getInputStream() != null) {
                        email.attach(new ByteArrayDataSource(attachment.getInputStream(), attachment.getContentType()), MimeUtility.encodeText(attachment.getName()), attachment.getDescription());
                    } else {// freemarker 模板
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        FreeMarkerTemplateUtils.writer(attachment.getModel(), this.configuration.getTemplate(attachment.getTemplate(), this.charset), out);
                        email.attach(new ByteArrayDataSource(out.toByteArray(), attachment.getContentType()), MimeUtility.encodeText(attachment.getName()), attachment.getDescription());
                    }
                }
            }
            send(email);
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void send(Email email) {
        getExecutor().execute(new SendEmailTask(email));
    }

    private Executor executor;

    private Executor getExecutor() {
        if (executor != null)
            return executor;
        executor = SpringContextUtil.getBean("spring.executor", Executor.class);
        if(executor == null){
            executor = Executors.newFixedThreadPool(5);
        }
        return executor;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private static enum EmailType {
        simple, html;

        public static Email createEmail(EmailType type) {
            switch (type) {
                case html:
                    return new HtmlEmail();
                case simple:
                    return new SimpleEmail();
                default:
                    return null;
            }
        }
    }

    public static class Attachment {
        /**
         * 文件名称
         */
        private String name;
        /**
         * 文件类型
         */
        private String contentType;
        /**
         * 文件描述
         */
        private String description;

        private InputStream inputStream;
        /**
         * freemarker 模板路径
         */
        private String template;
        /**
         * freemarker 数据
         */
        private Object model;

        public Attachment(InputStream inputStream, String contentType, String name, String description) {
            this.inputStream = inputStream;
            this.contentType = contentType;
            this.name = name;
            this.description = description;
        }

        public Attachment(String template, Object model, String contentType, String name, String description) {
            this.template = template;
            this.model = model;
            this.contentType = contentType;
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getContentType() {
            return contentType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public String getTemplate() {
            return template;
        }

        public Object getModel() {
            return model;
        }

    }

    private static class SendEmailTask implements Runnable {

        private Email email;

        private SendEmailTask(Email email) {
            this.email = email;
        }

        @SuppressWarnings("static-access")
        public void run() {
            try {
                // 邮件发送失败重试5次
                boolean success = false;
                int num = 0;
                do {
                    try {
                        String code = email.send();
                        if (logger.isDebugEnabled()) {
                            logger.debug("\r\n邮件<<" + email.getSubject() + ">>成功发送至:" + email.getToAddresses() + "\r\n返回代码:" + code);
                        }
                        success = true;
                    } catch (EmailException e) {
                        logger.error("发送次数:" + num + "," + e.getMessage(), e);
                        Thread.currentThread().sleep(2000);
                    }
                } while (!success && num++ < 5);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
