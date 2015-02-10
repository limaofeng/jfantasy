package com.fantasy.framework.struts2.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONValidationInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = -2475397645941297364L;

	private static final Logger LOG = LoggerFactory.getLogger(JSONValidationInterceptor.class);
	private static final String VALIDATE_ONLY_PARAM = "struts.validateOnly";
	private static final String VALIDATE_JSON_PARAM = "struts.enableJSONValidation";
	private static final String NO_ENCODING_SET_PARAM = "struts.JSONValidation.no.encoding";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private int validationFailedStatus = -1;

	public void setValidationFailedStatus(int validationFailedStatus) {
		this.validationFailedStatus = validationFailedStatus;
	}

	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		Object action = invocation.getAction();
		if (isJsonEnabled(request)) {
			if ((action instanceof ValidationAware)) {
				ValidationAware validationAware = (ValidationAware) action;
				if (validationAware.hasErrors()) {
					return generateJSON(request, response, validationAware);
				}
			}
			if (isValidateOnly(request)) {
				setupEncoding(response, request);
				response.getWriter().print("{}");
				response.setContentType("text/plain");
				return "none";
			}
			return invocation.invoke();
		}

		return invocation.invoke();
	}

	private void setupEncoding(HttpServletResponse response, HttpServletRequest request) {
		if (isSetEncoding(request)) {
			LOG.debug("Default encoding not set!", new String[0]);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Setting up encoding to: [UTF-8]!", new String[0]);
			}
			response.setCharacterEncoding(DEFAULT_ENCODING);
		}
	}

	private String generateJSON(HttpServletRequest request, HttpServletResponse response, ValidationAware validationAware) throws IOException {
		if (this.validationFailedStatus >= 0) {
			response.setStatus(this.validationFailedStatus);
		}
		setupEncoding(response, request);
		response.getWriter().print(buildResponse(validationAware));
		response.setContentType("text/plain");

		return "none";
	}

	private boolean isJsonEnabled(HttpServletRequest request) {
		return "true".equals(request.getParameter(VALIDATE_JSON_PARAM));
	}

	private boolean isValidateOnly(HttpServletRequest request) {
		return "true".equals(request.getParameter(VALIDATE_ONLY_PARAM));
	}

	private boolean isSetEncoding(HttpServletRequest request) {
		return "true".equals(request.getParameter(NO_ENCODING_SET_PARAM));
	}

	protected String buildResponse(ValidationAware validationAware) {
		StringBuilder sb = new StringBuilder();
		sb.append("{success:false,");

		if (validationAware.hasErrors()) {
			if (validationAware.hasActionErrors()) {
				sb.append("\"errors\":");
				sb.append(buildArray(validationAware.getActionErrors()));
			}

			if (validationAware.hasActionMessages()) {
				sb.append("\"messages\":");
				sb.append(buildArray(validationAware.getActionMessages()));
			}

			if (validationAware.hasFieldErrors()) {
				if ((validationAware.hasActionErrors()) || (validationAware.hasActionMessages())){
                    sb.append(",");
                }
				sb.append("\"fieldErrors\": {");
				Map<String, List<String>> fieldErrors = validationAware.getFieldErrors();

				Iterator<Map.Entry<String, List<String>>> localIterator = fieldErrors.entrySet().iterator();

				while (localIterator.hasNext()) {
					Map.Entry<String, List<String>> fieldError = localIterator.next();
					sb.append("\"");

					String fieldErrorKey = fieldError.getKey();
					sb.append(((validationAware instanceof ModelDriven<?>)) && (fieldErrorKey.startsWith("model.")) ? fieldErrorKey.substring(6) : fieldErrorKey);
					sb.append("\":");
					sb.append(buildArray((Collection<String>) fieldError.getValue()));
					sb.append(",");
				}

				sb.deleteCharAt(sb.length() - 1);
				sb.append("}");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	private String buildArray(Collection<String> values) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String value : values) {
			sb.append("\"");
			sb.append(StringEscapeUtils.escapeJavaScript(value));
			sb.append("\",");
		}
		if (values.size() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }
		sb.append("]");
		return sb.toString();
	}
}
