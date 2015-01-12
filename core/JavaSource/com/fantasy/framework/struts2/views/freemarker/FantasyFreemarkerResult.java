package com.fantasy.framework.struts2.views.freemarker;

import com.fantasy.framework.struts2.views.freemarker.FantasyFreemarkerManager.Mode;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.util.ResourceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public class FantasyFreemarkerResult extends org.apache.struts2.views.freemarker.FreemarkerResult {

	private static final long serialVersionUID = 711627091302669871L;
	private static final String PARENT_TEMPLATE_WRITER = FantasyFreemarkerResult.class.getName() + ".parentWriter";
	private transient FantasyFreemarkerManager freemarkerManager;
	protected String contentDisposition;
	private boolean allowCaching = true;

	public boolean isAllowCaching() {
		return allowCaching;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	@Inject("fantasy.freemarkermanager")
	public void setFreemarkerManager(FantasyFreemarkerManager mgr) {
		this.freemarkerManager = mgr;
		super.setFreemarkerManager(mgr);
	}

	@Override
	public void setFreemarkerManager(FreemarkerManager mgr) {
		super.setFreemarkerManager(mgr);
	}

	@Override
	public void doExecute(String locationArg, ActionInvocation invocation) throws IOException, TemplateException {
		this.location = locationArg;
		this.invocation = invocation;
		this.configuration = getConfiguration();
		this.wrapper = getObjectWrapper();
		ActionContext ctx = invocation.getInvocationContext();
		HttpServletRequest req = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		if (Mode.struts2.equals(this.freemarkerManager.getMode()) && !locationArg.startsWith("/")) {
				String base = ResourceUtil.getResourceBase(req);
				locationArg = base + "/" + locationArg;
		}
		Template template = configuration.getTemplate(locationArg, deduceLocale());
		TemplateModel model = createModel();
		if (preTemplateProcess(template, model)) {
			try {
				Writer writer = getWriter();
				if (isWriteIfCompleted() || configuration.getTemplateExceptionHandler() == TemplateExceptionHandler.RETHROW_HANDLER) {
					CharArrayWriter parentCharArrayWriter = (CharArrayWriter) req.getAttribute(PARENT_TEMPLATE_WRITER);
					boolean isTopTemplate = false;
					if (isTopTemplate = (parentCharArrayWriter == null)) {
						parentCharArrayWriter = new CharArrayWriter();
						req.setAttribute(PARENT_TEMPLATE_WRITER, parentCharArrayWriter);
					}
					try {
						template.process(model, parentCharArrayWriter);

						if (isTopTemplate) {
							parentCharArrayWriter.flush();
							parentCharArrayWriter.writeTo(writer);
						}
					} finally {
						if (isTopTemplate && parentCharArrayWriter != null) {
							req.removeAttribute(PARENT_TEMPLATE_WRITER);
							parentCharArrayWriter.close();
						}
					}
				} else {
					template.process(model, writer);
				}
			} finally {
				postTemplateProcess(template, model);
			}
		}
	}

	@Override
	protected boolean preTemplateProcess(Template template, TemplateModel model) throws IOException {
		super.preTemplateProcess(template, model);
		HttpServletResponse response = ServletActionContext.getResponse();
		if (contentDisposition != null) {
			response.addHeader("Content-Disposition", conditionalParse(contentDisposition, invocation));
		}
		if (!allowCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		}
		return true;
	}

}
