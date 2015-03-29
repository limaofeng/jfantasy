package com.fantasy.framework.struts2.core.result;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class JSONResult implements Result {

    private static final long serialVersionUID = 8624350183189931165L;

    private static final Logger LOG = LoggerFactory.getLogger(JSONResult.class);

    private String defaultEncoding = "ISO-8859-1";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    //    private List<Pattern> includeProperties;
//    private List<Pattern> excludeProperties;
    private String root;
    private boolean enableGZIP = false;
    private boolean noCache = false;
    private int statusCode;
    private int errorCode;
    private String callbackParameter;
    private String contentType;

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String val) {
        this.defaultEncoding = val;
    }

    public void execute(ActionInvocation invocation) throws Exception {
        ActionContext actionContext = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);

        Object action = invocation.getAction();
        if (action instanceof ValidationAware) {
            ValidationAware aware = (ValidationAware) action;
            if (aware.hasActionErrors() || aware.hasFieldErrors()) {
                this.setStatusCode(403);
            }
        }

        try {
            Object rootObject;
            rootObject = readRootObject(invocation);
            writeToResponse(response, createJSONString(request, rootObject), enableGzip(request));
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @SuppressWarnings("unchecked")
    protected Object readRootObject(ActionInvocation invocation) {
        Object rootObject = findRootObject(invocation);
        Map<String, Object> rootMap = ClassUtil.isMap(rootObject) ? (Map<String, Object>) rootObject : ObjectUtil.toMap(rootObject);
        for (Map.Entry<String, Object> entry : rootMap.entrySet()) {
            if (RegexpUtil.find(entry.getKey(), "^fantasy\\.", "^com\\.", "^struts\\.", ".*Filter\\..*", "^_", "^org\\.")) {
                rootMap.remove(entry.getKey());
            }
        }
        return rootMap.containsKey(ActionSupport.ROOT) ? rootMap.get(ActionSupport.ROOT) : rootMap;
    }

    protected Object findRootObject(ActionInvocation invocation) {
        Object rootObject;
        if (this.root != null) {
            ValueStack stack = invocation.getStack();
            rootObject = stack.findValue(root);
        } else {
            rootObject = invocation.getStack().peek(); // model overrides action
        }
        return rootObject;
    }

    protected String createJSONString(HttpServletRequest request, Object rootObject) {
        // TODO excludeProperties, includeProperties, ignoreHierarchy, enumAsBean, excludeNullProperties 不能使用
        // JSONUtil.serialize(rootObject, excludeProperties, includeProperties, ignoreHierarchy, enumAsBean, excludeNullProperties);
        return addCallbackIfApplicable(request, JSON.text().serialize(rootObject));
    }

    protected boolean enableGzip(HttpServletRequest request) {
        return enableGZIP && isGzipInRequest(request);
    }

    public static boolean isGzipInRequest(HttpServletRequest request) {
        String header = request.getHeader("Accept-Encoding");
        return (header != null) && header.contains("gzip");
    }

    protected void writeToResponse(HttpServletResponse response, String json, boolean gzip) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[JSON]" + json);
        }

        // status or error code
        if (statusCode > 0)
            response.setStatus(statusCode);
        else if (errorCode > 0)
            response.sendError(errorCode);

        // content type
        response.setContentType(StringUtils.defaultString(contentType, DEFAULT_CONTENT_TYPE) + ";charset=" + getEncoding());

        if (noCache) {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "No-cache");
        }

        if (gzip) {
            response.addHeader("Content-Encoding", "gzip");
            GZIPOutputStream out = null;
            InputStream in = null;
            try {
                out = new GZIPOutputStream(response.getOutputStream());
                in = new ByteArrayInputStream(json.getBytes(getEncoding()));
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                if (in != null)
                    in.close();
                if (out != null) {
                    out.finish();
                    out.close();
                }
            }

        } else {
            response.setContentLength(json.getBytes(getEncoding()).length);
            PrintWriter out = response.getWriter();
            out.print(json);
        }
    }

    /**
     * Retrieve the encoding
     * <p/>
     *
     * @return The encoding associated with this template (defaults to the value of 'struts.i18n.encoding' property)
     */
    protected String getEncoding() {
        String encoding = this.defaultEncoding;

        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }

        if (encoding == null) {
            encoding = "UTF-8";
        }

        return encoding;
    }

    protected String addCallbackIfApplicable(HttpServletRequest request, String json) {
        if ((callbackParameter != null) && (callbackParameter.length() > 0)) {
            String callbackName = request.getParameter(callbackParameter);
            if ((callbackName != null) && (callbackName.length() > 0)) {
                json = callbackName + "(" + json + ")";
            }

        }
        return json;
    }

    /**
     * @return OGNL expression of root object to be serialized
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * Sets the root object to be serialized, defaults to the Action
     *
     * @param root OGNL expression of root object to be serialized
     */
    public void setRoot(String root) {
        this.root = root;
    }

    public boolean isEnableGZIP() {
        return enableGZIP;
    }

    public void setEnableGZIP(boolean enableGZIP) {
        this.enableGZIP = enableGZIP;
    }

    public boolean isNoCache() {
        return noCache;
    }

    /**
     * Add headers to response to prevent the browser from caching the response
     *
     * @param noCache
     */
    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    /**
     * Status code to be set in the response
     *
     * @param statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Error code to be set in the response
     *
     * @param errorCode
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setCallbackParameter(String callbackParameter) {
        this.callbackParameter = callbackParameter;
    }

    public String getCallbackParameter() {
        return callbackParameter;
    }

    /**
     * Content type to be set in the response
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
