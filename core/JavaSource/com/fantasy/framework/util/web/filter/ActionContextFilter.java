package com.fantasy.framework.util.web.filter;

import com.fantasy.framework.util.web.context.ActionContext;
import com.fantasy.system.util.SettingUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-25 上午12:30:42
 */
public class ActionContextFilter extends OncePerRequestFilter {

    private String encoding = "UTF-8";

    private boolean forceEncoding = false;

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setForceEncoding(boolean forceEncoding) {
        this.forceEncoding = forceEncoding;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        SettingUtil.initialize(this.getServletContext());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (this.forceEncoding || (request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(this.encoding);
            if (this.forceEncoding) {
                response.setCharacterEncoding(this.encoding);
            }
        }
        ActionContext.getContext(request, response);
        chain.doFilter(request, response);
    }

}
